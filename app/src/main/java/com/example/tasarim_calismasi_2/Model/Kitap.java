package com.example.tasarim_calismasi_2.Model;

public class Kitap {
    public String kitapAd;
    public String kitapId;
    public String blokId;
    public String rafId;
    public String salonId;
    public String yazarId;
    public String kitapKodu;
    public Boolean kitapDurumu;

    public Kitap(){}

    public Kitap(String kitapAd, String kitapKodu,String kitapId,String blokId,String rafId, String salonId, String yazarId, Boolean kitapDurumu) {
        this.kitapAd = kitapAd;
        this.kitapKodu= kitapKodu;
        this.kitapDurumu = kitapDurumu;
        this.blokId = blokId;
        this.salonId = salonId;
        this.rafId = rafId;
        this.yazarId = yazarId;
        this.kitapId = kitapId;
    }

    public String getKitapAd() {
        return kitapAd;
    }

    public void setKitapAd(String kitapAd) {
        this.kitapAd = kitapAd;
    }

    public String getKitapId() {
        return kitapId;
    }

    public void setKitapId(String kitapId) {
        this.kitapId = kitapId;
    }

    public String getBlokId() {
        return blokId;
    }

    public void setBlokId(String blokId) {
        this.blokId = blokId;
    }

    public String getRafId() {
        return rafId;
    }

    public void setRafId(String rafId) {
        this.rafId = rafId;
    }

    public String getSalonId() {
        return salonId;
    }

    public void setSalonId(String salonId) {
        this.salonId = salonId;
    }

    public String getYazarId() {
        return yazarId;
    }

    public void setYazarId(String yazarId) {
        this.yazarId = yazarId;
    }

    public String getKitapKodu() {
        return kitapKodu;
    }

    public void setKitapKodu(String kitapKodu) {
        this.kitapKodu = kitapKodu;
    }

    public Boolean getKitapDurumu() {
        return kitapDurumu;
    }

    public void setKitapDurumu(Boolean kitapDurumu) {
        this.kitapDurumu = kitapDurumu;
    }
}

