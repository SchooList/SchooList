package br.com.poo.vinicius.scholist;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
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
import com.xwray.groupie.OnItemLongClickListener;
import com.xwray.groupie.ViewHolder;

import java.util.List;

import javax.annotation.Nullable;

import br.com.poo.vinicius.scholist.model.Turma;
import br.com.poo.vinicius.scholist.model.User;

public class TurmasActivity extends AppCompatActivity {

    GroupAdapter adapter;
    Button btnNovaTurma;
    RecyclerView rv;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_turmas);
        rv = findViewById(R.id.recycler);
        btnNovaTurma = findViewById(R.id.nova_turma);

        verifyAuthentication();
        if(isOnline() == false) {
            Intent backtoLogin = new Intent(TurmasActivity.this, LoginActivity.class);
            backtoLogin.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            finish();
            startActivity(backtoLogin);

        }


        adapter = new GroupAdapter<>();
        rv.setAdapter(adapter);
        rv.setLayoutManager(new LinearLayoutManager(this));


       adapter.setOnItemLongClickListener(new OnItemLongClickListener() {
           @Override
           public boolean onItemLongClick(@NonNull Item item, @NonNull View view) {
             Intent intent = new Intent(TurmasActivity.this,ChatActivity.class );
             TurmaItem turmaItem = (TurmaItem)item;
             intent.putExtra("turma",turmaItem.turma);
             startActivity(intent);
             return false;
           }
       });

        adapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(@NonNull Item item, @NonNull View view) {
                Intent intent = new Intent(TurmasActivity.this, ListaAtividadesActivity.class);
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_lista_turmas, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_profile_aluno:
                Intent intentProfile = new Intent(TurmasActivity.this, ProfileActivity.class);
                startActivity(intentProfile);
                break;
            case R.id.menu_profile_search:
                Intent searchTurma = new Intent(TurmasActivity.this, SearchTurmasActivity.class);
                startActivity(searchTurma);
                break;


        }



        return super.onOptionsItemSelected(item);
    }

    public boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
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
        } else{
            verifyTypeUser();
        }

    }
    private void fetchTurmas() {
        String userId = FirebaseAuth.getInstance().getUid();
        FirebaseFirestore.getInstance().collection("/users").document(userId)
                .collection("turmas").addSnapshotListener(new EventListener<QuerySnapshot>() {
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








