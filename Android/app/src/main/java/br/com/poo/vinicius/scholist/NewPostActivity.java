package br.com.poo.vinicius.scholist;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.security.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;

import br.com.poo.vinicius.scholist.model.Post;
import br.com.poo.vinicius.scholist.model.Turma;
import br.com.poo.vinicius.scholist.model.User;

public class NewPostActivity extends AppCompatActivity {
    Button addFile;
    ImageView imageAdmin;
    EditText  txtPost;
    String adminUid;
    Turma turma;
    Uri mSelectedUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_post);
        imageAdmin = findViewById(R.id.imageAdminTurma);
        addFile = findViewById(R.id.btn_addFile);
        txtPost = findViewById(R.id.editShareWithUs);

        addFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectFile();
            }
        });

        Intent intent = getIntent();
        turma = (Turma) intent.getParcelableExtra("turma");

        adminUid = turma.getUuidAdmin().toString();
        FirebaseFirestore.getInstance().collection("/users").document(adminUid)
                .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                User user = documentSnapshot.toObject(User.class);
                Picasso.get().load(user.getProfileUrl()).into(imageAdmin);
            }
        });


    }

    private void selectFile() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("application/pdf");
        startActivityForResult(intent, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 1){
            mSelectedUri = data.getData();
        }
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
        String timestamp = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new Date(System.currentTimeMillis()));
        Post post = new Post(descricao,timestamp);

        FirebaseFirestore.getInstance().collection("/turmas")
                .document(turma.getUuid()).collection("/posts").add(post).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                Toast.makeText(NewPostActivity.this, "Post criado com sucesso!", Toast.LENGTH_LONG).show();
                txtPost.setText("");
                txtPost.setHint("Compartilhe novamente com sua turma!");
            }
        });

    }


}
