package au.edu.federation.var;

import java.sql.*;


//TI: Run this class to create the varDB database.


public class SQLite {

    public static void main( String args[] ) {
        Connection c = null;
        Statement stmt = null;

        try {
            Class.forName("org.sqlite.JDBC");
            c = DriverManager.getConnection("jdbc:sqlite:varDB.db");
            System.out.println("Opened database successfully");

            stmt = c.createStatement();
            String sql =    "CREATE TABLE IF NOT EXISTS AudioHistory                        " +
                            "(AudioHistoryID    INTEGER     PRIMARY KEY     AUTOINCREMENT,  " +
                            " AudioID           INTEGER,                                    " +
                            " HistoryID         INTEGER,                                    " +
                            " FOREIGN KEY (HistoryID) REFERENCES History(HistoryID),   " +
                            " FOREIGN KEY (AudioID) REFERENCES Audio(AudioID) );     " +

                            "CREATE TABLE IF NOT EXISTS History                             " +
                            "(HistoryID         INTEGER     PRIMARY KEY     AUTOINCREMENT,  " +
                            " PatientName       VARCHAR(80)                 NOT NULL,       " +
                            " InterviewerName   VARCHAR(80)                 NOT NULL,       " +
                            " InterviewDate     DATETIME,                                   " +
                            " Location          VARCHAR(80) );                              " +

                            "CREATE TABLE IF NOT EXISTS Audio                               " +
                            "(AudioID           INTEGER     PRIMARY KEY     AUTOINCREMENT,  " +
                            " FileName          VARCHAR(80)                 NOT NULL,       " +
                            " Type              VARCHAR(80)                 NOT NULL,       " +
                            " Length            INTEGER,                                    " +
                            " File              BLOB );                                     " +

                            "CREATE TABLE IF NOT EXISTS AudioAnalysis                       " +
                            "(AnalysisID        INTEGER     PRIMARY KEY     AUTOINCREMENT,  " +
                            " AudioID           INTEGER                     NOT NULL,       " +
                            " STTID             INTEGER,                                    " +
                            " EAID              INTEGER,                                    " +
                            " EAPID             INTEGER,                                    " +
                            " FOREIGN KEY (AudioID) REFERENCES Audio(AudioID) );     " +

                            "CREATE TABLE IF NOT EXISTS STTAnalysis                         " +
                            "(STTID             INTEGER,                                    " +
                            " ChunkNo           INTEGER,                                    " +
                            " Timestamp         DATETIME,                                   " +
                            " Text              TEXT,                                       " +
                            " FOREIGN KEY (STTID) REFERENCES AudioAnalysis(STTID) );        " +

                            "CREATE TABLE IF NOT EXISTS EmotionalAnalysis                   " +
                            "(EAID              INTEGER,                                    " +
                            " ChunkNo           INTEGER,                                    " +
                            " Timestamp         DATETIME,                                   " +
                            " Text              TEXT,                                       " +
                            " FOREIGN KEY (EAID) REFERENCES AudioAnalysis(EAID) );          " +

                            "CREATE TABLE IF NOT EXISTS PredictionAnalysis                  " +
                            "(EAPID             INTEGER,                                    " +
                            " ChunkNo           INTEGER,                                    " +
                            " Timestamp         DATETIME,                                   " +
                            " Text              TEXT,                                       " +
                            " FOREIGN KEY (EAPID) REFERENCES AudioAnalysis(EAPID) );        " ;
            stmt.executeUpdate(sql);
            stmt.close();
            c.close();
        } catch ( Exception e ) {
            System.err.println( e.getClass().getName() + ": " + e.getMessage() );
            System.exit(0);
        }
        System.out.println("Table created successfully");
    }
}
