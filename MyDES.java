//导入库
import java.util.Arrays;

public class MyDES {
    /*数据初始化*/

    // 初始置换IP盒
    private static final byte[] IP = {
            58, 50, 42, 34, 26, 18, 10, 2,
            60, 52, 44, 36, 28, 20, 12, 4,
            62, 54, 46, 38, 30, 22, 14, 6,
            64, 56, 48, 40, 32, 24, 16, 8,
            57, 49, 41, 33, 25, 17, 9, 1,
            59, 51, 43, 35, 27, 19, 11, 3,
            61, 53, 45, 37, 29, 21, 13, 5,
            63, 55, 47, 39, 31, 23, 15, 7
    };
    // 逆置换IP-1盒
    private static final byte[] FP = {
            40, 8, 48, 16, 56, 24, 64, 32,
            39, 7, 47, 15, 55, 23, 63, 31,
            38, 6, 46, 14, 54, 22, 62, 30,
            37, 5, 45, 13, 53, 21, 61, 29,
            36, 4, 44, 12, 52, 20, 60, 28,
            35, 3, 43, 11, 51, 19, 59, 27,
            34, 2, 42, 10, 50, 18, 58, 26,
            33, 1, 41, 9, 49, 17, 57, 25
    };
    // 扩展E盒
    private static final byte[] E = {
            32, 1, 2, 3, 4, 5,
            4, 5, 6, 7, 8, 9,
            8, 9, 10, 11, 12, 13,
            12, 13, 14, 15, 16, 17,
            16, 17, 18, 19, 20, 21,
            20, 21, 22, 23, 24, 25,
            24, 25, 26, 27, 28, 29,
            28, 29, 30, 31, 32, 1
    };
    // 置换P盒
    private static final byte[] P = {
            16, 7, 20, 21,
            29, 12, 28, 17,
            1, 15, 23, 26,
            5, 18, 31, 10,
            2, 8, 24, 14,
            32, 27, 3, 9,
            19, 13, 30, 6,
            22, 11, 4, 25
    };
    // DES的8个S盒
    private static final byte[][] S = {{
            14, 4, 13, 1, 2, 15, 11, 8, 3, 10, 6, 12, 5, 9, 0, 7,
            0, 15, 7, 4, 14, 2, 13, 1, 10, 6, 12, 11, 9, 5, 3, 8,
            4, 1, 14, 8, 13, 6, 2, 11, 15, 12, 9, 7, 3, 10, 5, 0,
            15, 12, 8, 2, 4, 9, 1, 7, 5, 11, 3, 14, 10, 0, 6, 13
    }, {
            15, 1, 8, 14, 6, 11, 3, 4, 9, 7, 2, 13, 12, 0, 5, 10,
            3, 13, 4, 7, 15, 2, 8, 14, 12, 0, 1, 10, 6, 9, 11, 5,
            0, 14, 7, 11, 10, 4, 13, 1, 5, 8, 12, 6, 9, 3, 2, 15,
            13, 8, 10, 1, 3, 15, 4, 2, 11, 6, 7, 12, 0, 5, 14, 9
    }, {
            10, 0, 9, 14, 6, 3, 15, 5, 1, 13, 12, 7, 11, 4, 2, 8,
            13, 7, 0, 9, 3, 4, 6, 10, 2, 8, 5, 14, 12, 11, 15, 1,
            13, 6, 4, 9, 8, 15, 3, 0, 11, 1, 2, 12, 5, 10, 14, 7,
            1, 10, 13, 0, 6, 9, 8, 7, 4, 15, 14, 3, 11, 5, 2, 12
    }, {
            7, 13, 14, 3, 0, 6, 9, 10, 1, 2, 8, 5, 11, 12, 4, 15,
            13, 8, 11, 5, 6, 15, 0, 3, 4, 7, 2, 12, 1, 10, 14, 9,
            10, 6, 9, 0, 12, 11, 7, 13, 15, 1, 3, 14, 5, 2, 8, 4,
            3, 15, 0, 6, 10, 1, 13, 8, 9, 4, 5, 11, 12, 7, 2, 14
    }, {
            2, 12, 4, 1, 7, 10, 11, 6, 8, 5, 3, 15, 13, 0, 14, 9,
            14, 11, 2, 12, 4, 7, 13, 1, 5, 0, 15, 10, 3, 9, 8, 6,
            4, 2, 1, 11, 10, 13, 7, 8, 15, 9, 12, 5, 6, 3, 0, 14,
            11, 8, 12, 7, 1, 14, 2, 13, 6, 15, 0, 9, 10, 4, 5, 3
    }, {
            12, 1, 10, 15, 9, 2, 6, 8, 0, 13, 3, 4, 14, 7, 5, 11,
            10, 15, 4, 2, 7, 12, 9, 5, 6, 1, 13, 14, 0, 11, 3, 8,
            9, 14, 15, 5, 2, 8, 12, 3, 7, 0, 4, 10, 1, 13, 11, 6,
            4, 3, 2, 12, 9, 5, 15, 10, 11, 14, 1, 7, 6, 0, 8, 13
    }, {
            4, 11, 2, 14, 15, 0, 8, 13, 3, 12, 9, 7, 5, 10, 6, 1,
            13, 0, 11, 7, 4, 9, 1, 10, 14, 3, 5, 12, 2, 15, 8, 6,
            1, 4, 11, 13, 12, 3, 7, 14, 10, 15, 6, 8, 0, 5, 9, 2,
            6, 11, 13, 8, 1, 4, 10, 7, 9, 5, 0, 15, 14, 2, 3, 12
    }, {
            13, 2, 8, 4, 6, 15, 11, 1, 10, 9, 3, 14, 5, 0, 12, 7,
            1, 15, 13, 8, 10, 3, 7, 4, 12, 5, 6, 11, 0, 14, 9, 2,
            7, 11, 4, 1, 9, 12, 14, 2, 0, 6, 10, 13, 15, 3, 5, 8,
            2, 1, 14, 7, 4, 10, 8, 13, 15, 12, 9, 0, 3, 5, 6, 11
    }};
    //
    private static final byte[] rotations = {
            1, 1, 2, 2, 2, 2, 2, 2, 1, 2, 2, 2, 2, 2, 2, 1
    };
    // DES密钥安排中的比特选择PC1
    private static final byte[] PC1 = {
            57, 49, 41, 33, 25, 17, 9,
            1, 58, 50, 42, 34, 26, 18,
            10, 2, 59, 51, 43, 35, 27,
            19, 11, 3, 60, 52, 44, 36,
            63, 55, 47, 39, 31, 23, 15,
            7, 62, 54, 46, 38, 30, 22,
            14, 6, 61, 53, 45, 37, 29,
            21, 13, 5, 28, 20, 12, 4
    };
    // DES密钥安排中的比特选择PC2
    private static final byte[] PC2 = {
            14, 17, 11, 24, 1, 5,
            3, 28, 15, 6, 21, 10,
            23, 19, 12, 4, 26, 8,
            16, 7, 27, 20, 13, 2,
            41, 52, 31, 37, 47, 55,
            30, 40, 51, 45, 33, 48,
            44, 49, 39, 56, 34, 53,
            46, 42, 50, 36, 29, 32
    };

