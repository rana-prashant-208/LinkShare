package com.codingbucket.linkshare;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.widget.Toast;


import com.codingbucket.linkshare.pastebinapi.Paste;
import com.codingbucket.linkshare.pastebinapi.PastebinAPI;
import com.codingbucket.linkshare.pastebinapi.PastebinException;
import com.codingbucket.linkshare.pastebinapi.PrivacyLevel;
import com.codingbucket.linkshare.pastebinapi.User;

import java.util.ArrayList;
import java.util.List;

public class LinkHandler extends Activity {
    private static final String USER_KEY ="4b1eeb2d669edd48fea87d1bb459d997" ;
//    private static final String USER_KEY ="3c0ea986e6823d704a9c37cbd012d01c" ;//lor
    private static final String API_KEY ="15b4351df7b49dfe5afbe19156ff6a83" ;
//    private static final String API_KEY ="48486678f8e58508457b9bc1b8b083ae" ;//lor
    PastebinAPI api = new PastebinAPI(API_KEY);
    /* Getting a user */
    User user = new User(api);
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        user.setUserKey("3c0ea986e6823d704a9c37cbd012d01c");
        if (getIntent().hasExtra("ACTION")) {
            makeCalltoPasteBin(getIntent().getExtras().getString("ACTION"));
        }
        super.onCreate(savedInstanceState);
        finish();
    }

    private String getText(Intent intent) {
        return "";
    }

    private void makeCalltoPasteBin(String action)  {
        switch (action){
            case "OPEN":
                openLink();
                break;
            case "SEND":
                sendLink(getText(getIntent()));
                break;
            }
        finish();


    }
    public void sendLink(String text) {
        sendLink(text,this);
    }
    public void sendLink(String text, Context context) {
        user.setUserKey(USER_KEY);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                .permitAll().build();
        StrictMode.setThreadPolicy(policy);
        try {
            String output = user.createPaste()
                            .withName("From_phone")
                            .withText(getText(text))
                            .withPrivacyLevel(PrivacyLevel.PUBLIC)
                    .post();
            Toast.makeText(context, "Link shared ", Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String getText(String text) {
        List<String> arr = new ArrayList<>();
        for (int i = 0; i < text.length(); i++) {
            arr.add(String.valueOf((int) text.charAt(i)));
        }
        return arr.toString();
    }

    private void openLink() {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                .permitAll().build();
        StrictMode.setThreadPolicy(policy);
        Paste[] pastes = new Paste[0];
        try {

            pastes = user.getPastes();
        } catch (PastebinException e) {
            e.printStackTrace();
        }
        String key = pastes[0].getKey();
        String url = user.getRawPaste(key);
//        String url= null;
        try {
//            url = DocsQuickStart_test.getURL(this);
            url=getUrl(url);
        } catch (Exception e) {
            e.printStackTrace();
        }
        ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("From Linkshare", url);
        clipboard.setPrimaryClip(clip);
        Toast.makeText(this, "LinkShare " + url, Toast.LENGTH_LONG).show();
        try {
            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setData(Uri.parse(url));
            startActivity(i);
        }catch (Exception e){
            e.printStackTrace();

        }
        finish();
    }
    public String getUrl(String urlBase){
        StringBuilder str=new StringBuilder();
        String[] splits = urlBase.replace("[", "").replace("]", "").replace(" ","").split(",");
        for (String character:splits
        ) {
            str.append((char)(Integer.parseInt(character)));
        }
        return str.toString();
    }
}

