package com.example.user.proyec;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

public class conectar extends AppCompatActivity {

ImageView imagen;
TextView texto;
WebView web;

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
     * Inicializa la pantalla  y sus componentes despues intenta hacer un peticion a la direccion de configuracion
     * de el modulo en caso de que no carga oculta el webview y muestra pantalla de error
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conectar);


        imagen = findViewById(R.id.imageView3);
        texto=findViewById(R.id.textView16);
        web=findViewById(R.id.webview6);
        imagen.setVisibility(View.VISIBLE);
       texto.setVisibility(View.VISIBLE);
        web.setVisibility(View.GONE);



        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setIcon(R.mipmap.ic_launcher);
        progressDialog.setMessage("Cargando...");
        progressDialog.show();

        //Obtiene referencia en Layout de WebView.
        //final WebView webView = (WebView) findViewById(R.id.webview6);
        //Carga página.
        web.loadUrl("http://192.168.4.1");
        //Define WebViewClient() para poder leer eventos que ocurren durante el cargado de contenido en el WebView.



        web.setWebViewClient(new WebViewClient(){
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                //elimina ProgressBar.
                progressDialog.dismiss();

                web.setVisibility(View.VISIBLE);
                imagen.setVisibility(View.GONE);
                texto.setVisibility(View.GONE);

            }

            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                super.onReceivedError(view, errorCode, description, failingUrl);
                view.loadUrl("about:blank");

                web.setVisibility(View.GONE);
                imagen.setVisibility(View.VISIBLE);
                texto.setVisibility(View.VISIBLE);
            }

        });






      /*  WebView myWebView = (WebView) findViewById(R.id.webview);
        WebSettings webSettings = myWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        myWebView.setWebViewClient(new WebViewClient());
        myWebView.loadUrl("http://192.168.4.1");*/

    }


    /**
     * Si se preciona el boton de atras retrocedera en el navegador y no en la aplicacion
     * @param keyCode
     * @param event
     * @return
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        WebView mWebView;
        mWebView = (WebView) findViewById(R.id.webview6);
        if (event.getAction() == KeyEvent.ACTION_DOWN) {
            switch (keyCode) {
                case KeyEvent.KEYCODE_BACK:
                    if (mWebView.canGoBack()) {
                        mWebView.goBack();
                    } else {
                        finish();
                    }
                    return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }


}
