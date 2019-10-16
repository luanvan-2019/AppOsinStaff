package com.example.coosinstaff;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.coosinstaff.model.CheckLogined;
import com.thekhaeng.pushdownanim.PushDownAnim;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class LoginActivity extends AppCompatActivity {

    EditText edtPhoneNum,edtPassword;
    TextView txtDieuKienHopTac,txtForgetPassword;
    CheckBox checkBoxDieuKien;
    Button btnLogin;
    Connection connect;
    String phone_num,password;
    ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        pd = new ProgressDialog(LoginActivity.this);
        pd.setTitle("Đăng nhập");
        pd.setMessage("Vui lòng đợi...");
        pd.setCancelable(false);

        edtPhoneNum = findViewById(R.id.edt_phonenum);
        edtPassword = findViewById(R.id.edt_password);
        txtDieuKienHopTac = findViewById(R.id.txtDieuKienHopTac);
        checkBoxDieuKien = findViewById(R.id.checkbox_dieukien_hoptac);
        txtForgetPassword = findViewById(R.id.txt_forget_password);
        btnLogin = findViewById(R.id.btn_login);

        PushDownAnim.setPushDownAnimTo(btnLogin).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                phone_num = edtPhoneNum.getText().toString().trim();
                password= edtPassword.getText().toString().trim();
                try
                {
                    com.example.coosinstaff.connection.ConnectionDB conStr=new com.example.coosinstaff.connection.ConnectionDB();
                    connect =conStr.CONN();        // Connect to database
                    if (connect == null)
                    {
                        Toast.makeText(getApplicationContext(), "Không có kết nối mạng!", Toast.LENGTH_LONG).show();
                    }else if (phone_num.equals("")){
                        edtPhoneNum.setError("Bạn chưa nhập số điện thoại!");
                        edtPhoneNum.requestFocus();
                    }else if (password.equals("")){
                        edtPassword.setError("Bạn chưa nhập mật khẩu!");
                        edtPassword.requestFocus();
                    }else if (!checkBoxDieuKien.isChecked()){
                        Toast.makeText(getApplicationContext(), "Bạn chưa đồng ý điều kiện hợp tác!", Toast.LENGTH_LONG).show();
                    }else
                    {
                        String query = "select * from EMPLOYEE where PHONE_NUM= '" + phone_num
                                +"'AND PASSWORD='"+ password +"'";
                        Log.d("BBB",query);
                        Statement stmt = connect.createStatement();
                        ResultSet rs = stmt.executeQuery(query);
                        if(rs.next())
                        {
                            Intent login = new Intent(LoginActivity.this,HomeActivity.class);
                            login.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            CheckLogined.SharedPrefesSAVE(getApplicationContext(),phone_num);
                            startActivity(login);
                            connect.close();
                        }
                        else showAlertDialog();
                    }
                }
                catch (Exception ex)
                {

                }
            }
        });

        //back button
        Toolbar toolbar = findViewById(R.id.toolbar_login);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    //back button
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    public void showAlertDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Đăng nhập thất bại");
        builder.setMessage("Số điện thoại hoặc mật khẩu không đúng!");
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
