package com.codingbucket.linkshare.SpringServer;

import android.content.Context;
import android.os.StrictMode;
import android.widget.Toast;

import com.codingbucket.linkshare.BaseLinkHandler;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class SpringServerHandler implements BaseLinkHandler {

    @Override
    public String loadLink(String host) {
        String text=decrypt(requestServer(host,"/retrieveurl/",null));
        return text;
    }

    private static String decrypt(String content) {
        if(content==null||content.equalsIgnoreCase("")){
            return "";
        }
        System.out.println("decrypt: "+content);
        StringBuilder str=new StringBuilder();
        String[] splits = content.replace("[", "").replace("]", "").replace(" ","").split(",");
        for (String character:splits
        ) {
            str.append((char)(Integer.parseInt(character)));
        }
        return str.toString();
    }

    @Override
    public boolean sendLink(String text, String host) {
        requestServer(host,"/openurl/"+ encryptText(text),null);
        return true;
    }

    public static String encryptText(String text) {
        List<String> arr = new ArrayList<>();
        for (int i = 0; i < text.length(); i++) {
            arr.add(String.valueOf((int) text.charAt(i)));
        }
        return arr.toString().replace("[","").replace("]","").replace(" ","");
    }
    public static boolean executRemotecomand(String command, String host, Context c){
        requestServer(host, "/laptopcontrol/" + command, c);
        Toast.makeText(c, "Done", Toast.LENGTH_SHORT).show();
       return true;
    }
    public static String executRemotecomand(String command, String host, Context c,Boolean getResponse){
        String resp = requestServer(host, "/laptopcontrol/" + command, c);
        Toast.makeText(c, "Done", Toast.LENGTH_SHORT).show();
        return resp;
    }
    public static synchronized String requestServer(String host, String command,Context c){
        try {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
            URL url = new URL(host+command);
            System.out.println(url.toString());
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            BufferedReader rd = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String content = "", line;
            while ((line = rd.readLine()) != null) {
                content += line;
            }
            connection.disconnect();
            return content;
        }
        catch (Exception e) { e.printStackTrace();
            return null;
        }
    }
}
