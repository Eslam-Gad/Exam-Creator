package com.EX.examcreator.Activities;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NavUtils;
import androidx.preference.PreferenceManager;

import com.EX.examcreator.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import dmax.dialog.SpotsDialog;

public class RegisterationActivity extends AppCompatActivity {

    private EditText fName;
    private EditText e_mail;
    private EditText pass;
    private EditText cPass;
    LinearLayout root;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registeration);

        fName  = (EditText)findViewById(R.id.RG_Name);
        e_mail = (EditText)findViewById(R.id.RG_Email);
        pass   = (EditText)findViewById(R.id.RG_Password);
        cPass  = (EditText)findViewById(R.id.RG_Confirm);
        root   = (LinearLayout)findViewById(R.id.root);
        mAuth = FirebaseAuth.getInstance();

        /* enable back buutun */
        ActionBar actionBar = this.getSupportActionBar();
        if(actionBar!=null){
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home)
        {
            NavUtils.navigateUpFromSameTask(this);
        }
        return super.onOptionsItemSelected(item);
    }

    /* firebase registration */

    public void registerToFirebaseAuth(View view) {

        final AlertDialog dialog = new SpotsDialog(RegisterationActivity.this);
        dialog.setTitle("Create new User");

        if(!pass.getText().toString().isEmpty()&&!e_mail.getText().toString().isEmpty()
        &&!fName.getText().toString().isEmpty()&&!cPass.getText().toString().isEmpty()){

            if(pass.getText().toString().equals(cPass.getText().toString())){
                dialog.show();

                mAuth.createUserWithEmailAndPassword(e_mail.getText().toString(), pass.getText().toString()).addOnCompleteListener(
                    new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {

                            if (task.isSuccessful()) {

                                FirebaseUser firebaseUser = mAuth.getCurrentUser();

                                firebaseUser.sendEmailVerification();

                                /*  save name to shared pref */
                                SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit();
                                editor.putString("signature_key" , "Dr: "+fName.getText().toString());
                                editor.commit();

                                dialog.dismiss();
                                startActivity(new Intent(RegisterationActivity.this,LoginActivity.class));

                                Toast.makeText(RegisterationActivity.this, "Check email to verification", Toast.LENGTH_SHORT).show();

                            } else {
                                Snackbar.make(root, task.getException().getMessage(), Snackbar.LENGTH_LONG).show();
                                dialog.hide();
                            }
                        }
                    });}
            else {
                Snackbar.make(root,"password and confirm password not equal", Snackbar.LENGTH_LONG).show();
            }

        } else {
            Snackbar.make(root,"All fields required !!", Snackbar.LENGTH_LONG).show();
        }
    }
}