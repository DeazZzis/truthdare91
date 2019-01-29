package com.example.deathis.myapplication;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;


/**
 * A simple {@link Fragment} subclass.
 */
public class PostFragment extends Fragment {


    public PostFragment() {
        // Required empty public constructor
    }


    private Button btn;
    private TextView textView;
    private View view;
    private EditText editText;
    private int pass;
    private FirebaseAuth mAuth;
    private DatabaseReference myRef;
    private DatabaseReference ref;

    private String team;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_post, container, false);

        textView = view.findViewById(R.id.textView);
        editText = view.findViewById(R.id.editText);
        btn = view.findViewById(R.id.button);

        mAuth = FirebaseAuth.getInstance();
        myRef = FirebaseDatabase.getInstance().getReference();


        ref = myRef.child("teams").child(mAuth.getUid());

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                team = dataSnapshot.getValue(String.class);
            }
            @Override
            public void onCancelled(DatabaseError error) {
            }
        });


        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(editText.getText().toString())) {
                    editText.setError("Введіть код!");
                } else {

                    pass = Integer.parseInt(editText.getText().toString());
                    checkQuestion(team);
                }
            }
        });

        return view;
    }

    public void checkQuestion(String team){

    }


}
