package com.example.coosinstaff;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.coosinstaff.connection.ConnectionDB;
import com.example.coosinstaff.model.EncryptionPassword;
import com.thekhaeng.pushdownanim.PushDownAnim;

import java.sql.Connection;
import java.sql.Statement;

public class RepasswordActivity extends AppCompatActivity {

    String phone_num,newpw;
    EditText edtNewpw;
    Button btnHoanTat;
    Connection connect;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_repassword);

        edtNewpw = findViewById(R.id.edit_new_password);
        btnHoanTat = findViewById(R.id.buttonNewPw);
        phone_num = getIntent().getStringExtra("phone_num");

        PushDownAnim.setPushDownAnimTo(btnHoanTat).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    newpw = edtNewpw.getText().toString();
                    ConnectionDB connectionDB = new ConnectionDB();
                    connect = connectionDB.CONN();

                    if (connect==null){
                        Toast.makeText(getApplicationContext(),"Không có kết nối mạng!",Toast.LENGTH_LONG).show();
                    }
                    else if (newpw.trim().equals("")){
                        Toast.makeText(getApplicationContext(),"Bạn chưa nhập mật khẩu",Toast.LENGTH_LONG).show();
                    }
                    else {
                        String query = "UPDATE EMPLOYEE SET PASSWORD='"+EncryptionPassword.md5(newpw)+"' WHERE PHONE_NUM='"+phone_num+"'";
                        Statement statement = connect.createStatement();
                        statement.executeQuery(query);
                        connect.close();
                    }
                }
                catch (Exception e){
                    Toast.makeText(getApplicationContext(),"Đổi mật khẩu thành công",Toast.LENGTH_LONG).show();
                }
                Intent intent = new Intent(RepasswordActivity.this,LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });

    }
}
