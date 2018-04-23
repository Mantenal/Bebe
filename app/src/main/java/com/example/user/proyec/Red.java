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
import android.widget.Toast;

import com.example.user.proyec.entidades.Datos;

public class Red extends AppCompatActivity {

    int id,datos;

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

            case R.id.esta:
                startActivity(new Intent(getBaseContext(),estad.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP));
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

        setContentView(R.layout.activity_red);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

       /* Conexion_base conn=new Conexion_base(this,"bd",null,1);
        SQLiteDatabase db=conn.getWritableDatabase();

        db.execSQL("INSERT INTO info(id,años,alarmas,tempe,ppm_mx,perso)values(0,0,0,0,0,0)");
        Cursor cursor=db.rawQuery("SELECT id FROM info",null);

        while(cursor.moveToNext()) {
             id = cursor.getInt(0);
        }

        cursor.close();


        db.close();*/
        Toast.makeText(getBaseContext(),"Error al Actualizar"+id,Toast.LENGTH_SHORT).show();






    }

}
