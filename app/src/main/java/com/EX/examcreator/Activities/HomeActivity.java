package com.EX.examcreator.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.EX.examcreator.Adapters.HomeAdapter;
import com.EX.examcreator.MainActivity;
import com.EX.examcreator.Models.HomeData;
import com.EX.examcreator.R;
import com.EX.examcreator.Upload_Pdf.Upload_Pdf_Activity;
import com.google.firebase.auth.FirebaseAuth;
import java.util.ArrayList;
import java.util.List;


public class HomeActivity extends AppCompatActivity implements HomeAdapter.rv_onclickCallback {

    private HomeAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        setUpRecycleView();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.logout , menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.logout) {
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(HomeActivity.this , MainActivity.class));
            HomeActivity.this.finish();
        }
        else if (item.getItemId() == R.id.profile) {
            startActivity(new Intent(HomeActivity.this , ProfileActivity.class));

        }
        else if (item.getItemId() == R.id.about) {
            startActivity(new Intent(HomeActivity.this , AboutActivity.class));

        }
            return super.onOptionsItemSelected(item);
    }

    private void setUpRecycleView() {

        RecyclerView rv= findViewById(R.id.rv);
        GridLayoutManager gridLayoutManager=new GridLayoutManager(this,2);
        rv.setLayoutManager(gridLayoutManager);
        adapter=new HomeAdapter(this , this);
        rv.setAdapter(adapter);

        getDataSource();
    }

    private void getDataSource(){

        String DataName[]={"Courses","Add Courses","Create Exam","My Exams","Favourite","Exam Settings"};
        int DataImage[]={R.drawable.book,R.drawable.student,R.drawable.test,R.drawable.ic_share,R.drawable.ic_favorite,R.drawable.ic_setting};
        List<HomeData> homeDataList = new ArrayList<>();

        for (int i = 0; i < DataName.length; i++) {
            homeDataList.add(new HomeData(DataName[i],DataImage[i]));
        }
        adapter.setData(homeDataList);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onClick(int pos , List<HomeData> homeDataList) {

        switch (homeDataList.get(pos).getName()){

            case "Courses": startActivity(new Intent(this , Cources.class));
                break;

            case "Create Exam": startActivity(new Intent(this , CreateExam_ChosenCourse.class));
                break;

            case "Add Courses":
                startActivity(new Intent(this , CoursesControll.class));
                break;

            case "My Exams":startActivity(new Intent(this , Upload_Pdf_Activity.class));
                break;

            case "Favourite": startActivity(new Intent(this , Display_Favourite.class));
                break;

            case "Exam Settings": startActivity(new Intent(this , SettingsActivity.class));
                break;
        }
    }

}
