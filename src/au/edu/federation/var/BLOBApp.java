package au.edu.federation.var;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.sql.*;

public class BLOBApp {

    private byte[] readFile(String file) {
            ByteArrayOutputStream bos = null;
            try {
                File f = new File(file);
                FileInputStream fis = new FileInputStream(f);
                byte[] buffer = new byte[1024];
                bos = new ByteArrayOutputStream();
                for (int len; (len = fis.read(buffer)) != -1;) {
                    bos.write(buffer, 0, len);
                }
            } catch (FileNotFoundException e) {
                System.err.println(e.getMessage());
            } catch (IOException e2) {
                System.err.println(e2.getMessage());
            }
            return bos != null ? bos.toByteArray() : null;

    }

    public void updatePicture(int AudioID, String filename) {

        Connection c = null;

        // update sql
        String updateSQL = "UPDATE Audio "
                + "SET File = ? "
                + "WHERE AudioID = ?";

        try  {
            Class.forName("org.sqlite.JDBC");
            c = DriverManager.getConnection("jdbc:sqlite:varDB.db");

            PreparedStatement pstmt = c.prepareStatement(updateSQL);

            // set parameters
            pstmt.setBytes(1, readFile(filename));
            pstmt.setInt(2, AudioID);

            pstmt.executeUpdate();
            System.out.println("Stored the file in the BLOB column.");

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        BLOBApp app = new BLOBApp();
        app.updatePicture(1, "C:/Users/thoma/Documents/VAR/AudioSamples/clinton_1992_tammy.wav");
    }
}