    // 初始移位寄存器IV
    private static long IV;

    public static long getIv() {
        return IV;
    }

    public static void setIv(long iv) {
        IV = iv;
    }
    /*
    交换函数
    <<:循环左移
    >>:循环右移
    &:and
    |:or
    */
    private static long permute(byte[] table, int srcWidth, long src) {
        long dst = 0;
        for (int i = 0; i < table.length; i++) {
            int srcPos = srcWidth - table[i];
            dst = (dst << 1) | (src >> srcPos & 0x01);
        }
        return dst;
    }

    /**
     * 主要函数声明
     */
    private static long IP(long src) {
        return permute(IP, 64, src);
    } // 64-bit output

    private static long FP(long src) {
        return permute(FP, 64, src);
    } // 64-bit output

    private static long E(int src) {
        return permute(E, 32, src & 0xFFFFFFFFL);
    } // 48-bit output

    private static int P(int src) {
        return (int) permute(P, 32, src & 0xFFFFFFFFL);
    } // 32-bit output

    private static long PC1(long src) {
        return permute(PC1, 64, src);
    } // 56-bit output

    private static long PC2(long src) {
        return permute(PC2, 56, src);
    } // 48-bit output

    //将8个6bit的字符串映射成S盒中的4比特元素
    private static byte S(int boxNumber, byte src) {
        // The first aindex based on the following bit shuffle:
        // abcdef => afbcde
        src = (byte) (src & 0x20 | ((src & 0x01) << 4) | ((src & 0x1E) >> 1));
        return S[boxNumber - 1][src];
    }

