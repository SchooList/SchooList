package br.com.poo.vinicius.scholist;

import android.Manifest;
import android.content.ClipData;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthProvider;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.xwray.groupie.GroupAdapter;
import com.xwray.groupie.Item;
import com.xwray.groupie.OnItemClickListener;
import com.xwray.groupie.ViewHolder;

import java.security.Permission;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import br.com.poo.vinicius.scholist.model.Turma;

public class MaterialActivity extends AppCompatActivity {


    Turma turma;
    Button newMaterial;
    Uri uriPdf;
    String adminUserId;
    FirebaseFirestore database;
    FirebaseStorage storage;
    RecyclerView rv;
    GroupAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_material);
        newMaterial = findViewById(R.id.novo_material_turma);
        rv = findViewById(R.id.recycler);
        adminUserId = FirebaseAuth.getInstance().getUid();
        Intent intent = getIntent();
        turma = intent.getParcelableExtra("turma");


        adapter = new GroupAdapter();
        rv.setAdapter(adapter);
        rv.setLayoutManager(new LinearLayoutManager(this));
        fetchMaterial();


        adapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(@NonNull Item item, @NonNull View view) {
                MaterialItem materialItem = (MaterialItem)item;
                Intent intentSite = new Intent(Intent.ACTION_VIEW);
                String site = materialItem.material.getMaterialUrl();
                intentSite.setData(Uri.parse(site));
                startActivity(intentSite);
            }
        });


        if(adminUserId.equals(turma.getUuidAdmin())) { } else {
            newMaterial.setAlpha(0);
            newMaterial.setEnabled(false); }

        database = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance();

        newMaterial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ContextCompat.checkSelfPermission(MaterialActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)== PackageManager.PERMISSION_GRANTED){
                    selectPdf();
                } else {
                    ActivityCompat.requestPermissions(MaterialActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},9);
                }
            }
        });
    }

    private void fetchMaterial() {
        FirebaseFirestore.getInstance().collection("turmas").document(turma.getUuid())
                .collection("material").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@javax.annotation.Nullable QuerySnapshot queryDocumentSnapshots, @javax.annotation.Nullable FirebaseFirestoreException e) {
                if(e!= null) {
                    Log.d("teste", e.getMessage()); return;}

                    List<DocumentSnapshot> docs = queryDocumentSnapshots.getDocuments();
                for (DocumentSnapshot doc: docs) {
                    Material material = doc.toObject(Material.class);
                    adapter.add(new MaterialItem(material));
                }
            }
        });

    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode == 9 && grantResults[0]==PackageManager.PERMISSION_GRANTED) {
            selectPdf();
        } else {
        Toast.makeText(MaterialActivity.this, "Não tem permissão", Toast.LENGTH_LONG).show();
        }

    }

    private void selectPdf() {
        Intent intentPdf = new Intent();
        intentPdf.setType("application/pdf");
        intentPdf.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intentPdf,86);
    }

    private void uploadFile() {
        String filename = UUID.randomUUID().toString();
        final StorageReference ref = FirebaseStorage.getInstance().getReference("/pdf/" + filename);

        ref.putFile(uriPdf).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        String materialUrl = uri.toString();
                        String timestamp = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new Date(System.currentTimeMillis()));
                        Material material =  new Material(materialUrl, timestamp, "PDF");

                        CollectionReference doc = FirebaseFirestore.getInstance().collection("turmas");
                        doc.document(turma.getUuid()).collection("material").document().set(material).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(MaterialActivity.this, "Deu certo ", Toast.LENGTH_LONG).show();
                            }
                        });
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(MaterialActivity.this, "Deu errado ", Toast.LENGTH_LONG).show();
                    }
                });
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode == 86 && resultCode == RESULT_OK && data != null) {
            uriPdf = data.getData();
            uploadFile();
        } else {
            Toast.makeText(MaterialActivity.this, "Selecione o pdf", Toast.LENGTH_SHORT).show();
        }
    }




    private class MaterialItem extends Item<ViewHolder> {
        private final Material material;
        private MaterialItem(Material material) {this.material = material;}

        @Override
        public void bind(@NonNull ViewHolder viewHolder, int position) {
            TextView titulo = viewHolder.itemView.findViewById(R.id.material_titulo);
            titulo.setText(material.getTitulo());
        }

        @Override
        public int getLayout() {
            return R.layout.item_material;
        }
    }

}
