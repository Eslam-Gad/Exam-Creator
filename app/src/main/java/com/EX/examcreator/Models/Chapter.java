package com.EX.examcreator.Models;

import android.os.Parcel;
import android.os.Parcelable;

final public class Chapter implements Parcelable , Comparable<Chapter> {

    public Chapter(String name, String id, String courseName, String courseId, String courseCode ,String chapterNumber) {
        this.name = name;
        this.courseName = courseName;
        this.id =id;
        this.courseId =courseId;
        this.courseCode = courseCode;
        this.chapterNumber = chapterNumber;
    }

    public final String name;
    public final String id ;
    public final String courseName;
    public final String courseId;
    public final String courseCode;
    public final String chapterNumber;


    protected Chapter(Parcel in) {
        name = in.readString();
        id = in.readString();
        courseName = in.readString();
        courseId = in.readString();
        courseCode = in.readString();
        chapterNumber = in.readString();
    }

    public static final Creator<Chapter> CREATOR = new Creator<Chapter>() {
        @Override
        public Chapter createFromParcel(Parcel in) {
            return new Chapter(in);
        }

        @Override
        public Chapter[] newArray(int size) {
            return new Chapter[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(name);
        parcel.writeString(id);
        parcel.writeString(courseName);
        parcel.writeString(courseId);
        parcel.writeString(courseCode);
        parcel.writeString(chapterNumber);
    }

    @Override
    public int compareTo(Chapter chapter) {
        return chapterNumber.compareTo(chapter.chapterNumber);
    }
}
