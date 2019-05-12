package com.example.patryk.hairfire;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class Register extends AppCompatActivity {

    private FirebaseAuth mAuth;
    EditText loginEditText, passwordEditText, surnameEditText, nameEditText;
    Button loginbtn;
    RadioGroup typeAcc;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    boolean type=true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        loginEditText = (EditText) findViewById(R.id.login_register);
        passwordEditText = (EditText) findViewById(R.id.password_register);
        nameEditText = (EditText) findViewById(R.id.name_register);
        surnameEditText = (EditText) findViewById(R.id.surname_register);
        typeAcc = (RadioGroup) findViewById(R.id.radio_group);

        mAuth = FirebaseAuth.getInstance();

        loginbtn = (Button) findViewById(R.id.reg_btn);
        loginbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                register();
            }
        });


        typeAcc.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if(checkedId==R.id.client_radio){
                    type=true;
                } else{
                    type=false;
                }
            }
        });
    }

    private void register(){
        String login = loginEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();
        final String name = nameEditText.getText().toString().trim();
        final String surname = surnameEditText.getText().toString().trim();

        if(login.isEmpty()){
            loginEditText.setError("Email jest wymagany!");
            return;
        }

        if(!Patterns.EMAIL_ADDRESS.matcher(login).matches()){
            loginEditText.setError("Wpisz prawidłowy email!");
            return;
        }

        if(password.isEmpty()){
            passwordEditText.setError("Hasło jest wymagane!");
            return;
        }

        if(password.length()<6){
            passwordEditText.setError("Hasło musi mieć minimum 6 znaków!");
            return;
        }

        if(name.isEmpty()){
            nameEditText.setError("Podaj imie!");
            return;
        }

        if(surname.isEmpty()){
            surnameEditText.setError("Podaj nazwisko!");
            return;
        }

        mAuth.createUserWithEmailAndPassword(login, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    User user = new User(name, surname, type);
                    db.collection("Users").document(mAuth.getUid()).set(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            Toast.makeText(Register.this, "Konto utworzone pomyślnie!", Toast.LENGTH_LONG).show();
                            startActivity(new Intent(Register.this, UserProfile.class));
                            finish();
                        }
                    });

                }
            }
        });
    }



    @Override
    public void onBackPressed() {
        startActivity(new Intent(Register.this, MainActivity.class));
        finish();
    }
}
