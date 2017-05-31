package com.muratoter.isg.raporla;

/**
 * Created by Murat on 28.04.2017.
 */

public class Kullanicilar {
    private int kullaniciId;
    private String kullaniciAdSoyad;
    private String kullaniciEPosta;
    private String kullaniciSifre;
    private String kullaniciDurum;

    public Kullanicilar(int kullaniciId, String kullaniciAdSoyad, String kullaniciEPosta, String kullaniciSifre, String kullaniciDurum) {
        this.kullaniciId = kullaniciId;
        this.kullaniciAdSoyad = kullaniciAdSoyad;
        this.kullaniciEPosta = kullaniciEPosta;
        this.kullaniciSifre = kullaniciSifre;
        this.kullaniciDurum = kullaniciDurum;
    }

    public int getKullaniciId() {
        return kullaniciId;
    }

    public void setKullaniciId(int kullaniciId) {
        this.kullaniciId = kullaniciId;
    }

    public String getKullaniciAdSoyad() {
        return kullaniciAdSoyad;
    }

    public void setKullaniciAdSoyad(String kullaniciAdSoyad) {
        this.kullaniciAdSoyad = kullaniciAdSoyad;
    }

    public String getKullaniciEPosta() {
        return kullaniciEPosta;
    }

    public void setKullaniciEPosta(String kullaniciEPosta) {
        this.kullaniciEPosta = kullaniciEPosta;
    }

    public String getKullaniciSifre() {
        return kullaniciSifre;
    }

    public void setKullaniciSifre(String kullaniciSifre) {
        this.kullaniciSifre = kullaniciSifre;
    }

    public String getKullaniciDurum() {
        return kullaniciDurum;
    }

    public void setKullaniciDurum(String kullaniciDurum) {
        this.kullaniciDurum = kullaniciDurum;
    }
}
