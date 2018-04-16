package com.example.user.proyec;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ClipData;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
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
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.user.proyec.entidades.Datos;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity implements Response.Listener<JSONObject>,Response.ErrorListener{

    int id;
    String pos,dor;

    EditText edi_temp,edi_pos,edi_sue,edi_ppm;
    RequestQueue request;
    JsonObjectRequest jsonObjectR;


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
        edi_pos=findViewById(R.id.edit_posicion);
        edi_ppm=findViewById(R.id.edit_ppm);
        edi_sue=findViewById(R.id.edit_dor);
        edi_temp=findViewById(R.id.edit_temp);
        request= Volley.newRequestQueue(getBaseContext());




        time time= new time();

        cargawebservice();


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
time.execute();

    }

    public void cargawebservice(){

        Conexion_base conn=new Conexion_base(this,"bd",null,1);
        SQLiteDatabase db=conn.getWritableDatabase();
        Cursor cursor=db.rawQuery("SELECT id FROM info",null);
        while(cursor.moveToNext()) {
            id = cursor.getInt(0);
        }
        cursor.close();
        db.close();

        String url="http://simon-baby.com/bebe/consul_bebe.php?id="+id;
        jsonObjectR =new  JsonObjectRequest(Request.Method.GET,url,null,this,this);
        request.add(jsonObjectR);
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        Toast.makeText(getBaseContext(),"Error al Actualizar",Toast.LENGTH_SHORT).show();


    }



    @Override
    public void onResponse(JSONObject response) {

        Datos misdatos=new Datos();
        JSONArray json=response.optJSONArray("actual");
        JSONObject jsonObject=null;
        try {
            jsonObject=json.getJSONObject(0);
            misdatos.setPpm2(jsonObject.optString("ritmo_c"));
            misdatos.setDor(jsonObject.optString("e_dormir"));
            misdatos.setPos(jsonObject.optString("posicion"));
            misdatos.setTemp(jsonObject.optString("temperatura"));
        } catch (JSONException e) {
            Toast.makeText(getBaseContext(),"Error al Actualizar",Toast.LENGTH_SHORT).show();

        }
        edi_ppm.setText(misdatos.getPpm2());
        edi_temp.setText(misdatos.getTemp());
        pos=misdatos.getPos();
        dor=misdatos.getDor();
        switch (pos){
            case "0":
                edi_pos.setText("Boca Arriba");
                break;
            case "1":
                edi_pos.setText("Boca Abajo");
                break;
            case "3":
                edi_pos.setText("De Costado");
                break;
            default:
                edi_pos.setText("Sin Info");
                break;
        }

        if (dor=="1"){
            edi_sue.setText("Dormido");
        }
        else{
            edi_sue.setText("Despierto");
        }


    }
    public void hilo(){
        try {
            Thread.sleep(30000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void ejecutar(){
        time time= new time();
        time.execute();

    }

    public class  time extends AsyncTask<Void,Integer,Boolean>{


        @Override
        protected Boolean doInBackground(Void... voids) {
            hilo();
            return true;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            cargawebservice();
            ejecutar();

        }
    }


}
