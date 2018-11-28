package au.edu.federation.var;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Chooser {
    private JButton liveAudioButton;
    private JButton fromRecordingButton;
    private JPanel chooseruntype;

    public Chooser() {
        liveAudioButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                MainForm.main(null);
            }

        });
        fromRecordingButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                MainForm.main(null);
            }
        });
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Chooser");
        frame.setContentPane(new Chooser().chooseruntype);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
