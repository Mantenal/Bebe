package com.example.user.proyec;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.user.proyec.entidades.Datos;

import java.text.DecimalFormat;

public class estad extends AppCompatActivity {
    EditText temp_prom,ppm_prom,num_alar;
    int ppm;
    float temp;
    String temp2;
    int alar;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {


        switch (item.getItemId()) {



            case    R.id.mensa:

                Conexion_base conn = new Conexion_base(this, "bd", null, 1);
                SQLiteDatabase db = conn.getWritableDatabase();

                float  tempe2=0;
                int ppm=0;
                Cursor cursor2=db.rawQuery("SELECT tempe2,ppm_mx2 FROM info",null);
                while(cursor2.moveToNext()) {
                    tempe2=cursor2.getFloat(0);
                    ppm=cursor2.getInt(1);

                }
                cursor2.close();
                db.close();

                Intent sendText = new Intent();
                sendText.setAction(Intent.ACTION_SEND);
                sendText.putExtra(Intent.EXTRA_TEXT, "Temperatura actual: "+Float.toString(tempe2)+", Pulsaciones por minuto: "+Integer.toString(ppm));
                sendText.setType("text/plain");
                startActivity(sendText);


                return  true;

            case R.id.configu:

                startActivity(new Intent(getBaseContext(),config.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP));
                return true;

            case R.id.cora:
                startActivity(new Intent(getBaseContext(),MainActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP));
                return true;
            default:

                return super.onOptionsItemSelected(item);
        }

    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_estad);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        temp_prom=findViewById(R.id.Text_Prom_Temp);
        ppm_prom=findViewById(R.id.Text_prom_ppm);
        num_alar=findViewById(R.id.Text_Alarm);


        Conexion_base conn=new Conexion_base(this,"bd",null,1);
        SQLiteDatabase db=conn.getWritableDatabase();
        Cursor cursor=db.rawQuery("SELECT AVG(tempe),AVG(ppm) FROM datos",null);
        while(cursor.moveToNext()) {
            temp = cursor.getFloat(0);
            ppm=cursor.getInt(1);
        }
        cursor.close();
        db.close();

        String ppm2=Integer.toString(ppm);
      temp_prom.setText(obtieneDosDecimales(temp));
      ppm_prom.setText(ppm2);
      alarmas();
      num_alar.setText(Integer.toString(alar));



    }

    private String obtieneDosDecimales(float valor){
        DecimalFormat format = new DecimalFormat();
        format.setMaximumFractionDigits(1); //Define 2 decimales.
        return format.format(valor);
    }


    private void alarmas(){
        Conexion_base conn=new Conexion_base(this,"bd",null,1);
        SQLiteDatabase db=conn.getWritableDatabase();
        Cursor cursor=db.rawQuery("SELECT alarmas FROM info",null);
        while(cursor.moveToNext()) {
            alar = cursor.getInt(0);
        }
        cursor.close();
        db.close();

    }
}
