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

import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Nikola on 7.12.2017..
 */

public class PrikazPostavkeFragment extends Fragment {
    @BindView(R.id.progressBarUserSettings)
    ProgressBar pb;
    Button editNameBtn;
    EditText nameEditText;
    EditText userEmail;
    View view;
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    Activity act;
    private boolean editDNameActive = false;

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
        act = getActivity();
        pb = view.findViewById(R.id.progressBarUserSettings);
        //show user email address
        final FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        nameEditText = (EditText) view.findViewById(R.id.user_diplay_name);
        if(currentUser.getDisplayName()!=null){
            nameEditText.setText(currentUser.getDisplayName());
        }
        userEmail = (EditText) view.findViewById(R.id.user_mail_info);
        userEmail.setText(currentUser.getEmail());
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
        //edit user Display Name
        editNameBtn = (Button) view.findViewById(R.id.edit_name_btn);

        editNameBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //nameEditText = (EditText) view.findViewById(R.id.user_diplay_name);
                String ime = nameEditText.getText().toString();
                Log.i("ime:", ime);
                if (!editDNameActive) {
                    nameEditText.setEnabled(true);
                    editNameBtn.setText("Save");
                    editDNameActive = true;
                }else{

                    pb.setVisibility(View.VISIBLE);
                    if(!Objects.equals(ime, "")){
                        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                .setDisplayName(ime).build();
                        firebaseAuth.getCurrentUser().updateProfile(profileUpdates).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                pb.setVisibility(View.INVISIBLE);
                                if (task.isSuccessful()) {
                                    Toast.makeText(act,"Updated!", Toast.LENGTH_SHORT).show();
                                }else{
                                    Toast.makeText(act,"Update failed!", Toast.LENGTH_SHORT).show();
                                    nameEditText.setText(firebaseAuth.getCurrentUser().getDisplayName());
                                }
                            }
                        });
                    }else{
                        Toast.makeText(getActivity(),"Saving failed! Empty field", Toast.LENGTH_SHORT).show();
                        if(currentUser.getDisplayName()!=null)
                        nameEditText.setText(currentUser.getDisplayName());
                    }

                    nameEditText.setEnabled(false);
                    editNameBtn.setText("Edit");
                    editDNameActive = false;
                }

            }
        });
        return view;
    }

}