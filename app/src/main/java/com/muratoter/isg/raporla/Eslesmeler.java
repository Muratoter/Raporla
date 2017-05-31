package com.muratoter.isg.raporla;

/**
 * Created by Murat on 1.05.2017.
 */

public class Eslesmeler {
    private int EslesmeId;
    private int IsgUzmaniId;
    private int DepSorumluId;
    private String DepSorumluAdSoyadi;
    private String DepSorumluEposta;
    private String EslesmeDurum;

    public Eslesmeler(int eslesmeId, int isgUzmaniId, int depSorumluId, String depSorumluAdSoyadi, String depSorumluEposta, String eslesmeDurum) {
        EslesmeId = eslesmeId;
        IsgUzmaniId = isgUzmaniId;
        DepSorumluId = depSorumluId;
        DepSorumluAdSoyadi = depSorumluAdSoyadi;
        DepSorumluEposta = depSorumluEposta;
        EslesmeDurum = eslesmeDurum;
    }

    public int getEslesmeId() {
        return EslesmeId;
    }

    public void setEslesmeId(int eslesmeId) {
        EslesmeId = eslesmeId;
    }

    public int getIsgUzmaniId() {
        return IsgUzmaniId;
    }

    public void setIsgUzmaniId(int isgUzmaniId) {
        IsgUzmaniId = isgUzmaniId;
    }

    public int getDepSorumluId() {
        return DepSorumluId;
    }

    public void setDepSorumluId(int depSorumluId) {
        DepSorumluId = depSorumluId;
    }

    public String getDepSorumluAdSoyadi() {
        return DepSorumluAdSoyadi;
    }

    public void setDepSorumluAdSoyadi(String depSorumluAdSoyadi) {
        DepSorumluAdSoyadi = depSorumluAdSoyadi;
    }

    public String getDepSorumluEposta() {
        return DepSorumluEposta;
    }

    public void setDepSorumluEposta(String depSorumluEposta) {
        DepSorumluEposta = depSorumluEposta;
    }

    public String getEslesmeDurum() {
        return EslesmeDurum;
    }

    public void setEslesmeDurum(String eslesmeDurum) {
        EslesmeDurum = eslesmeDurum;
    }
}
