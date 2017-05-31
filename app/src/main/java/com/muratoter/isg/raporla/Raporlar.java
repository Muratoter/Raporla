package com.muratoter.isg.raporla;

/**
 * Created by Murat on 12.05.2017.
 */

public class Raporlar {
    private int RaporId;
    private int DepSorumluId;
    private String DepSorumluAdi;
    private int isg_uzman_id;
    private String RaporTarihi;
    private String KisiAdSoyad;
    private String OlayTarihi;
    private String OlayOlduguYer;
    private String OlayOlduguDepartman;
    private String OlaySebebi;
    private String HasarGorenYer;
    private String OlayKisaAciklama;
    private String OlayaSahitKisiAdSoyad;

    public Raporlar(int raporId, int depSorumluId, String depSorumluAdi,int isg_uzman_id, String raporTarihi, String kisiAdSoyad, String olayTarihi, String olayOlduguYer, String olayOlduguDepartman, String olaySebebi, String hasarGorenYer, String olayKisaAciklama, String olayaSahitKisiAdSoyad) {
        RaporId = raporId;
        DepSorumluId = depSorumluId;
        DepSorumluAdi=depSorumluAdi;
        this.isg_uzman_id = isg_uzman_id;
        RaporTarihi = raporTarihi;
        KisiAdSoyad = kisiAdSoyad;
        OlayTarihi = olayTarihi;
        OlayOlduguYer = olayOlduguYer;
        OlayOlduguDepartman = olayOlduguDepartman;
        OlaySebebi = olaySebebi;
        HasarGorenYer = hasarGorenYer;
        OlayKisaAciklama = olayKisaAciklama;
        OlayaSahitKisiAdSoyad = olayaSahitKisiAdSoyad;
    }

    public int getRaporId() {
        return RaporId;
    }

    public void setRaporId(int raporId) {
        RaporId = raporId;
    }

    public int getDepSorumluId() {
        return DepSorumluId;
    }

    public void setDepSorumluId(int depSorumluId) {
        DepSorumluId = depSorumluId;
    }

    public int getIsg_uzman_id() {
        return isg_uzman_id;
    }

    public void setIsg_uzman_id(int isg_uzman_id) {
        this.isg_uzman_id = isg_uzman_id;
    }

    public String getRaporTarihi() {
        return RaporTarihi;
    }

    public void setRaporTarihi(String raporTarihi) {
        RaporTarihi = raporTarihi;
    }

    public String getKisiAdSoyad() {
        return KisiAdSoyad;
    }

    public void setKisiAdSoyad(String kisiAdSoyad) {
        KisiAdSoyad = kisiAdSoyad;
    }

    public String getOlayTarihi() {
        return OlayTarihi;
    }

    public void setOlayTarihi(String olayTarihi) {
        OlayTarihi = olayTarihi;
    }

    public String getOlayOlduguYer() {
        return OlayOlduguYer;
    }

    public void setOlayOlduguYer(String olayOlduguYer) {
        OlayOlduguYer = olayOlduguYer;
    }

    public String getOlayOlduguDepartman() {
        return OlayOlduguDepartman;
    }

    public void setOlayOlduguDepartman(String olayOlduguDepartman) {
        OlayOlduguDepartman = olayOlduguDepartman;
    }

    public String getOlaySebebi() {
        return OlaySebebi;
    }

    public void setOlaySebebi(String olaySebebi) {
        OlaySebebi = olaySebebi;
    }

    public String getHasarGorenYer() {
        return HasarGorenYer;
    }

    public void setHasarGorenYer(String hasarGorenYer) {
        HasarGorenYer = hasarGorenYer;
    }

    public String getOlayKisaAciklama() {
        return OlayKisaAciklama;
    }

    public void setOlayKisaAciklama(String olayKisaAciklama) {
        OlayKisaAciklama = olayKisaAciklama;
    }

    public String getOlayaSahitKisiAdSoyad() {
        return OlayaSahitKisiAdSoyad;
    }

    public void setOlayaSahitKisiAdSoyad(String olayaSahitKisiAdSoyad) {
        OlayaSahitKisiAdSoyad = olayaSahitKisiAdSoyad;
    }

    public String getDepSorumluAdi() {
        return DepSorumluAdi;
    }

    public void setDepSorumluAdi(String depSorumluAdi) {
        DepSorumluAdi = depSorumluAdi;
    }
}
