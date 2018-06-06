package com.example.user.proyec;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
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
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.LegendRenderer;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.text.DecimalFormat;

public class estad extends AppCompatActivity {
    EditText temp_prom,ppm_prom,num_alar;
    int ppm;
    float temp;
    String temp2;
    int alar;

    /**
     * Incializa la barra superior
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


    /**
     * Incializa los componentes de la interfaz y se hace un promedio de los ultimos 500 valores recibidos
     * de temeperatura y ritmo
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_estad);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        temp_prom=findViewById(R.id.Text_Prom_Temp);
        ppm_prom=findViewById(R.id.Text_prom_ppm);
        num_alar=findViewById(R.id.Text_Alarm);
        GraphView graph = findViewById(R.id.graph);


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


      /* GraphView graph = (GraphView) findViewById(R.id.graph);
        LineGraphSeries<DataPoint> series = new LineGraphSeries<>(new DataPoint[] {
                new DataPoint(0, 1),
                new DataPoint(1, 5),
                new DataPoint(2, 3)
        });
        graph.addSeries(series);*/

        initGraph(graph);



    }



    public void initGraph(GraphView graph) {
        // first series
        LineGraphSeries<DataPoint> series = new LineGraphSeries<>(new DataPoint[] {
                new DataPoint(0, 90),
                new DataPoint(1, 110),
                new DataPoint(2, 75),
                new DataPoint(3, 90),
                new DataPoint(4, 90),
                new DataPoint(5, 110),
                new DataPoint(6, 75),
                new DataPoint(7, 90),
                new DataPoint(8, 91)
        });
        series.setTitle("PPM.");
        graph.addSeries(series);



        // second series
        LineGraphSeries<DataPoint> series2 = new LineGraphSeries<>(new DataPoint[] {
                new DataPoint(0, 33),
                new DataPoint(1, 38),
                new DataPoint(2, 37),
                new DataPoint(3, 34),
                new DataPoint(4, 39),
                new DataPoint(5, 35),
                new DataPoint(6, 34),
                new DataPoint(7, 36),
                new DataPoint(8, 38)
        });
        series2.setTitle("Temp.");
        series2.setDrawBackground(true);
        series2.setColor(Color.argb(255, 255, 60, 60));
        series2.setBackgroundColor(Color.argb(100, 204, 119, 119));
        series2.setDrawDataPoints(true);
        graph.addSeries(series2);


        graph.getViewport().setYAxisBoundsManual(true);
        graph.getViewport().setMinY(0);
        graph.getViewport().setMaxY(190);



        // enable scaling
        graph.getViewport().setScalable(true);
        graph.getViewport().setScalableY(true);


        // legend
        graph.getLegendRenderer().setVisible(true);
        graph.getLegendRenderer().setAlign(LegendRenderer.LegendAlign.TOP);


    }





    /**
     * Saca el valor en dos decimales de temperatura
     * @param valor
     * @return
     */
    private String obtieneDosDecimales(float valor){
        DecimalFormat format = new DecimalFormat();
        format.setMaximumFractionDigits(1); //Define 2 decimales.
        return format.format(valor);
    }


    /**
     * Consulta el numero de alarmas que tiene registrado el sistema
     */
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
