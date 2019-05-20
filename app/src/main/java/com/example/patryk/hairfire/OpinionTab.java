package com.example.patryk.hairfire;

import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaMuxer;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
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
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.net.URI;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class OpinionTab extends Fragment {

    private static final String TAG = "OpinionsTab";
    private static final int PICK_IMAGE = 100;
    public static EditText title_opinion, content_opinion, author_opinion;
    Button add_opinion, addPhoto;
    TextView error_empty, error_auth, info;
    ListView opinionList;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    String salon_id=SalonList.salon_id;
    OpinionAdapter adapter;
    String name, surname;
    ImageView opinionPhoto;
    private StorageReference mStorageRef;
    private FirebaseAuth auth;
    String randomKey;
    String downloadUrl= "null";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.tab3_fragment,container,false);

        auth = FirebaseAuth.getInstance();

        mStorageRef = FirebaseStorage.getInstance().getReference();

        error_empty = (TextView) view.findViewById(R.id.error_empty);
        error_auth = (TextView) view.findViewById(R.id.error_auth);
        info = (TextView) view.findViewById(R.id.photo_info);

        title_opinion = (EditText) view.findViewById(R.id.title_opinion);
        content_opinion = (EditText) view.findViewById(R.id.edit_text_opinion);
        add_opinion = (Button) view.findViewById(R.id.add_opinion_button);
        opinionPhoto = (ImageView) view.findViewById(R.id.opinion_photo);
        addPhoto = (Button) view.findViewById(R.id.add_image_button);

        randomKey = String.valueOf((Math. round(Math. random()*10000000)));

        if(user==null){
            add_opinion.setVisibility(View.GONE);
            addPhoto.setVisibility(View.GONE);
            error_auth.setVisibility(View.VISIBLE);
        }else {
            getAuthor();
            add_opinion.setVisibility(View.VISIBLE);
            addPhoto.setVisibility(View.VISIBLE);
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

        addPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery();
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
        Opinion newOpinion = new Opinion(salon_id, downloadUrl, title,content,author, df.format(Calendar.getInstance().getTime()));
        db.collection("Opinions").add(newOpinion);
        downloadUrl= "null";
    }


    public void ReadOpinions(){
        mStorageRef = FirebaseStorage.getInstance().getReference("images");
        db.collection("Opinions")
                .whereEqualTo("salon_id", salon_id)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        List<Opinion> opinions = new ArrayList<>();
                        if(task.isSuccessful()){
                            for(QueryDocumentSnapshot document : task.getResult()){
                                String salon_id = document.getString("salon_id");
                                String photo = document.getString("photo");
                                String title = document.getString("title");
                                String content = document.getString("content");
                                String author = document.getString("author");
                                String date = document.getString("date");
                                opinions.add(new Opinion(salon_id, photo, title, content, author, date));
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

    public void getAuthor() {
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

    private void openGallery(){
        Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        startActivityForResult(gallery, PICK_IMAGE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Uri imageUri = data.getData();

        final StorageReference storageReference = mStorageRef.child(randomKey + ".jpg");
        UploadTask uploadTask = storageReference.putFile(imageUri);

        Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                if (!task.isSuccessful()) {
                    throw task.getException();
                }

                // Continue with the task to get the download URL
                return storageReference.getDownloadUrl();
            }
        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(getContext(), "Zdjęcie wgrane poprawnie!", Toast.LENGTH_LONG).show();
                    Uri downloadUri = task.getResult();
                    downloadUrl = downloadUri.toString();
                } else {
                    Toast.makeText(getContext(), "Błąd podczas wgrywania zdjęcia!", Toast.LENGTH_LONG).show();
                }
            }
        });
    }


}

