package com.EX.examcreator.Models;

import android.os.Parcel;
import android.os.Parcelable;

public class Question implements Parcelable {

     String questionHead;
     String chooseOne;
     String chooseSecond;
     String chooseThird;
     String choosefourth;

    protected Question(Parcel in) {
        questionHead = in.readString();
        chooseOne = in.readString();
        chooseSecond = in.readString();
        chooseThird = in.readString();
        choosefourth = in.readString();
        id = in.readString();
        rightanswer = in.readString();
        difficulty = in.readString();
    }

    public static final Creator<Question> CREATOR = new Creator<Question>() {
        @Override
        public Question createFromParcel(Parcel in) {
            return new Question(in);
        }

        @Override
        public Question[] newArray(int size) {
            return new Question[size];
        }
    };

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRightanswer() {
        return rightanswer;
    }

    public void setRightanswer(String rightanswer) {
        this.rightanswer = rightanswer;
    }

     String id;
     String rightanswer;

    public String getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
    }

    String difficulty;


    public Question() {
    }

    public Question(String questionHead, String chooseOne, String chooseSecond, String chooseThird, String choosefourth
            ,String id , String rightanswer, String difficulty) {

        this.questionHead = questionHead;
        this.chooseOne = chooseOne;
        this.chooseSecond = chooseSecond;
        this.chooseThird = chooseThird;
        this.choosefourth = choosefourth;
        this.id = id;
        this.rightanswer = rightanswer;
        this.difficulty = difficulty;
    }

    public String getQuestionHead() {
        return questionHead;
    }

    public void setQuestionHead(String questionHead) {
        this.questionHead = questionHead;
    }

    public String getChooseOne() {
        return chooseOne;
    }

    public void setChooseOne(String chooseOne) {
        this.chooseOne = chooseOne;
    }

    public String getChooseSecond() {
        return chooseSecond;
    }

    public void setChooseSecond(String chooseSecond) {
        this.chooseSecond = chooseSecond;
    }

    public String getChooseThird() {
        return chooseThird;
    }

    public void setChooseThird(String chooseThird) {
        this.chooseThird = chooseThird;
    }

    public String getChoosefourth() {
        return choosefourth;
    }

    public void setChoosefourth(String choosefourth) {
        this.choosefourth = choosefourth;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(questionHead);
        dest.writeString(chooseOne);
        dest.writeString(chooseSecond);
        dest.writeString(chooseThird);
        dest.writeString(choosefourth);
        dest.writeString(id);
        dest.writeString(rightanswer);
        dest.writeString(difficulty);
    }
}
