package br.com.poo.vinicius.scholist;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
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

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.UUID;

import br.com.poo.vinicius.scholist.model.Turma;

public class ConfiguracoesTurmaActivity extends AppCompatActivity {

    Turma turma;
    EditText txtNomeTurma;
    ImageView imgTurma;
    EditText txtDescricaoTurma;
    Button btnSair;
    Button btnSelectedPhoto;
    private Uri mSelectedUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configuracoes_turma);
        txtNomeTurma = findViewById(R.id.settings_turma_nome);
        txtDescricaoTurma = findViewById(R.id.settings_turma_descricao);
        imgTurma = findViewById(R.id.settings_turma_imageView);
        btnSair = findViewById(R.id.settings_turma_sair);
        btnSelectedPhoto = findViewById(R.id.settings_turma_botao_foto);
        Intent intent = getIntent();
        turma = intent.getParcelableExtra("turma");

        txtNomeTurma.setText(turma.getNome());
        txtDescricaoTurma.setText(turma.getDescricao());
        Picasso.get().load(turma.getProfileUrl()).into(imgTurma);

        if(isAdmin()) {
            btnSair.setText("Deletar turma");
        } else {
            txtNomeTurma.setEnabled(false);
            txtDescricaoTurma.setEnabled(false);
            btnSelectedPhoto.setAlpha(0);
            btnSelectedPhoto.setEnabled(false);
        }


        btnSelectedPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectPhoto();
            }
        });

        btnSair.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isAdmin()) {
                    deletarTurma();
                } else {
                    sairDaTurma();
                }
            }
        });
    }

    private Boolean isAdmin() {
        if(FirebaseAuth.getInstance().getUid().equals(turma.getUuidAdmin())) {
            return true;
        } else {
            return false;
        }
    }

    private void selectPhoto() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, 0);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
            if(isAdmin()) { getMenuInflater().inflate(R.menu.menu_profile, menu); }


        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.menu_ok:
                updateTurma();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void updateTurma() {
        Toast.makeText(ConfiguracoesTurmaActivity.this, "CHAMOU", Toast.LENGTH_LONG).show();
        final String nomeTurma = txtNomeTurma.getText().toString();
        final String descricaoTurma = txtDescricaoTurma.getText().toString();

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        final DocumentReference ref = db.collection("turmas").document(turma.getUuid());


        String filename = UUID.randomUUID().toString();

        ref.update("nome", nomeTurma);
        ref.update("descricao", descricaoTurma);

        final StorageReference ref1 = FirebaseStorage.getInstance().getReference("/images/" + filename);
        ref1.putFile(mSelectedUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                ref1.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        String profileUrl = uri.toString();
                        ref.update("profileUrl", profileUrl);

                    }
                });
            }
        });





    }

    private void sairDaTurma() {
        FirebaseFirestore.getInstance().collection("users").document(FirebaseAuth.getInstance().getUid()).collection("turmas")
                .document(turma.getUuid()).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Intent intent = new Intent(ConfiguracoesTurmaActivity.this, TurmasActivity.class);
                startActivity(intent);
                Toast.makeText(ConfiguracoesTurmaActivity.this, "Turma deletada com sucesso", Toast.LENGTH_LONG).show();
            }
        });

    }

    private void deletarTurma() {
        FirebaseFirestore.getInstance().collection("turmas").document(turma.getUuid()).delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Intent intent = new Intent(ConfiguracoesTurmaActivity.this, TurmasActivity.class);
                        startActivity(intent);
                        Toast.makeText(ConfiguracoesTurmaActivity.this, "Turma deletada", Toast.LENGTH_LONG).show();
                    }
                });
    }

    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 0) {
            mSelectedUri = data.getData();
            Bitmap bitmap = null;
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), mSelectedUri);
                imgTurma.setImageDrawable(new BitmapDrawable(bitmap));
            } catch (IOException e) {
            }
        }
    }


}
