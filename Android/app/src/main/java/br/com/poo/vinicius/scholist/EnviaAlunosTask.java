package br.com.poo.vinicius.scholist;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import java.util.List;

import br.com.poo.vinicius.scholist.converter.AlunoConverter;
import br.com.poo.vinicius.scholist.dao.AlunoDAO;
import br.com.poo.vinicius.scholist.model.Aluno;

public class EnviaAlunosTask extends AsyncTask<Void, Void, String> {
    private ProgressDialog dialog;

    public EnviaAlunosTask(Context context) {
        this.context = context;
    }

    private Context context;

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        dialog = ProgressDialog.show(context, "Aguarde", "Enviando Alunos...", true, true);
    }

    @Override
    protected String doInBackground(Void... objects) {
        AlunoDAO dao = AlunoDAO.getInstance(context);
        List<Aluno> alunos = dao.buscaAlunos();
        dao.close();
        AlunoConverter converter = new AlunoConverter();
        String json = converter.converteToJSON(alunos);

        WebClient client = WebClient.getInstance();
        String resposta = client.post(json);


        return resposta;
    }

    @Override
    protected void onPostExecute(String resposta) {
        super.onPostExecute(resposta);
        dialog.dismiss();
        Toast.makeText(context, resposta, Toast.LENGTH_SHORT).show();

    }


}
