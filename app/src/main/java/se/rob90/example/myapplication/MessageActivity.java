package se.rob90.example.myapplication;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import se.rob90.example.myapplication.Adapter.MessageAdapter;
import se.rob90.example.myapplication.DatabaseLocal.DatabaseL;
import se.rob90.example.myapplication.Model.DatabaseHelper;
import se.rob90.example.myapplication.Model.Chat;
import se.rob90.example.myapplication.Model.Users;

public class MessageActivity extends AppCompatActivity {

    TextView username;
    ImageView imageView;

    EditText msg_editText;
    ImageButton sendBtn;

    FirebaseUser firebaseUser;
    DatabaseReference myRef;
    Intent intent;

    public ArrayList<String> arrayList;

    MessageAdapter messageAdapter;
    List<Chat> mChat;

    public String test;

    RecyclerView recyclerView1;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        imageView = findViewById(R.id.imageview_profile);
        username = findViewById(R.id.username_profile);
        sendBtn= findViewById(R.id.btn_send);
        msg_editText= findViewById(R.id.text_send);

        recyclerView1 = findViewById(R.id.recycler_View);
        recyclerView1.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setStackFromEnd(true);
        recyclerView1.setLayoutManager(linearLayoutManager);
        intent = getIntent();
        String userid = intent.getStringExtra("userid");

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        myRef = FirebaseDatabase.getInstance().getReference("MyUsers").child(userid);

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot DataSnapshot) {
                Users user = DataSnapshot.getValue(Users.class);
                username.setText(user.getUsername());

                if (user.getImageURL().equals("default")){
                    imageView.setImageResource(R.mipmap.ic_launcher);
                }else{
                    Glide.with(MessageActivity.this)
                            .load(user.getImageURL())
                            .into(imageView);
                }
                readMessages(firebaseUser.getUid(),userid,user.getImageURL());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String msg = msg_editText.getText().toString();
                test = msg_editText.getText().toString();
                DatabaseL databaseL = new DatabaseL(MessageActivity.this);

                if (!msg.equals("")){
                    DatabaseHelper databaseHelper = new DatabaseHelper(-1,msg);
                    test = msg_editText.getText().toString();
                    boolean Succes = databaseL.addOne(databaseHelper);
                    Toast.makeText(MessageActivity.this, "succesQ!!"+Succes, Toast.LENGTH_LONG).show();
                    //Toast.makeText(MessageActivity.this, databaseHelper.toString(), Toast.LENGTH_SHORT).show();
                    sendMessage(firebaseUser.getUid(),userid,msg);
                }else {
                    Toast.makeText(MessageActivity.this, "Please send a message", Toast.LENGTH_SHORT).show();
                }


                msg_editText.setText("");
            }
        });





    }
    private void sendMessage(String sender, String receiver, String message){
        DatabaseReference myRef = FirebaseDatabase.getInstance().getReference();

        HashMap<String , Object> hashMap = new HashMap<>();
        hashMap.put("sender",sender);
        hashMap.put("receiver", receiver);
        hashMap.put("message", message);

        myRef.child("Chats").push().setValue(hashMap);



    }


    private void readMessages(String myid,String userid,String imageurl){
        mChat = new ArrayList<>();
        myRef = FirebaseDatabase.getInstance().getReference("Chats");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot DataSnapshot) {
                mChat.clear();
                for (DataSnapshot snapshot : DataSnapshot.getChildren()){
                    Chat chat = snapshot.getValue(Chat.class);
                    if (chat.getReceiver().equals(myid) && chat.getSender().equals(userid) || chat.getReceiver().equals(userid) && chat.getSender().equals(myid)){
                    mChat.add(chat);
                    }
                    messageAdapter = new MessageAdapter(MessageActivity.this,mChat,imageurl);
                    recyclerView1.setAdapter(messageAdapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


}