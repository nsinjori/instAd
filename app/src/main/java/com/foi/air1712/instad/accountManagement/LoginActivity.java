package com.foi.air1712.instad.accountManagement;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.foi.air1712.instad.PrikazSvihActivity;
import com.foi.air1712.instad.R;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
    }
    @OnClick(R.id.skip_button)
    public void skipButtonClicked(View view){
        startActivity(new Intent(LoginActivity.this, PrikazSvihActivity.class));
    }
}
