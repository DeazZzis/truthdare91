package com.example.deathis.myapplication;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Layout;
import android.text.TextUtils;
import android.util.Log;
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

public class EmailPassActivity extends AppCompatActivity implements View.OnClickListener {


    private FirebaseAuth mAuth;
    private EditText editTextLogin, editTextPass;
    private Button btnLogin, btnReg;
    private LinearLayout layoutLoading;
    private View view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email_pass);

        mAuth = FirebaseAuth.getInstance();

        editTextLogin = findViewById(R.id.editTextEmail);
        editTextPass = findViewById(R.id.editTextPass);
        btnLogin = findViewById(R.id.buttonLogin);
        btnReg = findViewById(R.id.buttonReg);
        layoutLoading = findViewById(R.id.layoutLoading);
        view = findViewById(R.id.view2);

        btnLogin.setOnClickListener(this);
        btnReg.setOnClickListener(this);

        if (mAuth.getCurrentUser() != null){
            startActivity(new Intent(EmailPassActivity.this,
                    MapsActivity.class));
            finish();
        }
    }

//    @Override
//    public void onStart() {
//        super.onStart();
//        // Check if user is signed in (non-null) and update UI accordingly.
//        FirebaseUser currentUser = mAuth.getCurrentUser();
//        updateUI(currentUser);
//    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.buttonLogin){
            int i = 0;
            if (TextUtils.isEmpty(editTextLogin.getText())){
                i += 1;
                editTextLogin.setError("Це поле не може бути пустим!");
            }
            if (TextUtils.isEmpty(editTextPass.getText())){
                i += 1;
                editTextPass.setError("Це поле не може бути пустим!");
            }
            if (i == 0){
                Login();
            }
        } else if (v.getId() == R.id.buttonReg){
            Register();
        }
    }

    private void Register(){
    startActivity(new Intent(EmailPassActivity.this, RegistrationActivity.class));
    finish();
    }

    private void Login(){
        layoutLoading.setVisibility(View.VISIBLE);
        view.setVisibility(View.VISIBLE);

        mAuth.signInWithEmailAndPassword(editTextLogin.getText().toString(),
                editTextPass.getText().toString()).addOnCompleteListener(this,
                new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    FirebaseUser user = task.getResult().getUser();
                    Toast.makeText(EmailPassActivity.this,
                            "Login success!", Toast.LENGTH_SHORT);

                    layoutLoading.setVisibility(View.GONE);
                    view.setVisibility(View.GONE);

                    startActivity(new Intent(EmailPassActivity.this,
                            MapsActivity.class));
                    finish();
                 } else {
                    try {
                        throw task.getException();
                    } catch (FirebaseAuthWeakPasswordException e){
                        Toast.makeText(EmailPassActivity.this,
                                "Weak password, try again!", Toast.LENGTH_SHORT);
                        editTextPass.setError("Weak password!");
                        layoutLoading.setVisibility(View.GONE);
                        view.setVisibility(View.GONE);


                    } catch (FirebaseAuthInvalidCredentialsException e){
                        Toast.makeText(EmailPassActivity.this,
                                "Email or Password is wrong, try again!",
                                Toast.LENGTH_SHORT);
                        layoutLoading.setVisibility(View.GONE);
                        view.setVisibility(View.GONE);


                        editTextLogin.setError("Email is already registered, choose another!");
                    } catch (Exception e){

                    }
                }
            }
        });
    }
}
