package com.example.tasarim_calismasi_2.Model;

public class Masa {
    private String masaId;
    private String salonId;
    private Object kisiSayisi;
    private boolean masaDurumu;

    public Masa() {}

    public Masa(String masaId, String salonId, Object kisiSayisi, boolean masaDurumu) {
        this.masaId = masaId;
        this.salonId = salonId;
        this.kisiSayisi = kisiSayisi;
        this.masaDurumu = masaDurumu;
    }

    public String getMasaId() {
        return masaId;
    }

    public void setMasaId(String masaId) {
        this.masaId = masaId;
    }

    public String getSalonId() {
        return salonId;
    }

    public void setSalonId(String salonId) {
        this.salonId = salonId;
    }

    public Object getKisiSayisi() {
        return kisiSayisi;
    }

    public void setKisiSayisi(Object kisiSayisi) {
        this.kisiSayisi = kisiSayisi;
    }

    public boolean isMasaDurumu() {
        return masaDurumu;
    }

    public void setMasaDurumu(boolean masaDurumu) {
        this.masaDurumu = masaDurumu;
    }

    @Override
    public String toString() {
        return "Masa{" +
                "masaId='" + masaId + '\'' +
                ", salonId='" + salonId + '\'' +
                ", kisiSayisi=" + kisiSayisi +
                ", masaDurumu=" + masaDurumu +
                '}';
    }
}
