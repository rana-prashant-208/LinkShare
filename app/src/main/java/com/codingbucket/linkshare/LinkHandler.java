package com.codingbucket.linkshare;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.widget.Toast;

import com.codingbucket.linkshare.pastebin.PasteBin;

public class LinkHandler extends Activity {
   BaseLinkHandler baseLinkHandler=new PasteBin();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (getIntent().hasExtra("ACTION")) {
            handleAction(getIntent().getExtras().getString("ACTION"));
        }
        super.onCreate(savedInstanceState);
        finish();
    }
    private void handleAction(String action)  {
        switch (action){
            case "OPEN":
                String link=baseLinkHandler.loadLink();
                Utils.setClipboardText(this,link);
                Utils.openText(this,link);
                break;
            case "SEND":
                String text=Utils.getClipboardText(this);
                baseLinkHandler.sendLink(text);
                Toast.makeText(this, "LinkShare "+text, Toast.LENGTH_LONG).show();
                break;
            }
    }
    public static void sendLink(String text, Context context) {
        BaseLinkHandler baseLinkHandler=new PasteBin();
        baseLinkHandler.sendLink(text);
        Toast.makeText(context, "Link shared ", Toast.LENGTH_LONG).show();
    }

}

