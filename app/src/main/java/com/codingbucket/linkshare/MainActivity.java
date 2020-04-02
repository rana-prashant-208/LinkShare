package com.codingbucket.linkshare;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ClipData;
import android.content.ClipDescription;
import android.content.ClipboardManager;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.security.GeneralSecurityException;

public class MainActivity extends AppCompatActivity {

    private static final int NOTIFICATION_ID = 9321;
    private static final String CHANNEL_ID = "9321";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Intent intent = getIntent();
        String action = intent.getAction();
        String type = intent.getType();
        if (Intent.ACTION_SEND.equals(action) && type != null) {
            if ("text/plain".equals(type)) {
                handleSendText(intent); // Handle text being sent
            }
        }
        try {
//            Toast.makeText(this, "URL "+ DocsQuickStart_test.getURL(this), Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "URL "+ e.getMessage(), Toast.LENGTH_LONG).show();
        }
//       Intent myIntent = new Intent(MainActivity.this, LoadLink.class);
//        startActivity(myIntent);
    }

    private void handleSendText(Intent intent) {
        new LinkHandler().sendLink(intent.getStringExtra(Intent.EXTRA_TEXT),this);
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
    public void addNotification(View view){
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
        myIntent.putExtra("ACTION","SEND");
        PendingIntent pending2 = PendingIntent.getActivity(this, 8, myIntent2, PendingIntent.FLAG_UPDATE_CURRENT);
        builder.addAction(R.drawable.ic_android_circle, "Share",
                pending2);

        notificationManager.notify(NOTIFICATION_ID, builder.build());
        Toast.makeText(this, "Created Notification", Toast.LENGTH_LONG).show();
        finish();
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
    public void removeNotification(View view) {
        removeNotification();
        Toast.makeText(this, "Removed Notification", Toast.LENGTH_LONG).show();
    }
    public void removeNotification(){
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        notificationManager.cancel(NOTIFICATION_ID);

    }
}
