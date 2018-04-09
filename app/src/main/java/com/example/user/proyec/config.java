package com.example.user.proyec;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;


public class config extends AppCompatActivity implements Response.Listener<JSONObject> ,Response.ErrorListener{
    Spinner spinner;
    Switch switchE;
    EditText edit_Temp, edit_ritmo,edit_peso,edit_naci;
    Button restaurar,guardar;
    ProgressDialog progreso;
    RequestQueue reques;
    JsonObjectRequest jsonObjectRequest;

    private int ruido,sueño,per,sexo=0,bebe;



    /**
     * Esta funcion  es la encargada de generar el menu superior de la aplicacion
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
     * Funcion encargada de generar los botones de la parte superior y decirle la funcion que va a tomar
     * @param item
     * @return retorna el item que fue presionado para que se ejecute la ccion requerida
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

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

    /**
     * Esta funcion se encarga de abrir la pantalla de red cuando es presionado el boton correspondiente en la interfaz
     * @param v
     */
    public void lanzar(View v) {
        Intent i = new Intent(this, Red.class );
        startActivity(i);
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_config);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        switchE=findViewById(R.id.val_persona);
        edit_Temp =findViewById(R.id.edit_Temp);
        edit_ritmo =findViewById(R.id.edit_ritmo);
        edit_peso=findViewById(R.id.edit_peso);
        edit_naci=findViewById(R.id.edit_naci);
        restaurar=findViewById(R.id.btn_resta);
        guardar=findViewById(R.id.btn_guar);
        spinner=(Spinner)findViewById(R.id.spinnerid);
        reques= Volley.newRequestQueue(getApplicationContext());
        bebe=15;

        guardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cargarwebservice();
            }
        });

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText(adapterView.getContext(),"Seleccionado: " +adapterView.getItemAtPosition(i).toString(),Toast.LENGTH_SHORT).show();
                String selec;
                selec=adapterView.getItemAtPosition(i).toString();

                 switch (selec){

                     case "Femenino":

                         sexo=0;
                         Toast.makeText(config.this,"El id es "+sexo,Toast.LENGTH_SHORT).show();
                         break;
                     case "Masculino":
                         sexo=1;
                         Toast.makeText(config.this,"El id es "+ sexo,Toast.LENGTH_SHORT).show();
                         break;
                         default:
                             break;
                 }
            }


            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        if(switchE.isChecked()){
            per=1;
            edit_Temp.setEnabled(true);
            edit_Temp.setCursorVisible(true);
            edit_ritmo.setEnabled(true);
            edit_ritmo.setCursorVisible(true);
        }

        else{
            per=0;
            edit_Temp.setEnabled(false);
            edit_Temp.setCursorVisible(false);
            edit_ritmo.setEnabled(false);
            edit_ritmo.setCursorVisible(false);
        }
    }



    public void onclick2(View v){
        if (v.getId()==R.id.notif_ruid) {
            ruido=1;
            Toast.makeText(config.this, "Activaste la Notificación de Ruido", Toast.LENGTH_SHORT).show();
        }
        else{
            ruido=0;
            Toast.makeText(config.this, "Desactivaste la Notificación de Ruido", Toast.LENGTH_SHORT).show();

        }

    }

    public void onclick3 (View v){
        if(v.getId()==R.id.notifc_sueño) {
            sueño=1;
            Toast.makeText(config.this, "Activaste la Notificación de Sueño", Toast.LENGTH_SHORT).show();
        }
        else  {
            sueño=0;
            Toast.makeText(config.this, "Desactivaste la Notificación de Sueño", Toast.LENGTH_SHORT).show();
        }
    }


    public void onclick(View v) {
        if (v.getId()==R.id.val_persona){
            if(switchE.isChecked()){
                Toast.makeText(config.this,"Activaste la Configuración Personalizada",Toast.LENGTH_SHORT).show();
                per=1;
                edit_Temp.setEnabled(true);
                edit_Temp.setCursorVisible(true);
                edit_ritmo.setEnabled(true);
                edit_ritmo.setCursorVisible(true);
            }

            else{
                Toast.makeText(config.this,"Desactivaste la Configuración Personalizada",Toast.LENGTH_SHORT).show();
                per=0;
                edit_Temp.setEnabled(false);
                edit_Temp.setCursorVisible(false);
                edit_ritmo.setEnabled(false);
                edit_ritmo.setCursorVisible(false);
            }
        }
    }


    private void cargarwebservice() {

        String url="http://simon-baby.com/bebe/inicio_personalizacion.php";
    }


    @Override
    public void onErrorResponse(VolleyError error) {

    }

    @Override
    public void onResponse(JSONObject response) {

    }
}
