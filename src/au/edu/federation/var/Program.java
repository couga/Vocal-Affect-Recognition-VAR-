package au.edu.federation.var;

import java.awt.EventQueue;

import javax.swing.*;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import java.io.File;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class Program {

    private JFrame frame;
    private JTextField textField;
    private JTextField textField_1;
    private JTextField textField_2;
    private JTextField textField_3;

    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    Program window = new Program();
                    window.frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public Program() {
        initialize();
    }

    private void initialize() {
        frame = new JFrame();
        frame.setBounds(100, 100, 985, 543);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setLayout(null);

        JLabel lblNewLabel = new JLabel("Vocal Affect Recognition Program");
        lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
        lblNewLabel.setFont(new Font("Tahoma", Font.BOLD, 18));
        lblNewLabel.setBounds(10, 11, 345, 27);
        frame.getContentPane().add(lblNewLabel);

        JLabel lblP = new JLabel("Patient's Name:");
        lblP.setFont(new Font("Tahoma", Font.BOLD, 12));
        lblP.setBounds(20, 49, 109, 20);
        frame.getContentPane().add(lblP);

        JLabel lblInterviewersName = new JLabel("Interviewer's Name:");
        lblInterviewersName.setFont(new Font("Tahoma", Font.BOLD, 12));
        lblInterviewersName.setBounds(230, 49, 137, 20);
        frame.getContentPane().add(lblInterviewersName);

        JLabel lblDateOfInterview = new JLabel("Date Of Interview:");
        lblDateOfInterview.setFont(new Font("Tahoma", Font.BOLD, 12));
        lblDateOfInterview.setBounds(469, 49, 127, 20);
        frame.getContentPane().add(lblDateOfInterview);

        JLabel lblLocationOfInterview = new JLabel("Location Of Interview:");
        lblLocationOfInterview.setFont(new Font("Tahoma", Font.BOLD, 12));
        lblLocationOfInterview.setBounds(703, 49, 143, 20);
        frame.getContentPane().add(lblLocationOfInterview);

        textField = new JTextField();
        textField.setBounds(120, 50, 100, 20);
        frame.getContentPane().add(textField);
        textField.setColumns(10);

        textField_1 = new JTextField();
        textField_1.setBounds(359, 50, 100, 20);
        frame.getContentPane().add(textField_1);
        textField_1.setColumns(10);

        textField_2 = new JTextField();
        textField_2.setBounds(593, 50, 100, 20);
        frame.getContentPane().add(textField_2);
        textField_2.setColumns(10);

        textField_3 = new JTextField();
        textField_3.setBounds(848, 50, 100, 20);
        frame.getContentPane().add(textField_3);
        textField_3.setColumns(10);

        JSeparator separator = new JSeparator();
        separator.setBounds(10, 42, 949, 2);
        frame.getContentPane().add(separator);

        JSeparator separator_1 = new JSeparator();
        separator_1.setBounds(10, 80, 949, 48);
        frame.getContentPane().add(separator_1);

        //TI: Added a button to play audio. When i get the chance i will include pause, stop and a slider.
        JButton btnNewButton = new JButton("Play audio");
        btnNewButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
            }
        });
        btnNewButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent arg0) {
                audio();
            }

            private void audio() {
                JFileChooser fileChooser = new JFileChooser();
                int returnValue = fileChooser.showOpenDialog(null);
                if (returnValue == JFileChooser.APPROVE_OPTION) {
                    File selectedFile = fileChooser.getSelectedFile();
                    System.out.println(selectedFile.getName());

                    String path = selectedFile.getAbsolutePath();

                    File sound = new File(path);

                    try {
                        AudioInputStream ais = AudioSystem.getAudioInputStream(sound);
                        Clip c = AudioSystem.getClip();
                        c.open(ais);

                        System.out.println("Playing audio from file: " + path);

                        c.start();

                        Thread.sleep((int) (c.getMicrosecondLength() * 0.001));
                    } catch (Exception e) {
                        System.out.println(e.getMessage());
                    }

                }
            }
        });
        btnNewButton.setBounds(63, 470, 153, 23);
        frame.getContentPane().add(btnNewButton);

        JButton btnSave = new JButton("Save");
        btnSave.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {

//                Connection c = null;
//
//                try {
//
//                    Class.forName("org.sqlite.JDBC");
//                    c = DriverManager.getConnection("jdbc:sqlite:var.db");
//
//                    String query="insert into AudioHistory (PatientName,InterviewName,InterviewDate,Location) values (?,?,?,?)";
//                    PreparedStatement pst=c.prepareStatement(query);
//                    pst.setString(1, textField.getText() );
//                    pst.setString(2, textField_1.getText() );
//                    pst.setString(3, textField_2.getText() );
//                    pst.setString(4, textField_3.getText() );
//
//                    pst.execute();
//
                    JOptionPane.showMessageDialog(null, "Data Saved");
//
//                    pst.close();
//
//                }catch (Exception e) {
//                    e.printStackTrace();
//                }
            }
        });
        btnSave.setBounds(831, 470, 89, 23);
        frame.getContentPane().add(btnSave);

    }
}
