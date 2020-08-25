package com.codingbucket.linkshare;

import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
//https://stackoverflow.com/questions/15563921/how-to-detect-incoming-calls-in-an-android-device
public class MainActivity extends AppCompatActivity {

    private static final int NOTIFICATION_ID = 9321;
    private static final String CHANNEL_ID = "9321";
    private static final int MY_PERMISSIONS_REQUEST_READ_PHONE_STATE =1 ;
    private static final int MY_PERMISSIONS_REQUEST_PROCESS_OUTGOING_CALLS =2 ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Intent intent = getIntent();
        String action = intent.getAction();
        String type = intent.getType();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (Intent.ACTION_SEND.equals(action) && type != null) {
            if ("text/plain".equals(type)) {
                handleSendText(intent); // Handle text being sent
                finish();
            }
        }else{
            final Switch simpleSwitch1 = (Switch) findViewById(R.id.switch1);
            simpleSwitch1.setChecked(isNotificationSet());
            simpleSwitch1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (simpleSwitch1.isChecked())
                        addNotification();
                    else {
                        removeNotification();
                    }
                }
            });
            final Switch simpleSwitch2 = (Switch) findViewById(R.id.switch2);
            simpleSwitch2.setChecked(isMuteOnCallSet());
            simpleSwitch2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (simpleSwitch2.isChecked())
                        setMuteOnCall(true);
                    else {
                        setMuteOnCall(false);
                    }
                }
            });
            EditText text = (EditText)findViewById(R.id.editText);
            text.setText(getHostName(this));

        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (getApplicationContext().checkSelfPermission(Manifest.permission.READ_PHONE_STATE)
                    != PackageManager.PERMISSION_GRANTED) {
                // Permission has not been granted, therefore prompt the user to grant permission
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.READ_PHONE_STATE},
                        MY_PERMISSIONS_REQUEST_READ_PHONE_STATE);
            }
            if (getApplicationContext().checkSelfPermission(Manifest.permission.PROCESS_OUTGOING_CALLS)
                    != PackageManager.PERMISSION_GRANTED) {
                // Permission has not been granted, therefore prompt the user to grant permission
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.PROCESS_OUTGOING_CALLS},
                        MY_PERMISSIONS_REQUEST_PROCESS_OUTGOING_CALLS);
            }
        }

    }

    private void setMuteOnCall(boolean b) {
        Utils.saveSetting(this,"Mute_ON_Call",String.valueOf(b));
        Toast.makeText(this, "Mute on call: "+(b?"Enabled":"Disabled"), Toast.LENGTH_SHORT).show();
    }

    private boolean isMuteOnCallSet() {
        return Boolean.parseBoolean(Utils.getSetting(this,"Mute_ON_Call"));
    }

    private boolean isNotificationSet() {
        return Boolean.parseBoolean(Utils.getSetting(this,"Create Notification"));
    }

    public void saveSettings(View view) {
        EditText text = (EditText)findViewById(R.id.editText);
        Utils.saveSetting(this,getString(R.string.hostAddress),text.getText().toString());
        Toast.makeText(this, "Host address saved", Toast.LENGTH_SHORT).show();
    }
    public void muteClicked(View view){
        Toast.makeText(this, "Request Sent", Toast.LENGTH_SHORT).show();
        LaptopControlsHandler.muteClicked(null,this);
    }
    public void unmuteClicked(View view){
        Toast.makeText(this, "Request Sent", Toast.LENGTH_SHORT).show();
        LaptopControlsHandler.unmuteClicked(null,this);
    }
    public void sleepClicked(View view){
        Toast.makeText(this, "Request Sent", Toast.LENGTH_SHORT).show();
        LaptopControlsHandler.sleepClicked(null,this);
    }
    public void volumeUpClicked(View view){
        Toast.makeText(this, "Request Sent", Toast.LENGTH_SHORT).show();
        LaptopControlsHandler.volumeUpClicked(null,this);    }
    public void volumeDownClicked(View view){
        Toast.makeText(this, "Request Sent", Toast.LENGTH_SHORT).show();
        LaptopControlsHandler.volumeDownClicked(null,this);
    }
    public void lockClicked(View view){
        Toast.makeText(this, "Request Sent", Toast.LENGTH_LONG).show();
        LaptopControlsHandler.lockClicked(null,this);
    }
    public void loadClicked(View view){
        Toast.makeText(this, "Loading", Toast.LENGTH_LONG).show();
        LaptopControlsHandler.loadClicked(null,this);
    }
    public static String getHostName(Context context) {
        return Utils.getSetting(context,context.getString(R.string.hostAddress),"http://192.168.0.104:8080");
    }

    private void handleSendText(Intent intent) {
        LinkHandler.sendLink(intent.getStringExtra(Intent.EXTRA_TEXT),this);
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    public void addNotification(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (getApplicationContext().checkSelfPermission(Manifest.permission.ACCESS_NOTIFICATION_POLICY)
                    != PackageManager.PERMISSION_GRANTED) {
                // Permission has not been granted, therefore prompt the user to grant permission
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_NOTIFICATION_POLICY},
                        MY_PERMISSIONS_REQUEST_READ_PHONE_STATE);
            }
        }
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        notificationManager.cancel(NOTIFICATION_ID);
        Intent myIntent1 = new Intent(MainActivity.this, LinkHandler.class);
        myIntent1.putExtra("ACTION","OPEN");
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 10, myIntent1, PendingIntent.FLAG_UPDATE_CURRENT);

        createNotificationChannel();
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_android_circle)
                .setContentTitle("Tap to load the shared link")
                .setContentText("Select Action")
                .setPriority(NotificationCompat.PRIORITY_MIN)
                .setContentIntent(pendingIntent)
                .setAutoCancel(false)
                .setOngoing(true);


        Intent myIntent = new Intent(MainActivity.this, LinkHandler.class);
        myIntent.putExtra("ACTION","OPEN");
        PendingIntent pending = PendingIntent.getActivity(this, 9, myIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        builder.addAction(R.drawable.ic_android_circle, "Load",
                pending);

        Intent myIntent2 = new Intent(MainActivity.this, LinkHandler.class);
        myIntent2.putExtra("ACTION","MUTE");
        PendingIntent pending2 = PendingIntent.getActivity(this, 8, myIntent2, PendingIntent.FLAG_UPDATE_CURRENT);
        builder.addAction(R.drawable.ic_android_circle, "Mute",
                pending2);

        Intent myIntent3 = new Intent(MainActivity.this, LinkHandler.class);
        myIntent3.putExtra("ACTION","UNMUTE");
        PendingIntent pending3 = PendingIntent.getActivity(this, 7, myIntent3, PendingIntent.FLAG_UPDATE_CURRENT);
        builder.addAction(R.drawable.ic_android_circle, "Unmute",
                pending3);

        notificationManager.notify(NOTIFICATION_ID, builder.build());
        setNotification(true);
        Toast.makeText(this, "Notification Created", Toast.LENGTH_LONG).show();
    }

    private void setNotification(boolean b) {
        SharedPreferences sharedPref = this.getSharedPreferences(
                getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putBoolean(getString(R.string.create_notification), b);
        editor.commit();
        Utils.saveSetting(this,getString(R.string.create_notification),String.valueOf(b));
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getString(R.string.channel_name);
            String description = getString(R.string.channel_description);
            int importance = NotificationManager.IMPORTANCE_NONE;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            channel.setShowBadge(true);
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }
    public void removeNotification(){
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        notificationManager.cancel(NOTIFICATION_ID);
        setNotification(false);
        Toast.makeText(this, "Notification Removed", Toast.LENGTH_LONG).show();
    }
}
