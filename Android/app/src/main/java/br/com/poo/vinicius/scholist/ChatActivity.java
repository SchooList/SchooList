package br.com.poo.vinicius.scholist;

import br.com.poo.vinicius.scholist.model.Message;
import br.com.poo.vinicius.scholist.model.Turma;
import br.com.poo.vinicius.scholist.model.User;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;
import com.xwray.groupie.GroupAdapter;
import com.xwray.groupie.Item;
import com.xwray.groupie.ViewHolder;

import org.w3c.dom.Text;

import java.util.List;

import javax.annotation.Nullable;

public class ChatActivity extends AppCompatActivity {

    private GroupAdapter adapter;
    private EditText editChat;
    private User me;
    private Message conversas;
    private Turma turma;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        RecyclerView rv = findViewById(R.id.recycler_chat);
        Button btnChat = findViewById(R.id.btnChat);
        editChat = findViewById(R.id.edit_chat);

        Intent intentTurma = getIntent();
         turma =  (Turma) intentTurma.getParcelableExtra("turma");

        btnChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMessage();
            }
        });

        adapter = new GroupAdapter();
        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.setAdapter(adapter);

        FirebaseFirestore.getInstance().collection("/users").document(FirebaseAuth.getInstance().getUid())
                .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                me = documentSnapshot.toObject(User.class);
                fetchMessages();

            }
        });



    }

    private void fetchMessages() {
        if(me != null) {
            String fromId = me.getUuid();

            FirebaseFirestore.getInstance().collection("/turmas").document(turma.getUuid())
                    .collection("conversas").orderBy("timeStamp", Query.Direction.ASCENDING)
                    .addSnapshotListener(new EventListener<QuerySnapshot>() {
                        @Override
                        public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                            List<DocumentChange> documentChanges = queryDocumentSnapshots.getDocumentChanges();
                                if(documentChanges != null) {
                                    for (DocumentChange doc: documentChanges) {
                                        if(doc.getType() == DocumentChange.Type.ADDED) {
                                            Message message = doc.getDocument().toObject(Message.class);
                                            adapter.add(new MessageItem(message));
                                        }
                                    }
                                }
                        }
                    });
        }

    }

    private void sendMessage() {
        String text = editChat.getText().toString();
        editChat.setText("");

        String fromId = FirebaseAuth.getInstance().getUid();
        long timeStamp = System.currentTimeMillis();

        Message message = new Message();
        message.setFromId(fromId);
        message.setToId("");
        message.setTimeStamp(timeStamp);
        message.setText(text);

        if(!message.getText().isEmpty()) {
            FirebaseFirestore.getInstance().collection("/turmas").document(turma.getUuid())
                    .collection("conversas").add(message).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                @Override
                public void onSuccess(DocumentReference documentReference) {
                    Log.d("Deu certo", documentReference.getId());
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.d("Deu errado", e.getMessage(), e);
                }
            });

        }
    }

    private class MessageItem extends Item<ViewHolder> {

        private final Message message;

        private MessageItem(Message message) {this.message = message;}

        @Override
        public void bind(@NonNull ViewHolder viewHolder, int position) {
            TextView txtMsg = viewHolder.itemView.findViewById(R.id.txt_msg);
            final ImageView imgMessage = viewHolder.itemView.findViewById(R.id.img_message_user);
            txtMsg.setText(message.getText());

            FirebaseFirestore.getInstance().collection("/turmas").document(turma.getUuid())
                    .collection("/conversas").document().get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    conversas = documentSnapshot.toObject(Message.class);
                }
            });

            FirebaseFirestore.getInstance().collection("/users").document("jmxmNp1cMbP4yg4U01IOGSUHzh12")
                    .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    User user = documentSnapshot.toObject(User.class);
                    Picasso.get().load(user.getProfileUrl().toString()).into(imgMessage);
                }
            });

        }

        @Override
        public int getLayout() {
            return message.getFromId().equals(FirebaseAuth.getInstance().getUid()) ? R.layout.item_from_message : R.layout.item_to_message;
        }
    }

}
