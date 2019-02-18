package br.com.poo.vinicius.scholist;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ForgotPasswordActivity extends AppCompatActivity {


    EditText EditEmail;
    Button confirmEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        EditEmail = findViewById(R.id.editTextEmail);
        confirmEmail = findViewById(R.id.confirmEmail);

        confirmEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String emailUser = EditEmail.getText().toString();
                sendEmailToResetPassword(emailUser);
            }
        });
    }

    private void sendEmailToResetPassword(String emailUser) {
        FirebaseAuth.getInstance().sendPasswordResetEmail(emailUser).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()) {
                    Log.d("teste", "email enviado!");
                    alert("Verifique o seu email para alterar a senha");
                    Intent intent = new Intent(ForgotPasswordActivity.this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                }
                else {
                    Log.d("teste", "email nao enviado!");
                    alert("Que pena! E-mail digitado é inválido");
                }
            }
        });

    }
    private void alert(String menssagem) {
        Toast.makeText(this, menssagem, Toast.LENGTH_LONG).show();
    }


}
