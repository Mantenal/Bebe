package com.example.user.proyec;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.CompoundButton;
import android.widget.DatePicker;
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
import com.example.user.proyec.entidades.Datos;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;


public class config extends AppCompatActivity implements Response.Listener<JSONObject>, Response.ErrorListener {
    Spinner spinner;
    Switch switchE, notifc_sueño, notif_ruid;
    EditText edit_Temp;
    EditText edit_ritmo;
    EditText edit_peso;
    EditText edit_naci;
    Button restaurar, guardar;

    RequestQueue reques;
    JsonObjectRequest jsonObjectR;

    public int ruido, sueño, per, sexo = 0, bebe, id = 0, dia, mes, año, actu, val = 0;
    public String sexo_ob, confi, sue, rui, fecha;
    public static int años, años2, axu = 0;


    /**
     * Esta funcion  es la encargada de generar el menu superior de la aplicacion
     *
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
     *
     * @param item
     * @return retorna el item que fue presionado para que se ejecute la ccion requerida
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

            case R.id.esta:
                startActivity(new Intent(getBaseContext(), estad.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP));
                return true;

            case R.id.cora:
                startActivity(new Intent(getBaseContext(), MainActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    /**
     * Esta funcion se encarga de abrir la pantalla de red cuando es presionado el boton correspondiente en la interfaz
     *
     * @param v
     */
    public void lanzar(View v) {
        Intent i = new Intent(this, Red.class);
        startActivity(i);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_config);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        switchE = findViewById(R.id.val_persona);
        notif_ruid = findViewById(R.id.notif_ruid);
        notifc_sueño = findViewById(R.id.notifc_sueño);
        edit_Temp = findViewById(R.id.edit_Temp);
        edit_ritmo = findViewById(R.id.edit_ritmo);
        edit_peso = findViewById(R.id.edit_peso);
        edit_naci = findViewById(R.id.edit_naci);
        restaurar = findViewById(R.id.btn_resta);
        guardar = findViewById(R.id.btn_guar);
        spinner = (Spinner) findViewById(R.id.spinnerid);
        reques = Volley.newRequestQueue(getApplicationContext());
        bebe = 15;
        edit_naci.setInputType(InputType.TYPE_NULL);

        llamar_info();


        Conexion_base conn = new Conexion_base(this, "bd", null, 1);
        SQLiteDatabase db = conn.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT id FROM info", null);
        while (cursor.moveToNext()) {
            id = cursor.getInt(0);
        }
        cursor.close();
        db.close();


