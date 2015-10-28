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
import org.apache.http.impl.client.LaxRedirectStrategy;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
//import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClientBuilder;

import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.HttpEntity;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.commons.codec.binary.Base64;
import org.apache.http.client.methods.CloseableHttpResponse;

import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import org.apache.http.util.EntityUtils;

import java.io.File;
import java.util.Properties;
import java.io.InputStream;
import java.io.FileInputStream;
import java.io.IOException;

public class SendSMS {

    /**
     * @param args the command line arguments
     */
    private static final int TotalSMSfile = Integer.parseInt(Config.GetdataConfig("SMSNumFile"));

    public static void main(String[] args) throws Exception {
        //ShowConfig();
        Log.WriteLog("============== start !================");
        if ((Config.GetdataConfig("ISCreateFile")).equals("1")) {
            System.out.println("Creating File ");
            for (int i = 1; i <= TotalSMSfile; i++) {
                ReadData.callStoredProcINParameter(Config.GetdataConfig("SMSstored" + i), Config.GetdataConfig("SMSfileName" + i));
            }
        }
        if (Config.GetdataConfig("ISSendFile").equals("1")) {
            System.out.println("Sending File File ");
            for (int i = 1; i <= TotalSMSfile; i++) {
                try {
                    Log.WriteLog("SendFile" + Config.GetdataConfig("SMSfileName" + 1));
                    System.out.println("Sending File " + Config.GetdataConfig("SMSfileName" + i));
                    SendSMStoServer(Config.GetdataConfig("SMSfileName" + i));
                } catch (IOException ex) {
                    //Log.WriteLog(ex.getLocalizedMessage());
                    //ex.printStackTrace();
                }
            }
        }
        Log.WriteLog("============== Finish !================");
        //TestSendSMS();
        //private ReadData = new 
        // TODO code application logic here
    }

    private static void ShowConfig() {
        Properties prop = new Properties();
        InputStream input = null;
        try {

            input = new FileInputStream("Config.properties");

            // load a properties file
            prop.load(input);

            // get the property value and print it out
            System.out.println(prop.getProperty("database"));
            System.out.println(prop.getProperty("dbuser"));
            System.out.println(prop.getProperty("dbpassword"));

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
        }
    }

    private static void SendSMStoServer(String SMSfileName) throws IOException {
        System.out.println("Start SendFile SMS");
        LaxRedirectStrategy redirectStrategy = new LaxRedirectStrategy();
        CloseableHttpClient httpclient = HttpClients.custom().setRedirectStrategy(redirectStrategy).build();
        try {
            //String i = "1";
            HttpPost httppost = new HttpPost(Config.GetdataConfig("SMSServerUrl"));
            //for (int i = 1; i <= TotalSMSfile; i++) {
            FileBody smsFile = new FileBody(new File(SMSfileName + Config.GetCurrentDate() + ".csv"));

            HttpEntity reqEntity = MultipartEntityBuilder.create()
                    .addTextBody("user", Config.GetdataConfig("SMSUser"))
                    .addTextBody("pass", Base64.encodeBase64String(Config.GetdataConfig("SMSPassword").getBytes()))
                    .addPart("file_content", smsFile)
                    .build();
            httppost.setEntity(reqEntity);

            System.out.println("Upload File :" + SMSfileName + Config.GetCurrentDate() + ".csv");
            System.out.println("executing request " + httppost.getRequestLine());
            CloseableHttpResponse response = httpclient.execute(httppost);
            try {

                System.out.println(response.getStatusLine());
                HttpEntity resEntity = response.getEntity();
                if (resEntity != null) {
                    Log.WriteLog("Upoad File :" + SMSfileName + "Complete || Response : " + EntityUtils.toString(resEntity));
                    System.out.println("Response content length: " + resEntity.getContentLength());
                    System.out.println(EntityUtils.toString(resEntity));
                }
                EntityUtils.consume(resEntity);
            } finally {
                response.close();
            }
            //}
        } catch (IOException ex) {
            Log.WriteLog("Send File Failed " + ex.getLocalizedMessage());
        } finally {
            httpclient.close();
        }
    }

    private static void TestSendSMS() throws IOException {
        LaxRedirectStrategy redirectStrategy = new LaxRedirectStrategy();
        CloseableHttpClient httpclient = HttpClients.custom().setRedirectStrategy(redirectStrategy).build();
        try {
            System.out.println("Start SendFile SMS");
            HttpPost httppost = new HttpPost("http://172.18.35.62/smsgateway/ws/service/provider/sendsms");

            FileBody smsFile = new FileBody(new File("SMS.csv"));

            HttpEntity reqEntity = MultipartEntityBuilder.create()
                    .addTextBody("user", "smsbaaclife")
                    .addTextBody("pass", Base64.encodeBase64String("smsbaaclife".getBytes()))
                    .addPart("file_content", smsFile)
                    .build();

            httppost.setEntity(reqEntity);

            System.out.println("executing request " + httppost.getRequestLine());
            CloseableHttpResponse response = httpclient.execute(httppost);
            try {

                System.out.println(response.getStatusLine());
                HttpEntity resEntity = response.getEntity();
                if (resEntity != null) {
                    System.out.println("Response content length: " + resEntity.getContentLength());
                    System.out.println(EntityUtils.toString(resEntity));

                }
                EntityUtils.consume(resEntity);
            } finally {
                response.close();
            }
        } finally {
            httpclient.close();
        }

    }
}
