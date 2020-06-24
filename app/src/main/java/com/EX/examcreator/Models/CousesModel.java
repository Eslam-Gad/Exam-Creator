package com.EX.examcreator.Models;

import android.os.Parcel;
import android.os.Parcelable;

public class CousesModel implements Parcelable {

    protected CousesModel(Parcel in) {
        id = in.readString();
        dr_id = in.readString();
        coursesName = in.readString();
        course_code = in.readString();
    }

    public static final Creator<CousesModel> CREATOR = new Creator<CousesModel>() {
        @Override
        public CousesModel createFromParcel(Parcel in) {
            return new CousesModel(in);
        }

        @Override
        public CousesModel[] newArray(int size) {
            return new CousesModel[size];
        }
    };

    public void setId(String id) {
        this.id = id;
    }

    public void setDr_id(String dr_id) {
        this.dr_id = dr_id;
    }

    public void setCoursesName(String coursesName) {
        this.coursesName = coursesName;
    }

    String id;
    String dr_id;
    String coursesName;
    String course_code;




    public String getCourse_code() {
        return course_code;
    }

    public void setCourse_code(String course_code) {
        this.course_code = course_code;
    }

    public String getId() {
        return id;
    }

    public String getDr_id() {
        return dr_id;
    }

    public String getCoursesName() {
        return coursesName;
    }

    public CousesModel(){

    }


    public CousesModel(String coursesName , String dr_id , String id , String course_code) {
        this.coursesName = coursesName;
        this.dr_id = dr_id;
        this.id = id;
        this.course_code = course_code;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(dr_id);
        dest.writeString(coursesName);
        dest.writeString(course_code);
    }
}
