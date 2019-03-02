package br.com.poo.vinicius.scholist;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

import br.com.poo.vinicius.scholist.adapter.AlunosAdapter;
import br.com.poo.vinicius.scholist.dao.AlunoDAO;
import br.com.poo.vinicius.scholist.model.Aluno;
import br.com.poo.vinicius.scholist.model.Turma;
import br.com.poo.vinicius.scholist.model.User;

public class ListaAtividadesActivity extends AppCompatActivity {
    ListView listaAlunos;
    Button novoComentario;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_atividades);
            novoComentario = findViewById(R.id.nova_atividade);

            final Intent intent = getIntent();
            final Turma turma = (Turma) intent.getParcelableExtra("turma");


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
                Intent intentGoToProof = new Intent(ListaAtividadesActivity.this, ProvasActivity.class);
                startActivity(intentGoToProof);
                break;
            case R.id.menu_mapa:
                Intent vaiParaMapa = new Intent(ListaAtividadesActivity.this, MapsActivity.class);
                startActivity(vaiParaMapa);
                break;
            case R.id.menuVerPerfil:
                Intent goToProfile = new Intent(ListaAtividadesActivity.this, ProfileActivity.class);
                startActivity(goToProfile);
                break;
        }
        return super.onOptionsItemSelected(item);
    }


}
