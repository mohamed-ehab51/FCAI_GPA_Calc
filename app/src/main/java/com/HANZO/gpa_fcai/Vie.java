package com.HANZO.gpa_fcai;

import android.view.View;

public class Vie {
    View vi;
    int place;

    public Vie(View vi, int place) {
        this.vi = vi;
        this.place = place;
    }

    public View getVi() {
        return vi;
    }

    public void setVi(View vi) {
        this.vi = vi;
    }

    public int getPlace() {
        return place;
    }

    public void setPlace(int place) {
        this.place = place;
    }
}
