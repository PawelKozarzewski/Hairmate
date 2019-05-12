package com.example.patryk.hairfire;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class OpinionTab extends Fragment {

    private static final String TAG = "OpinionsTab";

    public static EditText title_opinion, content_opinion, author_opinion;
    Button add_opinion;
    TextView error_empty, error_auth;
    ListView opinionList;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    String salon_id=SalonList.salon_id;
    OpinionAdapter adapter;
    String name, surname;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.tab3_fragment,container,false);
        error_empty = (TextView) view.findViewById(R.id.error_empty);
        error_auth = (TextView) view.findViewById(R.id.error_auth);

        title_opinion = (EditText) view.findViewById(R.id.title_opinion);
        content_opinion = (EditText) view.findViewById(R.id.edit_text_opinion);
        add_opinion = (Button) view.findViewById(R.id.add_opinion_button);


        if(user==null){
            add_opinion.setVisibility(View.GONE);
            error_auth.setVisibility(View.VISIBLE);
        }else {
            getAuthor();
            add_opinion.setVisibility(View.VISIBLE);
            error_auth.setVisibility(View.GONE);
        }


        add_opinion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WriteOpinion();
                error_empty.setVisibility(View.GONE);
                AlertDialog alertDialog = new AlertDialog.Builder(getContext()).create();
                alertDialog.setTitle("Informacja");
                alertDialog.setMessage("Dziękujemy za dodanie opinii!");
                alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "Zamknij", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        title_opinion.setText("");
                        content_opinion.setText("");
                    }
                });
                alertDialog.show();
                ReadOpinions();
            }

        });


        ReadOpinions();

        return view;
    }


    public void WriteOpinion(){
        String title = title_opinion.getText().toString();
        String content = content_opinion.getText().toString();
        String author = name + " " + surname;
        DateFormat df = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss");
        Opinion newOpinion = new Opinion(salon_id,title,content,author, df.format(Calendar.getInstance().getTime()));
        db.collection("Opinions").add(newOpinion);
    }


    public void ReadOpinions(){
        db.collection("Opinions")
                .whereEqualTo("salon_id", salon_id)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        List<Opinion> opinions = new ArrayList<>();
                        if(task.isSuccessful()){
                            for(QueryDocumentSnapshot document : task.getResult()){
                                Opinion opinion = document.toObject(Opinion.class);
                                opinions.add(opinion);
                            }
                            if(opinions.isEmpty()){
                                error_empty.setVisibility(View.VISIBLE);
                                error_empty.setText("Brak opinii o tym salonie!");
                            }else {
                                opinionList = (ListView) getView().findViewById(R.id.opinion_listview);
                                adapter = new OpinionAdapter(getContext(), opinions);
                                opinionList.setAdapter(adapter);
                                adapter.notifyDataSetChanged();
                            }
                        }
                    }

                });
    }

    public void getAuthor(){
        db.collection("Users")
                .document(user.getUid())
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        name = documentSnapshot.getString("name");
                        surname = documentSnapshot.getString("surname");
                    }
                });
        }
}
