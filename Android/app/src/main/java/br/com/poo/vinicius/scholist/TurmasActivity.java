package br.com.poo.vinicius.scholist;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;
import com.xwray.groupie.GroupAdapter;
import com.xwray.groupie.Item;
import com.xwray.groupie.OnItemClickListener;
import com.xwray.groupie.ViewHolder;

import java.util.List;

import javax.annotation.Nullable;

import br.com.poo.vinicius.scholist.model.Turma;
import br.com.poo.vinicius.scholist.model.User;

public class TurmasActivity extends AppCompatActivity {

    GroupAdapter adapter;
    Button btnNovaTurma;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_turmas);
        verifyTypeUser();
        RecyclerView rv = findViewById(R.id.recycler);
        btnNovaTurma = findViewById(R.id.nova_turma);

        verifyAuthentication();
        verifyTypeUser();

        adapter = new GroupAdapter<>();
        rv.setAdapter(adapter);
        rv.setLayoutManager(new LinearLayoutManager(this));



        adapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(@NonNull Item item, @NonNull View view) {
                Intent intent = new Intent(TurmasActivity.this, ListaAlunosActivity.class);
                TurmaItem turmaItem = (TurmaItem)item;
                intent.putExtra("turma",turmaItem.turma);
                startActivity(intent);
            }
        });

        fetchTurmas();


        btnNovaTurma.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentNewTurma = new Intent(TurmasActivity.this, FormularioTurmaActivity.class);
                startActivity(intentNewTurma);
            }
        });
    }

    void verifyTypeUser() {
        FirebaseFirestore.getInstance().collection("/users")
                .document(FirebaseAuth.getInstance().getUid())
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        User user = documentSnapshot.toObject(User.class);
                        if(user.getTipo().equals("Aluno")) {
                            System.out.print(user.getUsername());
                            btnNovaTurma.setAlpha(0);
                            btnNovaTurma.setEnabled(false);
                        }
                    }
                });


    }

    private void verifyAuthentication() {
        if(FirebaseAuth.getInstance().getUid() == null) {
            Intent backtoLogin = new Intent(TurmasActivity.this, LoginActivity.class);
            backtoLogin.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(backtoLogin);
        }
    }
    private void fetchTurmas() {
        FirebaseFirestore.getInstance().collection("/turmas")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                        if(e != null) {
                            Log.e("Teste", e.getMessage());
                            return;
                        }
                        List<DocumentSnapshot> docs = queryDocumentSnapshots.getDocuments();
                        for (DocumentSnapshot doc: docs) {
                            Turma turma = doc.toObject(Turma.class);
                            adapter.add(new TurmaItem(turma));
                        }
                        
                    }
                });


    }

    private class TurmaItem extends Item<ViewHolder> {

        private final Turma turma;

        private TurmaItem(Turma turma) {
            this.turma = turma;
        }

        @Override
        public void bind(@NonNull ViewHolder viewHolder, int position) {
            TextView txtTurmaNome = viewHolder.itemView.findViewById(R.id.turma_textViewNome);
            TextView txtTurmaDescricao = viewHolder.itemView.findViewById(R.id.turma_textViewDescricao);
            ImageView imgPhoto = viewHolder.itemView.findViewById(R.id.turma_imageView);
            txtTurmaNome.setText(turma.getNome());
            txtTurmaDescricao.setText(turma.getDescricao());
            Picasso.get()
                    .load(turma.getProfileUrl())
                    .into(imgPhoto);
        }
        @Override
        public int getLayout() {
            return R.layout.item_turma;
        }
    }
}








