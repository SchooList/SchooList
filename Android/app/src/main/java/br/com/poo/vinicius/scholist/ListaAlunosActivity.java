package br.com.poo.vinicius.scholist;

import android.content.Intent;
import android.net.Uri;
import android.provider.Browser;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.util.List;

import br.com.poo.vinicius.scholist.dao.AlunoDAO;
import br.com.poo.vinicius.scholist.model.Aluno;

public class ListaAlunosActivity extends AppCompatActivity {
    ListView listaAlunos;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_alunos);
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

        Button novoAluno = findViewById(R.id.novo_aluno);
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
        ArrayAdapter<Aluno> adapter = new ArrayAdapter<Aluno>(this, android.R.layout.simple_list_item_1, alunos);
        listaAlunos.setAdapter(adapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        carregaLista();
        }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, final ContextMenu.ContextMenuInfo menuInfo) {

        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
        final Aluno aluno = (Aluno) listaAlunos.getItemAtPosition(info.position);

        Uri uri = Uri.parse("http://"+aluno.getSite());
        MenuItem itemSite = menu.add("Visitar Site");
        Intent intentSite = new Intent(Intent.ACTION_VIEW, uri);

        startActivity(intentSite);

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
