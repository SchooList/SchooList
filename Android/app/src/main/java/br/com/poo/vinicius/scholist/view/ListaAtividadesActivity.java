package br.com.poo.vinicius.scholist.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
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
import com.xwray.groupie.ViewHolder;

import java.util.List;

import javax.annotation.Nullable;

import br.com.poo.vinicius.scholist.EnviaAlunosTask;
import br.com.poo.vinicius.scholist.R;
import br.com.poo.vinicius.scholist.model.Post;
import br.com.poo.vinicius.scholist.model.Turma;
import br.com.poo.vinicius.scholist.model.User;

public class ListaAtividadesActivity extends AppCompatActivity {
    ListView listaAlunos;
    Button novoComentario;
    GroupAdapter adapter;
    RecyclerView rv;
    Turma turma;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_atividades);
            novoComentario = findViewById(R.id.nova_atividade);
            rv = findViewById(R.id.listaPosts);
            final Intent intent = getIntent();
            turma = (Turma) intent.getParcelableExtra("turma");
            final String adminUid = turma.getUuidAdmin().toString();
            final String userId = FirebaseAuth.getInstance().getUid().toString();
            if(userId.equals(adminUid)) { } else {
                novoComentario.setAlpha(0);
                novoComentario.setEnabled(false); }

            novoComentario.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intentToNewPost = new Intent(ListaAtividadesActivity.this, NewPostActivity.class);
                    intentToNewPost.putExtra("turma", turma);
                    startActivity(intentToNewPost);
                }
            });
             adapter = new GroupAdapter();
             rv.setAdapter(adapter);
             rv.setLayoutManager(new LinearLayoutManager(this));
             fetchPosts();
    }
    private void fetchPosts() {
        FirebaseFirestore.getInstance().collection("/turmas").document(turma.getUuid()).collection("/posts").orderBy("timestamp")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                        if(e != null) {
                            Log.e("Teste", e.getMessage());
                            return;
                        }
                        List<DocumentSnapshot> docs = queryDocumentSnapshots.getDocuments();
                        for (DocumentSnapshot doc: docs) {
                            Post post = doc.toObject(Post.class);
                            adapter.add(new PostItem(post));
                        }
                    }
                });
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_lista_alunos, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId())  {
            case R.id.menuEnviarNotas:
                new EnviaAlunosTask(this).execute();
                break;
            case R.id.menu_baixar_provas:
                Intent intentGoToProof = new Intent(ListaAtividadesActivity.this, MaterialActivity.class);
                intentGoToProof.putExtra("turma", turma);
                startActivity(intentGoToProof);
                break;
            case R.id.menu_mapa:
                Intent vaiParaMapa = new Intent(ListaAtividadesActivity.this, MapsActivity.class);
                startActivity(vaiParaMapa);
                break;
            case R.id.menuVerPerfil:
                Intent goToProfile = new Intent(ListaAtividadesActivity.this, ConfiguracoesTurmaActivity.class);
                goToProfile.putExtra("turma", turma);
                startActivity(goToProfile);
                break;
        }
        return super.onOptionsItemSelected(item);
    }
    private class PostItem extends Item<ViewHolder>  {
        private final Post post;
        private PostItem(Post post) {this.post = post;}
        @Override
        public void bind(@NonNull ViewHolder viewHolder, int position) {
            final ImageView imagePost = viewHolder.itemView.findViewById(R.id.imagePostsUser);
            TextView descricao = viewHolder.itemView.findViewById(R.id.descriptionPost);
            TextView timeStamp = viewHolder.itemView.findViewById(R.id.txtTimeStamp);
            final TextView nomeProfessor = viewHolder.itemView.findViewById(R.id.txtName);
           FirebaseFirestore.getInstance().collection("/users").document(turma.getUuidAdmin())
                   .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
               @Override
               public void onSuccess(DocumentSnapshot documentSnapshot) {
                    User user = documentSnapshot.toObject(User.class);
                    nomeProfessor.setText(user.getUsername());
                    Picasso.get().load(user.getProfileUrl()).into(imagePost);
               }
           });
            timeStamp.setText(post.getTimestamp());
            descricao.setText(post.getDescricao());
        }
        @Override
        public int getLayout() {
            return R.layout.item_posts;
        }
    }
}
