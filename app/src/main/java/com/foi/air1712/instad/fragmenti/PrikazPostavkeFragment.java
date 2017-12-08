package com.foi.air1712.instad.fragmenti;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.foi.air1712.instad.MainActivity;
import com.foi.air1712.instad.R;
import com.foi.air1712.instad.accountManagement.LoginActivity;
import com.google.android.gms.internal.zzaap;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Nikola on 7.12.2017..
 */

public class PrikazPostavkeFragment extends Fragment {
    View view;
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    public static PrikazPostavkeFragment newInstance() {
        PrikazPostavkeFragment fragment = new PrikazPostavkeFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_postavke, container, false);
        //user logout
        Button button = (Button) view.findViewById(R.id.logout_button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                firebaseAuth.signOut();
                if(firebaseAuth.getCurrentUser()==null){
                    Toast.makeText(getActivity(), "Odlogiran", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getActivity(), LoginActivity.class);
                    startActivity(intent);
                    getActivity().finish();
                }else{
                    Toast.makeText(getActivity(), "Nije odlogiran", Toast.LENGTH_SHORT).show();
                }

            }
        });
        return view;
    }
}
