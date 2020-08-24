package com.codingbucket.linkshare;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.View;
import android.widget.Toast;

import com.codingbucket.linkshare.SpringServer.SpringServerHandler;

public class LaptopControlsHandler {
    public static void muteClicked(View view,Context ctx){
        Toast.makeText(ctx, "Request Sent", Toast.LENGTH_SHORT).show();
        SpringServerHandler.executRemotecomand("mute",getHostName(ctx),ctx);
    }
    public static void unmuteClicked(View view,Context ctx){
        Toast.makeText(ctx, "Request Sent", Toast.LENGTH_SHORT).show();
        SpringServerHandler.executRemotecomand("unmute",getHostName(ctx),ctx);
    }
    public static void sleepClicked(View view,Context ctx){
        Toast.makeText(ctx, "Request Sent", Toast.LENGTH_SHORT).show();
        SpringServerHandler.executRemotecomand("sleep",getHostName(ctx),ctx);
    }
    public static void volumeUpClicked(View view,Context ctx){
        Toast.makeText(ctx, "Request Sent", Toast.LENGTH_SHORT).show();
        SpringServerHandler.executRemotecomand("increase",getHostName(ctx),ctx);
    }
    public static void volumeDownClicked(View view,Context ctx){
        Toast.makeText(ctx, "Request Sent", Toast.LENGTH_SHORT).show();
        SpringServerHandler.executRemotecomand("decrease",getHostName(ctx),ctx);

    }
    public static void lockClicked(View view,Context ctx){
        Toast.makeText(ctx, "Request Sent", Toast.LENGTH_LONG).show();
        SpringServerHandler.executRemotecomand("lock",getHostName(ctx),ctx);
    }
    public static void loadClicked(View view,Context ctx){
        Toast.makeText(ctx, "Loading", Toast.LENGTH_LONG).show();
        LinkHandler.openLink(getHostName(ctx),ctx);
    }
    public static String incomingCall(View view,Context ctx){
        return SpringServerHandler.executRemotecomand("incomingCall",getHostName(ctx),ctx,true);
    }
    public static void incomingCallDisconnected(View view,Context ctx){
        if(Utils.getSetting(ctx,"DID_MUTE_SERVER","true").equalsIgnoreCase("true")) {
            Toast.makeText(ctx, "Going to un-mute Laptop playback", Toast.LENGTH_SHORT).show();
            SpringServerHandler.executRemotecomand("incomingCallDisconnected", getHostName(ctx), ctx);
        }else{
            System.out.println("Server was already muted so not muting");
        }
    }

    public static String getHostName(Context context) {
        SharedPreferences sharedPref = context.getSharedPreferences(
                context.getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        return sharedPref.getString(context.getString(R.string.hostAddress), "");
    }
}
