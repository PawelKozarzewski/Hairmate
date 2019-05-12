package com.example.patryk.hairfire;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class Login extends AppCompatActivity {
    private static final String TAG = "Login";

    EditText loginEditText, passwordEditText;
    Button loginBtn;

    FirebaseAuth mAuth;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private boolean is_client = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        loginEditText = (EditText) findViewById(R.id.login_edit_text);
        passwordEditText = (EditText) findViewById(R.id.passsword_edit_text);

        mAuth = FirebaseAuth.getInstance();


        loginBtn = (Button) findViewById(R.id.login_button);
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });
    }

    private void login(){
        String login = loginEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();

        if(login.isEmpty()){
            loginEditText.setError("Podaj email!");
            return;
        }


        if(password.isEmpty()){
            passwordEditText.setError("Podaj hasło!");
            return;
        }

        mAuth.signInWithEmailAndPassword(login, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){

                    FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
                    String RegisteredUserID = currentUser.getUid();

                    DocumentReference userRef = db.collection("Users").document(RegisteredUserID);
                    userRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.isSuccessful()) {
                                DocumentSnapshot document = task.getResult();
                                if (document.exists()) {
                                    is_client = document.getBoolean("client");

                                    if(is_client == true) {
                                        Intent intent = new Intent(Login.this, MainActivity.class);
                                        startActivity(intent);
                                        finish();
                                    } else{
                                        Intent intent = new Intent(Login.this, OwnerActivity.class);
                                        startActivity(intent);
                                        finish();
                                    }

                                } else {
                                    Log.d(TAG, "No such user");
                                }
                            } else {
                                Log.d(TAG, "get failed with ", task.getException());
                            }
                        }
                    });

                } else{
                    Toast.makeText(Login.this, "Logowanie niepowiodło się!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        if(is_client == true){
            startActivity(new Intent(Login.this, MainActivity.class));
            finish();
        } else{
            startActivity(new Intent(Login.this, OwnerActivity.class));
            finish();
        }


    }
}