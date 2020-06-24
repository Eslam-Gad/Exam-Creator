package com.EX.examcreator.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.EX.examcreator.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import dmax.dialog.SpotsDialog;

public class LoginActivity extends AppCompatActivity {

    EditText mail;
    EditText pass;

    CoordinatorLayout root;

    FirebaseAuth mAuth;

     AlertDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Login");
        setSupportActionBar(toolbar);

        mail = (EditText)findViewById(R.id.ET_Email);
        pass = (EditText)findViewById(R.id.ET_Password);

        root = (CoordinatorLayout)findViewById(R.id.root);

        mAuth = FirebaseAuth.getInstance();

        dialog = new SpotsDialog(LoginActivity.this);

    }

    public void mLogin(View view) {

        if(!mail.getText().toString().isEmpty()&&!pass.getText().toString().isEmpty()) {
            dialog.show();
            mAuth.signInWithEmailAndPassword(mail.getText().toString(), pass.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {

                    if (task.isSuccessful()) {

                        FirebaseUser firebaseUser = mAuth.getCurrentUser();

                        if(firebaseUser.isEmailVerified()) {

                            Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);

                        }else {
                            firebaseUser.sendEmailVerification();
                            Toast.makeText(LoginActivity.this, "please Verify your email", Toast.LENGTH_SHORT).show();
                            mAuth.signOut();
                            dialog.dismiss();
                        }

                    } else {
                        Snackbar.make(root, task.getException().getMessage(), Snackbar.LENGTH_LONG).show();
                        dialog.hide();
                    }
                }
            });
        }
        else {
            Snackbar.make(root, "All fields required !!", Snackbar.LENGTH_LONG).show();
            dialog.hide();
        }
    }


    public void forgetPass(View view) {

        if (!mail.getText().toString().isEmpty()) {
            dialog.show();

            mAuth.sendPasswordResetEmail(mail.getText().toString())
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Snackbar.make(root, "We have sent you email with instructions to reset your password!", Snackbar.LENGTH_LONG).show();
                                dialog.hide();
                            }else {
                                Snackbar.make(root, "Failed to send reset email!", Snackbar.LENGTH_LONG).show();
                                dialog.hide();
                            }
                        }
                    });
        }else {
            Snackbar.make(root, "pls: type e-mail address !!", Snackbar.LENGTH_LONG).show();
            dialog.hide();
        }
    }
}
