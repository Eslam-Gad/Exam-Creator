package com.EX.examcreator.Activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import com.EX.examcreator.Models.Question;
import com.EX.examcreator.OCR_TextDetected;
import com.EX.examcreator.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import es.dmoral.toasty.Toasty;


public class addq extends AppCompatActivity {

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
    RadioButton a, b, c , d , hard, med , easy;
    String courseId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addq);


        toolbar = (Toolbar) findViewById(R.id.tooBar);
        setSupportActionBar(toolbar);
        setTitle("Add New Question");
        toolbar.setSubtitle("Fill the few fields below, all required");

        root = (LinearLayout)findViewById(R.id.root);

        chapterId = getIntent().getStringExtra("chap_ID");
        courseId = getIntent().getStringExtra("course_ID");

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


        String t_detected;

        if(getIntent().hasExtra("textDetected")){
            t_detected = getIntent().getStringExtra("textDetected");
            if(t_detected.equals("No Text Detected")) {
                ques.setText("");
            }
            else {
                ques.setText(t_detected);
            }
        }

        if(savedInstanceState!=null){
            ques.setText(savedInstanceState.getString("ques_saved"));
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.saveQuestion) {
            addQuestionToFirebase();
        }
        return super.onOptionsItemSelected(item);
    }

    private void addQuestionToFirebase(){

        final ProgressDialog dialog = ProgressDialog.show(addq.this, "",
                "Loading. Please wait...", true);

        String id = databasequestion.push().getKey();
        String queshead = ques.getText().toString().trim();
        String answerA = answer_a.getText().toString().trim();
        String answerB = answer_b.getText().toString().trim();
        String answerC = answer_c.getText().toString().trim();
        String answerD = answer_d.getText().toString().trim();
        String RA = getRightAnswer();
        String diff = String.valueOf(getdiff());

        if(!TextUtils.isEmpty(queshead)&&!TextUtils.isEmpty(answerA)&&!TextUtils.isEmpty(answerB)&&
                !TextUtils.isEmpty(answerC)&&!TextUtils.isEmpty(answerD)&&!TextUtils.isEmpty(RA)&&!TextUtils.isEmpty(diff)){

            queshead = (queshead.charAt(queshead.length()-1)!='?')?queshead.concat(" ?"):queshead;

            Question question = new Question(queshead, answerA, answerB, answerC, answerD, id, RA, diff);
            databasequestion.child(id).setValue(question).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {

                    if(task.isSuccessful()){
                        dialog.dismiss();
                        Toasty.success(addq.this, "Success!", Toast.LENGTH_LONG, true).show();
                        clear();

                    } else {
                        Snackbar.make(root, task.getException().getMessage(), Snackbar.LENGTH_LONG).show();
                    }
                }

                private void clear() {
                    ques.setText("");
                    answer_a.setText("");
                    answer_b.setText("");
                    answer_c.setText("");
                    answer_d.setText("");
                    ques.requestFocus();
                }
            });

        } else {
            Snackbar.make(root, "All fields required !", Snackbar.LENGTH_LONG).show();
            dialog.dismiss();
        }

    }

    private String getRightAnswer() {
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.addq, menu);
        return true;
    }

    public void TextDetect(View view) {
       startActivity(new Intent(addq.this , OCR_TextDetected.class).putExtra("chap_ID" , chapterId)
               .putExtra("course_ID" , courseId));
       finish();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState, @NonNull PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);

        outState.putString("ques_saved" , ques.getText().toString());
    }
}
