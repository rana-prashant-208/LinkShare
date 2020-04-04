package com.codingbucket.linkshare.pastebin;

import android.os.StrictMode;

import com.codingbucket.linkshare.BaseLinkHandler;
import com.codingbucket.linkshare.pastebin.pastebinapi.Paste;
import com.codingbucket.linkshare.pastebin.pastebinapi.PastebinAPI;
import com.codingbucket.linkshare.pastebin.pastebinapi.PastebinException;
import com.codingbucket.linkshare.pastebin.pastebinapi.PrivacyLevel;
import com.codingbucket.linkshare.pastebin.pastebinapi.User;

import java.util.ArrayList;
import java.util.List;

public class PasteBin implements BaseLinkHandler {
    private static final String USER_KEY ="15b4351df7b49dfe5afbe19156ff6a83" ;
    //    private static final String USER_KEY ="3c0ea986e6823d704a9c37cbd012d01c" ;//lor
    private static final String API_KEY ="4b1eeb2d669edd48fea87d1bb459d997" ;
    //    private static final String API_KEY ="48486678f8e58508457b9bc1b8b083ae" ;//lor
    PastebinAPI api = null;
    User user =null;
    public PasteBin(){
        api=new PastebinAPI(API_KEY);
        user = new User(api);
        user.setUserKey(USER_KEY);
    }
    @Override
    public String loadLink() {
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
        try {
            url=getUrl(url);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return url;
    }

    @Override
    public boolean sendLink(String text) {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                .permitAll().build();
        StrictMode.setThreadPolicy(policy);
        try {
            String output = user.createPaste()
                    .withName("From_phone")
                    .withText(getText(text))
                    .withPrivacyLevel(PrivacyLevel.PUBLIC)
                    .post();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
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
    public static String getText(String text) {
        List<String> arr = new ArrayList<>();
        for (int i = 0; i < text.length(); i++) {
            arr.add(String.valueOf((int) text.charAt(i)));
        }
        return arr.toString();
    }
}
