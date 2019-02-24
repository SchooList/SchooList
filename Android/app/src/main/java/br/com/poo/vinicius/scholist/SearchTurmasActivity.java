package br.com.poo.vinicius.scholist;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

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

public class SearchTurmasActivity extends AppCompatActivity {


    GroupAdapter adapter;
    RecyclerView rv;
    EditText nameTurma;
    ImageButton btnSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_turmas);
        btnSearch = findViewById(R.id.btnSearchTurma);
        rv = findViewById(R.id.searchTurmasReciclyerView);
        nameTurma = findViewById(R.id.searchNameTurma);
        adapter = new GroupAdapter();
        rv.setAdapter(adapter);
        rv.setLayoutManager(new LinearLayoutManager(this));

        adapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(@NonNull Item item, @NonNull View view) {
                //Moviment here
            }
        });

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fetchTurmas();
            }
        });

    }


    private void fetchTurmas() {
        final String nomeTurma = nameTurma.getText().toString();
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

                            if(turma.getNome().equals(nomeTurma)) {
                                adapter.add(new SearchTurmasActivity.TurmaItem(turma));
                            } 


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
