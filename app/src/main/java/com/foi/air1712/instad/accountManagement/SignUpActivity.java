package com.foi.air1712.instad.accountManagement;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.foi.air1712.instad.R;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class SignUpActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        ButterKnife.bind(this);
    }
    @OnClick(R.id.back_sign_in_button)
    public void backButtonClicked(View view){
        Intent intent = new Intent(SignUpActivity.this,LoginActivity.class);
        finish();
    }
}
