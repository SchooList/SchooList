package br.com.poo.vinicius.scholist;

import android.widget.EditText;
import android.widget.RatingBar;

import br.com.poo.vinicius.scholist.model.Aluno;

public class FormularioHelper {

    EditText campoNome;
    EditText campoEndereco;
    EditText campoTelefone;
    EditText campoSite;
    RatingBar campoNota;

    public FormularioHelper(FormularioActivity activity) {
        campoNome = (EditText) activity.findViewById(R.id.formulario_nome);
        campoEndereco = (EditText) activity.findViewById(R.id.formulario_endereco);
        campoTelefone = (EditText) activity.findViewById(R.id.formulario_telefone);
        campoSite = (EditText) activity.findViewById(R.id.formulario_site);
        campoNota = (RatingBar) activity.findViewById(R.id.formulario_nota);
    }


    //Private because after tests make in singleton pattern
    private FormularioHelper() {

    }


    public Aluno getAluno() {
    Aluno aluno = new Aluno();
    aluno.setNome(campoNome.getText().toString());
    aluno.setEndereco(campoEndereco.getText().toString());
    aluno.setTelefone(campoTelefone.getText().toString());
    aluno.setSite(campoSite.getText().toString());
    aluno.setNota(Double.valueOf(campoNota.getProgress()));
    return aluno;
    }
}
