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
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
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
    private EditText editOldPassword;
    private EditText editNewPassword;
    private EditText editConfirmPass;
    private Button changePassBtn;
    private boolean editPasswordActive = false;
    private Button editEmailBtn;
    private boolean editEmailActive = false;

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
                        pb.setVisibility(View.INVISIBLE);
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
        //edit user email address
        editEmailBtn = (Button) view.findViewById(R.id.edit_mail_btn);
        editEmailBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = userEmail.getText().toString();
                if (!editEmailActive) {
                    userEmail.setEnabled(true);
                    editEmailBtn.setText("Save");
                    editEmailActive = true;
                }else{
                    pb.setVisibility(View.VISIBLE);
                    if(!Objects.equals(email, "")){
                        currentUser.updateEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                pb.setVisibility(View.INVISIBLE);
                                if(!task.isSuccessful()){
                                    Toast.makeText(getActivity(),"Saving failed! "+task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                }else{
                                    Toast.makeText(getActivity(),"User email updated!", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }else{
                        pb.setVisibility(View.INVISIBLE);
                        Toast.makeText(getActivity(),"Saving failed! Empty field!", Toast.LENGTH_SHORT).show();
                        userEmail.setText(currentUser.getEmail());
                    }

                    userEmail.setEnabled(false);
                    editEmailBtn.setText("Edit");
                    editEmailActive = false;
                }

            }
        });
        //change password
        editOldPassword = (EditText) view.findViewById(R.id.old_pass);
        editNewPassword = (EditText) view.findViewById(R.id.new_pass);
        editConfirmPass = (EditText) view.findViewById(R.id.confirm_pass);
        changePassBtn = (Button) view.findViewById(R.id.change_pass_btn);
        changePassBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String oldPass = editOldPassword.getText().toString();
                final String newPass = editNewPassword.getText().toString();
                String confPass = editConfirmPass.getText().toString();
                if (!editPasswordActive) {
                    editOldPassword.setEnabled(true);
                    editNewPassword.setEnabled(true);
                    editConfirmPass.setEnabled(true);
                    changePassBtn.setText("Save new password");
                    editPasswordActive = true;
                }else{
                    pb.setVisibility(View.VISIBLE);
                    if(!Objects.equals(oldPass, "")&&!Objects.equals(newPass,"")&&!Objects.equals(confPass,"")){
                        if(Objects.equals(newPass,confPass)){
                            AuthCredential authCredential = EmailAuthProvider.getCredential(currentUser.getEmail(),oldPass);
                            currentUser.reauthenticate(authCredential).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()){
                                        currentUser.updatePassword(newPass).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                pb.setVisibility(View.INVISIBLE);
                                                if(task.isSuccessful()){
                                                    Toast.makeText(getActivity(),"New password is saved!", Toast.LENGTH_SHORT).show();
                                                }else{
                                                    //nije uspjela zamjena lozinke
                                                    Toast.makeText(getActivity(),"Saving failed! " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });
                                    }else{
                                        //nie dobra stara sifra
                                        pb.setVisibility(View.INVISIBLE);
                                        Toast.makeText(getActivity(),"Reauthentication failed! Wrong current pass!", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        }else {
                            //pass i conf se ne poklapaju
                            pb.setVisibility(View.INVISIBLE);
                            Toast.makeText(getActivity(),"Saving failed! New password and confirm password are not equal!", Toast.LENGTH_SHORT).show();
                        }
                    }else{
                        pb.setVisibility(View.INVISIBLE);
                        Toast.makeText(getActivity(),"Saving failed! Empty fields!", Toast.LENGTH_SHORT).show();
                    }
                    editOldPassword.setText("");
                    editNewPassword.setText("");
                    editConfirmPass.setText("");
                    editOldPassword.setEnabled(false);
                    editNewPassword.setEnabled(false);
                    editConfirmPass.setEnabled(false);
                    changePassBtn.setText("Change password");
                    editPasswordActive = false;
                }

            }
        });
        return view;
    }

}
