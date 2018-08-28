package com.foi.air1712.instad.fragmenti;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.foi.air1712.database.Dogadaji;
import com.foi.air1712.instad.R;
import com.foi.air1712.instad.accountManagement.LoginActivity;
import com.foi.air1712.instad.notifikacije.GeoFenceLocationServis;
import com.foi.air1712.instad.notifikacije.IModulNotifikacija;
import com.foi.air1712.instad.notifikacije.ModulNotifikacija;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingClient;
import com.google.android.gms.location.GeofencingRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;

/**
 * Created by Nikola on 7.12.2017..
 */

public class PrikazPostavkeFragment extends Fragment {
    private static final float GEOFENCE_RADIUS = 25;
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
    private Button notificiranje;
    private boolean startaniServis;
    private GeofencingClient geofencingClient;
    private IModulNotifikacija modulNotifikacija;

    public static PrikazPostavkeFragment newInstance() {
        PrikazPostavkeFragment fragment = new PrikazPostavkeFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        modulNotifikacija =  new ModulNotifikacija();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        startaniServis=false;
        view = inflater.inflate(R.layout.fragment_postavke, container, false);
        act = getActivity();
        pb = view.findViewById(R.id.progressBarUserSettings);
        //show user email address
        geofencingClient = LocationServices.getGeofencingClient(getActivity());
        postaviGeofence();
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
                    //Toast.makeText(getActivity(), "Odlogiran", Toast.LENGTH_SHORT).show();
                    //getContext().stopService(new Intent(getContext(),NoviDogadajServis.class));

                    //<Novi kod>
                    modulNotifikacija.stop(getContext());
                    //</Novi kod>

                    Intent intent = new Intent(getActivity(), LoginActivity.class);
                    startActivity(intent);
                    getActivity().finish();
                }else{
                    Toast.makeText(getActivity(), "Korisnik je i dalje logiran!", Toast.LENGTH_SHORT).show();
                }

            }
        });
        notificiranje = (Button) view.findViewById(R.id.novi_ev_notify);
        provjeraServisa();
        notificiranje.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(startaniServis){
                    //getContext().stopService(new Intent(getContext(),NoviDogadajServis.class));

                    //<Novi kod>
                    modulNotifikacija.stop(getContext());
                    //</Novi kod>

                    //Toast.makeText(getActivity(), "Ugašeni servis", Toast.LENGTH_SHORT).show();
                    provjeraServisa();
                }else{
                    //getContext().startService(new Intent(getContext(),NoviDogadajServis.class));

                    //<Novi kod>
                    modulNotifikacija.start(getContext());
                    //</Novi kod>

                    //Toast.makeText(getActivity(), "Pokrenuti servis", Toast.LENGTH_SHORT).show();
                    provjeraServisa();
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

    private void postaviGeofence() {
        if(isLocationAccessPermitted()){
            Log.i("GEOFENCE", "falta permisiona");
            ActivityCompat.requestPermissions(act, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        }else{
            removeLocationAlert();
            Log.i("GEOFENCE", "dodavanje");
            //addLocationAlert(46.30764,16.33857,"Foijo");
            List<Dogadaji> dogadaji = Dogadaji.getAll();
            for (Dogadaji dogadaj:dogadaji){
                String dtStart = dogadaj.getDatum_kraj();
                String dtPocetak = dogadaj.getDatum_pocetka();
                Date datumPocetka = new Date();
                Date date = new Date();
                Calendar c = Calendar.getInstance();
                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");// HH:mm:ss");
                //String reg_date = df.format(c.getTime());
                c.add(Calendar.DATE, 2);  // number of days to add
                String end_date = df.format(c.getTime());
                Date preksutra = new Date();
                try {
                    date = new SimpleDateFormat("yyyy-MM-dd").parse(dtStart);
                    datumPocetka = new SimpleDateFormat("yyyy-MM-dd").parse(dtPocetak);
                    preksutra = new SimpleDateFormat("yyyy-MM-dd").parse(end_date);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                if(!date.before(new Date())&&datumPocetka.before(preksutra)){
                    addLocationAlert(Double.parseDouble(dogadaj.getLatitude()),Double.parseDouble(dogadaj.getLongitude()),dogadaj.getHash());
                }
            }
        }
    }

    private void provjeraServisa() {
        //if(isMyServiceRunning(NoviDogadajServis.class)){
        //    startaniServis = true;
        //    notificiranje.setText("Isključi notifikacije!");
        //}else{
        //   startaniServis = false;
        //    notificiranje.setText("Uključi notifikacije!");
        //}

        //<Novi kod>
        if(modulNotifikacija.getIsRunning(act)){
            startaniServis = true;
            notificiranje.setText("Isključi notifikacije!");
        }else{
            startaniServis = false;
            notificiranje.setText("Uključi notifikacije!");
        }
        //</Novi kod>
    }

    //vise nije potrebno
    //private boolean isMyServiceRunning(Class<?> serviceClass) {
    //    ActivityManager manager = (ActivityManager) act.getSystemService(Context.ACTIVITY_SERVICE);
    //    for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
    //        if (serviceClass.getName().equals(service.service.getClassName())) {
    //            return true;
    //        }
    //    }
    //    return false;
    //}
    @SuppressLint("MissingPermission")
    public void addLocationAlert(double lat, double lng, final String key){
        //String key = ""+lat+"-"+lng;
        Geofence geofence = getGeofence(lat, lng, key);
        geofencingClient.addGeofences(getGeofencingRequest(geofence),
                getGeofencePendingIntent())
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            //Toast.makeText(act, "Location alter has been added" + key, Toast.LENGTH_SHORT).show();
                        }else{
                            //Toast.makeText(act, "Location alter could not be added", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
    private void removeLocationAlert(){
        geofencingClient.removeGeofences(getGeofencePendingIntent())
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            //Toast.makeText(act, "Location alters have been removed", Toast.LENGTH_SHORT).show();
                        }else{
                            // Toast.makeText(act, "Location alters could not be removed", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
    private PendingIntent getGeofencePendingIntent() {
        Intent intent = new Intent(act, GeoFenceLocationServis.class);
        return PendingIntent.getService(act, 0, intent,
                PendingIntent.FLAG_UPDATE_CURRENT);
    }
    private GeofencingRequest getGeofencingRequest(Geofence geofence) {
        GeofencingRequest.Builder builder = new GeofencingRequest.Builder();
        builder.setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_DWELL);
        builder.addGeofence(geofence);
        return builder.build();
    }

    private Geofence getGeofence(double lat, double lang, String key) {
        return new Geofence.Builder()
                .setRequestId(key)
                .setCircularRegion(lat, lang, GEOFENCE_RADIUS)
                .setExpirationDuration(Geofence.NEVER_EXPIRE)
                .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER |
                        Geofence.GEOFENCE_TRANSITION_DWELL)
                .setLoiteringDelay(1000)
                .build();
    }
    private boolean isLocationAccessPermitted(){
        if (ContextCompat.checkSelfPermission(act,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            return true;
        }else{
            return false;
        }
    }
}
