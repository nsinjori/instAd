package com.foi.air1712.instad.accountManagement;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.foi.air1712.instad.MainActivity;
import com.foi.air1712.instad.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SignUpActivity extends AppCompatActivity {
    @BindView(R.id.email)
    TextView emailTextView;
    @BindView(R.id.password)
    TextView passTextView;
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        ButterKnife.bind(this);
        mAuth = FirebaseAuth.getInstance();
            //TODO: check if user is already logged in
    }
    @OnClick(R.id.back_sign_in_button)
    public void backButtonClicked(View view){
        Intent intent = new Intent(SignUpActivity.this,LoginActivity.class);
        finish();
    }
    @OnClick(R.id.sign_up_button)
    public void signUpButtonClicked(View view){
        String email = emailTextView.getText().toString().trim();
        String pass = passTextView.getText().toString().trim();
        if(TextUtils.isEmpty(email)||TextUtils.isEmpty(pass)){
            //TODO: show message about empty fields
            return;
        }
        mAuth.createUserWithEmailAndPassword(email,pass).addOnCompleteListener(SignUpActivity.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(!task.isSuccessful()){
                    Toast.makeText(SignUpActivity.this,task.getException().getMessage(),Toast.LENGTH_LONG).show();
                }else{
                    Intent intent = new Intent(SignUpActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        });
    }
}
