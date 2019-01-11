package br.com.poo.vinicius.scholist;

import android.media.Rating;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import br.com.poo.vinicius.scholist.dao.AlunoDAO;
import br.com.poo.vinicius.scholist.model.Aluno;

public class FormularioActivity extends AppCompatActivity {


    FormularioHelper helper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_formulario);
        helper = new FormularioHelper(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_formulario, menu);

        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_ok:
                Aluno aluno = helper.getAluno();
                AlunoDAO dao = AlunoDAO.getInstance(this);
                dao.insere(aluno);

                Toast.makeText(FormularioActivity.this,"Aluno: " + aluno.getNome() + " Salvo!", Toast.LENGTH_LONG).show();
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }







}
