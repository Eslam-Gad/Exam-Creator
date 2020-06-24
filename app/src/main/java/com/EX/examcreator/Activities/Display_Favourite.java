package com.EX.examcreator.Activities;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.EX.examcreator.Adapters.Favourite_Adapter;
import com.EX.examcreator.Database_OFF_Line.AppExecutor;
import com.EX.examcreator.Database_OFF_Line.Database_Favourite;
import com.EX.examcreator.Database_OFF_Line.Model_Favourite;
import com.EX.examcreator.R;
import java.util.List;

public class Display_Favourite extends AppCompatActivity implements Favourite_Adapter.ListItemClickListener {
    private Favourite_Adapter mAdapter;
    private RecyclerView recyclerView;
    TextView textView;
    Database_Favourite db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display__favourite);

        textView = findViewById(R.id.markEmpty);
        db = Database_Favourite.getInstance(getApplicationContext());
        recyclerView = (RecyclerView) findViewById(R.id.RV_Fav);

        favouritLoadData();
    }

    @Override
    public void onClick(final Model_Favourite model_favourite) {

        AlertDialog diaBox = delFav(model_favourite);
        diaBox.show();
    }

    private AlertDialog delFav(final Model_Favourite model_favourite) {
        final AlertDialog myQuittingDialogBox = new AlertDialog.Builder(Display_Favourite.this)
                .setTitle("Delete")
                .setMessage("Do you want to Delete this question from your offline library ?\n"+
                        "you can't return back!!")
                .setIcon(R.drawable.ic_delete_black_24dp)
                .setPositiveButton("Delete", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int whichButton) {

                        AppExecutor.getInstance().diskIO().execute(new Runnable() {
                            @Override
                            public void run() {
                                db.taskDao().deleteTask(model_favourite);
                            }
                        });
                }})
                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        dialog.dismiss();
                    }
                })
                .create();
        return myQuittingDialogBox;
    }


    public void favouritLoadData() {

        final LiveData<List<Model_Favourite>> list = db.taskDao().loadAllTasks();

        list.observe(this, new Observer<List<Model_Favourite>>() {
            @Override
            public void onChanged(@Nullable List<Model_Favourite> taskEntries) {

                GridLayoutManager layoutManager = new GridLayoutManager(Display_Favourite.this, 1);
                mAdapter = new Favourite_Adapter(Display_Favourite.this, taskEntries);
                recyclerView = findViewById(R.id.RV_Fav);
                recyclerView.setAdapter(mAdapter);
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(layoutManager);
                if(taskEntries.size()==0){
                    textView.setVisibility(View.VISIBLE);
                }else {
                    textView.setVisibility(View.INVISIBLE);
                }
            }
        });
    }
}