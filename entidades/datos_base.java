package com.example.user.proyec.entidades;

public class datos_base {

    private int id;
    private float  temp;
    private int ppm;


    public datos_base(int id, float temp, int ppm) {
        this.id = id;
        this.temp = temp;
        this.ppm = ppm;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public float getTemp() {
        return temp;
    }

    public void setTemp(float temp) {
        this.temp = temp;
    }

    public int getPpm() {
        return ppm;
    }

    public void setPpm(int ppm) {
        this.ppm = ppm;
    }
}
