package br.com.poo.vinicius.scholist.view;

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

import br.com.poo.vinicius.scholist.R;
import br.com.poo.vinicius.scholist.model.Turma;

public class EntrarNaTurmaActivity extends AppCompatActivity {


    EditText  nomeTurma, descricaoTurma;
    ImageView photoTurma;
    Button enterInGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entrar_na_turma);
        nomeTurma  = findViewById(R.id.entrar_turma_nome);
        descricaoTurma = findViewById(R.id.entrar_turma_descricao);
        photoTurma = findViewById(R.id.entrar_turma_imageView);
        enterInGroup = findViewById(R.id.entrar_turma);
        Intent intentTurma = getIntent();
        final Turma turma =  (Turma) intentTurma.getParcelableExtra("turma");
        nomeTurma.setText(turma.getNome());
        descricaoTurma.setText(turma.getDescricao());
        nomeTurma.setEnabled(false); descricaoTurma.setEnabled(false);
        Picasso.get().load(turma.getProfileUrl()).into(photoTurma);
        enterInGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MoveToGroup(turma);
            }
        });

    }

    private void MoveToGroup(final Turma turma) {
        String userId = FirebaseAuth.getInstance().getUid();
        final String idTurma = turma.getUuid().toString();

        Intent intentBackToList = new Intent(EntrarNaTurmaActivity.this, TurmasActivity.class);
        startActivity(intentBackToList);
        FirebaseFirestore.getInstance().collection("/users").document(userId).collection("turmas")
                .document().set(turma).addOnSuccessListener(new OnSuccessListener<Void>() {

            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(EntrarNaTurmaActivity.this, "Você entrou em " + turma.getNome(), Toast.LENGTH_LONG).show();
                finish();
            }
        });



    }
}
