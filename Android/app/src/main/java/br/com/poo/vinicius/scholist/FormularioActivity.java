package br.com.poo.vinicius.scholist;

import android.content.Intent;
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

        Intent intent = getIntent();
        Aluno aluno = (Aluno) intent.getSerializableExtra("aluno");
        helper = new FormularioHelper(this);
        if(aluno != null) {
            helper.preencheFormulario(aluno);
        }
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
                if(aluno.getId() != null) {
                    dao.update(aluno);
                    Toast.makeText(FormularioActivity.this,"Aluno: " + aluno.getNome() + " Editado!", Toast.LENGTH_LONG).show();
                } else {
                    dao.insere(aluno);
                    Toast.makeText(FormularioActivity.this,"Aluno: " + aluno.getNome() + " Salvo!", Toast.LENGTH_LONG).show();
                }
                dao.close();


                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
