package com.example.coosinstaff;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class RegisterActivity extends AppCompatActivity {

    Button btnRegister;
    EditText edtFullname,edtPhoneNum,edtPassword,edtRePassword;
    CheckBox checkBoxNVVeSinh,checkBoxNVNauAn;
    Integer loaiCongViec=0;
    Connection connect;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        btnRegister = findViewById(R.id.btn_register);
        edtFullname = findViewById(R.id.edt_fullname);
        edtPhoneNum = findViewById(R.id.edt_phonenum);
        edtPassword = findViewById(R.id.edt_password);
        edtRePassword = findViewById(R.id.edt_repassword);
        checkBoxNVVeSinh = findViewById(R.id.checkbox_nhanvien_vesinh);
        checkBoxNVNauAn = findViewById(R.id.checkbox_nhanvien_nauan);

        //back button
        Toolbar toolbar = findViewById(R.id.toolbar_register);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        initialUISetup();

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                String code = CountryData.countryAreaCodes[spinner.getSelectedItemPosition()];

                String number = edtPhoneNum.getText().toString().trim();
                String fullname = edtFullname.getText().toString().trim();
                String password = edtPassword.getText().toString().trim();
                String repassword = edtRePassword.getText().toString().trim();

                if (fullname.isEmpty()){
                    edtFullname.setError("Họ và tên không được để trống!");
                    edtFullname.requestFocus();
                    return;
                }else if (number.isEmpty() || number.length() < 10) {
                    edtPhoneNum.setError("Số điện thoại không hợp lệ hoặc để trống!");
                    edtPhoneNum.requestFocus();
                    return;
                }else if(password.isEmpty()){
                    edtPassword.setError("Mật khẩu không được để trống!");
                    edtPassword.requestFocus();
                    return;
                } else if (repassword.isEmpty()){
                    edtRePassword.setError("Chưa nhập lại mật khẩu!");
                    edtRePassword.requestFocus();
                    return;
                } else if (!repassword.equals(password)){
                    edtRePassword.setError("Mật khẩu không khớp!");
                    edtRePassword.requestFocus();
                    return;
                }else if(loaiCongViec==0){
                    Toast.makeText(getApplicationContext(),"Bạn chưa chọn công việc!",Toast.LENGTH_LONG).show();
                    return;
                }

                //check so dien thoai chua dang ky
                try
                {
                    com.example.coosinstaff.connection.ConnectionDB conStr=new com.example.coosinstaff.connection.ConnectionDB();
                    connect =conStr.CONN();        // Connect to database
                    if (connect == null)
                    {
                        Toast.makeText(getApplicationContext(), "Không có kết nối mạng!", Toast.LENGTH_LONG).show();
                    }
                    else
                    {
                        // Change below query according to your own database.
                        String query = "select * from EMPLOYEE where PHONE_NUM= '" + number  +"'  ";
                        Statement stmt = connect.createStatement();
                        ResultSet rs = stmt.executeQuery(query);
                        if(rs.next())
                        {
                            showAlertDialog();
                            connect.close();
                            return;
                        }
                    }
                }
                catch (Exception ex)
                {

                }

                String phonenumber = "+" + "84" + number;

                Intent intent = new Intent(RegisterActivity.this, OTPActivity.class);
                intent.putExtra("phonenumber", phonenumber);
                intent.putExtra("phone_num", number);
                intent.putExtra("fullname",fullname);
                intent.putExtra("password",password);
                intent.putExtra("loaicongviec",loaiCongViec);
                Toast.makeText(getApplicationContext(), "Đã gửi mã xác thực đến " + number, Toast.LENGTH_LONG).show();
                startActivity(intent);

            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();

        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
//            Intent intent = new Intent(this, HomeActivity.class);
//            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//
//            startActivity(intent);
        }
    }

    //back button
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    //lay lich lam viec
    public void initialUISetup() {
        checkBoxNVVeSinh.setOnCheckedChangeListener(new myCheckBoxChnageClicker());
        checkBoxNVNauAn.setOnCheckedChangeListener(new myCheckBoxChnageClicker());
    }

    class myCheckBoxChnageClicker implements CheckBox.OnCheckedChangeListener
    {

        @Override
        public void onCheckedChanged(CompoundButton buttonView,
                                     boolean isChecked) {
            // TODO Auto-generated method stub

            // Toast.makeText(CheckBoxCheckedDemo.this, &quot;Checked =&gt; &quot;+isChecked, Toast.LENGTH_SHORT).show();

            if(isChecked) {
                if(buttonView==checkBoxNVVeSinh) {
                    loaiCongViec = 1;
                }
                if(buttonView==checkBoxNVNauAn) {
                    loaiCongViec = 2;
                }
                if(buttonView==checkBoxNVVeSinh && buttonView == checkBoxNVNauAn) {
                    loaiCongViec = 12;
                }

            }else {
                if(buttonView==checkBoxNVNauAn && buttonView!=checkBoxNVVeSinh) {
                    loaiCongViec = 1;
                }
                if(buttonView!=checkBoxNVNauAn && buttonView==checkBoxNVVeSinh) {
                    loaiCongViec = 2;
                }
                if(buttonView==checkBoxNVNauAn && buttonView==checkBoxNVVeSinh) {
                    loaiCongViec = 0;
                }

            }

        }
    }

    public void showAlertDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Thông báo");
        builder.setMessage("Số điện thoại này đã được đăng ký trước đó!");
        builder.setCancelable(false);
        builder.setNegativeButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();

            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
}
