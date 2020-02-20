package com.sameer.tashbih;

import android.os.Parcel;
import android.os.Parcelable;

public class Tasbih implements Parcelable {
    public static final Creator<Tasbih> CREATOR = new Creator<Tasbih>() {

        @Override
        public Tasbih createFromParcel(Parcel parcel) {
            return new Tasbih(parcel);
        }

        @Override
        public Tasbih[] newArray(int size) {
            return new Tasbih[size];
        }


    };
    private int viewType;
    private int tasbih_id;
    private String arabic;
    private String pashto;
    private String farsi;
    private String english;
    private String fazilat;
    private int count;
    private int state;
    private int category;
    private int total_count;
    public Tasbih() {
    }

    public Tasbih(int tasbih_id, String arabic, String pashto, String farsi, String english, String fazilat ,int count, int state, int category, int total_count) {
        this.english = english;
        this.tasbih_id = tasbih_id;
        this.arabic = arabic;
        this.pashto = pashto;
        this.farsi = farsi;
        this.fazilat = fazilat;
        this.count = count;
        this.state = state;
        this.category = category;
        this.total_count = total_count;
    }
    public Tasbih(int tasbih_id,int count, int state, int category, int total_count) {
        this.tasbih_id = tasbih_id;
        this.count = count;
        this.state = state;
        this.category = category;
        this.total_count = total_count;
    }

    public Tasbih(int tasbih_id, String arabic, int count) {
        this.tasbih_id = tasbih_id;
        this.arabic = arabic;
        this.count = count;
    }

    public Tasbih(int category, int count, int total_count) {

    }

    protected Tasbih(Parcel parcel) {
        this.tasbih_id = parcel.readInt();
        this.viewType = parcel.readInt();
        this.arabic = parcel.readString();
        this.farsi = parcel.readString();
        this.count = parcel.readInt();
        this.state = parcel.readInt();
        this.category = parcel.readInt();
        this.total_count = parcel.readInt();
    }

    public String getFazilat() {
        return fazilat;
    }

    public void setFazilat(String fazilat) {
        this.fazilat = fazilat;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public int getCategory() {
        return category;
    }

    public void setCategory(int category) {
        this.category = category;
    }

    public int getTasbih_id() {
        return tasbih_id;
    }

    public void setTasbih_id(int tasbih_id) {
        this.tasbih_id = tasbih_id;
    }

    public String getArabic() {
        return arabic;
    }

    public void setArabic(String arabic) {
        this.arabic = arabic;
    }

    @Override
    public String toString() {
        return "Tasbih{" +
                "tasbih_id=" + tasbih_id +
                ", arabic='" + arabic + '\'' +
                ", farsi='" + farsi + '\'' +
                ", count=" + count +
                ", state=" + state +
                ", category=" + category +
                ", total_count=" + total_count +
                '}';
    }

    public String getFarsi() {
        return farsi;
    }

    public void setFarsi(String farsi) {
        this.farsi = farsi;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getTotal_count() {
        return total_count;
    }

    public void setTotal_count(int total_count) {
        this.total_count = total_count;
    }

    public int getViewType() {
        return viewType;
    }

    public void setViewType(int viewType) {
        this.viewType = viewType;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeInt(this.tasbih_id);
        parcel.writeString(this.arabic);
        parcel.writeString(this.farsi);
        parcel.writeInt(this.count);
        parcel.writeInt(this.total_count);
        parcel.writeInt(this.viewType);
        parcel.writeInt(this.state);
        parcel.writeInt(this.category);
        parcel.writeInt(this.tasbih_id);
    }

    public String getPashto() {
        return pashto;
    }

    public void setPashto(String pashto) {
        this.pashto = pashto;
    }

    public String getEnglish() {
        return english;
    }

    public void setEnglish(String english) {
        this.english = english;
    }
}