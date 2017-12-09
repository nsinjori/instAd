package com.foi.air1712.instad.accountManagement;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.foi.air1712.instad.MainActivity;
import com.foi.air1712.instad.PrikazSvihActivity;
import com.foi.air1712.instad.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginActivity extends AppCompatActivity {
    @BindView(R.id.email_login)
    TextView emailLTextView;
    @BindView(R.id.password_login) TextView passLTextView;
    private FirebaseAuth mAuth;
    @BindView(R.id.progressBarLogin)
    ProgressBar pb;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        mAuth = FirebaseAuth.getInstance();
        if(FirebaseAuth.getInstance().getCurrentUser()!=null){
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }
    }
    @OnClick(R.id.skip_button)
    public void skipButtonClicked(View view){
        startActivity(new Intent(LoginActivity.this, PrikazSvihActivity.class));
    }
    @OnClick(R.id.to_sign_up_button)
    public void createAccButtonClicked(View view){
        Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
        startActivity(intent);
        //finish();
    }
    @OnClick(R.id.sign_in_button)
    public void signInButtonClicked(View view){
        String email = emailLTextView.getText().toString().trim();
        String pass = passLTextView.getText().toString().trim();
        if(TextUtils.isEmpty(email)||TextUtils.isEmpty(pass)){
            if(TextUtils.isEmpty(email)){
                Toast.makeText(LoginActivity.this,"Email field is empty!",Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(LoginActivity.this,"Password field is empty!",Toast.LENGTH_SHORT).show();
            }
            return;
        }
        pb.setVisibility(View.VISIBLE);

        mAuth.signInWithEmailAndPassword(email,pass).addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(!task.isSuccessful()){
                    pb.setVisibility(View.GONE);
                    Toast.makeText(LoginActivity.this, task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                }else{
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        });
    }
}
