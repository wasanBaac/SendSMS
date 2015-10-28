/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sendsms;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Properties;

/**
 *
 * @author Wasan
 */
public class Config {
    public static String GetdataConfig(String ConfigName){
        Properties prop = new Properties();
        InputStream input = null;
        try {
            
            input = new FileInputStream("Config.properties");
            String propFileName = "config.properties";
            // load a properties file
            prop.load(input);

            // get the property value and print it out
            // System.out.println(prop.getProperty("database"));

        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return prop.getProperty(ConfigName);
        }
        
    }
    
    public static String GetCurrentDate(){
        return new SimpleDateFormat("yyyyMMdd").format(Calendar.getInstance().getTime());
    }
    
    public static String GetCurrentDateFull(){
        return new SimpleDateFormat("yyyyMMdd HH:mm:ss.S").format(Calendar.getInstance().getTime());
        
    }
    
}
