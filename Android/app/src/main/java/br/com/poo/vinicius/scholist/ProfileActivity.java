package br.com.poo.vinicius.scholist;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import java.util.Collection;
import java.util.List;

import javax.annotation.Nullable;

import br.com.poo.vinicius.scholist.model.User;

public class ProfileActivity extends AppCompatActivity {


    ImageView imageProfile;
    EditText editUsername;
    EditText typeUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        imageProfile = findViewById(R.id.img_photoProfile);
        editUsername = findViewById(R.id.userNameEdit);
        typeUser = findViewById(R.id.typeUser);
        getUser();


    }


    public void getUser() {
       CollectionReference doc = FirebaseFirestore.getInstance().collection("/users");
       doc.addSnapshotListener(new EventListener<QuerySnapshot>() {
           @Override
           public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
               if(e != null ) {
                   return;
               } else {
                   List<DocumentSnapshot> docs = queryDocumentSnapshots.getDocuments();
                   for (DocumentSnapshot doc: docs) {
                       User user = doc.toObject(User.class);

                        editUsername.setText("Name: " + user.getUsername(), null);
                        typeUser.setText("Type: " + user.getTipo(),null);
                       Picasso.get()
                               .load(user.getProfileUrl())
                               .into(imageProfile);


                       Log.d("Teste", "onEvent" + user.getUsername());


                   }
                   
               }
           }
       });
    }

}
