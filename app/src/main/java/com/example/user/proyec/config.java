package com.example.user.proyec;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;


import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;

import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.json.JSONObject;


public class config extends AppCompatActivity implements Response.Listener<JSONObject>,Response.ErrorListener{

    Switch switchE;
    EditText campo1,campo2;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

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
        switchE=findViewById(R.id.switch8);
        campo1=findViewById(R.id.editText);
        campo2=findViewById(R.id.editText6);

        if(switchE.isChecked()){
            campo1.setEnabled(true);
            campo1.setCursorVisible(true);
            campo2.setEnabled(true);
            campo2.setCursorVisible(true);
        }

        else{
            campo1.setEnabled(false);
            campo1.setCursorVisible(false);
            campo2.setEnabled(false);
            campo2.setCursorVisible(false);
        }
    }


    public void onclick(View v) {
        if (v.getId()==R.id.switch8){
            if(switchE.isChecked()){
                campo1.setEnabled(true);
                campo1.setCursorVisible(true);
                campo2.setEnabled(true);
                campo2.setCursorVisible(true);
            }

            else{
                campo1.setEnabled(false);
                campo1.setCursorVisible(false);
                campo2.setEnabled(false);
                campo2.setCursorVisible(false);
            }
        }
    }

    @Override
    public void onErrorResponse(VolleyError error) {

    }

    @Override
    public void onResponse(JSONObject response) {

    }
}
