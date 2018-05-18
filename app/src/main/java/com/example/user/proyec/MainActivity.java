
package com.example.user.proyec;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
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

public class MainActivity extends AppCompatActivity implements Response.Listener<JSONObject>, Response.ErrorListener {

    int id, datos = 0;
    String pos, dor;
    int ruido, ppm2;
    float tempe;


    int años_bb, ppm_actual, perso, ppm_perso, not_ruido, not_sueño;
    float temperatura_actual, temperatura_perso;

    EditText edi_temp, edi_pos, edi_sue, edi_ppm;
    RequestQueue request;
    JsonObjectRequest jsonObjectR;


    /**
     * Funcion encargada de crear el menu superior en la interfaz
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }


    /**
     * Interfaz encargada de darle funcionalidad al menu superior
     * Existen 4 botones en la interfaz que al ser presionados hacen accion determinada
     * Mensaje, id mensa: hace una consulta de la base de datos interna para obtener la informacion
     * de la temperatura actual y las pulsaciones por minuto actuales para despues mandarse mediante un mensaje
     *
     * Configuracion, id config:se encarga de abrir la actividad de configuracion
     *
     * Estadisticasm id esta: se encarga de abrir la actividad de estadisticas
     *
     * Corazon, id cora: se encarga de abir la actividad principal (Datos tiempo real)
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Datos misdatos = new Datos();

        switch (item.getItemId()) {

            case R.id.mensa:

                Conexion_base conn = new Conexion_base(this, "bd", null, 1);
                SQLiteDatabase db = conn.getWritableDatabase();

                float tempe2 = 0;
                int ppm = 0;
                Cursor cursor2 = db.rawQuery("SELECT tempe2,ppm_mx2 FROM info", null);
                while (cursor2.moveToNext()) {
                    tempe2 = cursor2.getFloat(0);
                    ppm = cursor2.getInt(1);

                }
                cursor2.close();
                db.close();
                Intent sendText = new Intent();
                sendText.setAction(Intent.ACTION_SEND);
                sendText.putExtra(Intent.EXTRA_TEXT, "Temperatura actual: " + Float.toString(tempe2) + ", Pulsaciones por minuto: " + Integer.toString(ppm));
                sendText.setType("text/plain");
                startActivity(sendText);
                return true;


            case R.id.configu:
                startActivity(new Intent(getBaseContext(), config.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP));
                return true;

            case R.id.esta:
                startActivity(new Intent(getBaseContext(), estad.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP));
                return true;
            default:

                return super.onOptionsItemSelected(item);
        }


    }

    /**
     * Funcion encargada de inicializar la pantalla principal y todos sus componetes, una vez
     * inicializado se llama a la funcion de cargar web service y se inicia un nuevo hilo para hacer las consultas
     * cada 30 segundos
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        edi_pos = findViewById(R.id.edit_posicion);
        edi_ppm = findViewById(R.id.edit_ppm);
        edi_sue = findViewById(R.id.edit_dor);
        edi_temp = findViewById(R.id.edit_temp);
        request = Volley.newRequestQueue(getBaseContext());

        time time = new time();
        cargawebservice();

       /* NotificationCompat.Builder mBuilder;
        NotificationManager notificacion = (NotificationManager) getApplicationContext().getSystemService(NOTIFICATION_SERVICE);

        Intent i = new Intent(MainActivity.this, estad.class);
        PendingIntent pendingintent = PendingIntent.getActivity(MainActivity.this, 0, i, 0);
        mBuilder = new NotificationCompat.Builder(getApplicationContext())
                .setContentIntent(pendingintent)
                .setSmallIcon(R.drawable.notificacion)
                .setContentTitle("Tu bebe va a explotar")
                .setContentText("Ya exploto")
                .setVibrate(new long[]{100, 250, 100, 500})
                .setAutoCancel(true);
        notificacion.notify(1, mBuilder.build());*/
        time.execute();

    }


    /**
     * Funcion encargada de abrir la base de datos interna para consultar el id a que consultar
     * y luego llama a el web service que le es regresado un objeto tipo Json
     */
    public void cargawebservice() {

        Conexion_base conn = new Conexion_base(this, "bd", null, 1);
        SQLiteDatabase db = conn.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT id FROM info", null);
        while (cursor.moveToNext()) {
            id = cursor.getInt(0);
        }
        cursor.close();
        db.close();

        String url = "http://simon-baby.com/bebe/consul_bebe.php?id=" + id;
        jsonObjectR = new JsonObjectRequest(Request.Method.GET, url, null, this, this);
        request.add(jsonObjectR);
    }


    /***
     * En caso de que el web service no responda a la peticion se mostrara un mensaje de error
     * @param error
     */
    @Override
    public void onErrorResponse(VolleyError error) {
        Toast.makeText(getBaseContext(), "Error al Actualizar", Toast.LENGTH_SHORT).show();


    }


    /**
     * Funcion que es activada en caso de que el servidor si responda, todos los datos son extraidos
     * del objeto Json para que despues los datos de temperatura y ppm se inserten en un tabla de la bd
     * interna para su posterior promedio, luego se escribe en pantalla los datos obtenidos y se llama a la funcion de notificaciones
     *
     * @param response
     */
    @Override
    public void onResponse(JSONObject response) {

        Datos misdatos = new Datos();
        JSONArray json = response.optJSONArray("actual");

        JSONObject jsonObject = null; //Intenta extrar todos los datos del objeto Json y se llama a  los set correspondiente
        try {
            jsonObject = json.getJSONObject(0);
            misdatos.setPpm2(jsonObject.optString("ritmo_c"));
            misdatos.setDor(jsonObject.optString("e_dormir"));
            misdatos.setPos(jsonObject.optString("posicion"));
            misdatos.setTemp(jsonObject.optString("temperatura"));
            misdatos.setRuido(jsonObject.optString("ruido"));
        } catch (JSONException e) {
            Toast.makeText(getBaseContext(), "Error al Actualizar", Toast.LENGTH_SHORT).show();

        }


        Conexion_base conn = new Conexion_base(this, "bd", null, 1);
        SQLiteDatabase db = conn.getWritableDatabase();
        db.execSQL("INSERT INTO datos(tempe,ppm) values(" + misdatos.getTemp() + "," + misdatos.getPpm2() + ")");
        db.execSQL("UPDATE info set tempe2=" + misdatos.getTemp() + ",ppm_mx2=" + misdatos.getPpm2() + " WHERE id=" + id);


        Cursor cursor = db.rawQuery("SELECT count(*) FROM datos", null);
        while (cursor.moveToNext()) {
            datos = cursor.getInt(0);

        }
        cursor.close();


        if (datos >= 500) { //cuando los datos de temperatura y ppm superan las 500 filas es borrada la tabla para que se siga puedan insertar datos mas recientes
            db.execSQL("Delete  From datos");
        }
        db.close();


        edi_ppm.setText(misdatos.getPpm2());
        edi_temp.setText(misdatos.getTemp());


        notificaciones();

        ruido = Integer.parseInt(misdatos.getRuido());
        ppm2 = Integer.parseInt(misdatos.getPpm2());
        tempe = Float.parseFloat(misdatos.getTemp());


        pos = misdatos.getPos();
        dor = misdatos.getDor();



        switch (pos) {
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




    }

    /**
     * Funcion que se encarga de poner a dormir un hilo durante 30 segundos
     */
    public void hilo() {
        try {
            Thread.sleep(30000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * Funcion encargada de inicializar tiempo y llamar a la nueva funcion
     */
    public void ejecutar() {
        time time = new time();
        time.execute();

    }


    /**
     * Fucion encargada de llamar a la funcion de hilo en segundo plano para que siempre se este
     * actualizando los datos aunque este fuera de la app y en cuando acabe el hilo lo vuelve llamar
     * para que sea un ciclo repetitivo hasta que la app se cierre
     */
    public class time extends AsyncTask<Void, Integer, Boolean> {

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


    /**
     * Funcion encargada de usar los datos obtenidos del servidor para mandar notificaciones segun los
     * parametros que fueron ingresados en la interfaz de configuraciones
     *
     * En caso de que personalizacion este activada revisa si se desea recibir notificaciones de
     * sueño y de ruido y revisa los valores de personalizacion en caso de que algun valor recibido
     * salio de los parametros se lanza una notificacion con la descripcion
     */
    public void notificaciones() {

        Conexion_base conn = new Conexion_base(this, "bd", null, 1);
        SQLiteDatabase db = conn.getWritableDatabase();

        //datos que debo consultar años temperatura actual,ppp actual, si esta activada perspnalizacion,temperatura per,ppm_per,notificacion sueño y nositificacion ruido

        Cursor cursor2 = db.rawQuery("SELECT * FROM info", null);


        while (cursor2.moveToNext()) {

            años_bb = cursor2.getInt(1);
            temperatura_perso = cursor2.getFloat(3);
            ppm_perso = cursor2.getInt(4);
            perso = cursor2.getInt(5);
            temperatura_actual = cursor2.getFloat(6);
            ppm_actual = cursor2.getInt(7);
            not_ruido = cursor2.getInt(8);
            not_sueño = cursor2.getInt(9);
        }


        cursor2.close();
        db.close();


       // Toast.makeText(getBaseContext(),"Error"+Integer.toString(años_bb)+", "+Float.toString(temperatura_actual)+", "+Integer.toString(ppm_actual)+", "+Integer.toString(perso)+", "+Float.toString(temperatura_perso)+", "+Integer.toString(ppm_perso)+", "+Integer.toString(not_ruido)+", "+Integer.toString(not_sueño),Toast.LENGTH_LONG).show();


        NotificationCompat.Builder mBuilder;
        NotificationManager notificacion = (NotificationManager) getApplicationContext().getSystemService(NOTIFICATION_SERVICE);
        PendingIntent pendingintent = PendingIntent.getActivity(MainActivity.this, 0, this.getIntent(), 0);


        if (perso == 0) {


            switch (años_bb){

                case 0:
                    if (ppm_actual>=165){
                        ritmo_alto();

                    }
                    break;

                case 1:
                    if (ppm_actual>=135){
                        ritmo_alto();

                    }
                    break;

                case 2:
                    if (ppm_actual>=125){
                        ritmo_alto();

                    }
                    break;

                case 3:
                    if (ppm_actual>=125){
                        ritmo_alto();

                    }

                    break;

                case 4:

                    if (ppm_actual>=125){
                        ritmo_alto();

                    }
                    break;


                    default:
                        if (ppm_actual>=120){
                            ritmo_alto();

                        }
                        break;

            }

            if (ppm_actual<85&&ppm_actual>75){

                mBuilder = new NotificationCompat.Builder(getApplicationContext())
                        .setContentIntent(pendingintent)
                        .setSmallIcon(R.drawable.notificacion)
                        .setContentTitle("Dormido")
                        .setContentText("Su bebe esta dormido")
                        .setVibrate(new long[]{100, 250, 100, 500})
                        .setAutoCancel(true);
                notificacion.notify(1, mBuilder.build());
                edi_sue.setText("Dormido");



            }

            else{
                edi_sue.setText("Despierto");

            }

            if (temperatura_actual>37.9){

                mBuilder = new NotificationCompat.Builder(getApplicationContext())
                        .setContentIntent(pendingintent)
                        .setSmallIcon(R.drawable.notificacion)
                        .setContentTitle("Temperatura Alta")
                        .setContentText("La temperatura esta fuera de los los parametros")
                        .setVibrate(new long[]{100, 250, 100, 500})
                        .setAutoCancel(true);
                notificacion.notify(2, mBuilder.build());


            }


            if (temperatura_actual<36.9){

                mBuilder = new NotificationCompat.Builder(getApplicationContext())
                        .setContentIntent(pendingintent)
                        .setSmallIcon(R.drawable.notificacion)
                        .setContentTitle("Temperatura Baja")
                        .setContentText("La temperatura esta fuera de los los parametros")
                        .setVibrate(new long[]{100, 250, 100, 500})
                        .setAutoCancel(true);
                notificacion.notify(3, mBuilder.build());

            }
            if (ppm_actual<75){

                mBuilder = new NotificationCompat.Builder(getApplicationContext())
                        .setContentIntent(pendingintent)
                        .setSmallIcon(R.drawable.notificacion)
                        .setContentTitle("Ritmo Cardiaco Bajo")
                        .setContentText("El ritmo cardiaco esta fuera de los parametros")
                        .setVibrate(new long[]{100, 250, 100, 500})
                        .setAutoCancel(true);
                notificacion.notify(4, mBuilder.build());
            }



            if (ruido==1){
                mBuilder = new NotificationCompat.Builder(getApplicationContext())
                        .setContentIntent(pendingintent)
                        .setSmallIcon(R.drawable.notificacion)
                        .setContentTitle("Ruido")
                        .setContentText("Se a detectado mucho ruido")
                        .setVibrate(new long[]{100, 250, 100, 500})
                        .setAutoCancel(true);
                notificacion.notify(5, mBuilder.build());

            }





        }



        if (perso==1){

            if (ppm_actual>ppm_perso){

                ritmo_alto();


            }




            if (temperatura_actual>temperatura_perso+.9){

                mBuilder = new NotificationCompat.Builder(getApplicationContext())
                        .setContentIntent(pendingintent)
                        .setSmallIcon(R.drawable.notificacion)
                        .setContentTitle("Temperatura Alta")
                        .setContentText("La temperatura esta fuera de los los parametros")
                        .setVibrate(new long[]{100, 250, 100, 500})
                        .setAutoCancel(true);
                notificacion.notify(6, mBuilder.build());


            }


            if (temperatura_actual<temperatura_perso-.9){

                mBuilder = new NotificationCompat.Builder(getApplicationContext())
                        .setContentIntent(pendingintent)
                        .setSmallIcon(R.drawable.notificacion)
                        .setContentTitle("Temperatura Baja")
                        .setContentText("La temperatura esta fuera de los los parametros")
                        .setVibrate(new long[]{100, 250, 100, 500})
                        .setAutoCancel(true);
                notificacion.notify(7, mBuilder.build());

            }


            if (not_ruido==1){


                if (ruido==1){
                    mBuilder = new NotificationCompat.Builder(getApplicationContext())
                            .setContentIntent(pendingintent)
                            .setSmallIcon(R.drawable.notificacion)
                            .setContentTitle("Ruido")
                            .setContentText("Se a detectado mucho ruido")
                            .setVibrate(new long[]{100, 250, 100, 500})
                            .setAutoCancel(true);
                    notificacion.notify(8, mBuilder.build());

                }


            }


            if(not_sueño==1){



                if (ppm_actual<85&&ppm_actual>75){

                    mBuilder = new NotificationCompat.Builder(getApplicationContext())
                            .setContentIntent(pendingintent)
                            .setSmallIcon(R.drawable.notificacion)
                            .setContentTitle("Dormido")
                            .setContentText("Su bebe esta dormido")
                            .setVibrate(new long[]{100, 250, 100, 500})
                            .setAutoCancel(true);
                    notificacion.notify(9, mBuilder.build());
                    edi_sue.setText("Dormido");



                }

                else {
                    edi_sue.setText("Despierto");
                }




            }





        }

        if ( ppm_actual<85&&ppm_actual>75){
            edi_sue.setText("Dormido");
        }

        else {
            edi_sue.setText("Despidsadaserto");
        }




    }




    public void ritmo_alto(){

        NotificationCompat.Builder mBuilder;
        NotificationManager notificacion = (NotificationManager) getApplicationContext().getSystemService(NOTIFICATION_SERVICE);
        PendingIntent pendingintent = PendingIntent.getActivity(MainActivity.this, 0, this.getIntent(), 0);

        mBuilder = new NotificationCompat.Builder(getApplicationContext())
                .setContentIntent(pendingintent)
                .setSmallIcon(R.drawable.notificacion)
                .setContentTitle("Ritmo Cardiaco Elevado")
                .setContentText("El Ritmo cardiaco de su bebe esta arriba de los parametros")
                .setVibrate(new long[]{100, 250, 100, 500})
                .setAutoCancel(true);
        notificacion.notify(10, mBuilder.build());

    }

}
