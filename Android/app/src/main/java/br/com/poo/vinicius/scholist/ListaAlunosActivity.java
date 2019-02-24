package br.com.poo.vinicius.scholist;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.provider.Browser;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.net.HttpURLConnection;
import java.util.List;

import br.com.poo.vinicius.scholist.adapter.AlunosAdapter;
import br.com.poo.vinicius.scholist.converter.AlunoConverter;
import br.com.poo.vinicius.scholist.dao.AlunoDAO;
import br.com.poo.vinicius.scholist.model.Aluno;
import br.com.poo.vinicius.scholist.model.User;

public class ListaAlunosActivity extends AppCompatActivity {
    ListView listaAlunos;
    Button novoAluno;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_alunos);
        novoAluno = findViewById(R.id.novo_aluno);
        listaAlunos = (ListView) findViewById(R.id.lista_alunos);
        listaAlunos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> lista, View item, int position, long id) {
                Aluno aluno = (Aluno) lista.getItemAtPosition(position);
                Intent intentToFormWithDates = new Intent(ListaAlunosActivity.this, FormularioActivity.class);
                intentToFormWithDates.putExtra("aluno", aluno);
                startActivity(intentToFormWithDates);
            }
        });

        listaAlunos.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                return false;
            }
        });


        registerForContextMenu(listaAlunos);


        novoAluno.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentToForm = new Intent(ListaAlunosActivity.this,FormularioActivity.class);
                startActivity(intentToForm);
            }
        });
    }





    private void carregaLista() {
        AlunoDAO dao = AlunoDAO.getInstance(this);
        List<Aluno> alunos = dao.buscaAlunos();
        dao.close();
        AlunosAdapter adapter = new AlunosAdapter(this,alunos);
        listaAlunos.setAdapter(adapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        carregaLista();
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
                Intent intentGoToProof = new Intent(ListaAlunosActivity.this, ProvasActivity.class);
                startActivity(intentGoToProof);
                break;
            case R.id.menu_mapa:
                Intent vaiParaMapa = new Intent(ListaAlunosActivity.this, MapsActivity.class);
                startActivity(vaiParaMapa);
                break;
            case R.id.menuVerPerfil:
                Intent goToProfile = new Intent(ListaAlunosActivity.this, ProfileActivity.class);
                startActivity(goToProfile);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, final ContextMenu.ContextMenuInfo menuInfo) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
        final Aluno aluno = (Aluno) listaAlunos.getItemAtPosition(info.position);

        MenuItem itemLigar = menu.add("Ligar");

        itemLigar.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if(ActivityCompat.checkSelfPermission(ListaAlunosActivity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(ListaAlunosActivity.this, new String[] {Manifest.permission.CALL_PHONE}, 123);
                } else {
                    Intent intentLigar = new Intent(Intent.ACTION_CALL);
                    intentLigar.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intentLigar.setData(Uri.parse("tel:" + aluno.getTelefone()));
                    startActivity(intentLigar);
                }
                return false;
            }
        });
        MenuItem itemSMS = menu.add("Enviar SMS");
        Intent intentSMS = new Intent(Intent.ACTION_VIEW);
        intentSMS.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intentSMS.setData(Uri.parse("sms:" + aluno.getTelefone()));
        itemSMS.setIntent(intentSMS);

        MenuItem itemMapa = menu.add("Visualizar no mapa");
        Intent intentMapa = new Intent(Intent.ACTION_VIEW);
        intentMapa.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intentMapa.setData(Uri.parse("geo:0,0?q=" + aluno.getEndereco()));
        itemMapa.setIntent(intentMapa);

        MenuItem ItemSite = menu.add("Visitar site");
        Intent intentSite = new Intent(Intent.ACTION_VIEW);
        String site = aluno.getSite();
        if (!site.startsWith("http://")){
            site = "http://" + site;
        }
        intentSite.setData(Uri.parse(site));
        ItemSite.setIntent(intentSite);

        MenuItem delete = menu.add("Deletar");
        delete.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                Toast.makeText(ListaAlunosActivity.this, "Aluno: " + aluno.getNome() + " Deletado", Toast.LENGTH_SHORT).show();
                AlunoDAO dao = AlunoDAO.getInstance(ListaAlunosActivity.this);
                dao.delete(aluno);
                dao.close();
                carregaLista();
                return false;
            }
        });
    }
}
