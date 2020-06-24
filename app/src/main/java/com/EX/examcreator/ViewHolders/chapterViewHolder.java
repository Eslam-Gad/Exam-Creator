package com.EX.examcreator.ViewHolders;

import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import com.EX.examcreator.Activities.Chapterq;
import com.EX.examcreator.Models.Chapter;
import com.EX.examcreator.R;
import com.thoughtbot.expandablerecyclerview.viewholders.ChildViewHolder;

public class chapterViewHolder extends ChildViewHolder {

    private TextView mtextview;
    private String courseName;
    private String chapter_id;
    private String course_id;
    private String course_code;
    private String chapter_num;
    private String chapter_name;


    public chapterViewHolder(final View itemView) {
        super(itemView);
        mtextview = (TextView) itemView.findViewById(R.id.textview);
        mtextview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), Chapterq.class);
                intent.putExtra("courseName" , courseName);
                intent.putExtra("chapterName" , chapter_name);
                intent.putExtra("chapterId" , chapter_id);
                intent.putExtra("courseId" , course_id);
                intent.putExtra("courseCode" , course_code);
                intent.putExtra("chapterNum" , chapter_num);

                view.getContext().startActivity(intent);
            }
        });
    }

    public void bind(final Chapter chapter){

        mtextview.setText(chapter.chapterNumber+". "+chapter.name);
        courseName=chapter.courseName;
        course_id =chapter.courseId;
        chapter_id=chapter.id;
        course_code = chapter.courseCode;
        chapter_num = chapter.chapterNumber;
        chapter_name = chapter.name;
    }

}
