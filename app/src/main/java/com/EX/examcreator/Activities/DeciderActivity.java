package com.EX.examcreator.Activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.EX.examcreator.MainActivity;


public class DeciderActivity extends AppCompatActivity {
    Intent intent;
    public  static String MYPREFERENCES="androidx.appcompat.app.SHARED_PREF";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // setContentView(R.layout.activity_decider);
        prepareProperties();
        finish();
    }

    private void prepareProperties() {
        SharedPreferences Shared=getSharedPreferences(MYPREFERENCES, Context.MODE_PRIVATE);
        boolean seen=Shared.getBoolean("seen",false);
        // Log.d("seen",String.valueOf(seen));
        if (seen){
            intent =new Intent(this, MainActivity.class);
        } else {

        intent =new Intent(this,OnBoardingScreen.class);
         }
        startActivity(intent);


    }



}
