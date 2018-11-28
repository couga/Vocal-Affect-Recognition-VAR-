package au.edu.federation.var;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.*;
import java.awt.event.*;
import java.io.File;
import java.sql.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.nio.file.*;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.ArrayUtils;
import com.opencsv.CSVWriter;

public class MainForm {
    private JButton saveDataButton;
    private JPanel mainPanel;
    private JTextField textField1;
    private JTextArea textOutput;
    private JButton startspeechtotext;
    private JButton startEmotionalAnalysisButton;
    private JButton openAudacityButton;
    private JButton moveSTTFileButton;
    private JButton startWekaButton;
    private JButton moveEmoVoiceFileButton;
    private JButton openEmoVoiceFileButton;
    private JButton viewKeywordsButton;
    private JButton enterKeywordButton;
    private JTextField textField2;
    private JButton OKButton;
    private JButton resetButton;
    private JButton transposeButton;


    public MainForm() {
        //TI 15/2: This line below is fine if you are running the program for the first time with varDB already created.
        //          If varDB is already created then the program fails to continue.
        //TI 15/2 QUICKFIX: On first time run, just run the SQLite class individually first, then run the program.

        //TI 15/2: This line of code is now safe due to including 'create table if not exists' within SQLite class

        SQLite.main(null);

        saveDataButton.setEnabled(false);
        startspeechtotext.setEnabled(false);
        startEmotionalAnalysisButton.setEnabled(false);
        openAudacityButton.setEnabled(false);
        moveSTTFileButton.setEnabled(false);
        startWekaButton.setEnabled(false);
        moveEmoVoiceFileButton.setEnabled(false);
        openEmoVoiceFileButton.setEnabled(false);
        viewKeywordsButton.setEnabled(false);
        enterKeywordButton.setEnabled(false);
        textField2.setEnabled(false);
        OKButton.setEnabled(false);
        resetButton.setEnabled(false);
        transposeButton.setEnabled(false);


        saveDataButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                File dir = new File(textField1.getText());
                dir.mkdir();

                startspeechtotext.setEnabled(true);
                startEmotionalAnalysisButton.setEnabled(true);
                openAudacityButton.setEnabled(true);
                moveSTTFileButton.setEnabled(true);
                startWekaButton.setEnabled(true);
                moveEmoVoiceFileButton.setEnabled(true);
                resetButton.setEnabled(true);

                System.out.println("A directory for the " + textField1.getText() + " session has been created. \n");
                textOutput.append("A directory for the " + textField1.getText() + " session has been created. \n");

                //Create a table in the database for each session
                Connection c = null;

                try {

                    Class.forName("org.sqlite.JDBC");
                    c = DriverManager.getConnection("jdbc:sqlite:varDB.db");

                    String query = "CREATE TABLE IF NOT EXISTS "+dir+" (\n" +
                                   " id integer PRIMARY KEY AUTOINCREMENT,\n" +
                                   " words text\n" +
                                   ");";
                    PreparedStatement pst = c.prepareStatement(query);

                    pst.execute();

                    pst.close();



                } catch (Exception f) {
                    f.printStackTrace();
                }

            }
        });

        //Open Audacity Button
        openAudacityButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try
                {
                    Runtime.getRuntime().exec("Audacity/audacity.exe"); //Open Audacity's executable
                    System.out.println("Audio recording software has opened. \n"); //Write message to console
                    textOutput.append("Audio recording software has opened.\n"); //Output message in 'Task Description' area
                } catch (IOException t) {
                    t.printStackTrace();
                }
            }
        });

        //Start Speech-to-Text Button
        startspeechtotext.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    Runtime.getRuntime().exec("powershell /C start startsphinx.bat"); //Start Sphinx in Command Prompt (for text output)
                    Runtime.getRuntime().exec("powershell /C start startsphinx2.bat"); //Start Sphinx in PowerShell (for live viewing)
                    System.out.println("Speech to text has started. \n"); //Write message to console
                    textOutput.append("Speech to text has started. \n"); //Output message in 'Task Description' area
                    startspeechtotext.setEnabled(false); //Disable start button until session is reset
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        });

        //Start Emotional Analysis Button
        startEmotionalAnalysisButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    Runtime.getRuntime().exec("cmd /C start startemovoice.bat"); //Start EmoVoice in Command Prompt
                    System.out.println("Emotional Analysis started. \n"); //Write message to console
                    textOutput.append("Emotional Analysis has started. \n"); //Output message in 'Task Description' area
                    startEmotionalAnalysisButton.setEnabled(false); //Disable start button until session is reset
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        });


        moveSTTFileButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try
                {
                    //Runtime.getRuntime().exec("cmd /C start C:/Users/thoma/Documents/VAR/sphinx/pocketsphinx/testfile.bat");
                    //Runtime.getRuntime().exec("cmd /C start copy C:/Users/thoma/Documents/VAR/sphinx/pocketsphinx/words.csv C:/Users/thoma/Documents/VAR/words2.csv");


                    //OR runtime.exec("notepad");
                    File dir = new File(textField1.getText());
                    File destDir = new File("../VAR/" + dir);
                    File srcFile = new File("sphinx/pocketsphinx/words.csv");
                    FileUtils.copyFileToDirectory(srcFile, destDir);

                    System.out.println("Speech to text output file has been copied into the " + textField1.getText() + " folder. \n");
                    textOutput.append("Speech to text output file has been copied into the " + textField1.getText() + " folder.  \n");

                    viewKeywordsButton.setEnabled(true);
                    enterKeywordButton.setEnabled(true);
                }
                catch (IOException t)
                {
                    t.printStackTrace();
                }
                Connection c = null;
                try {
                    Class.forName("org.sqlite.JDBC");
                    c = DriverManager.getConnection("jdbc:sqlite:varDB.db");

                    String sesh = textField1.getText();

                    BufferedReader br = new BufferedReader(new FileReader(sesh+"/words.csv"));
                    //br.readLine().replace("'","zzz");

                    String line = "";
                    line=br.readLine(); //used to ignore the first line in the csv (allocating buffer line)

                    while((line=br.readLine())!=null){
                        String[] value=line.split(",");//seperator...
                        value[0] = value[0].replace("'", "zzz");
                        System.out.println(value[0] + "\n");

                        String sql="insert into "+sesh+" (words)" +
                                "values ('"+value[0]+"')";

                        PreparedStatement pst=c.prepareStatement(sql);

                        pst.execute();
                        pst.close();

                    }
                    br.close();
                    System.out.println("Speech translation imported into database. \n");
                    textOutput.append("Speech translation imported into database.  \n");

                } catch (Exception f) {
                    f.printStackTrace();
                }

            }
        });


        startWekaButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    Runtime.getRuntime().exec("cmd /C start startweka.bat");
                    System.out.println("Weka started. \n");
                    textOutput.append("Weka has started.\n");

                    /*
                    //creates a folder with the current datetime as title
                    Date now = new Date();
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd hh mm ss");
                    String time = dateFormat.format(now);
                    File dir = new File(time);
                    dir.mkdir();
                    */

                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        });


        textField1.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                super.keyTyped(e);
                    saveDataButton.setEnabled(true);
            }
        });
        moveEmoVoiceFileButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    File dir = new File(textField1.getText());
                    File destDir = new File("../VAR/" + dir);
                    File srcFile = new File("emovoice-master/result.events");
                    FileUtils.copyFileToDirectory(srcFile, destDir);
                    System.out.println("Emotional analysis output file has been copied into the " + textField1.getText() + " folder. \n");
                    textOutput.append("Emotional analysis output file has been copied into the " + textField1.getText() + " folder.  \n");
                    openEmoVoiceFileButton.setEnabled(true);


                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        });
        openEmoVoiceFileButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                try {
                    Runtime.getRuntime().exec("cmd /C start convertevents.bat");
                    System.out.println("Events file opening and saving as CSV in progress. \n");
                    textOutput.append("Events file opening and saving as CSV in progress. \n");

                    transposeButton.setEnabled(true);

                } catch (IOException e1) {
                    e1.printStackTrace();
                }


            }
        });

        viewKeywordsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Connection c = null;
                try {
                    Class.forName("org.sqlite.JDBC");
                    c = DriverManager.getConnection("jdbc:sqlite:varDB.db");

                    String sesh = textField1.getText();

                    //PreparedStatement pst = c.prepareStatement(query);
                    Statement stmt = c.createStatement();
                    ResultSet rs = stmt.executeQuery("select 'Happy' [Word],                  " +
                            "       count(words) [Occurance]  " +
                            "  from "+sesh+"                  " +
                            " where words like '%happy%'      " +
                            " union                           " +
                            "select 'Sad',                    " +
                            "       count(words)              " +
                            "  from "+sesh+"                  " +
                            " where words like '%sad%'        " +
                            " union                           " +
                            "select 'Angry',                  " +
                            "       count(words)              " +
                            "  from "+sesh+"                  " +
                            " where words like '%angry%'      " +
                            " union                           " +
                            "select 'Moody',                    " +
                            "       count(words)              " +
                            "  from "+sesh+"                  " +
                            " where words like '%moody%'        " +
                            " union                           " +
                            "select 'Smile',                    " +
                            "       count(words)              " +
                            "  from "+sesh+"                  " +
                            " where words like '%Smile%'        " +
                            " union                           " +
                            "select 'Frown',                    " +
                            "       count(words)              " +
                            "  from "+sesh+"                  " +
                            " where words like '%Frown%'        " +
                            " union                           " +
                            "select 'Fun',                    " +
                            "       count(words)              " +
                            "  from "+sesh+"                  " +
                            " where words like '%Fun%'        " +
                            " union                           " +
                            "select 'Funny',                    " +
                            "       count(words)              " +
                            "  from "+sesh+"                  " +
                            " where words like '%Funny%'        " +
                            " union                           " +
                            "select 'Lonely',                  " +
                            "       count(words)              " +
                            "  from "+sesh+"                  " +
                            " where words like '%lonely%'      " +
                            " union                           " +
                            "select 'Miserable',                    " +
                            "       count(words)              " +
                            "  from "+sesh+"                  " +
                            " where words like '%miserable%'        " +
                            " union                           " +
                            "select 'No',                    " +
                            "       count(words)              " +
                            "  from "+sesh+"                  " +
                            " where words like '%no%'        " +
                            " union                           " +
                            "select 'Yes',                    " +
                            "       count(words)              " +
                            "  from "+sesh+"                  " +
                            " where words like '%Yes%'        " +
                            " union                           " +
                            "select 'Good',                    " +
                            "       count(words)              " +
                            "  from "+sesh+"                  " +
                            " where words like '%good%'        " +
                            " union                           " +
                            "select 'Bad',                    " +
                            "       count(words)              " +
                            "  from "+sesh+"                  " +
                            " where words like '%bad%'        " +
                            " union                           " +
                            "select 'Nervous',                  " +
                            "       count(words)              " +
                            "  from "+sesh+"                  " +
                            " where words like '%nervous%'      " +
                            " union                           " +
                            "select 'Hate',                    " +
                            "       count(words)              " +
                            "  from "+sesh+"                  " +
                            " where words like '%hate%'        " +
                            " union                           " +
                            "select 'Kill',                    " +
                            "       count(words)              " +
                            "  from "+sesh+"                  " +
                            " where words like '%kill%'        " +
                            " union                           " +
                            "select 'Pain',                    " +
                            "       count(words)              " +
                            "  from "+sesh+"                  " +
                            " where words like '%pain%'        " +
                            " union                           " +
                            "select 'Hurt',                    " +
                            "       count(words)              " +
                            "  from "+sesh+"                  " +
                            " where words like '%hurt%'        " +
                            " union                           " +
                            "select 'Broken',                    " +
                            "       count(words)              " +
                            "  from "+sesh+"                  " +
                            " where words like '%Broken%'        " +
                            " union                           " +
                            "select 'Panic',                  " +
                            "       count(words)              " +
                            "  from "+sesh+"                  " +
                            " where words like '%panic%'      " +
                            " union                           " +
                            "select 'Scared',                    " +
                            "       count(words)              " +
                            "  from "+sesh+"                  " +
                            " where words like '%scared%'        " +
                            " union                           " +
                            "select 'Tired',                    " +
                            "       count(words)              " +
                            "  from "+sesh+"                  " +
                            " where words like '%tired%'        " +
                            " union                           " +
                            "select 'Empty',                    " +
                            "       count(words)              " +
                            "  from "+sesh+"                  " +
                            " where words like '%empty%'        " +
                            " union                           " +
                            "select 'Suicide',                    " +
                            "       count(words)              " +
                            "  from "+sesh+"                  " +
                            " where words like '%suicide%'        " +
                            " order by 2 desc      ");

                    while (rs.next()) {
                        String word = rs.getString("Word");
                        String occurance = rs.getString("Occurance");
                        System.out.println("Keyword: " + word + " spoken " + occurance + " times. \n");
                        textOutput.append("Keyword: " + word + " spoken " + occurance + " times. \n");
                    }

                    c.close();


                } catch (Exception f) {
                    f.printStackTrace();
                }



            }
        });


        enterKeywordButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                textField2.setEnabled(true);
            }
        });

        textField2.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                super.keyTyped(e);
                OKButton.setEnabled(true);
            }
        });

        OKButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Connection c = null;


                try {
                    Class.forName("org.sqlite.JDBC");
                    c = DriverManager.getConnection("jdbc:sqlite:varDB.db");

                    String sesh = textField1.getText();
                    String keyword = textField2.getText();

                    //PreparedStatement pst = c.prepareStatement(query);
                    Statement stmt = c.createStatement();
                    ResultSet rs = stmt.executeQuery("select '"+keyword+"' [Word],                  " +
                            "       count(words) [Occurance]  " +
                            "  from "+sesh+"                  " +
                            " where words like '%"+keyword+"%'      ");

                    while (rs.next()) {
                        String word = rs.getString("Word");
                        String occurance = rs.getString("Occurance");
                        System.out.println("Selected word: " + word + " spoken " + occurance + " times. \n");
                        textOutput.append("Selected word: " + word + " spoken " + occurance + " times. \n");
                    }

                    c.close();


                } catch (Exception f) {
                    f.printStackTrace();
                }
            }
        });
        resetButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                saveDataButton.setEnabled(false);
                startspeechtotext.setEnabled(false);
                startEmotionalAnalysisButton.setEnabled(false);
                openAudacityButton.setEnabled(false);
                moveSTTFileButton.setEnabled(false);
                startWekaButton.setEnabled(false);
                moveEmoVoiceFileButton.setEnabled(false);
                openEmoVoiceFileButton.setEnabled(false);
                viewKeywordsButton.setEnabled(false);
                enterKeywordButton.setEnabled(false);
                textField2.setEnabled(false);
                OKButton.setEnabled(false);
                resetButton.setEnabled(false);
                transposeButton.setEnabled(false);
                textField1.setText("");
                textField2.setText("");
                textOutput.append("Session reset. \n");
            }
        });
        transposeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Connection c = null;

                String dir = textField1.getText();

                //CREATE THE TABLE TO STORE EMOVOICE ANALYSIS
                try {

                    Class.forName("org.sqlite.JDBC");
                    c = DriverManager.getConnection("jdbc:sqlite:varDB.db");

                    String query = "CREATE TABLE IF NOT EXISTS "+dir+"_emo (\n" +
                            " timestamp integer,\n" +
                            " emotion text,\n" +
                            " value double \n" +
                            ");";
                    PreparedStatement pst = c.prepareStatement(query);

                    pst.execute();

                    pst.close();

                    System.out.println("Emotional analysis data has been imported into the database. \n");
                    textOutput.append("Emotional analysis data has been imported into the database.. \n");

                } catch (Exception f) {
                    f.printStackTrace();
                }

                //IMPORT THE FILE INTO THE DATABASE
                try {
                    Class.forName("org.sqlite.JDBC");
                    c = DriverManager.getConnection("jdbc:sqlite:varDB.db");

                    BufferedReader br = new BufferedReader(new FileReader(dir+"/result.csv"));
                    //br.readLine().replace("'","zzz");

                    String line = "";
                    line=br.readLine(); //used to ignore the first line in the csv (header row)

                    while((line=br.readLine())!=null){
                        String[] value=line.split(",");//seperator...
                        value[0] = value[0].replace("'", "zzz");

                        //System.out.println(value[0] + "\n");

                        String sql="insert into "+dir+"_emo (timestamp,emotion,value)" +
                                "values ('"+value[3]+"','"+value[9]+"','"+value[10]+"')";
                        PreparedStatement pst=c.prepareStatement(sql);
                        pst.execute();
                        ////JOptionPane.showMessageDialog(null, "Data uploaded");
                        pst.close();

                    }
                    br.close();
                    System.out.println("Data imported into database. \n");
                    textOutput.append("Data imported into database. \n");

                } catch (Exception f) {
                    f.printStackTrace();
                }

                //TRANSPOSE THE IMPORTED DATA AND SAVE TO NEW FILE
                try {
                    Class.forName("org.sqlite.JDBC");
                    c = DriverManager.getConnection("jdbc:sqlite:varDB.db");

                    //String sesh = textField1.getText();
                    String keyword = textField2.getText();

                    //PreparedStatement pst = c.prepareStatement(query);
                    Statement stmt = c.createStatement();
                    ResultSet rs = stmt.executeQuery("select i.timestamp\n" +
                            "      ,'anger'\n" +
                            "      ,anger_value.value anger_value\n" +
                            "      ,'calmness'\n" +
                            "      ,calmness_value.value calmness_value\n" +
                            "      ,'disgust'\n" +
                            "      ,disgust_value.value disgust_value\n" +
                            "      ,'fear'\n" +
                            "      ,fear_value.value fear_value\n" +
                            "      ,'happiness'\n" +
                            "      ,happiness_value.value happiness_value\n" +
                            "      ,'neutral'\n" +
                            "      ,neutral_value.value neutral_value\n" +
                            "      ,'sadness'\n" +
                            "      ,sadness_value.value sadness_value\n" +
                            "      ,'surprise'\n" +
                            "      ,surprise_value.value surprise_value\n" +
                            "  from "+dir+"_emo i\n" +
                            "  join (select timestamp\n" +
                            "              ,value\n" +
                            "          from "+dir+"_emo\n" +
                            "         where emotion = 'anger'\n" +
                            "       ) anger_value on anger_value.timestamp = i.timestamp\n" +
                            "  join (select timestamp\n" +
                            "              ,value\n" +
                            "          from "+dir+"_emo\n" +
                            "         where emotion = 'calmness'\n" +
                            "       ) calmness_value on calmness_value.timestamp = i.timestamp\n" +
                            "  join (select timestamp\n" +
                            "              ,value\n" +
                            "          from "+dir+"_emo\n" +
                            "         where emotion = 'disgust'\n" +
                            "       ) disgust_value on disgust_value.timestamp = i.timestamp\n" +
                            "  join (select timestamp\n" +
                            "              ,value\n" +
                            "          from "+dir+"_emo\n" +
                            "         where emotion = 'fear'\n" +
                            "       ) fear_value on fear_value.timestamp = i.timestamp\n" +
                            "  join (select timestamp\n" +
                            "              ,value\n" +
                            "          from "+dir+"_emo\n" +
                            "         where emotion = 'happiness'\n" +
                            "       ) happiness_value on happiness_value.timestamp = i.timestamp\n" +
                            " join (select timestamp\n" +
                            "              ,value\n" +
                            "          from "+dir+"_emo\n" +
                            "         where emotion = 'neutral'\n" +
                            "       ) neutral_value on neutral_value.timestamp = i.timestamp\n" +
                            " join (select timestamp\n" +
                            "              ,value\n" +
                            "          from "+dir+"_emo\n" +
                            "         where emotion = 'sadness'\n" +
                            "       ) sadness_value on sadness_value.timestamp = i.timestamp\n" +
                            " join (select timestamp\n" +
                            "              ,value\n" +
                            "          from "+dir+"_emo\n" +
                            "         where emotion = 'surprise'\n" +
                            "       ) surprise_value on surprise_value.timestamp = i.timestamp\n" +
                            " group by i.timestamp\n" +
                            "         ,'anger'\n" +
                            "         ,anger_value.value\n" +
                            "         ,'calmness'\n" +
                            "         ,calmness_value.value\n" +
                            "         ,'disgust'\n" +
                            "         ,disgust_value.value\n" +
                            "         ,'fear'\n" +
                            "         ,fear_value.value\n" +
                            "         ,'happiness'\n" +
                            "         ,happiness_value.value\n" +
                            "         ,'neutral'\n" +
                            "         ,neutral_value.value\n" +
                            "         ,'sadness'\n" +
                            "         ,sadness_value.value\n" +
                            "         ,'surprise'\n" +
                            "         ,surprise_value.value");



                    CSVWriter writer = new CSVWriter(new FileWriter(dir+"/importEMOforweka.csv"), '\t');
                    Boolean includeHeaders = true;

                    java.sql.ResultSet myResultSet = rs; //your resultset logic here

                    writer.writeAll(myResultSet, includeHeaders);

                    writer.close();

                    c.close();

                    System.out.println("Transposed. \n");
                    textOutput.append("Transposed. \n");


                } catch (Exception f) {
                    f.printStackTrace();
                }
            }
        });
    }


    public static void main(String[] args) {
        JFrame frame = new JFrame("Voice Affect Recognition System");
        frame.setContentPane(new MainForm().mainPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

}
