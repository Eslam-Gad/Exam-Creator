package com.EX.examcreator.Adapters;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import com.EX.examcreator.Fragments.QuestionFragnebt;
import com.EX.examcreator.Models.Question;
import java.util.ArrayList;

public class ViewPagerAdapter extends FragmentStatePagerAdapter {

    public ArrayList<Question> getQuestions() {
        return questions;
    }

    public void setQuestions(ArrayList<Question> questions) {
        this.questions = questions;
    }

    private ArrayList<Question> questions;

    public String getChapterId() {
        return chapterId;
    }

    public void setChapterId(String chapterId) {
        this.chapterId = chapterId;
    }

    private String chapterId;

    public String getCourseId() {
        return courseId;
    }

    public void setCourseId(String courseId) {
        this.courseId = courseId;
    }

    private String courseId;

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getChapterName() {
        return chapterName;
    }

    public void setChapterName(String chapterName) {
        this.chapterName = chapterName;
    }

    private String courseName;
    private String chapterName;


    public ViewPagerAdapter(FragmentManager fm) {

        super(fm);
    }

    @Override
    public Fragment getItem(int position) {

        QuestionFragnebt questionFragnebt = new QuestionFragnebt();
        Bundle bundle = new Bundle();
        bundle.putParcelable("question" , questions.get(position));
        bundle.putString("chapterId" , chapterId);
        bundle.putString("courseId" , courseId);
        bundle.putString("chapterName" , chapterName);
        bundle.putString("courseName" , courseName);
        bundle.putInt("pos" , position);

        questionFragnebt.setArguments(bundle);
        return questionFragnebt;
    }

    @Override
    public int getCount() {
        return questions.size();
    }
}
