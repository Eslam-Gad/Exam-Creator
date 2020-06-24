package com.EX.examcreator.Activities;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import com.EX.examcreator.Models.Question;
import com.EX.examcreator.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import es.dmoral.toasty.Toasty;

public class EditQuestion extends AppCompatActivity {

    private Toolbar toolbar;
    String chapterId;
    DatabaseReference databasequestion;
    LinearLayout root;
    EditText ques;
    EditText answer_a;
    EditText answer_b;
    EditText answer_c;
    EditText answer_d;
    RadioGroup rightanswe , difficulty;
    RadioButton a, b, c , d , hard , easy , med;
    String courseId;
    Question question;
    ImageButton ocr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addq);

        toolbar = (Toolbar) findViewById(R.id.tooBar);
        setSupportActionBar(toolbar);
        setTitle("Edit existing  Question");
        toolbar.setSubtitle("Edit the few fields below then save");

        root = (LinearLayout)findViewById(R.id.root);

        Bundle bundle = getIntent().getExtras();
        chapterId = bundle.getString("chapter_id");
        courseId = bundle.getString("course_id");
        question = bundle.getParcelable("question");

        databasequestion = FirebaseDatabase.getInstance().getReference("questions").child(courseId).child(chapterId);

        ques = (EditText)findViewById(R.id.quesHead);
        answer_a=(EditText)findViewById(R.id.answer_a);
        answer_b=(EditText)findViewById(R.id.answer_b);
        answer_c=(EditText)findViewById(R.id.answer_c);
        answer_d=(EditText)findViewById(R.id.answer_d);

        rightanswe =(RadioGroup)findViewById(R.id.RG);
        a=(RadioButton)findViewById(R.id.a);
        b=(RadioButton)findViewById(R.id.b);
        c=(RadioButton)findViewById(R.id.c);
        d=(RadioButton)findViewById(R.id.d);

        difficulty =(RadioGroup)findViewById(R.id.sb);
        hard=(RadioButton)findViewById(R.id.H);
        med=(RadioButton)findViewById(R.id.M);
        easy=(RadioButton)findViewById(R.id.E);

        ques.setText(question.getQuestionHead());
        ques.clearFocus();
        answer_a.setText(question.getChooseOne());
        answer_b.setText(question.getChooseSecond());
        answer_c.setText(question.getChooseThird());
        answer_d.setText(question.getChoosefourth());
        getRightAnswer();
        getRightDiff();

        ocr = (ImageButton)findViewById(R.id.ocr);
        ocr.setVisibility(View.GONE);

        if(savedInstanceState!=null){
            ques.setText(savedInstanceState.getString("ques_saved"));
        }
    }

    private void getRightAnswer() {
        if(question.getRightanswer().equals("a"))
        rightanswe.check(R.id.a);
        else if(question.getRightanswer().equals("b"))
            rightanswe.check(R.id.b);
        else if(question.getRightanswer().equals("c"))
            rightanswe.check(R.id.c);
        else
            rightanswe.check(R.id.d);
    }

    private void getRightDiff() {
        if(question.getDifficulty().equals("1"))
            difficulty.check(R.id.E);
        else if(question.getDifficulty().equals("2"))
            difficulty.check(R.id.M);
        else if(question.getDifficulty().equals("3"))
            difficulty.check(R.id.H);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.saveQuestion) {
            EditQuestionToFirebase();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.addq, menu);
        return true;
    }

    private String getdiff() {
        String r_answer="";

        int itemSelected = difficulty.getCheckedRadioButtonId();
        if(itemSelected == hard.getId()){
            r_answer = "3";
        }else if(itemSelected == easy.getId()){
            r_answer = "1";
        }else if(itemSelected == med.getId()){
            r_answer = "2";
        }
        return  r_answer;
    }

    private void EditQuestionToFirebase() {

        final ProgressDialog dialog = ProgressDialog.show(EditQuestion.this, "",
                "Loading. Please wait...", true);

        String queshead = ques.getText().toString().trim();
        String answerA = answer_a.getText().toString().trim();
        String answerB = answer_b.getText().toString().trim();
        String answerC = answer_c.getText().toString().trim();
        String answerD = answer_d.getText().toString().trim();
        String RA = getrightAnswer();
        String diff = String.valueOf(getdiff());

        if(!TextUtils.isEmpty(queshead)&&!TextUtils.isEmpty(answerA)&&!TextUtils.isEmpty(answerB)&&
                !TextUtils.isEmpty(answerC)&&!TextUtils.isEmpty(answerD)&&!TextUtils.isEmpty(RA)&&!TextUtils.isEmpty(diff)) {

            Question question1 = new Question(queshead, answerA, answerB, answerC, answerD, question.getId(), RA, diff);
            databasequestion.child(question.getId()).setValue(question1).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {

                    if(task.isSuccessful()){
                        dialog.dismiss();
                        Toasty.success(EditQuestion.this, "Success!", Toast.LENGTH_LONG, true).show();
                    } else {
                        Snackbar.make(root, task.getException().getMessage(), Snackbar.LENGTH_LONG).show();
                    }
                }
            });

        } else {
            Snackbar.make(root, "All fields required !", Snackbar.LENGTH_LONG).show();
            dialog.dismiss();
        }
    }

    private String getrightAnswer() {
        String r_answer="";

        int itemSelected = rightanswe.getCheckedRadioButtonId();
        if(itemSelected == a.getId()){
            r_answer = "a";
        }else if(itemSelected == b.getId()){
            r_answer = "b";
        }else if(itemSelected == c.getId()){
            r_answer = "c";
        }else if(itemSelected == d.getId()) {
            r_answer = "d";
        }
        return  r_answer;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState, @NonNull PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);

        outState.putString("ques_saved" , ques.getText().toString());

    }
}