    //bytes转long数据
    private static long getLongFromBytes(byte[] ba, int offset) {
        long l = 0;
        for (int i = 0; i < 8; i++) {
            byte value;
            if ((offset + i) < ba.length) {
                value = ba[offset + i];
            } else {
                value = 0;
            }
            l = l << 8 | (value & 0xFFL);
        }
        return l;
    }

    //long转bytes数据
    private static void getBytesFromLong(byte[] ba, int offset, long l) {
        for (int i = 7; i > -1; i--) {
            if ((offset + i) < ba.length) {
                ba[offset + i] = (byte) (l & 0xFF);
                l = l >> 8;
            } else {
                break;
            }
        }
    }

    /*DES中核心的feistel结构*/
    private static int feistel(int r, /* 48 bits */ long subkey) {
        // 1. expansion
        //32为扩展成48位
        long e = E(r);
        // 2. key mixing
        //和子秘钥异或
        long x = e ^ subkey;
        // 3. substitution
        //48bit分成8*6后分别进行8个S盒替换组成新的8*4比特
        int dst = 0;
        for (int i = 0; i < 8; i++) {
            dst >>>= 4;
            int s = S(8 - i, (byte) (x & 0x3F));
            dst |= s << 28;
            x >>= 6;
        }
        // 4. permutation
        //返回P置换后的32位数据
        return P(dst);
    }

    /*根据64bit秘钥生成16个48bit子秘钥*/
    private static long[] createSubkeys(/* 64 bits */ long key) {
        long subkeys[] = new long[16];

        // 64位初始秘钥结果置换选择PC1变成56位秘钥
        key = PC1(key);

        // 分成前后两个28bit的C和D
        int c = (int) (key >> 28);
        int d = (int) (key & 0x0FFFFFFF);

        // 对于16个子密钥，每个按照28bit前后分成C和D后，进行循环移位然后重新组合
        for (int i = 0; i < 16; i++) {
            // 28bit值循环移位
            if (rotations[i] == 1) {
                // 1 bit循环移位
                c = ((c << 1) & 0x0FFFFFFF) | (c >> 27);
                d = ((d << 1) & 0x0FFFFFFF) | (d >> 27);
            } else {
                //2 bits循环移位
                c = ((c << 2) & 0x0FFFFFFF) | (c >> 26);
                d = ((d << 2) & 0x0FFFFFFF) | (d >> 26);
            }

            // 将C和D重新组合
            long cd = (c & 0xFFFFFFFFL) << 28 | (d & 0xFFFFFFFFL);

            // 置换选择PC2（56->48），得到48bit秘钥
            subkeys[i] = PC2(cd);
        }

        return subkeys; /* 48-bit values */
    }

