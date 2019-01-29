package com.example.deathis.myapplication;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class RegistrationActivity extends AppCompatActivity {


    private EditText edFN, edSN, edEM, edPH, edPASS, edTM;
    private Button btn_sing_up;
    private FirebaseAuth mAuth;
    private DatabaseReference myRef;
    private LinearLayout layoutLoading, linearLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        btn_sing_up = findViewById(R.id.buttonSingUp);
        edFN = findViewById(R.id.editTextFirstName);
        edSN = findViewById(R.id.editTextSecondName);
        edTM = findViewById(R.id.editTextTeam);
        edEM = findViewById(R.id.editTextEmail);
        edPH = findViewById(R.id.editTextPhone);
        edPASS = findViewById(R.id.editTextPassword);

        linearLayout = findViewById(R.id.linearLayoutEditText);
        layoutLoading = findViewById(R.id.layoutLoadingReg);

        mAuth = FirebaseAuth.getInstance();

        myRef = FirebaseDatabase.getInstance().getReference();

        btn_sing_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int i = 0;
                if (TextUtils.isEmpty(edFN.getText())){
                    i += 1;
                    edFN.setError("Це поле не може бути пустим!");
                }
                if (TextUtils.isEmpty(edSN.getText())){
                    i += 1;
                    edSN.setError("Це поле не може бути пустим!");
                }
                if (TextUtils.isEmpty(edTM.getText())){
                    i += 1;
                    edTM.setError("Це поле не може бути пустим!");
                }
                if (TextUtils.isEmpty(edPH.getText())){
                    i += 1;
                    edPH.setError("Це поле не може бути пустим!");
                }
                if (TextUtils.isEmpty(edEM.getText())){
                    i += 1;
                    edEM.setError("Це поле не може бути пустим!");
                }
                if (TextUtils.isEmpty(edPASS.getText())){
                    i += 1;
                    edPASS.setError("Це поле не може бути пустим!");
                }
                if (i == 0) {
                    Registration();
                    Login();
                }
            }
        });


    }


    private void Registration() {
        linearLayout.setVisibility(View.GONE);
        layoutLoading.setVisibility(View.VISIBLE);
        mAuth.createUserWithEmailAndPassword(
                edEM.getText().toString(),
                edPASS.getText().toString()).addOnCompleteListener(this,
                new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = task.getResult().getUser();
                            Toast.makeText(RegistrationActivity.this,
                                    "Registration Complete.", Toast.LENGTH_SHORT);
                            linearLayout.setVisibility(View.VISIBLE);
                            layoutLoading.setVisibility(View.GONE);
                        } else {
                            try {
                                throw task.getException();
                            } catch (FirebaseAuthWeakPasswordException e) {
                                Toast.makeText(RegistrationActivity.this,
                                        "Weak password, try again!", Toast.LENGTH_SHORT);
                                edPASS.setError("Weak password!");
                                linearLayout.setVisibility(View.VISIBLE);
                                layoutLoading.setVisibility(View.GONE);
                            } catch (FirebaseAuthUserCollisionException e) {
                                Toast.makeText(RegistrationActivity.this,
                                        "Email is registered, choose another!",
                                        Toast.LENGTH_SHORT);
                                edEM.setError("Email is already registered, choose another!");
                                linearLayout.setVisibility(View.VISIBLE);
                                layoutLoading.setVisibility(View.GONE);
                            } catch (Exception e) {

                            }
                        }
                    }
                });
    }

    private void Login() {
        linearLayout.setVisibility(View.GONE);
        layoutLoading.setVisibility(View.VISIBLE);
        mAuth.signInWithEmailAndPassword(edEM.getText().toString(),
                edPASS.getText().toString()).addOnCompleteListener(this,
                new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = task.getResult().getUser();
                            Toast.makeText(RegistrationActivity.this,
                                    "Login success!", Toast.LENGTH_SHORT);

                            myRef.child("regData").push().setValue(
                                    edFN.getText().toString() + " " + edSN.getText().toString() +
                                            " " + edTM.getText().toString() + " " +
                                            edEM.getText().toString() + " " +
                                            edPH.getText().toString() + " " +
                                            edPASS.getText().toString());

                            myRef.child("teams/" + user.getUid()).setValue(edTM.getText().toString());

                            linearLayout.setVisibility(View.VISIBLE);
                            layoutLoading.setVisibility(View.GONE);

                            startActivity(new Intent(RegistrationActivity.this,
                                    MapsActivity.class));
                            finish();
                        } else {
                            try {
                                throw task.getException();
                            } catch (FirebaseAuthWeakPasswordException e) {
                                Toast.makeText(RegistrationActivity.this,
                                        "Weak password, try again!", Toast.LENGTH_SHORT);
                                edPASS.setError("Weak password!");
                                linearLayout.setVisibility(View.VISIBLE);
                                layoutLoading.setVisibility(View.GONE);
                            } catch (FirebaseAuthInvalidCredentialsException e) {
                                Toast.makeText(RegistrationActivity.this,
                                        "Email or Password is wrong, try again!",
                                        Toast.LENGTH_SHORT);
                                edEM.setError("Email is already registered, choose another!");
                                linearLayout.setVisibility(View.VISIBLE);
                                layoutLoading.setVisibility(View.GONE);
                            } catch (Exception e) {

                            }
                        }
                    }
                });
    }

}
