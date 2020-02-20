package com.sameer.tashbih;

public class Amal {

    private String prayerName;
    private int jamaat;
    private int moqim;
    private int qaza;
    private int fawot;
    private int tahajod;
    private int sport;
    private int study;
    private int murmur;
    private int image;

    public int getViewType() {
        return viewType;
    }

    public void setViewType(int viewType) {
        this.viewType = viewType;
    }

    private int viewType;
    public Amal(String prayerName, int jamaat, int moqim, int qaza, int fawot, int tahajod, int sport, int study, int murmur) {
        this.prayerName = prayerName;
        this.jamaat = jamaat;
        this.moqim = moqim;
        this.qaza = qaza;
        this.fawot = fawot;
        this.tahajod = tahajod;
        this.sport = sport;
        this.study = study;
        this.murmur = murmur;
    }
    public Amal(String prayerName, int jamaat, int moqim, int qaza, int fawot, int image, int viewType){
        this.prayerName = prayerName;
        this.jamaat = jamaat;
        this.moqim = moqim;
        this.qaza = qaza;
        this.fawot = fawot;
        this.image = image;
        this.viewType = viewType;
    }

    public Amal(int tahajod, int sport, int study, int murmur, int viewType){
        this.tahajod = tahajod;
        this.sport = sport;
        this.study = study;
        this.murmur = murmur;
        this.viewType = viewType;
    }
    public String getPrayerName() {
        return prayerName;
    }

    public void setPrayerName(String prayerName) {
        this.prayerName = prayerName;
    }

    public int getJamaat() {
        return jamaat;
    }

    public void setJamaat(int jamaat) {
        this.jamaat = jamaat;
    }

    public int getMoqim() {
        return moqim;
    }

    public void setMoqim(int moqim) {
        this.moqim = moqim;
    }

    public int getQaza() {
        return qaza;
    }

    public void setQaza(int qaza) {
        this.qaza = qaza;
    }

    public int getFawot() {
        return fawot;
    }

    public void setFawot(int fawot) {
        this.fawot = fawot;
    }

    public int getTahajod() {
        return tahajod;
    }

    public void setTahajod(int tahajod) {
        this.tahajod = tahajod;
    }

    public int getSport() {
        return sport;
    }

    public void setSport(int sport) {
        this.sport = sport;
    }

    public int getStudy() {
        return study;
    }

    public void setStudy(int study) {
        this.study = study;
    }

    public int getMurmur() {
        return murmur;
    }

    public void setMurmur(int murmur) {
        this.murmur = murmur;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }
}
