package br.com.poo.vinicius.scholist;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
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

        btnSair.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(FirebaseAuth.getInstance().getUid().equals(turma.getUuidAdmin())) {
                    deletarTurma();
                } else {
                    sairDaTurma();
                }
            }
        });
    }

    private void sairDaTurma() {
        FirebaseFirestore.getInstance().collection("users").document(FirebaseAuth.getInstance().getUid()).collection("turmas")
                .document(turma.getUuid()).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Intent intent = new Intent(ConfiguracoesTurmaActivity.this, TurmasActivity.class);
                startActivity(intent);
                Toast.makeText(ConfiguracoesTurmaActivity.this, "Turma deletada com sucesso", Toast.LENGTH_LONG).show();
            }
        });

    }

    private void deletarTurma() {
        FirebaseFirestore.getInstance().collection("turmas").document(turma.getUuid()).delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Intent intent = new Intent(ConfiguracoesTurmaActivity.this, TurmasActivity.class);
                        startActivity(intent);
                        Toast.makeText(ConfiguracoesTurmaActivity.this, "Turma deletada", Toast.LENGTH_LONG).show();
                    }
                });
    }
}
