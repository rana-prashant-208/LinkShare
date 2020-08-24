package com.codingbucket.CallHandler;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.TelephonyManager;
import android.widget.Toast;

import com.codingbucket.linkshare.LaptopControlsHandler;
import com.codingbucket.linkshare.Utils;

import java.util.Date;

public class CallHandlerImpl extends BroadcastReceiver {

    //The receiver will be recreated whenever android feels like it.  We need a static variable to remember data between instantiations
    private static int lastState = TelephonyManager.CALL_STATE_IDLE;
    private static Date callStartTime;
    private static boolean isIncoming;
    private static String savedNumber;  //because the passed incoming is only valid in ringing



    @Override
    public void onReceive(Context context, Intent intent) {
        if(!Boolean.parseBoolean(Utils.getSetting(context,"Mute_ON_Call"))){
            System.out.println("Mute on call is not allowed");
            return;
        }
        //We listen to two intents.  The new outgoing call only tells us of an outgoing call.  We use it to get the number.
        if (intent.getAction().equals("android.intent.action.NEW_OUTGOING_CALL")) {
            savedNumber = intent.getExtras().getString("android.intent.extra.PHONE_NUMBER");
        } else {
            String stateStr = intent.getExtras().getString(TelephonyManager.EXTRA_STATE);
            String number = intent.getExtras().getString(TelephonyManager.EXTRA_INCOMING_NUMBER);
            int state = 0;
            if (stateStr.equals(TelephonyManager.EXTRA_STATE_IDLE)) {
                state = TelephonyManager.CALL_STATE_IDLE;
            } else if (stateStr.equals(TelephonyManager.EXTRA_STATE_OFFHOOK)) {
                state = TelephonyManager.CALL_STATE_OFFHOOK;
            } else if (stateStr.equals(TelephonyManager.EXTRA_STATE_RINGING)) {
                state = TelephonyManager.CALL_STATE_RINGING;
            }
            onCallStateChanged(context, state, number);
        }
    }

    protected void onIncomingCallReceived(Context ctx, String number, Date start) {
        System.out.println("INCOMING CALL Received");
        Toast.makeText(ctx, "Going to mute Laptop playback", Toast.LENGTH_SHORT).show();
        String resp = LaptopControlsHandler.incomingCall(null, ctx);
        if(resp.contains("Already mute")){
            System.out.println("Server was already muted");
            Utils.saveSetting(ctx,"DID_MUTE_SERVER","false");
        }else{
            Utils.saveSetting(ctx,"DID_MUTE_SERVER","true");
        }
    }

    protected void onIncomingCallAnswered(Context ctx, String number, Date start) {
        System.out.println("onIncomingCallAnswered");
    }


    protected void onIncomingCallEnded(Context ctx, String number, Date start, Date end) {
        System.out.println("onIncomingCallEnded");
        LaptopControlsHandler.incomingCallDisconnected(null, ctx);

    }

    protected void onOutgoingCallStarted(Context ctx, String number, Date start) {
        System.out.println("onOutgoingCallStarted");
        Toast.makeText(ctx, "Going to mute Laptop playback", Toast.LENGTH_SHORT).show();
        LaptopControlsHandler.incomingCall(null, ctx);
    }

    protected void onOutgoingCallEnded(Context ctx, String number, Date start, Date end) {
        //
        System.out.println("onOutgoingCallEnded");

        LaptopControlsHandler.incomingCallDisconnected(null, ctx);
    }

    protected void onMissedCall(Context ctx, String number, Date start) {
        //
        System.out.println("onMissedCall");
        LaptopControlsHandler.incomingCallDisconnected(null, ctx);
    }
    //Deals with actual events
    //Incoming call-  goes from IDLE to RINGING when it rings, to OFFHOOK when it's answered, to IDLE when its hung up
    //Outgoing call-  goes from IDLE to OFFHOOK when it dials out, to IDLE when hung up
    public void onCallStateChanged(Context context, int state, String number) {
        System.out.println(number);
        if (lastState == state) {
            //No change, debounce extras
            return;
        }
        switch (state) {
            case TelephonyManager.CALL_STATE_RINGING:
                isIncoming = true;
                callStartTime = new Date();
                savedNumber = number;
                onIncomingCallReceived(context, number, callStartTime);
                break;
            case TelephonyManager.CALL_STATE_OFFHOOK:
                //Transition of ringing->offhook are pickups of incoming calls.  Nothing done on them
                if (lastState != TelephonyManager.CALL_STATE_RINGING) {
                    isIncoming = false;
                    callStartTime = new Date();
                    onOutgoingCallStarted(context, savedNumber, callStartTime);
                } else {
                    isIncoming = true;
                    callStartTime = new Date();
                    onIncomingCallAnswered(context, savedNumber, callStartTime);
                }

                break;
            case TelephonyManager.CALL_STATE_IDLE:
                //Went to idle-  this is the end of a call.  What type depends on previous state(s)
                if (lastState == TelephonyManager.CALL_STATE_RINGING) {
                    //Ring but no pickup-  a miss
                    onMissedCall(context, savedNumber, callStartTime);
                } else if (isIncoming) {
                    onIncomingCallEnded(context, savedNumber, callStartTime, new Date());
                } else {
                    onOutgoingCallEnded(context, savedNumber, callStartTime, new Date());
                }
                break;
        }
        lastState = state;
    }
}