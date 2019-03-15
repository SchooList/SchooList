package br.com.poo.vinicius.scholist.view;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import br.com.poo.vinicius.scholist.R;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        final EditText mEditEmail = findViewById(R.id.edit_email);
        final EditText mEditPassword = findViewById(R.id.edit_password);
        Button btnEnter = (Button) findViewById(R.id.btn_enter);
        TextView txtAccount = (TextView) findViewById(R.id.txt_account);
        TextView txtForgotAccount = (TextView) findViewById(R.id.txt_forgotAccount);
        btnEnter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = mEditEmail.getText().toString();
                String senha = mEditPassword.getText().toString();
                if(email == null || email.isEmpty() || senha.isEmpty() || senha == null) {
                    Toast.makeText(LoginActivity.this, "Nome e Senha devem ser preenchidos", Toast.LENGTH_SHORT).show();
                    return;
                }
                FirebaseAuth.getInstance().signInWithEmailAndPassword(email, senha).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()) {
                            Log.i("Sucesso", task.getResult().getUser().getUid());
                            Intent intent = new Intent(LoginActivity.this, TurmasActivity.class);
                            startActivity(intent);
                            finish();

                        }
                                           }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.i("Error",e.getMessage().toString());
                        Toast.makeText(LoginActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        return;
                    }
                });
            }
        });

        txtAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
                finish();
            }
        });

        txtForgotAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, ForgotPasswordActivity.class);
                startActivity(intent);
            }
        });


    }
}
