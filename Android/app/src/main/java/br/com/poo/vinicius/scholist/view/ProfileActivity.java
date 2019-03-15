package br.com.poo.vinicius.scholist.view;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.UUID;

import br.com.poo.vinicius.scholist.R;
import br.com.poo.vinicius.scholist.model.User;

public class ProfileActivity extends AppCompatActivity {
    ImageView imageProfile;
    EditText editUsername;
    EditText typeUser;
    EditText editEmail;
    Button btnLogout;
    Button btnSelectedPhoto;
    private Uri mSelectedUri;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        imageProfile = findViewById(R.id.photoProfile);
        editUsername = findViewById(R.id.userNameEdit);
        typeUser = findViewById(R.id.typeUser);
        editEmail = findViewById(R.id.editEmail);
        btnLogout = findViewById(R.id.bnt_logout);
        btnSelectedPhoto = findViewById(R.id.btn_selected_photo);
        btnSelectedPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectPhoto();
            }
        });
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            logout();
            }
        });
        editEmail.setEnabled(false);
        typeUser.setEnabled(false);
        getUser();
    }
    private void selectPhoto() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, 0);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_profile, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.menu_ok:
                updateDatesinUser();
                break;


        }
        return super.onOptionsItemSelected(item);
    }
    private void updateDatesinUser() {
        String newName = editUsername.getText().toString();

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        final DocumentReference ref = db.collection("users").document(FirebaseAuth.getInstance().getUid());

        String filename = UUID.randomUUID().toString();
        final StorageReference ref1 = FirebaseStorage.getInstance().getReference("/images/" + filename);


        ref1.putFile(mSelectedUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                ref1.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        String profileUrl = uri.toString();
                        ref.update("profileUrl", profileUrl).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(ProfileActivity.this, "Dados alterados com sucesso!", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });
            }
        });
        ref.update("username", newName).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
            }
        });
    }

    public void logout() {
           FirebaseAuth.getInstance().signOut();
           Intent intentSignOut = new Intent(ProfileActivity.this, LoginActivity.class);
           intentSignOut.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
           startActivity(intentSignOut);
           finish();
    }
    public void getUser() {
        FirebaseFirestore.getInstance().collection("/users")
                .document(FirebaseAuth.getInstance().getUid())
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        User user = documentSnapshot.toObject(User.class);
                        editUsername.setText(user.getUsername(), null);
                        typeUser.setText("Type: " + user.getTipo(),null);
                        String email = FirebaseAuth.getInstance().getCurrentUser().getEmail();
                        editEmail.setText("Email: " + email, null);
                        Picasso.get()
                                .load(user.getProfileUrl())
                                .into(imageProfile);
                        Log.d("Teste", "onEvent" + user.getUsername());
                    }
                });
    }
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 0) {
            mSelectedUri = data.getData();

            Bitmap bitmap = null;
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), mSelectedUri);
                imageProfile.setImageDrawable(new BitmapDrawable(bitmap));
                btnSelectedPhoto.setAlpha(0);
            } catch (IOException e) {
            }
        }
    }

}
