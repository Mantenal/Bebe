package com.example.user.proyec;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;


public class config extends AppCompatActivity implements Response.Listener<JSONObject> ,Response.ErrorListener{
    Spinner spinner;
    Switch switchE,notifc_sueño,notif_ruid;
    EditText edit_Temp, edit_ritmo,edit_peso,edit_naci;
    Button restaurar,guardar;
    ProgressDialog progreso;
    RequestQueue reques;
    JsonObjectRequest jsonObjectR;

    public int ruido,sueño,per,sexo=0,bebe,id=0;




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
        notif_ruid=findViewById(R.id.notif_ruid);
        notifc_sueño=findViewById(R.id.notifc_sueño);
        edit_Temp =findViewById(R.id.edit_Temp);
        edit_ritmo =findViewById(R.id.edit_ritmo);
        edit_peso=findViewById(R.id.edit_peso);
        edit_naci=findViewById(R.id.edit_naci);
        restaurar=findViewById(R.id.btn_resta);
        guardar=findViewById(R.id.btn_guar);
        spinner=(Spinner)findViewById(R.id.spinnerid);
        reques= Volley.newRequestQueue(getApplicationContext());
        bebe=15;


        notif_ruid.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                if (b==true){
                    ruido = 1;
                    Toast.makeText(config.this, "Activaste la Notificación de Ruido :3", Toast.LENGTH_SHORT).show();

                }
                else{
                    ruido = 0;
                    Toast.makeText(config.this, "Desactivaste la Notificación de Ruido :3", Toast.LENGTH_SHORT).show();

                }

            }
        });

        switchE.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b==true){
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
        });

        notifc_sueño.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b==true){
                    sueño = 1;
                    Toast.makeText(config.this, "Activaste la Notificación de Sueño", Toast.LENGTH_SHORT).show();

                }
                else{
                    sueño = 0;
                    Toast.makeText(config.this, "Desactivaste la Notificación de Sueño", Toast.LENGTH_SHORT).show();
                }
            }
        });

        guardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                confirmar_carga();
            }
        });

        restaurar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                confirmar_reiniciar();
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


   private void confirmar_reiniciar(){

       AlertDialog.Builder dialogo=new AlertDialog.Builder(this);
       dialogo.setTitle("Reiniciar");
       dialogo.setMessage("¿Estas seguro de reiniciar todos  tus datos?");
       dialogo.setCancelable(false);
       dialogo.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
           @Override
           public void onClick(DialogInterface dialogInterface, int i) {

             seguro();

           }
       });
       dialogo.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
           @Override
           public void onClick(DialogInterface dialogInterface, int i) {
               Toast.makeText(getBaseContext(),"Cancelado",Toast.LENGTH_SHORT).show();
           }
       });
       dialogo.show();

    }


    private void seguro(){
        AlertDialog.Builder dialogo=new AlertDialog.Builder(this);
        dialogo.setTitle("¿Seguro?");
        dialogo.setMessage("Todos tus datos seran borrados,¿Estas seguro?");
        dialogo.setCancelable(false);
        dialogo.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

               reiniciar();

            }
        });
        dialogo.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Toast.makeText(getBaseContext(),"Cancelado",Toast.LENGTH_SHORT).show();
            }
        });
        dialogo.show();

    }

    private void reiniciar(){

        String url="http://simon-baby.com/bebe/borrar.php?id="+id;
        jsonObjectR=new JsonObjectRequest(Request.Method.GET,url,null,this,this);
        reques.add(jsonObjectR);

    }


    private void confirmar_carga(){

        AlertDialog.Builder dialogo=new AlertDialog.Builder(this);
        dialogo.setTitle("Guardar");
        dialogo.setMessage("¿Estas seguro de cambiar tus configuraciones?");
        dialogo.setCancelable(false);
        dialogo.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                cargarwebservice();

            }
        });
        dialogo.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Toast.makeText(getBaseContext(),"Actualizacion Cancelada",Toast.LENGTH_SHORT).show();
            }
        });
        dialogo.show();

    }



    private void cargarwebservice() {

        String url="http://simon-baby.com/bebe/actu_bebe.php?id="+id+"&n_nacimiento="+edit_naci.getText().toString()+"&peso="+edit_peso.getText().toString()+"&sexo="+sexo;
        jsonObjectR=new JsonObjectRequest(Request.Method.GET,url,null,this,this);
        reques.add(jsonObjectR);
        if (per==1){

            String url2="http://simon-baby.com/bebe/actu_config.php?id="+id+"&ritmo_c_max="+edit_ritmo.getText().toString()+"&ritmo_c_min="+edit_Temp.getText().toString()+"&notificacion_s="+sueño+"&notificacion_r="+ruido;
            jsonObjectR=new JsonObjectRequest(Request.Method.GET,url2,null,this,this);
            reques.add(jsonObjectR);
        }
    }


    @Override
    public void onErrorResponse(VolleyError error) {

        Toast.makeText(getBaseContext(),"Error al Actualizar",Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onResponse(JSONObject response) {

        Toast.makeText(getBaseContext(),"Actualizacion Correcta",Toast.LENGTH_SHORT).show();



    }
}
