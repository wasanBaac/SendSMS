/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sendsms;

/**
 *
 * @author Wasan
 */
import com.microsoft.sqlserver.jdbc.SQLServerDriver;
import java.sql.*;
import java.sql.CallableStatement;
import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.SQLException;
//import java.io.File;
import java.io.IOException;
import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Properties;
import org.apache.commons.io.FileUtils;

public class ReadData {

    Properties prop = new Properties();
    InputStream input = null;

    private static final String DB_DRIVER = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
    private static final String DB_CONNECTION = "jdbc:sqlserver://" + Config.GetdataConfig("dbServer");
    private static final String DB_DATABASE = Config.GetdataConfig("dbName");//"baaclife_20150727";
    private static final String DB_USER = Config.GetdataConfig("dbUser");//"sa";
    private static final String DB_PASSWORD = Config.GetdataConfig("dbPassword");//"baac@123";

    public static void main(String[] args) {
//        try {
//            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
//            String connectionUrl = "jdbc:sqlserver://alldev;"
//                    + "databaseName=baaclife_20150727;user=sa;password=baac@123;";
//            Connection con = DriverManager.getConnection(connectionUrl);
//            callStoredProcINParameter();
//
//        } catch (SQLException e) {
//            System.out.println("SQL Exception: " + e.toString());
//        } catch (ClassNotFoundException cE) {
//            System.out.println("Class Not Found Exception: " + cE.toString());
//        }
    }

    public static void callStoredProcINParameter(String strStoredName, String strFileName) throws SQLException, IOException {
        Connection dbConnection = null;
        CallableStatement callableStatement = null;
        String insertStoreProc = "{call " + strStoredName + "}";
        try {
            dbConnection = getDBConnection();
            callableStatement = dbConnection.prepareCall(insertStoreProc);
            //callableStatement.setString(1, "11");
            // execute insertDBUSER store procedure
            ResultSet rs = callableStatement.executeQuery();
            BufferedWriter writer = null;
            String strText = "";
            int CntSMS = 0;
            //System.out.println("Generating File " + strFileName);
            while (rs.next()) {
                String CONTACTPHONE = rs.getString(1);
                String CONTENT = rs.getString(2);
                String SEND_DATE = rs.getString(3);
                String SEND_BY = rs.getString(4);
                String SERVICE_ID = rs.getString(5);
                String PRIORITY = rs.getString(6);
                String SITE_ID = rs.getString(7);
                //for (int i = 0; i < 5000; i++) {
                strText = strText + (CONTACTPHONE + "," + CONTENT + "," + SEND_DATE + "," + SEND_BY + "," + SERVICE_ID + "," + PRIORITY + "," + SITE_ID) + System.getProperty("line.separator");
                //}
                //FileUtils.writeStringToFile(new File("SendSMS.csv"), "strText");
                //System.out.println(CONTACTPHONE + "," + CONTENT + "," + SEND_DATE + "," + SEND_BY + "," + SERVICE_ID + "," + PRIORITY + "," + SITE_ID);
                CntSMS += 1;
            }
            System.out.println("Create File " + strFileName + Config.GetCurrentDate() + ".csv" + " Total : " + CntSMS + " Records");
            try {

                writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(strFileName + Config.GetCurrentDate() + ".csv"), "UTF-8"));
                writer.append(strText);

            } catch (IOException e) {
            } finally {
                try {
                    if (writer != null) {
                        writer.close();
                    }
                } catch (IOException e) {
                }
            }
            Log.WriteLog("File :" + strFileName + " Created! " + CntSMS + "Record");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            Log.WriteLog(e.getLocalizedMessage());
        } finally {

            if (callableStatement != null) {
                callableStatement.close();
            }

            if (dbConnection != null) {
                dbConnection.close();
            }

        }

    }

    private static Connection getDBConnection() throws IOException {
        Connection dbConnection = null;
        try {
            Class.forName(DB_DRIVER);
            String connectionUrl = DB_CONNECTION
                    + ";databaseName=" + DB_DATABASE + ";user=" + DB_USER + ";password=" + DB_PASSWORD + ";";
            dbConnection = DriverManager.getConnection(connectionUrl);

        } catch (SQLException e) {
            System.out.println("SQL Exception: " + e.toString());
            //Log.WriteLog(e.toString());
        } catch (ClassNotFoundException cE) {
            System.out.println("Class Not Found Exception: " + cE.toString());
            //Log.WriteLog(cE.toString());
        }
        return dbConnection;
    }

    private static java.sql.Date getCurrentDate() {
        java.util.Date today = new java.util.Date();
        return new java.sql.Date(today.getTime());
    }
}