    /*
    * 将string格式的秘钥变成byte的数组
    * */
    private static byte[] passwordToKey(String password) {
        byte[] pwbytes = password.getBytes();
        byte[] key = new byte[8];
        for (int i = 0; i < 8; i++) {
            if (i < pwbytes.length) {
                byte b = pwbytes[i];
                // flip the byte
                byte b2 = 0;
                for (int j = 0; j < 8; j++) {
                    b2 <<= 1;
                    b2 |= (b & 0x01);
                    b >>>= 1;
                }
                key[i] = b2;
            } else {
                key[i] = 0;
            }
        }
        return key;
    }
    // 所有的加密函数 -----------------------------------------------------------------------------------------
    /**
     * 基本块加密：64bit明文加密成64bit密文
     */
    public static long encryptBlock(long m, /* 64 bits */ long key) {
        // 生成16个子密钥
        long subkeys[] = createSubkeys(key);

        // 64bit明文进行初始IP置换
        long ip = IP(m);

        // 得到左右各32bit的值
        int l = (int) (ip >> 32);//左边32位
        int r = (int) (ip & 0xFFFFFFFFL);//右边32位

        // 16轮迭代
        for (int i = 0; i < 16; i++) {
            //保存左边32bit的值
            int previous_l = l;

            // 左边32bit赋值为右边的32bit
            l = r;

            // 左边32bit的值与右边经过feistel迭代加密的值进行异或
            r = previous_l ^ feistel(r, subkeys[i]);
        }
        // 交换左右32bit
        long rl = (r & 0xFFFFFFFFL) << 32 | (l & 0xFFFFFFFFL);

        // FP置换
        long fp = FP(rl);

        // 返回密文
        return fp;
    }
    /**
     * 可以byte-8bit加密而不一定是long-64bit加密
     */
    public static void encryptBlock(
            byte[] message,
            int messageOffset,
            byte[] ciphertext,
            int ciphertextOffset,
            byte[] key
    ) {
        long m = getLongFromBytes(message, messageOffset);
        long k = getLongFromBytes(key, 0);
        long c = encryptBlock(m, k);
        getBytesFromLong(ciphertext, ciphertextOffset, c);
    }
    /**
     * ECB加密方式，明文按照64bit切分，不足64bit填0
     */
    public static byte[] encryptECB(byte[] message, byte[] key) {
        byte[] ciphertext = new byte[message.length];

        // encrypt each 8-byte (64-bit) block of the message.
        for (int i = 0; i < message.length; i += 8) {
            encryptBlock(message, i, ciphertext, i, key);
        }

        return ciphertext;
    }
    /**
     * CBC加密方式，明文按照64bit切分，不足64bit填0
     */
    public static byte[] encryptCBC(byte[] message, byte[] key) {
        byte[] ciphertext = new byte[message.length];
        long k = getLongFromBytes(key, 0);
        long previousCipherBlock = IV;

        for (int i = 0; i < message.length; i += 8) {
            // 明文message中逐8byte(64bit)取
            long messageBlock = getLongFromBytes(message, i);

            // 先将明文与初始IV进行异或，然后进行64bit块加密
            long cipherBlock = encryptBlock(messageBlock ^ previousCipherBlock, k);

            // 将生成的64bit密文按照顺序放到总密文变量中
            getBytesFromLong(ciphertext, i, cipherBlock);

            // 将IV替换成生成的密文
            previousCipherBlock = cipherBlock;
        }

        return ciphertext;
    }
    /*
     * CFB加密方式，明文按照64bit切分，不足64bit填0
     * */
    public static byte[] encryptCFB(byte[] message, byte[] key) {
        byte[] ciphertext = new byte[message.length];
        long k = getLongFromBytes(key, 0);
        long previousCipherBlock = IV;

        for (int i = 0; i < message.length; i += 8) {

            // 明文message中逐8byte(64bit)取
            long messageBlock = getLongFromBytes(message, i);

            // 对IV加密
            long cipherBlock = encryptBlock(previousCipherBlock, k);

            // 明文与生成的密文异或
            long cipherBlock_xor = messageBlock ^ cipherBlock;

            // 密文块存到相应的密文中
            getBytesFromLong(ciphertext, i, cipherBlock_xor);

            // 将IV替换成生成的密文
            previousCipherBlock = cipherBlock_xor;
        }
        return ciphertext;
    }
    /*
     *OFB加密方式，明文按照64bit切分，不足64bit填0
     * */
    public static byte[] encryptOFB(byte[] message, byte[] key) {
        byte[] ciphertext = new byte[message.length];
        long k = getLongFromBytes(key, 0);
        long previousCipherBlock = IV;

        for (int i = 0; i < message.length; i += 8) {
            // 明文message中逐8byte(64bit)取
            long messageBlock = getLongFromBytes(message, i);
            // 对64bit加密
            long cipherBlock = encryptBlock(previousCipherBlock, k);

            // 明文与生成的密文异或
            long cipherBlock_xor = messageBlock ^ cipherBlock;
            // 密文块存到相应的密文中

            getBytesFromLong(ciphertext, i, cipherBlock_xor);
            // 将IV替换成生成的密文

            previousCipherBlock = cipherBlock;
        }
        return ciphertext;
    }
    // 所有的加密函数 -----------------------------------------------------------------------------------------

