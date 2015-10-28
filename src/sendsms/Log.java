/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sendsms;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 *
 * @author Wasan
 */
public class Log {

    public static void WriteLog(String strlog) throws IOException {
        BufferedWriter writer = null;
        writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("Log/Log" + Config.GetCurrentDate() + ".txt", true), "UTF-8"));
        writer.append(Config.GetCurrentDateFull() + " : " + strlog + System.getProperty("line.separator"));
        if (writer != null) {
            writer.close();
        }
        //Files.write(Paths.get("Log/" + Config.GetCurrentDate() + ".txt"), strlog.getBytes(), StandardOpenOption.APPEND);
    }
}
