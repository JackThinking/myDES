import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class MyGUI {
    private JButton encButton;
    private JButton decButton;
    private JTextField message;
    private JTextField key;
    private JTextField ciphertext;
    private JPanel GUI;

    public MyGUI() {
        encButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println(message.getText());
                ciphertext.setText(message.getText());
            }
        });
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("MyGUI");
        frame.setContentPane(new MyGUI().GUI);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }


}