    // 所有的解密函数 -----------------------------------------------------------------------------------------
    /**
     * 将64bit密文解密为64bit明文
     */
    public static long decryptBlock(long c, /* 64 bits */ long key) {
        // 生成16个子密钥
        long[] subkeys = createSubkeys(key);

        // IP置换
        long ip = IP(c);

        // 分成左右各32bit的分组
        int l = (int) (ip >> 32);
        int r = (int) (ip & 0xFFFFFFFFL);

        // 16轮迭代
        // 注意: 16个子密钥的顺序反过来解密用
        for (int i = 15; i > -1; i--) {
            // 左边32bit赋值为右边的32bit
            int previous_l = l;
            // 左边32bit赋值为右边的32bit
            l = r;
            // 左边32bit的值与右边经过feistel迭代加密的值进行异或
            r = previous_l ^ feistel(r, subkeys[i]);
        }

        // 最后一轮左右32bit交换
        long rl = (r & 0xFFFFFFFFL) << 32 | (l & 0xFFFFFFFFL);

        // FP置换
        long fp = FP(rl);

        // 返回明文
        return fp;
    }
    /**
     * byte也可以解密了
     */
    public static void decryptBlock(
            byte[] ciphertext,
            int ciphertextOffset,
            byte[] message,
            int messageOffset,
            byte[] key
    ) {
        long c = getLongFromBytes(ciphertext, ciphertextOffset);
        long k = getLongFromBytes(key, 0);
        long m = decryptBlock(c, k);
        getBytesFromLong(message, messageOffset, m);
    }
    /*
    * ECB解密方式，明文按照64bit切分，不足64bit填0
    * */
    public static byte[] decryptECB(byte[] ciphertext, byte[] key) {
        byte[] message = new byte[ciphertext.length];

        // encrypt each 8-byte (64-bit) block of the message.
        for (int i = 0; i < ciphertext.length; i += 8) {
            decryptBlock(ciphertext, i, message, i, key);
        }

        return message;
    }
    /**
     * CBC解密方式，明文按照64bit切分，不足64bit填0
     */
    public static byte[] decryptCBC(byte[] ciphertext, byte[] key) {
        byte[] message = new byte[ciphertext.length];
        long k = getLongFromBytes(key, 0);
        long previousCipherBlock = IV;

        for (int i = 0; i < ciphertext.length; i += 8) {
            // get the cipher block to be decrypted (8bytes = 64bits)
            long cipherBlock = getLongFromBytes(ciphertext, i);

            // Decrypt the cipher block and XOR with previousCipherBlock
            // First previousCiphertext = Initial Vector (IV)
            long messageBlock = decryptBlock(cipherBlock, k);
            messageBlock = messageBlock ^ previousCipherBlock;

            // Store the messageBlock in the correct position in message
            getBytesFromLong(message, i, messageBlock);

            // Update previousCipherBlock
            previousCipherBlock = cipherBlock;
        }

        return message;
    }
    /*
     * CFB解密方式，明文按照64bit切分，不足64bit填0
     * */
    public static byte[] decryptCFB(byte[] ciphertext, byte[] key) {
        byte[] message = new byte[ciphertext.length];
        long k = getLongFromBytes(key, 0);
        long previousCipherBlock = IV;

        for (int i = 0; i < message.length; i += 8) {

            long cipherBlock = getLongFromBytes(ciphertext, i);

            long messageBlock = encryptBlock(previousCipherBlock, k);

            long messageBlock_xor = messageBlock ^ cipherBlock;

            getBytesFromLong(message, i, messageBlock_xor);

            previousCipherBlock = cipherBlock;
        }
        return message;
    }
    /*
     * OFB解密方式，明文按照64bit切分，不足64bit填0
     * */
    public static byte[] decryptOFB(byte[] ciphertext, byte[] key) {
        byte[] message = new byte[ciphertext.length];
        long k = getLongFromBytes(key, 0);
        long previousCipherBlock = IV;

        for (int i = 0; i < message.length; i += 8) {

            long cipherBlock = getLongFromBytes(ciphertext, i);
            //都是加密的方式嘛
            long messageBlock = encryptBlock(previousCipherBlock, k);

            long messageBlock_xor = messageBlock ^ cipherBlock;

            getBytesFromLong(message, i, messageBlock_xor);

            previousCipherBlock = messageBlock;
        }
        return message;
    }
    // 所有的解密函数 -----------------------------------------------------------------------------------------
    /*
    * char转int
    * */
    private static int charToNibble(char c) {
        if (c >= '0' && c <= '9') {
            return (c - '0');
        } else if (c >= 'a' && c <= 'f') {
            return (10 + c - 'a');
        } else if (c >= 'A' && c <= 'F') {
            return (10 + c - 'A');
        } else {
            return 0;
        }
    }
    /*
    * string转byte
    * */
    private static byte[] parseBytes(String s) {
        s = s.replace(" ", "");
        byte[] ba = new byte[s.length() / 2];
        if (s.length() % 2 > 0) {
            s = s + '0';
        }
        for (int i = 0; i < s.length(); i += 2) {
            ba[i / 2] = (byte) (charToNibble(s.charAt(i)) << 4 | charToNibble(s.charAt(i + 1)));
        }
        return ba;
    }
    /*
    * hex表示
    * */
    private static String hex(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < bytes.length; i++) {
            sb.append(String.format("%02X ", bytes[i]));
        }
        return sb.toString();
    }
    /*
     * string转hex
     * */
    public static String convertStringToHex(String str){

        char[] chars = str.toCharArray();

        StringBuffer hex = new StringBuffer();
        for(int i = 0; i < chars.length; i++){
            hex.append(Integer.toHexString((int)chars[i]));
        }

        return hex.toString();
    }

    /*
    * 程序运行主函数
    * */
    public static void main(String[] args) {
        //String oriText = "f3ed a6dc f8b7 9dd6 5be0 db8b 1e7b a551";
        //String oriText = "0123 4567 89ab cdef";
        String oriText = "123456789";
        int yushu = oriText.length() % 8;
        System.out.println(yushu);
        if (yushu != 0){
            for(int i=0;i<8-yushu;i++){
                oriText += "0";
            }
        }

        oriText = convertStringToHex(oriText);

        String key = convertStringToHex("12345678");

        byte[] keys = parseBytes(key);
        byte[] test = parseBytes(oriText);

        System.out.println("Key:  " + hex(keys));

        String result = hex(encryptECB(test, keys));
        System.out.println("ORIGINAL TEXT:  " + hex(test));
        System.out.println("Encryption result:  " + result);

        byte[] encResult = parseBytes(result);
        String decResult = hex(decryptECB(encResult, keys));
        System.out.println("Decryption result:  " + decResult);
    }
}