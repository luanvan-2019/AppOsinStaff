package com.example.coosinstaff;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.coosinstaff.adapter.ListMessageAdapter;
import com.example.coosinstaff.model.AccountAvatar;
import com.example.coosinstaff.model.ChatMessage;
import com.example.coosinstaff.notifications.MySingleton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import com.thekhaeng.pushdownanim.PushDownAnim;

import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatActivity extends AppCompatActivity {

    RelativeLayout activity_chat;
    FloatingActionButton fab;
    String sender,receiver,accountName,avatarReceiver;
    Connection connect;
    TextView txtReceiverToolbar;
    ListMessageAdapter listMessageAdapter;
    List<ChatMessage> chatMessages;
    RecyclerView recyclerView;
    DatabaseReference reference;

    final private String FCM_API = "https://fcm.googleapis.com/fcm/send";
    final private String serverKey = "key=" + "AAAAZNB3xoU:APA91bGxISuo_YVJ7-142Aua8xMYvafuhaZvNIf01IeOzVrZ1hEypTqdP53X3pMZg_Mx3XkkVJOdiiDCMnHp00ytrTJxLDaozcdVpEXc1AsciThWq5ZkiDOHswqSHsLEskoVXOpC8SZC";
    final private String contentType = "application/json";
    final String TAG = "NOTIFICATION TAG";
    String NOTIFICATION_TITLE;
    String NOTIFICATION_MESSAGE;
    CircleImageView avartar_title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        avartar_title = findViewById(R.id.avartar_title);
        activity_chat = findViewById(R.id.activity_chat);
        fab = findViewById(R.id.fab);
        recyclerView = findViewById(R.id.recyclerview_chat);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);

        //lay so dien thoai
        SharedPreferences SP = getApplicationContext().getSharedPreferences("PHONE",0);
        sender = SP.getString("phone_num",null);

        try
        {
            com.example.coosinstaff.connection.ConnectionDB conStr=new com.example.coosinstaff.connection.ConnectionDB();
            connect =conStr.CONN();        // Connect to database
            if (connect == null)
            {
                Toast.makeText(getApplicationContext(),"Không có kết nối",Toast.LENGTH_LONG).show();
            }
            else
            {
                // Change below query according to your own database.
                String query = "select * from EMPLOYEE WHERE PHONE_NUM='"+sender+"'";
                Statement stmt = connect.createStatement();
                ResultSet rs = stmt.executeQuery(query);
                if (rs.next())
                {
                    accountName = rs.getString("FULL_NAME");
                }
                connect.close();
            }
        }
        catch (Exception ex)
        {
            Toast.makeText(getApplicationContext(), ex.getMessage(), Toast.LENGTH_LONG).show();
        }

        if (getIntent().getStringExtra("receiver").equals("Quản trị viên")){
            receiver = "admin";
        }else {
            receiver = getIntent().getStringExtra("receiver");
        }

        //back button
        Toolbar toolbar = findViewById(R.id.toolbar_chat_only);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        txtReceiverToolbar = findViewById(R.id.toolbar_name_receiver);

        try{
            com.example.coosinstaff.connection.ConnectionDB conStr=new com.example.coosinstaff.connection.ConnectionDB();
            connect =conStr.CONN();        // Connect to database
            if (connect == null)
            {
                Toast.makeText(getApplicationContext(), "Không có kết nối mạng!", Toast.LENGTH_LONG).show();
            }
            else
            {
                if (!receiver.equals("admin")){
                    // Change below query according to your own database.
                    String query = "select * from CUSTOMER where PHONE_NUM= '" + receiver  + "'";
                    Statement stmt = connect.createStatement();
                    ResultSet rs = stmt.executeQuery(query);
                    if(rs.next())
                    {
                        txtReceiverToolbar.setText(rs.getString("FULL_NAME"));
                    }
                }else txtReceiverToolbar.setText("Quản trị viên");

            }
        }catch (Exception e){

        }

        DatabaseReference reference1 = FirebaseDatabase.getInstance().getReference("avatars");
        reference1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    AccountAvatar accountAvatar = snapshot.getValue(AccountAvatar.class);
                    if (accountAvatar.getAcountPhone().equals(receiver)) {
                        avatarReceiver = accountAvatar.getImageUrl();
                        Picasso.get().load(accountAvatar.getImageUrl()).into(avartar_title);
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

        PushDownAnim.setPushDownAnimTo(fab).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText input = findViewById(R.id.input);
                String msg = input.getText().toString();
                if (!msg.equals("")){
                    sendMessage(sender,receiver,msg);
                }else {
                    Toast.makeText(getApplicationContext(),"Không thể gửi tin nhắn trống!",Toast.LENGTH_LONG).show();
                }
                input.setText("");
            }
        });

//        readMessage(sender,receiver);
        reference = FirebaseDatabase.getInstance().getReference("chats");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                readMessage(sender,receiver);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void sendMessage(String sender, String receiver, String message){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();

        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("sender",sender);
        hashMap.put("receiver",receiver);
        hashMap.put("message",message);
        long time;
        time = new Date().getTime();
        hashMap.put("time",time);

        reference.child("chats").push().setValue(hashMap);

        //notification
        NOTIFICATION_TITLE = "Tin nhắn từ NV "+accountName+"["+sender+"] đến ["+receiver+"]";
        NOTIFICATION_MESSAGE =message;

        JSONObject notification = new JSONObject();
        JSONObject notifcationBody = new JSONObject();
        try {
            notifcationBody.put("title", NOTIFICATION_TITLE);
            notifcationBody.put("message", NOTIFICATION_MESSAGE);

            notification.put("to", "/topics/lich");

            notification.put("data", notifcationBody);
        } catch (JSONException e) {
            Log.e(TAG, "onCreate: " + e.getMessage());
        }
        sendNotification(notification);
    }

    private void readMessage(final String sender, final String receiver){

        chatMessages = new ArrayList<>();
        reference = FirebaseDatabase.getInstance().getReference("chats");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                chatMessages.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    ChatMessage chatMessage = snapshot.getValue(ChatMessage.class);
                    if(chatMessage.getSender()!=null && chatMessage.getReceiver()!= null){
                        if (chatMessage.getSender().equals(sender) && chatMessage.getReceiver().equals(receiver)
                                || chatMessage.getSender().equals(receiver) && chatMessage.getReceiver().equals(sender)){
                            chatMessages.add(chatMessage);
                        }
                    }
                    listMessageAdapter = new ListMessageAdapter(ChatActivity.this,chatMessages,avatarReceiver);
                    recyclerView.setAdapter(listMessageAdapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    //back button
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private void sendNotification(JSONObject notification) {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(FCM_API, notification,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(ChatActivity.this, "Request error", Toast.LENGTH_LONG).show();
                        Log.i(TAG, "onErrorResponse: Didn't work");
                    }
                }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("Authorization", serverKey);
                params.put("Content-Type", contentType);
                return params;
            }
        };
        MySingleton.getInstance(getApplicationContext()).addToRequestQueue(jsonObjectRequest);
    }
}