        edit_naci.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {

                    InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    inputMethodManager.hideSoftInputFromWindow(edit_naci.getWindowToken(), 0);

                    fecha();
                }

            }
        });


        notif_ruid.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                if (b == true) {
                    ruido = 1;
                    Toast.makeText(config.this, "Activaste la Notificación de Ruido", Toast.LENGTH_SHORT).show();

                } else {
                    ruido = 0;
                    Toast.makeText(config.this, "Desactivaste la Notificación de Ruido", Toast.LENGTH_SHORT).show();

                }

            }
        });

        switchE.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b == true) {
                    Toast.makeText(config.this, "Activaste la Configuración Personalizada", Toast.LENGTH_SHORT).show();
                    per = 1;
                    edit_Temp.setEnabled(true);
                    edit_Temp.setCursorVisible(true);
                    edit_ritmo.setEnabled(true);
                    edit_ritmo.setCursorVisible(true);
                    base_config();
                    llamar_info2();

                } else {

                    Toast.makeText(config.this, "Desactivaste la Configuración Personalizada", Toast.LENGTH_SHORT).show();

                    per = 0;
                    edit_Temp.setEnabled(false);
                    edit_Temp.setCursorVisible(false);
                    edit_ritmo.setEnabled(false);
                    edit_ritmo.setCursorVisible(false);
                    base_config();

                }

            }
        });

        notifc_sueño.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b == true) {
                    sueño = 1;
                    Toast.makeText(config.this, "Activaste la Notificación de Sueño", Toast.LENGTH_SHORT).show();

                } else {
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
                String selec;

                selec = adapterView.getItemAtPosition(i).toString();

                switch (selec) {

                    case "Femenino":
                        sexo = 0;

                        break;
                    case "Masculino":
                        sexo = 1;
                        break;
                    default:
                        break;
                }
            }


            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        if (switchE.isChecked()) {
            per = 1;
            edit_Temp.setEnabled(true);
            edit_Temp.setCursorVisible(true);
            edit_ritmo.setEnabled(true);
            edit_ritmo.setCursorVisible(true);
            base_config();
            llamar_info2();
        } else {
            per = 0;
            edit_Temp.setEnabled(false);
            edit_Temp.setCursorVisible(false);
            edit_ritmo.setEnabled(false);
            edit_ritmo.setCursorVisible(false);
            base_config();
        }


    }


    private void fecha() {

        final Calendar c = Calendar.getInstance();
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                edit_naci.setText(i + "-" + (i1 + 1) + "-" + i2);
                año = i;
                mes = i1 + 1;
                dia = i2;

            }
        }, dia, mes, año);

        datePickerDialog.updateDate(c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();

    }


    private void confirmar_reiniciar() {

        AlertDialog.Builder dialogo = new AlertDialog.Builder(this);
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
                Toast.makeText(getBaseContext(), "Cancelado", Toast.LENGTH_SHORT).show();
            }
        });
        dialogo.show();

    }


    private void seguro() {
        AlertDialog.Builder dialogo = new AlertDialog.Builder(this);
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
                Toast.makeText(getBaseContext(), "Cancelado", Toast.LENGTH_SHORT).show();
            }
        });
        dialogo.show();

    }

    private void reiniciar() {

        String url = "http://simon-baby.com/bebe/borrar.php?id=" + id;
        jsonObjectR = new JsonObjectRequest(Request.Method.GET, url, null, this, this);
        reques.add(jsonObjectR);

    }


    private void confirmar_carga() {

        AlertDialog.Builder dialogo = new AlertDialog.Builder(this);
        dialogo.setTitle("Guardar");
        dialogo.setMessage("¿Estas seguro de cambiar tus configuraciones?");
        dialogo.setCancelable(false);
        dialogo.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                val = 1;
                cargarwebservice();

            }
        });
        dialogo.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Toast.makeText(getBaseContext(), "Actualizacion Cancelada", Toast.LENGTH_SHORT).show();
            }
        });
        dialogo.show();

    }


    private void llamar_info() {
        String url = "http://simon-baby.com/bebe/consul_config.php?id=" + id;
        jsonObjectR = new JsonObjectRequest(Request.Method.GET, url, null, this, this);
        reques.add(jsonObjectR);
        actu = 1;


    }


    public void llamar_info2() {

        String url = "http://simon-baby.com/bebe/consul_perso.php?id=" + id;
        jsonObjectR = new JsonObjectRequest(Request.Method.GET, url, null, this, this);
        reques.add(jsonObjectR);


    }

    private void cargarwebservice() {


        String url = "http://simon-baby.com/bebe/actu_bebe.php?id=" + id + "&n_nacimiento=" + edit_naci.getText().toString() + "&peso=" + edit_peso.getText().toString() + "&sexo=" + sexo + "&config=" + per;
        jsonObjectR = new JsonObjectRequest(Request.Method.GET, url, null, this, this);
        reques.add(jsonObjectR);
        if (per == 1) {

            String url2 = "http://simon-baby.com/bebe/actu_config.php?id=" + id + "&ritmo_c_max=" + edit_ritmo.getText().toString() + "&ritmo_c_min=" + edit_Temp.getText().toString() + "&notificacion_s=" + sueño + "&notificacion_r=" + ruido;
            jsonObjectR = new JsonObjectRequest(Request.Method.GET, url2, null, this, this);
            reques.add(jsonObjectR);
        }
    }


    @Override
    public void onErrorResponse(VolleyError error) {

        Toast.makeText(getBaseContext(), "Error al Actualizar", Toast.LENGTH_SHORT).show();


    }


    private static int getEdad(Date fechaNacimiento, Date fechaActual) {
        DateFormat formatter = new SimpleDateFormat("yyyyMMdd");
        int dIni = Integer.parseInt(formatter.format(fechaNacimiento));
        int dEnd = Integer.parseInt(formatter.format(fechaActual));
        int age = (dEnd - dIni) / 10000;
        años = age;
        return age;
    }


    @Override
    public void onResponse(JSONObject response) {


        if (actu == 1) {


            Datos misdatos = new Datos();
            JSONArray json = response.optJSONArray("actual");
            JSONObject jsonObject = null;

            try {
                jsonObject = json.getJSONObject(0);
                misdatos.setSexo(jsonObject.optString("sexo"));
                misdatos.setNacimiento(jsonObject.optString("n_nacimiento"));
                misdatos.setPeso(jsonObject.optString("peso"));
                misdatos.setConf(jsonObject.optString("config"));
            } catch (JSONException e) {
                Toast.makeText(getBaseContext(), "Error al Actualizar", Toast.LENGTH_SHORT).show();
            }

            edit_naci.setText(misdatos.getNacimiento());
            edit_peso.setText(misdatos.getPeso());
            confi = misdatos.getConf();
            sexo_ob = misdatos.getSexo();
            if (sexo_ob == "0") {
                spinner.setSelection(0);
            } else {
                spinner.setSelection(1);
            }

            switch (confi) {
                case "1":
                    per = 1;
                    switchE.setChecked(true);
                    break;
                case "0":
                    per = 0;
                    switchE.setChecked(false);
                    break;
                default:
                    break;
            }

            try {

                DateFormat dateFormat = dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                fecha = misdatos.getNacimiento();
                Date fechaNacimiento = dateFormat.parse(fecha);
                Calendar cal = Calendar.getInstance();
                Date fechaActual = cal.getTime();
                getEdad(fechaNacimiento, fechaActual);


                Conexion_base conn = new Conexion_base(this, "bd", null, 1);
                SQLiteDatabase db = conn.getWritableDatabase();

                db.execSQL("UPDATE info SET años="+años+" WHERE id="+id);//cosa que modifique

               // db.execSQL("INSERT INTO info(años)values(" + años + ")");
                Cursor cursor = db.rawQuery("SELECT años FROM info", null);

                while (cursor.moveToNext()) {
                    años2 = cursor.getInt(0);
                }

                cursor.close();
                db.close();


            } catch (ParseException e) {
                e.printStackTrace();
            }


            actu = 0;
        }


        if (per == 1 && val == 0) {
            Datos misdatos = new Datos();
            JSONArray json = response.optJSONArray("actual");
            JSONObject jsonObject = null;

            try {
                jsonObject = json.getJSONObject(0);
                misdatos.setRimo_C(jsonObject.optString("ritmo_c_max"));
                misdatos.setTempera(jsonObject.optString("ritmo_c_min"));
                misdatos.setNot_r(jsonObject.optString("notificacion_r"));
                misdatos.setNot_s(jsonObject.optString("notificacion_s"));
            } catch (JSONException e) {
                e.printStackTrace();
            }


            edit_ritmo.setText(misdatos.getRimo_C());
            edit_Temp.setText(misdatos.getTempera());
            axu++;
            if (axu == 2) {
                int ppm = 0;
                float tempe = 0;


                Conexion_base conn = new Conexion_base(this, "bd", null, 1);
                SQLiteDatabase db = conn.getWritableDatabase();

                db.execSQL("UPDATE info SET ppm_mx="+misdatos.getRimo_C()+",tempe="+misdatos.getTempera()+" WHERE id="+id);


                //db.execSQL("INSERT INTO info(ppm_mx,tempe)values(" + misdatos.getRimo_C() + "," + misdatos.getTempera() + ")");

             /*   Cursor cursor=db.rawQuery("SELECT ppm_mx,tempe FROM info",null);
                while(cursor.moveToNext()) {
                    ppm = cursor.getInt(0);
                    tempe=cursor.getFloat(1);
                }

                cursor.close();*/
                db.close();

            }

            sue = misdatos.getNot_s();
            rui = misdatos.getNot_r();


            switch (rui) {
                case "1":
                    notif_ruid.setChecked(true);
                    break;
                case "2":
                    notif_ruid.setChecked(false);
                    break;
                default:
                    break;


            }

            switch (sue) {
                case "1":
                    notifc_sueño.setChecked(true);
                    break;
                case "2":
                    notif_ruid.setChecked(false);
                    break;
                default:
                    break;


            }


        }

    }


    public void base_config(){

        Conexion_base conn = new Conexion_base(this, "bd", null, 1);
        SQLiteDatabase db = conn.getWritableDatabase();
        db.execSQL("UPDATE info  SET perso="+per+" WHERE id="+id);
        db.close();

    }



}
