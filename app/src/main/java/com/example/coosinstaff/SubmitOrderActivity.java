package com.example.coosinstaff;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.thekhaeng.pushdownanim.PushDownAnim;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.DecimalFormat;

public class SubmitOrderActivity extends AppCompatActivity {

    String orderType,orderStatus,userOrder;
    Integer idOrder;
    TextView txtOrderType,txtThu,txtNgay,txtCa,txtGioCa,txtAddress,txtSoBuoi,txtSoGio,txtTongTien,txtPaymentType,
            txtCusName,txtCusPhone,txtDangtrongCa,txtHoanTat,txtPayStatus,txtMDH;
    LinearLayout linCall,linChat,linMapDirection;
    View line1,line2;
    ImageView node2,node3,avartarCustomer;
    Button btnConfirmPay,btnBaoNghi;
    Connection connect;
    Integer paymentStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_submit_order);

        anhXa();
        idOrder = getIntent().getIntExtra("idOrder",0);
        orderType = getIntent().getStringExtra("orderType");

        //anh xa
        Toolbar toolbar = findViewById(R.id.toolbar_detail);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        checkOrderType();

        if (orderStatus.trim().equals("Đang trong ca làm")){
            line1.setBackgroundColor(getResources().getColor(R.color.enable));
            node2.setImageResource(R.drawable.ic_node_enable);
            txtDangtrongCa.setTextColor(getResources().getColor(R.color.enable));
        }
        if (orderStatus.trim().equals("Hoàn thành")){
            line1.setBackgroundColor(getResources().getColor(R.color.enable));
            node2.setImageResource(R.drawable.ic_node_enable);
            txtDangtrongCa.setTextColor(getResources().getColor(R.color.enable));
            line2.setBackgroundColor(getResources().getColor(R.color.enable));
            node3.setImageResource(R.drawable.ic_node_enable);
            txtHoanTat.setTextColor(getResources().getColor(R.color.enable));
        }
        if (paymentStatus==0){
            txtPayStatus.setText("Chưa thanh toán");
            btnConfirmPay.setVisibility(View.VISIBLE);
        }else{
            txtPayStatus.setText("Đã thanh toán");
            txtPayStatus.setBackgroundResource(R.drawable.bg_text_green);
            btnConfirmPay.setVisibility(View.GONE);
        }

        try {
            com.example.coosinstaff.connection.ConnectionDB conStr=new com.example.coosinstaff.connection.ConnectionDB();
            connect =conStr.CONN();
            // Connect to database
            if (connect == null){checkConnectDialog();}
            else {
                // Change below query according to your own database.
                String query = "select * from CUSTOMER where PHONE_NUM= '" + userOrder  + "'";
                Statement stmt = connect.createStatement();
                ResultSet rs = stmt.executeQuery(query);
                if (rs.next()){
                    txtCusName.setText(rs.getString("FULL_NAME"));
                    txtCusPhone.setText(userOrder);
                }
            }
            connect.close();
        }
        catch (Exception ex){
            Toast.makeText(getApplicationContext(), ex.getMessage(), Toast.LENGTH_LONG).show();
        }

        PushDownAnim.setPushDownAnimTo(btnConfirmPay).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkPaytedDialog();
            }
        });

        PushDownAnim.setPushDownAnimTo(linMapDirection).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SubmitOrderActivity.this,MapDirectionActivity.class);
                intent.putExtra("idOrder",idOrder);
                intent.putExtra("orderType",orderType);
                startActivity(intent);
            }
        });

    }

    //toolbar back button
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private void anhXa(){
        txtDangtrongCa = findViewById(R.id.txt_dangtrong_calam);
        txtHoanTat = findViewById(R.id.txt_hoantat);
        line1 = findViewById(R.id.line_1);
        line2 = findViewById(R.id.line_2);
        node2 = findViewById(R.id.node2);
        node3 = findViewById(R.id.node3);
        txtCusName = findViewById(R.id.txt_customer_name);
        txtCusPhone = findViewById(R.id.txt_customer_phone);
        linCall = findViewById(R.id.linear_call);
        linChat = findViewById(R.id.linear_chat);
        txtOrderType = findViewById(R.id.txt_order_type);
        txtThu = findViewById(R.id.txt_thu);
        txtNgay = findViewById(R.id.txt_ngay);
        txtCa = findViewById(R.id.txt_buoi);
        txtGioCa = findViewById(R.id.txt_time);
        btnBaoNghi = findViewById(R.id.btn_baonghi);
        txtAddress = findViewById(R.id.txt_address);
        txtPayStatus = findViewById(R.id.txt_pay_status);
        txtSoBuoi = findViewById(R.id.txt_sobuoi);
        txtSoGio = findViewById(R.id.txt_sogio);
        txtTongTien = findViewById(R.id.txt_tongtien);
        txtPaymentType = findViewById(R.id.txt_payment_type);
        btnConfirmPay = findViewById(R.id.btnConfirmPay);
        txtMDH = findViewById(R.id.txt_ma_dh);
        linMapDirection = findViewById(R.id.linear_direction);
    }

    public void checkConnectDialog(){
        TextView title = new TextView(this);
        title.setText("THÔNG BÁO");
        title.setPadding(10, 40, 10, 10);
        title.setGravity(Gravity.CENTER);
        title.setTextSize(20);
        title.setTextColor(Color.RED);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCustomTitle(title);
        builder.setMessage("Không có kết nối mạng !");
        builder.setCancelable(false);
        builder.setNegativeButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
        Button b = alertDialog.getButton(DialogInterface.BUTTON_NEGATIVE);
        b.setBackground(getResources().getDrawable(R.drawable.btn_round_coner));
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        layoutParams.gravity = Gravity.CENTER;
        layoutParams.setMargins(20,20,20,20);
        alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(Color.WHITE);
        alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setLayoutParams(layoutParams);

        TextView messageText = alertDialog.findViewById(android.R.id.message);
        messageText.setGravity(Gravity.CENTER);
    }

    private void checkPaytedDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("XÁC NHẬN");
        builder.setMessage("Bạn đã nhận tiền thanh toán từ khách hàng đúng không?");
        builder.setCancelable(false);
        builder.setPositiveButton("Hủy", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        builder.setNegativeButton("Đã nhận", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                try {
                    com.example.coosinstaff.connection.ConnectionDB conStr=new com.example.coosinstaff.connection.ConnectionDB();
                    connect =conStr.CONN();
                    // Connect to database
                    if (connect == null){checkConnectDialog();}
                    else {
                        // Change below query according to your own database.
                        String query = "UPDATE ORDER_SINGLE SET PAYMENT_STATUS=1 WHERE ID="+idOrder+"";
                        Statement stmt = connect.createStatement();
                        stmt.executeQuery(query);
                    }
                    connect.close();
                }
                catch (Exception ex){
                    Toast.makeText(getApplicationContext(), "Xác nhận thành công", Toast.LENGTH_LONG).show();
                    txtPayStatus.setBackgroundResource(R.drawable.bg_text);
                }
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void checkOrderType(){
        if (orderType.trim().equals("Dùng lẻ")){
            try
            {
                com.example.coosinstaff.connection.ConnectionDB conStr=new com.example.coosinstaff.connection.ConnectionDB();
                connect =conStr.CONN();
                // Connect to database
                if (connect == null)
                {
                    checkConnectDialog();
                }
                else
                {
                    // Change below query according to your own database.
                    String query = "select * from ORDER_SINGLE where ID= '" + idOrder  + "'";
                    Statement stmt = connect.createStatement();
                    ResultSet rs = stmt.executeQuery(query);
                    if (rs.next())
                    {
                        txtOrderType.setText(orderType);
                        txtThu.setText(rs.getString("DATE_WORK").substring(0,rs.getString("DATE_WORK").length()-12));
                        txtNgay.setText(rs.getString("DATE_WORK").substring(rs.getString("DATE_WORK").length()-10));
                        txtCa.setText(rs.getString("TIME_WORK").substring(0,rs.getString("TIME_WORK").length()-15));
                        txtGioCa.setText(rs.getString("TIME_WORK").substring(rs.getString("TIME_WORK").length()-14,rs.getString("TIME_WORK").length()-1));
                        txtAddress.setText(rs.getString("ADDRESS_ORDER"));
                        txtSoGio.setText(rs.getString("TOTAL_TIME"));
                        DecimalFormat decimalFormat = new DecimalFormat("#,###");
                        String totalGiaString = decimalFormat.format(rs.getInt("TOTAL_PRICE"));
                        txtTongTien.setText(totalGiaString+"đ");
                        txtPaymentType.setText(rs.getString("PAYMENT_TYPE"));
                        orderStatus = rs.getString("STATUS_ORDER");
                        userOrder = rs.getString("USER_ORDER");
                        paymentStatus=rs.getInt("PAYMENT_STATUS");
                        txtMDH.setText("MĐH: DL"+idOrder);
                    }
                }
                connect.close();
            }
            catch (Exception ex)
            {
                Toast.makeText(getApplicationContext(), ex.getMessage(), Toast.LENGTH_LONG).show();
            }
        }
    }

}
