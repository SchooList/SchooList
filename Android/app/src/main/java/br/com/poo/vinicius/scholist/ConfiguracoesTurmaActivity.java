package br.com.poo.vinicius.scholist;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import br.com.poo.vinicius.scholist.model.Turma;

public class ConfiguracoesTurmaActivity extends AppCompatActivity {

    Turma turma;
    EditText txtNomeTurma;
    ImageView imgTurma;
    EditText txtDescricaoTurma;
    Button btnSair;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configuracoes_turma);
        txtNomeTurma = findViewById(R.id.settings_turma_nome);
        txtDescricaoTurma = findViewById(R.id.settings_turma_descricao);
        imgTurma = findViewById(R.id.settings_turma_imageView);
        btnSair = findViewById(R.id.settings_turma_sair);
        Intent intent = getIntent();
        turma = intent.getParcelableExtra("turma");

        txtNomeTurma.setText(turma.getNome());
        txtDescricaoTurma.setText(turma.getDescricao());
        Picasso.get().load(turma.getProfileUrl()).into(imgTurma);

        if(FirebaseAuth.getInstance().getUid().equals(turma.getUuidAdmin())) {
            btnSair.setText("Deletar turma");
        }



    }
}
