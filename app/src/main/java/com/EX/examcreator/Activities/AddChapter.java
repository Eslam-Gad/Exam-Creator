package com.EX.examcreator.Activities;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.EX.examcreator.Adapters.AddChapterAdapter;
import com.EX.examcreator.MainActivity;
import com.EX.examcreator.Models.ChapterModel;
import com.EX.examcreator.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class AddChapter extends AppCompatActivity {

    DatabaseReference chapterReference;

    TextView couseName;
    EditText chapterName;
    EditText chapterNumber;

    RecyclerView rvChapters;
    ScrollView root;

    ProgressBar progressBar;
    TextView emptyMark;

    String courseName;
    String corseId;

    List<ChapterModel> chapterModels;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_chapter);

        progressBar = (ProgressBar)findViewById(R.id.pb_loading_indicator);
        emptyMark =(TextView)findViewById(R.id.emptyMark);

        emptyMark.setText("No Chapters added yet !\nlet's add new chapter");
        chapterNumber = (EditText)findViewById(R.id.chapterNumber);

        couseName = (TextView)findViewById(R.id.courseName);
        rvChapters = (RecyclerView)findViewById(R.id.rv);
        chapterName=(EditText)findViewById(R.id.chapterName);
        root =(ScrollView) findViewById(R.id.root);

        rvChapters.setLayoutManager(new LinearLayoutManager(this));
        new ItemTouchHelper(simpleCallback).attachToRecyclerView(rvChapters);

        courseName = getIntent().getStringExtra("course_name");
        corseId = getIntent().getStringExtra("course_id");

        couseName.setText(courseName);

        chapterReference = FirebaseDatabase.getInstance().getReference("chapters").child(corseId);

        chapterModels = new ArrayList<>();

        /* enable back buutun */
        ActionBar actionBar = this.getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.home_icon , menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        else if (item.getItemId() == R.id.home_ic) {

            Intent intent = new Intent(AddChapter.this , HomeActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStart() {
        super.onStart();


        chapterReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                chapterModels.clear();
                for(DataSnapshot chaptersnapshot : dataSnapshot.getChildren()){
                    ChapterModel chapterModel = chaptersnapshot.getValue(ChapterModel.class);
                    chapterModels.add(chapterModel);
                }


               /* chapterModels.sort(); */
                Collections.sort(chapterModels);


                AddChapterAdapter addChapterAdapter = new AddChapterAdapter(chapterModels);

                rvChapters.setAdapter(addChapterAdapter);

                progressBar.setVisibility(View.GONE);
                emptyMark.setVisibility(View.GONE);

                if(chapterModels.isEmpty())
                emptyMark.setVisibility(View.VISIBLE);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    public void addnewChap(View view) {

        String ch_name = chapterName.getText().toString();
        String ch_num = chapterNumber.getText().toString();

        if(!TextUtils.isEmpty(ch_name)&&!TextUtils.isEmpty(ch_num)){

            String new_id = chapterReference.push().getKey();
            ChapterModel chapterModel = new ChapterModel(ch_name , new_id , ch_num);
            chapterReference.child(new_id).setValue(chapterModel);


            chapterName.setText("");
            chapterNumber.setText("");

        }else {
            Snackbar.make(root, "All fields are required !", Snackbar.LENGTH_LONG).show();
        }
    }


    ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.RIGHT|ItemTouchHelper.LEFT) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(@NonNull final RecyclerView.ViewHolder viewHolder, int direction) {

            if(direction == ItemTouchHelper.LEFT){
                AlertDialog diaBox = AskOption(viewHolder.getAdapterPosition());
                diaBox.show();

            }else {

                AlertDialog.Builder builder = new AlertDialog.Builder(AddChapter.this);
                View mview = getLayoutInflater().inflate(R.layout.course_dialog, null);
                final EditText sub_name = (EditText) mview.findViewById(R.id.sub_name);
                final EditText sub_num = (EditText) mview.findViewById(R.id.sub_code);
                sub_name.setText(chapterModels.get(viewHolder.getAdapterPosition()).getName());
                sub_num.setText(chapterModels.get(viewHolder.getAdapterPosition()).getNumber());
                sub_name.requestFocus();
                final TextView textView = (TextView) mview.findViewById(R.id.name);
                textView.setText("Edit Chapter Name");

                final Button save = (Button) mview.findViewById(R.id.save);

                builder.setView(mview);

                final AlertDialog alertDialog = builder.create();
                alertDialog.show();

                alertDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialogInterface) {
                        AddChapterAdapter addChapterAdapter = new AddChapterAdapter(chapterModels);
                        rvChapters.setAdapter(addChapterAdapter);
                    }
                });

                final String id = chapterModels.get(viewHolder.getAdapterPosition()).getId();

                save.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (!sub_name.getText().toString().isEmpty() && !sub_num.getText().toString().isEmpty()&&
                        TextUtils.isDigitsOnly(sub_num.getText().toString())) {
                            alertDialog.cancel();
                            final ProgressDialog dialog = ProgressDialog.show(AddChapter.this, "",
                                    "Loading. Please wait...", true);

                            String name = sub_name.getText().toString().trim();
                            String num = sub_num.getText().toString().trim();

                            if(updateCohapterName(name, id , num))
                                dialog.dismiss();
                            else{
                                Toast.makeText(AddChapter.this, "error", Toast.LENGTH_LONG).show();
                            }

                        } else {
                            alertDialog.cancel();
                            Toast.makeText(AddChapter.this, "pls: All fields required\n chapter number accept integer only", Toast.LENGTH_LONG).show();
                        }
                    }
                });

            }
        }
    };

    private boolean updateCohapterName(String name, String id , String num) {

        DatabaseReference databaseReference1 = FirebaseDatabase.getInstance().getReference("chapters").child(corseId)
                .child(id);

        ChapterModel chapterModel = new ChapterModel(name, id , num);

        databaseReference1.setValue(chapterModel);

        return true;
    }

    private AlertDialog AskOption(final int pos)
    {
        AlertDialog myQuittingDialogBox = new AlertDialog.Builder(this)
                // set message, title, and icon
                .setTitle("Delete")
                .setMessage("Do you want to Delete " +chapterModels.get(pos).getName()+ " Chapter ?" +
                        "\nyou can't return back"+
                        "\nwill delete all related questions !")
                .setIcon(R.drawable.ic_delete_black_24dp)

                .setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        AddChapterAdapter addChapterAdapter = new AddChapterAdapter(chapterModels);
                        rvChapters.setAdapter(addChapterAdapter);
                    }
                })
                .setPositiveButton("Delete", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int whichButton) {
                        //your deleting code
                        dialog.dismiss();
                        final ProgressDialog deldialog = ProgressDialog.show(AddChapter.this, "",
                                "Loading. Please wait...", true);

                        String id = chapterModels.get(pos).getId();

                        DatabaseReference chappterReferencedelete = FirebaseDatabase.getInstance().getReference("chapters").child(corseId)
                                .child(id);

                        DatabaseReference questionReferencedelete = FirebaseDatabase.getInstance().getReference("questions").child(corseId)
                                .child(id);

                        questionReferencedelete.removeValue();
                        chappterReferencedelete.removeValue()
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            deldialog.dismiss();
                                        } else {
                                            Snackbar.make(root, task.getException().getMessage(), Snackbar.LENGTH_LONG).show();
                                        }
                                    }
                                });
                    }

                })
                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        dialog.dismiss();
                        AddChapterAdapter addChapterAdapter = new AddChapterAdapter(chapterModels);
                        rvChapters.setAdapter(addChapterAdapter);
                    }
                })
                .create();

        return myQuittingDialogBox;
    }
}
