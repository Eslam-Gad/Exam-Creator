package com.EX.examcreator.Fragments;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.EX.examcreator.Activities.EditQuestion;
import com.EX.examcreator.Database_OFF_Line.AppExecutor;
import com.EX.examcreator.Database_OFF_Line.Database_Favourite;
import com.EX.examcreator.Database_OFF_Line.Model_Favourite;
import com.EX.examcreator.Models.Question;
import com.EX.examcreator.R;
import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import es.dmoral.toasty.Toasty;

public class QuestionFragnebt extends Fragment  {
    Database_Favourite db;
    String Tag1="test";

    private int previousScrollY = 0;

    public QuestionFragnebt() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_question_fragnebt, container, false);
        final Question question = getArguments().getParcelable("question");
        final String chapterId = getArguments().getString("chapterId");
        final String courseId = getArguments().getString("courseId");

        final TextView textView = view.findViewById(R.id.qnumber);
        final TextView ques = view.findViewById(R.id.textQuestionid);
        final TextView answer_a = view.findViewById(R.id.choose1id);
        final TextView answer_b = view.findViewById(R.id.choose2id);
        final TextView answer_c = view.findViewById(R.id.choose3id);
        final TextView answer_d = view.findViewById(R.id.choose4id);
        final TextView right = view.findViewById(R.id.rightAns);
        final TextView diff = view.findViewById(R.id.difff);


        textView.setText((getArguments().getInt("pos")+1)+" - ");
        ques.setText(question.getQuestionHead());
        answer_a.setText(question.getChooseOne());
        answer_b.setText(question.getChooseSecond());
        answer_c.setText(question.getChooseThird());
        answer_d.setText(question.getChoosefourth());
        right.setText("Answer : "+question.getRightanswer());
        diff.setText(getDiff(question.getDifficulty()));

        FloatingActionButton floatingActionDel = view.findViewById(R.id.dell);
        FloatingActionButton floatingActionedit = view.findViewById(R.id.edit);
        FloatingActionButton floatingActionshare = view.findViewById(R.id.share);
        FloatingActionButton floatingActionfav = view.findViewById(R.id.fav);
        final ScrollView scrollView = view.findViewById(R.id.scrollview);
        final FloatingActionMenu floatingActionMenu = view.findViewById(R.id.menu);
        floatingActionMenu.setVisibility(View.VISIBLE);


        scrollView.getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {
            @Override
            public void onScrollChanged() {

                if (scrollView.getScrollY() > previousScrollY) {
                    floatingActionMenu.setVisibility(View.INVISIBLE);
                } else if (scrollView.getScrollY() < previousScrollY) {
                    floatingActionMenu.setVisibility(View.VISIBLE);
                }
                previousScrollY = scrollView.getScrollY();
            }
        });


        floatingActionshare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.setType("text/plain");
                shareIntent.putExtra(Intent.EXTRA_TEXT , "Course: "+getArguments().getString("courseName")+"\n"+
                        "Chapter: "+getArguments().getString("chapterName")+"\n"+
                        "question_num: "+(getArguments().getInt("pos")+1)+"\n\n"+
                        "Question :\n"+question.getQuestionHead()+"\n\n"+
                        "Answers :\n"+question.getChooseOne()+"\n"+
                        question.getChooseSecond()+"\n"+
                        question.getChooseThird()+"\n"+
                        question.getChoosefourth()+"\n");
                shareIntent.putExtra(Intent.EXTRA_SUBJECT , "share from Exam Creator\n");
                startActivity(Intent.createChooser(shareIntent , "Share via"));
            }
        });


        floatingActionedit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(v.getContext() , EditQuestion.class);
                Bundle bundle = new Bundle();
                bundle.putParcelable("question" , question);
                bundle.putString("chapter_id" , chapterId);
                bundle.putString("course_id" , courseId);
                intent.putExtras(bundle);
                v.getContext().startActivity(intent);
            }
        });

        floatingActionDel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog alertDialog = AskOption(question.getId() , chapterId, courseId);
                alertDialog.show();
            }
        });

        /*------------------------Favourite-----------------------*/
        db = Database_Favourite.getInstance(getContext());

        floatingActionfav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Model_Favourite task = new Model_Favourite();
                task.setId(question.getId());
                task.setQuestions(question.getQuestionHead());
                task.setChoice1(question.getChooseOne());
                task.setChoice2(question.getChooseSecond());
                task.setChoice3(question.getChooseThird());
                task.setChoice4(question.getChoosefourth());
                task.setAnswer(question.getRightanswer());
                task.setDifficulty(getDiff(question.getDifficulty()));
                task.setChapterName(getArguments().getString("chapterName"));
                task.setCourseName(getArguments().getString("courseName"));

                AppExecutor.getInstance().diskIO().execute(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            // insert new task
                            db.taskDao().insertTask(task);

                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toasty.success(getContext(), "Success!", Toast.LENGTH_LONG, true).show();
                                }
                            });

                        }catch (Exception ex){
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toasty.info(getContext(), "Aready saved!", Toast.LENGTH_LONG, true).show();
                                }
                            });
                        }
                    }

                });
            }
        });
        return  view;
    }


    private AlertDialog AskOption(final String id , final String chapterId , final String courseId)
    {
        AlertDialog myQuittingDialogBox = new AlertDialog.Builder(getContext())
                // set message, title, and icon
                .setTitle("Delete")
                .setMessage("Do you want to Delete this question ?"+
                        "\nyou can't return back !")
                .setIcon(R.drawable.ic_delete_black_24dp)

                .setPositiveButton("Delete", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int whichButton) {
                        //your deleting code
                        dialog.dismiss();
                        final ProgressDialog deldialog = ProgressDialog.show(getContext(), "",
                                "Loading. Please wait...", true);

                        DatabaseReference questionReferencedelete = FirebaseDatabase.getInstance()
                                .getReference("questions").child(courseId).child(chapterId).child(id);

                        questionReferencedelete.removeValue()

                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            deldialog.dismiss();
                                        } else {
                                            Toast.makeText(getContext(), task.getException().getMessage(), Toast.LENGTH_LONG).show();
                                        }
                                    }
                                });
                    }

                })
                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        dialog.dismiss();
                    }
                }).create();

        return myQuittingDialogBox;
    }

    private String getDiff(String diff){
        if(diff.equals("1")){
            return  "Easy";
        }else if(diff.equals("2")){
            return "Medium";
        }else{
            return "Hard";
        }
    }

}
