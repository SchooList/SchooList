package br.com.poo.vinicius.scholist;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.media.Image;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.UUID;

import br.com.poo.vinicius.scholist.model.Turma;

public class FormularioTurmaActivity extends AppCompatActivity {



    Button btnNovaTurma;
    EditText editNome;
    EditText editDescricao;
    Button btnSelectedPhoto;
    ImageView imagePhoto;
    private Uri mSelectedUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_formulario_turma);
        btnNovaTurma = findViewById(R.id.formulario_nova_turma);
        editDescricao = findViewById(R.id.formulario_turma_descricao);
        editNome = findViewById(R.id.formulario_turma_nome);
        btnSelectedPhoto = findViewById(R.id.btn_selected_photo);
        imagePhoto = findViewById(R.id.formulario_turma_imageView);

        btnNovaTurma.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createTurmaInFirebase();
            }
        });


        btnSelectedPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectPhoto();
            }
        });

    }

    private void createTurmaInFirebase() {
        final String nome = editNome.getText().toString();
        final String descricao = editDescricao.getText().toString();

        if(nome.isEmpty() || descricao.isEmpty() || nome == null || descricao == null) {
            Toast.makeText(this, "Nome e Descrição da turma devem ser preenchidos", Toast.LENGTH_SHORT).show();
            return;
        }

        String filename = UUID.randomUUID().toString();

        final StorageReference ref = FirebaseStorage.getInstance().getReference("/images/" + filename);
        ref.putFile(mSelectedUri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                String profileUrl = uri.toString();
                                Turma turma = new Turma(nome, descricao, profileUrl);

                                CollectionReference doc = FirebaseFirestore.getInstance().collection("/turmas");
                                doc.document()
                                        .set(turma)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                Intent backToList = new Intent(FormularioTurmaActivity.this, TurmasActivity.class);
                                                startActivity(backToList);
                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Log.i("Teste", e.getMessage());
                                    }
                                });


                            }
                        });
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e("Teste", e.getMessage(), e);
                Toast.makeText(FormularioTurmaActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                return;
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
                imagePhoto.setImageDrawable(new BitmapDrawable(bitmap));
                btnSelectedPhoto.setAlpha(0);
            } catch (IOException e) {
            }
        }
    }


    private void selectPhoto() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, 0);
    }

}
