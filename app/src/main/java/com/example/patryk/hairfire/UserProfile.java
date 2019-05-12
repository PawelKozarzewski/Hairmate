package com.example.patryk.hairfire;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class UserProfile extends AppCompatActivity {

    TextView user_email, user_name, user_surname, user_personal;
    Button logoutBtn,visitsBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        final String email = user.getEmail();
        final String uid = user.getUid();

        user_email = (TextView) findViewById(R.id.user_email);
        user_name = (TextView) findViewById(R.id.user_name);
        user_surname = (TextView) findViewById(R.id.user_surname);
        user_personal = (TextView) findViewById(R.id.user_personal);


        logoutBtn = (Button) findViewById(R.id.logout_button);
        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserProfile.this, MainActivity.class);
                startActivity(intent);
                FirebaseAuth.getInstance().signOut();
                finish();
            }
        });


        visitsBtn = (Button) findViewById(R.id.visits_button);
        visitsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserProfile.this, VisitsList.class);
                startActivity(intent);
            }
        });



        db.collection("Users")
                .document(uid)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if(task.isSuccessful()){
                            user_personal.setText(task.getResult().getString("name") + " " + task.getResult().getString("surname"));
                            user_email.setText("Email: " + email);
                            user_name.setText("Imię: " + task.getResult().getString("name"));
                            user_surname.setText("Nazwisko: " + task.getResult().getString("surname"));
                        }
                    }
                });
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(UserProfile.this, MainActivity.class));
        finish();
    }
}
