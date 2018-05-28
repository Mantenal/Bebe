package com.example.user.proyec;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.FloatRange;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
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

public class Primera extends AppCompatActivity implements Response.Listener<JSONObject>, Response.ErrorListener {

    EditText ID;
    RequestQueue request;
    JsonObjectRequest jsonObjectR;
    Button acep;
    float id2;
    String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_primera);
        ID=findViewById(R.id.iD);
        acep=findViewById(R.id.Aceptar);

        request= Volley.newRequestQueue(getBaseContext());


        acep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tomar();
            }
        });



    }


    public void tomar(){

         id= this.ID.getText().toString();

        if (id.equals("")) {
            Toast.makeText(this, "Inserte un numero",
                    Toast.LENGTH_LONG).show();
        }

        else {
             id2= Float.parseFloat(id);

            cargarweb();

        }
    }



    public void cargarweb(){
        String url = "http://simon-baby.com/bebe/conexion.php?id="+id;
        jsonObjectR= new JsonObjectRequest(Request.Method.GET, url, null, this, this);
       request.add(jsonObjectR);

    }


    @Override
    public void onErrorResponse(VolleyError error) {
        Toast.makeText(getBaseContext(), "Error al Actualizar", Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onResponse(JSONObject response) {
        float dato1,dato2;

        Datos misdatos = new Datos();
        JSONArray json = response.optJSONArray("actual");

        JSONObject jsonObject = null; //Intenta extrar todos los datos del objeto Json y se llama a  los set correspondiente
        try {
            jsonObject = json.getJSONObject(0);
            misdatos.setId_bebe(jsonObject.optString("id"));

        } catch (JSONException e) {
            Toast.makeText(getBaseContext(), "Error al Actualizar", Toast.LENGTH_SHORT).show();

        }



        dato1=Float.parseFloat(id);
        dato2=Float.parseFloat(misdatos.getId_bebe());

        if (dato1==dato2){
            Toast.makeText(getBaseContext(), "Id encontrado ", Toast.LENGTH_SHORT).show();


            Conexion_base conn = new Conexion_base(this, "bd", null, 1);
            SQLiteDatabase db = conn.getWritableDatabase();
            db.execSQL("UPDATE info set id="+dato2+" WHERE id=0");
            db.close();
            Intent i = new Intent(this, MainActivity.class);
            startActivity(i);




        }

        else{
            Toast.makeText(getBaseContext(), "Id no encontrado favor de volver a intentar", Toast.LENGTH_SHORT).show();

        }



    }
}
