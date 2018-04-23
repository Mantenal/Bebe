package com.example.user.proyec;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class Conexion_base extends SQLiteOpenHelper{

    final String CREAR_TABLA_DATOS="CREATE TABLE datos(tempe FLOAT,ppm INT)";
    final String CREAR_TABLA_INFO="CREATE TABLE info(id INT, años INT,alarmas INT,tempe float,ppm_mx int,perso int,tempe2 float,ppm_mx2 int,not_rui int,not_sue int)";


    public Conexion_base(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREAR_TABLA_DATOS);
        db.execSQL(CREAR_TABLA_INFO);
        db.execSQL("INSERT INTO info(id,años,alarmas,tempe,ppm_mx,perso,tempe2,ppm_mx2,not_rui,not_sue)values(0,0,0,0,0,0,0,0,0,0)");




    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int version_antigua, int version_nueva) {
        db.execSQL("DROP TABLE IF EXISTS datos");
        db.execSQL("DROP TABLE IF EXISTS info");
        onCreate(db);
    }
}
