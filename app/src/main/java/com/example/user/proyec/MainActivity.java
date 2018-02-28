package com.example.user.proyec;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ClipData;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.content.Intent;

public class MainActivity extends AppCompatActivity {



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {


        switch (item.getItemId()) {

            case R.id.configu:
                startActivity(new Intent(getBaseContext(),config.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP));
                return true;

            case R.id.esta:
                startActivity(new Intent(getBaseContext(),estad.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP));
                return true;
            default:

                return super.onOptionsItemSelected(item);
        }


    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        NotificationCompat.Builder mBuilder;

        NotificationManager notificacion=(NotificationManager)getApplicationContext().getSystemService(NOTIFICATION_SERVICE);

        Intent i=new Intent(MainActivity.this,estad.class);
        PendingIntent pendingintent=PendingIntent.getActivity(MainActivity.this,0,i,0);
        mBuilder=new NotificationCompat.Builder(getApplicationContext())
                .setContentIntent(pendingintent)
                .setSmallIcon(R.drawable.notificacion)
                .setContentTitle("Tu bebe va a explotar")
                .setContentText("Ya exploto")
                .setVibrate(new long[]{100,250,100,500})
                .setAutoCancel(true);
        notificacion.notify(1,mBuilder.build());

    }

}
