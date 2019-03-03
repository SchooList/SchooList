package br.com.poo.vinicius.scholist;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import java.security.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;

import br.com.poo.vinicius.scholist.model.Post;
import br.com.poo.vinicius.scholist.model.Turma;
import br.com.poo.vinicius.scholist.model.User;

public class NewPostActivity extends AppCompatActivity {

    ImageView imageAdmin;
    EditText  txtPost;
    String adminUid;
    Turma turma;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_post);
        imageAdmin = findViewById(R.id.imageAdminTurma);
        txtPost = findViewById(R.id.editShareWithUs);

        Intent intent = getIntent();
        turma = (Turma) intent.getParcelableExtra("turma");

        FirebaseFirestore.getInstance().collection("/users").document(turma.getUuidAdmin().toString())
                .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                User user = documentSnapshot.toObject(User.class);
                Picasso.get().load(user.getProfileUrl()).into(imageAdmin);
            }
        });



    }


    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_profile, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId())  {
            case R.id.menu_ok:
                createNewPost();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void createNewPost() {
        String descricao = txtPost.getText().toString();
        String timestamp = new SimpleDateFormat("dd/MM/yyyy").format(new Date(System.currentTimeMillis()));
        Post post = new Post(descricao,timestamp);
        FirebaseFirestore.getInstance().collection("/turmas")
                .document(turma.getUuid()).collection("/posts").add(post).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                Toast.makeText(NewPostActivity.this, "Deu certo cara", Toast.LENGTH_LONG).show();
            }
        });
    }


}
