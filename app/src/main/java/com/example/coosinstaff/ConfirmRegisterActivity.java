package com.example.coosinstaff;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.coosinstaff.model.CheckLogined;
import com.thekhaeng.pushdownanim.PushDownAnim;

public class ConfirmRegisterActivity extends AppCompatActivity {

    TextView txtFullName,txtPhoneNum,txtPassword,txtLoaiViecLam;
    Button btnLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_register);

        txtFullName = findViewById(R.id.txt_fullname);
        txtPhoneNum = findViewById(R.id.txt_phone_num);
        txtPassword = findViewById(R.id.txt_password);
        txtLoaiViecLam = findViewById(R.id.txt_loaivieclam);
        btnLogin = findViewById(R.id.buttonRegisLogin);

        txtFullName.setText(getIntent().getStringExtra("fullname"));
        txtPhoneNum.setText(getIntent().getStringExtra("phone_num"));
        txtPassword.setText(getIntent().getStringExtra("password"));
        if (getIntent().getIntExtra("loaicongviec",0)==1){
            txtLoaiViecLam.setText("Nhân viên vệ sinh");
        }else if(getIntent().getIntExtra("loaicongviec",0)==2){
            txtLoaiViecLam.setText("Nhân viên nấu ăn");
        }else txtLoaiViecLam.setText("- Nhân viên vệ sinh\n- Nhân viên nấu ăn");

        PushDownAnim.setPushDownAnimTo(btnLogin).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent login = new Intent(ConfirmRegisterActivity.this,HomeActivity.class);
                CheckLogined.SharedPrefesSAVE(getApplicationContext(),getIntent().getStringExtra("phone_num"));
                login.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(login);
            }
        });
    }
}
