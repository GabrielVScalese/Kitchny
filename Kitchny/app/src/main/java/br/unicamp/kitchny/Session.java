package br.unicamp.kitchny;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class Session {

    private SharedPreferences prefs;

    public Session(Context cntx) {
        // TODO Auto-generated constructor stub
        prefs = PreferenceManager.getDefaultSharedPreferences(cntx);
    }

    public void setEmail(String email) {
        prefs.edit().putString("email", email).commit();
    }
    public void setTela (int tela) { prefs.edit().putInt("tela", tela).commit();}

    public String getEmail() {
        String email = prefs.getString("email","");
        return email;
    }

    public int getTela() {
        int tela = prefs.getInt("tela", 1);
        return tela;
    }
}
