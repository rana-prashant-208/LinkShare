package com.codingbucket.linkshare;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;

import com.codingbucket.linkshare.SpringServer.SpringServerHandler;
public class LinkHandler extends Activity {
   BaseLinkHandler baseLinkHandler=new SpringServerHandler();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (getIntent().hasExtra("ACTION")) {
            handleAction(getIntent().getExtras().getString("ACTION"));
        }
        super.onCreate(savedInstanceState);
        finish();
    }
    public static String getHostName(Context context) {
        SharedPreferences sharedPref = context.getSharedPreferences(
                context.getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        return sharedPref.getString(context.getString(R.string.hostAddress), "http://192.168.0.104:8080");
    }
    public void handleAction(String action)  {
        switch (action){
            case "OPEN":
                String link=baseLinkHandler.loadLink(getHostName(this));
                Utils.setClipboardText(this,link);
                Utils.openText(this,link);
                break;
            case "MUTE":
                LaptopControlsHandler.muteClicked(null, this);
                break;

            case "UNMUTE":
                LaptopControlsHandler.unmuteClicked(null, this);
                break;
            }
    }
    public static void sendLink(String text, Context context) {
        BaseLinkHandler baseLinkHandler=new SpringServerHandler();
        boolean sent = baseLinkHandler.sendLink(text,getHostName(context));
        Toast.makeText(context, sent?"Link shared ":"Failed to Send", Toast.LENGTH_LONG).show();
    }
    public static void openLink(String host,Context context) {
        String link=new SpringServerHandler().loadLink(host);
        Utils.setClipboardText(context,link);
        Utils.openText(context,link);
    }

}

