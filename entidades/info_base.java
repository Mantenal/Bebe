package com.example.user.proyec.entidades;

public class info_base {
    int id_bebe;
    int alarmas;
    int años;

    public info_base(int id_bebe, int alarmas, int años) {
        this.id_bebe = id_bebe;
        this.alarmas = alarmas;
        this.años = años;
    }

    public int getId_bebe() {
        return id_bebe;
    }

    public void setId_bebe(int id_bebe) {
        this.id_bebe = id_bebe;
    }

    public int getAlarmas() {
        return alarmas;
    }

    public void setAlarmas(int alarmas) {
        this.alarmas = alarmas;
    }

    public int getAños() {
        return años;
    }

    public void setAños(int años) {
        this.años = años;
    }
}
