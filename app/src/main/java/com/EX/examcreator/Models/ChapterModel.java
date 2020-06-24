package com.EX.examcreator.Models;

import android.os.Parcel;
import android.os.Parcelable;

public class ChapterModel implements Comparable<ChapterModel>, Parcelable {

    public ChapterModel(String name , String id , String number) {
        this.name = name;
        this.id = id;
        this.number = number;
    }

    protected ChapterModel(Parcel in) {
        name = in.readString();
        id = in.readString();
        number = in.readString();
    }

    public static final Creator<ChapterModel> CREATOR = new Creator<ChapterModel>() {
        @Override
        public ChapterModel createFromParcel(Parcel in) {
            return new ChapterModel(in);
        }

        @Override
        public ChapterModel[] newArray(int size) {
            return new ChapterModel[size];
        }
    };

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    String name;
    String id;
    String number;

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public ChapterModel(){

    }

    @Override
    public int compareTo(ChapterModel chapterModel) {
        return Integer.parseInt(number) - Integer.parseInt(chapterModel.getNumber()) ;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(id);
        dest.writeString(number);
    }
}
