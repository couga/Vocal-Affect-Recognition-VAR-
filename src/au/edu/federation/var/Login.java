package au.edu.federation.var;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Login {
    private JButton loginButton;
    private JButton resetButton;
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JPanel dialogPanel;

    public Login() {
        resetButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {


            }
        });
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                    MainForm.main(null);

                    // We should remove this window so it doesn't confuse the users...
                    dialogPanel.getParent().setVisible(false); // Hide the login window for now
                    // We need to do more than hide the window - should destroy it, but I don't know how yet.

            }
        });
    }

    private void resetFields() {
        usernameField.setText(null);
        passwordField.setText(null);
        usernameField.requestFocus();
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("VAR");
        frame.setContentPane(new Login().dialogPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
