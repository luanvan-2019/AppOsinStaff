package com.example.coosinstaff;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.coosinstaff.model.AccountAvatar;
import com.example.coosinstaff.notifications.MySingleton;
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
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class SubmitOrderActivity extends AppCompatActivity {

    String orderType,orderStatus,userOrder,userSubmit,userSubmit2,userSubmit3;
    Integer idOrder;
    TextView txtOrderType,txtThu,txtNgay,txtCa,txtGioCa,txtAddress,txtSoBuoi,txtSoGio,txtTongTien,txtPaymentType,
            txtCusName,txtCusPhone,txtDangtrongCa,txtHoanTat,txtPayStatus,txtMDH,txtSobuoicon,txtDientich,txtListDongnhgiep,
            txtNameDN1,txtNameDN2,txtPhoneDN1,txtPhoneDN2,txtSoMon,txtTenMon,txtKhauVi,txtTraiCay,txtMaxMarket,labelSonguoi;
    LinearLayout linCall,linChat,linMapDirection,linDongngiep;
    RelativeLayout relSobuoicon,relDientich,relDN1,relDN2,relNauAn,relMaxMarket;
    View line1,line2;
    ImageView node2,node3;
    Button btnConfirmPay,btnBaoNghi;
    Connection connect;
    Integer paymentStatus,sobuoiDalam;
    String account,accountName;

    CircleImageView avatar_customer,avatarDN1,avatarDN2;

    SwipeRefreshLayout swipeRefreshLayout;

    final private String FCM_API = "https://fcm.googleapis.com/fcm/send";
    final private String serverKey = "key=" + "AAAAZNB3xoU:APA91bGxISuo_YVJ7-142Aua8xMYvafuhaZvNIf01IeOzVrZ1hEypTqdP53X3pMZg_Mx3XkkVJOdiiDCMnHp00ytrTJxLDaozcdVpEXc1AsciThWq5ZkiDOHswqSHsLEskoVXOpC8SZC";
    final private String contentType = "application/json";
    final String TAG = "NOTIFICATION TAG";
    String NOTIFICATION_TITLE;
    String NOTIFICATION_MESSAGE;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_submit_order);

        anhXa();
        idOrder = getIntent().getIntExtra("idOrder",0);
        orderType = getIntent().getStringExtra("orderType");
        swipeRefreshLayout = findViewById(R.id.swipe_detail);

        //anh xa
        Toolbar toolbar = findViewById(R.id.toolbar_detail);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        //lay so dien thoai da luu khi dang nhap
        SharedPreferences SP = this.getSharedPreferences("PHONE",0);
        account = SP.getString("phone_num",null);

        try
        {
            com.example.coosinstaff.connection.ConnectionDB conStr=new com.example.coosinstaff.connection.ConnectionDB();
            connect =conStr.CONN();        // Connect to database
            if (connect == null)
            {
                checkConnectDialog();
            }
            else
            {
                // Change below query according to your own database.
                String query = "select * from EMPLOYEE WHERE PHONE_NUM='"+account+"'";
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

        todo();

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                todo();
                swipeRefreshLayout.setRefreshing(false);
            }
        });



    }

    public void todo(){
        relSobuoicon.setVisibility(View.GONE);
        relDientich.setVisibility(View.GONE);
        linDongngiep.setVisibility(View.GONE);
        relDN2.setVisibility(View.GONE);
        txtListDongnhgiep.setVisibility(View.GONE);
        relNauAn.setVisibility(View.GONE);
        relMaxMarket.setVisibility(View.GONE);

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
                LoadingDialog loadingDialog = new LoadingDialog();
                loadingDialog.loading(SubmitOrderActivity.this);
                Intent intent = new Intent(SubmitOrderActivity.this,MapDirectionActivity.class);
                intent.putExtra("idOrder",idOrder);
                intent.putExtra("orderType",orderType);
                startActivity(intent);
            }
        });

        PushDownAnim.setPushDownAnimTo(linCall).setOnClickListener(new View.OnClickListener() {
            @SuppressLint("MissingPermission")
            @Override
            public void onClick(View view) {
                String s = "tel:" + userOrder;
                Intent intent = new Intent(Intent.ACTION_CALL);
                intent.setData(Uri.parse(s));
                startActivity(intent);
            }
        });

        PushDownAnim.setPushDownAnimTo(txtListDongnhgiep).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (linDongngiep.getVisibility()==View.GONE){
                    linDongngiep.setVisibility(View.VISIBLE);
                }else linDongngiep.setVisibility(View.GONE);
                if (userSubmit.trim().equals(account)){
                    try {
                        com.example.coosinstaff.connection.ConnectionDB conStr=new com.example.coosinstaff.connection.ConnectionDB();
                        connect =conStr.CONN();
                        // Connect to database
                        if (connect == null){checkConnectDialog();}
                        else {
                            // Change below query according to your own database.
                            String query = "select * from EMPLOYEE where PHONE_NUM= '" + userSubmit2  + "'";
                            Statement stmt = connect.createStatement();
                            ResultSet rs = stmt.executeQuery(query);
                            if (rs.next()){
                                txtNameDN1.setText(rs.getString("FULL_NAME"));
                                txtNameDN1.setTextColor(getResources().getColor(R.color.enable));
                                txtPhoneDN1.setText(userSubmit2);
                                avatarDN1.setImageResource(R.drawable.ic_account_circle_black_24dp);
                            }
                            String query1 = "select * from EMPLOYEE where PHONE_NUM= '" + userSubmit3  + "'";
                            Statement stmt1 = connect.createStatement();
                            ResultSet rs1 = stmt1.executeQuery(query1);
                            if (rs1.next()){
                                txtNameDN2.setText(rs1.getString("FULL_NAME"));
                                txtNameDN2.setTextColor(getResources().getColor(R.color.enable));
                                txtPhoneDN2.setText(userSubmit3);
                                avatarDN2.setImageResource(R.drawable.ic_account_circle_black_24dp);
                            }
                        }
                        connect.close();
                    }
                    catch (Exception ex){
                        Log.d("BBB",ex.getMessage()+"1");
                    }
                }else if (userSubmit2.trim().equals(account)){
                    try {
                        com.example.coosinstaff.connection.ConnectionDB conStr=new com.example.coosinstaff.connection.ConnectionDB();
                        connect =conStr.CONN();
                        // Connect to database
                        if (connect == null){checkConnectDialog();}
                        else {
                            // Change below query according to your own database.
                            String query = "select * from EMPLOYEE where PHONE_NUM= '" + userSubmit  + "'";
                            Statement stmt = connect.createStatement();
                            ResultSet rs = stmt.executeQuery(query);
                            if (rs.next()){
                                txtNameDN1.setText(rs.getString("FULL_NAME"));
                                txtNameDN1.setTextColor(getResources().getColor(R.color.enable));
                                txtPhoneDN1.setText(userSubmit);
                                avatarDN1.setImageResource(R.drawable.ic_account_circle_black_24dp);
                            }
                            String query1 = "select * from EMPLOYEE where PHONE_NUM= '" + userSubmit3  + "'";
                            Statement stmt1 = connect.createStatement();
                            ResultSet rs1 = stmt1.executeQuery(query1);
                            if (rs1.next()){
                                txtNameDN2.setText(rs1.getString("FULL_NAME"));
                                txtNameDN2.setTextColor(getResources().getColor(R.color.enable));
                                txtPhoneDN2.setText(userSubmit3);
                                avatarDN2.setImageResource(R.drawable.ic_account_circle_black_24dp);
                            }
                        }
                        connect.close();
                    }
                    catch (Exception ex){
                        Log.d("BBB",ex.getMessage()+"2");
                    }
                }else if (userSubmit3.trim().equals(account)){
                    try {
                        com.example.coosinstaff.connection.ConnectionDB conStr=new com.example.coosinstaff.connection.ConnectionDB();
                        connect =conStr.CONN();
                        // Connect to database
                        if (connect == null){checkConnectDialog();}
                        else {
                            // Change below query according to your own database.
                            String query = "select * from EMPLOYEE where PHONE_NUM= '" + userSubmit  + "'";
                            Statement stmt = connect.createStatement();
                            ResultSet rs = stmt.executeQuery(query);
                            if (rs.next()){
                                txtNameDN1.setText(rs.getString("FULL_NAME"));
                                txtNameDN1.setTextColor(getResources().getColor(R.color.enable));
                                txtPhoneDN1.setText(userSubmit);
                                avatarDN1.setImageResource(R.drawable.ic_account_circle_black_24dp);
                            }
                            String query1 = "select * from EMPLOYEE where PHONE_NUM= '" + userSubmit2  + "'";
                            Statement stmt1 = connect.createStatement();
                            ResultSet rs1 = stmt1.executeQuery(query1);
                            if (rs1.next()){
                                txtNameDN2.setText(rs1.getString("FULL_NAME"));
                                txtNameDN2.setTextColor(getResources().getColor(R.color.enable));
                                txtPhoneDN2.setText(userSubmit2);
                                avatarDN2.setImageResource(R.drawable.ic_account_circle_black_24dp);
                            }
                        }
                        connect.close();
                    }
                    catch (Exception ex){
                        Log.d("BBB",ex.getMessage()+"3");
                    }

                }
                DatabaseReference reference1 = FirebaseDatabase.getInstance().getReference("avatars");
                reference1.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                            AccountAvatar accountAvatar = snapshot.getValue(AccountAvatar.class);
                            if (!txtPhoneDN1.getText().toString().trim().equals("")){
                                if (accountAvatar.getAcountPhone().equals(txtPhoneDN1.getText().toString())){
                                    Picasso.get().load(accountAvatar.getImageUrl()).into(avatarDN1);
                                }
                            }
                            if (!txtPhoneDN2.getText().toString().trim().equals("")){
                                if (accountAvatar.getAcountPhone().equals(txtPhoneDN2.getText().toString())){
                                    Picasso.get().load(accountAvatar.getImageUrl()).into(avatarDN2);
                                }
                            }
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                    }
                });
            }
        });

        PushDownAnim.setPushDownAnimTo(btnBaoNghi).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DateFormat formatter = new SimpleDateFormat("HH:mm dd/MM/yyyy");
                Date date = new Date();
                String today = formatter.format(date);
                String timecancelString = today.substring(0,2);
                String timeValidString = txtGioCa.getText().toString().substring(0,2);
                if (timecancelString.substring(1,2).equals(":")){
                    timecancelString= timecancelString.substring(0,1);
                }
                if (timeValidString.substring(1,2).equals(":")){
                    timeValidString = timeValidString.substring(0,1);
                }
                Integer timecancel = Integer.valueOf(timecancelString);
                Integer timeValid = Integer.valueOf(timeValidString);
                if (txtNgay.getText().toString().substring(0,4).equals(today.substring(6,11))
                        && timeValid-timecancel<4){
                    checkCancel();
                }else {
                    baoNghiDialog();
                }
            }
        });

        PushDownAnim.setPushDownAnimTo(linChat).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SubmitOrderActivity.this,ChatActivity.class);
                intent.putExtra("receiver",userOrder);
                startActivity(intent);
            }
        });

        DatabaseReference reference1 = FirebaseDatabase.getInstance().getReference("avatars");
        reference1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    AccountAvatar accountAvatar = snapshot.getValue(AccountAvatar.class);
                    if (accountAvatar.getAcountPhone().equals(userOrder)){
                        Picasso.get().load(accountAvatar.getImageUrl()).into(avatar_customer);
                    }
                    if (orderType.trim().equals("Tổng vệ sinh")){
                        if (!txtPhoneDN1.getText().toString().trim().equals("")){
                            if (accountAvatar.getAcountPhone().equals(txtPhoneDN1.getText().toString())){
                                Picasso.get().load(accountAvatar.getImageUrl()).into(avatarDN1);
                            }
                        }
                        if (!txtPhoneDN2.getText().toString().trim().equals("")){
                            if (accountAvatar.getAcountPhone().equals(txtPhoneDN2.getText().toString())){
                                Picasso.get().load(accountAvatar.getImageUrl()).into(avatarDN2);
                            }
                        }
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

    }

    //toolbar back button
    @Override
    public boolean onSupportNavigateUp() {
        LoadingDialog loadingDialog = new LoadingDialog();
        loadingDialog.loading(this);
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
        relSobuoicon =findViewById(R.id.rel_sobuoicon);
        txtSobuoicon = findViewById(R.id.txt_sobuoicon);
        txtDientich = findViewById(R.id.txt_dientich);
        relDientich = findViewById(R.id.rel_dientich);
        linDongngiep = findViewById(R.id.lin_dongnghiep);
        txtListDongnhgiep = findViewById(R.id.list_dongnghiep);
        avatarDN1 = findViewById(R.id.avatar_dongnghiep1);
        avatarDN2 = findViewById(R.id.avatar_dongnghiep2);
        avatar_customer = findViewById(R.id.avatar_customer);
        txtNameDN1 = findViewById(R.id.txt_name_dn1);
        txtNameDN2 = findViewById(R.id.txt_name_dn2);
        txtPhoneDN1 = findViewById(R.id.txt_phone_dn1);
        txtPhoneDN2 = findViewById(R.id.txt_phone_dn2);
        relDN2 = findViewById(R.id.rel_dn2);
        relNauAn = findViewById(R.id.rel_nauan);
        relMaxMarket =  findViewById(R.id.rel_max_market);
        txtSoMon = findViewById(R.id.txt_somon);
        txtTenMon = findViewById(R.id.txt_ten_mon);
        txtKhauVi = findViewById(R.id.txt_khauvi);
        txtTraiCay = findViewById(R.id.txt_traicay);
        txtMaxMarket = findViewById(R.id.txt_max_market);
        labelSonguoi = findViewById(R.id.labelGio_to_labelSonguoi);
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
                    //notification
                    NOTIFICATION_TITLE = "CoOsin thanh toán ["+userOrder+"]";
                    NOTIFICATION_MESSAGE ="Ca làm "+txtMDH.getText().toString()+" đã được thanh toán";

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
                    Toast.makeText(getApplicationContext(), "Xác nhận thành công", Toast.LENGTH_LONG).show();
                    txtPayStatus.setBackgroundResource(R.drawable.bg_text);
                }
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void checkOrderType() {
        if (orderType.trim().equals("Dùng lẻ")) {
            try {
                com.example.coosinstaff.connection.ConnectionDB conStr = new com.example.coosinstaff.connection.ConnectionDB();
                connect = conStr.CONN();
                // Connect to database
                if (connect == null) {
                    checkConnectDialog();
                } else {
                    // Change below query according to your own database.
                    String query = "select * from ORDER_SINGLE where ID= '" + idOrder + "'";
                    Statement stmt = connect.createStatement();
                    ResultSet rs = stmt.executeQuery(query);
                    if (rs.next()) {
                        txtOrderType.setText(orderType);
                        txtThu.setText(rs.getString("DATE_WORK").substring(0, rs.getString("DATE_WORK").length() - 12));
                        txtNgay.setText(rs.getString("DATE_WORK").substring(rs.getString("DATE_WORK").length() - 10));
                        txtCa.setText(rs.getString("TIME_WORK").substring(0, rs.getString("TIME_WORK").length() - 15));
                        txtGioCa.setText(rs.getString("TIME_WORK").substring(rs.getString("TIME_WORK").length() - 13, rs.getString("TIME_WORK").length() - 1));
                        if (txtGioCa.getText().toString().substring(0, 1).equals("(")) {
                            txtGioCa.setText(rs.getString("TIME_WORK").substring(rs.getString("TIME_WORK").length() - 14, rs.getString("TIME_WORK").length() - 1));
                        }
                        txtAddress.setText(rs.getString("ADDRESS_ORDER"));
                        txtSoGio.setText(rs.getString("TOTAL_TIME"));
                        DecimalFormat decimalFormat = new DecimalFormat("#,###");
                        String totalGiaString = decimalFormat.format(rs.getInt("TOTAL_PRICE"));
                        txtTongTien.setText(totalGiaString + "đ");
                        txtPaymentType.setText(rs.getString("PAYMENT_TYPE"));
                        orderStatus = rs.getString("STATUS_ORDER");
                        userOrder = rs.getString("USER_ORDER");
                        paymentStatus = rs.getInt("PAYMENT_STATUS");
                        txtMDH.setText("MĐH: DL" + idOrder);
                    }
                }
                connect.close();
            } catch (Exception ex) {
                Toast.makeText(getApplicationContext(), ex.getMessage(), Toast.LENGTH_LONG).show();
            }
        } else if (orderType.trim().equals("Định kỳ")) {
            try {
                com.example.coosinstaff.connection.ConnectionDB conStr = new com.example.coosinstaff.connection.ConnectionDB();
                connect = conStr.CONN();
                // Connect to database
                if (connect == null) {
                    checkConnectDialog();
                } else {
                    // Change below query according to your own database.
                    String query = "select * from ORDER_MULTI where ID= '" + idOrder + "'";
                    Statement stmt = connect.createStatement();
                    ResultSet rs = stmt.executeQuery(query);
                    if (rs.next()) {
                        txtOrderType.setText(orderType);
                        txtThu.setText(rs.getString("SCHEDULE"));
                        txtNgay.setText(rs.getString("DATE_START") + "\n" + rs.getString("DATE_END"));
                        txtCa.setText(rs.getString("TIME_WORK").substring(0, rs.getString("TIME_WORK").length() - 15));
                        txtGioCa.setText(rs.getString("TIME_WORK").substring(rs.getString("TIME_WORK").length() - 13, rs.getString("TIME_WORK").length() - 1));
                        if (txtGioCa.getText().toString().substring(0, 1).equals("(")) {
                            txtGioCa.setText(rs.getString("TIME_WORK").substring(rs.getString("TIME_WORK").length() - 14, rs.getString("TIME_WORK").length() - 1));
                        }
                        txtAddress.setText(rs.getString("ADDRESS_ORDER"));
                        txtSoGio.setText(rs.getString("TOTAL_TIME"));
                        DecimalFormat decimalFormat = new DecimalFormat("#,###");
                        String totalGiaString = decimalFormat.format(rs.getInt("TOTAL_PRICE"));
                        txtTongTien.setText(totalGiaString + "đ");
                        txtPaymentType.setText(rs.getString("PAYMENT_TYPE"));
                        orderStatus = rs.getString("ORDER_STATUS");
                        txtOrderType.setText(orderType);
                        userOrder = rs.getString("USER_ORDER");
                        paymentStatus = rs.getInt("PAYMENT_STATUS");
                        int sobuoi = rs.getInt("TOTAL_BUOI");
                        sobuoiDalam = rs.getInt("TOTAL_WORKED");
                        int sobuoicon = sobuoi - sobuoiDalam;
                        txtSobuoicon.setText(sobuoicon + " buổi");
                        txtSoBuoi.setText(sobuoi + " buổi");
                        relSobuoicon.setVisibility(View.VISIBLE);
                        txtMDH.setText("MĐH: DK" + idOrder);
                        connect.close();
                    }
                }
                connect.close();
            } catch (Exception ex) {
                Toast.makeText(getApplicationContext(), ex.getMessage(), Toast.LENGTH_LONG).show();
            }
        } else if (orderType.trim().equals("Tổng vệ sinh")) {
            try
            {   txtListDongnhgiep.setVisibility(View.VISIBLE);
                com.example.coosinstaff.connection.ConnectionDB conStr = new com.example.coosinstaff.connection.ConnectionDB();
                connect = conStr.CONN();
                if (connect == null)
                {
                    checkConnectDialog();
                }else{
                    // Change below query according to your own database.
                    String query = "select * from ORDER_OVERVIEW where ID= '" + idOrder  + "'";
                    Statement stmt = connect.createStatement();
                    ResultSet rs = stmt.executeQuery(query);
                    if (rs.next())
                    {
                        txtOrderType.setText(orderType);
                        txtThu.setText(rs.getString("DATE_WORK").substring(0,rs.getString("DATE_WORK").length()-12));
                        txtNgay.setText(rs.getString("DATE_WORK").substring(rs.getString("DATE_WORK").length()-10));
                        txtGioCa.setText(rs.getString("TIME_START"));
                        txtCa.setText("Giờ bắt đầu");
                        txtAddress.setText(rs.getString("ADDRESS_ORDER"));
                        if (rs.getInt("AREA_TYPE")==2){
                            txtSoGio.setText("3h");
                        }else txtSoGio.setText("4h");
                        relDientich.setVisibility(View.VISIBLE);
                        if (rs.getInt("AREA_TYPE")==1){
                            txtDientich.setText("80m\u00B2");
                        }else if (rs.getInt("AREA_TYPE")==2){
                            txtDientich.setText("100m\u00B2");
                            relDN2.setVisibility(View.VISIBLE);
                        }else {
                            txtDientich.setText("150m\u00B2");
                            relDN2.setVisibility(View.VISIBLE);
                        }
                        DecimalFormat decimalFormat = new DecimalFormat("#,###");
                        String totalGiaString = decimalFormat.format(rs.getInt("TOTAL_PRICE"));
                        txtTongTien.setText(totalGiaString+"đ");
                        txtPaymentType.setText(rs.getString("PAYMENT_TYPE"));
                        orderStatus = rs.getString("ORDER_STATUS");
                        userSubmit = rs.getString("USER_SUBMIT1");
                        userSubmit2 = rs.getString("USER_SUBMIT2");
                        userOrder = rs.getString("USER_ORDER");
                        userSubmit3 = rs.getString("USER_SUBMIT3");
                        txtOrderType.setText(orderType);
                        paymentStatus=rs.getInt("PAYMENT_STATUS");
                        txtMDH.setText("MĐH: TVS"+idOrder);
                        connect.close();
                    }
                }
            }
            catch (Exception ex)
            {
                Toast.makeText(getApplicationContext(), ex.getMessage(), Toast.LENGTH_LONG).show();
            }
        }else {
            try
            {
                com.example.coosinstaff.connection.ConnectionDB conStr = new com.example.coosinstaff.connection.ConnectionDB();
                connect = conStr.CONN();
                relNauAn.setVisibility(View.VISIBLE);
                // Connect to database
                if (connect == null)
                {
                    checkConnectDialog();
                }
                else
                {
                    // Change below query according to your own database.
                    String query = "select * from ORDER_COOK where ID='"+idOrder+"'";
                    Statement stmt = connect.createStatement();
                    ResultSet rs = stmt.executeQuery(query);
                    if (rs.next())
                    {
                        txtOrderType.setText(orderType);
                        txtThu.setText(rs.getString("DATE_WORK").substring(0,rs.getString("DATE_WORK").length()-12));
                        txtNgay.setText(rs.getString("DATE_WORK").substring(rs.getString("DATE_WORK").length()-10));
                        txtCa.setText("Giờ ăn");
                        txtGioCa.setText(rs.getString("TIME_WORK"));
                        txtAddress.setText(rs.getString("ADDRESS_ORDER"));
                        txtSoGio.setText(rs.getString("PEOPLE_AMOUNT")+" người");
                        labelSonguoi.setText("Số người ăn");
                        DecimalFormat decimalFormat = new DecimalFormat("#,###");
                        String totalGiaString = decimalFormat.format(rs.getInt("TOTAL_PRICE"));
                        if (rs.getInt("MAX_MARKET_PRICE")!=0){
                            String maxMarketString = decimalFormat.format(rs.getInt("MAX_MARKET_PRICE"));
                            relMaxMarket.setVisibility(View.VISIBLE);
                            txtMaxMarket.setText(maxMarketString+"đ");
                        }
                        txtTraiCay.setText(rs.getString("FRUIT"));
                        txtKhauVi.setText(rs.getString("TASTE"));
                        txtSoMon.setText(rs.getString("DISH_AMOUNT")+" món");
                        txtTenMon.setText(rs.getString("DISH_NAME"));
                        txtTongTien.setText(totalGiaString+"đ");
                        txtPaymentType.setText(rs.getString("PAYMENT_TYPE"));
                        orderStatus = rs.getString("ORDER_STATUS");
                        userOrder = rs.getString("USER_ORDER");
                        userSubmit = rs.getString("USER_SUBMIT");
                        txtOrderType.setText(orderType);
                        paymentStatus=rs.getInt("PAYMENT_STATUS");
                        txtMDH.setText("MĐH: NA"+idOrder);
                        connect.close();
                    }
                }
            }
            catch (Exception ex)
            {
                Log.d("BBB",ex.getMessage());
            }
        }
    }

    private void checkCancel(){
        TextView title = new TextView(this);
        title.setText("THÔNG BÁO");
        title.setPadding(10, 30, 10, 10);
        title.setGravity(Gravity.CENTER);
        title.setTextSize(20);
        title.setTextColor(Color.RED);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCustomTitle(title);
        builder.setMessage("Ca làm sắp bắt đầu, bạn không thể hủy lúc này.\nVui lòng thực hiện hủy trước 4 tiếng khi ca làm bắt đầu !");
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

    private void baoNghiDialog(){

        TextView title = new TextView(this);
        title.setText("THÔNG BÁO");
        title.setPadding(10, 30, 10, 10);
        title.setGravity(Gravity.CENTER);
        title.setTextSize(20);
        title.setTextColor(Color.RED);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCustomTitle(title);
        builder.setMessage("Nếu hủy bạn sẽ mất cơ hội việc làm. Bạn có muốn tiếp tục ?");
        builder.setCancelable(false);
        builder.setNegativeButton("Tiếp tục", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (orderType.trim().equals("Dùng lẻ") || orderType.trim().equals("Nấu ăn")){
                    try {
                        com.example.coosinstaff.connection.ConnectionDB conStr=new com.example.coosinstaff.connection.ConnectionDB();
                        connect =conStr.CONN();
                        // Connect to database
                        if (connect == null){checkConnectDialog();}
                        else {
                            if (orderType.trim().equals("Dùng lẻ")){
                                String query = "UPDATE ORDER_SINGLE SET USER_SUBMIT='', DATE_SUBMIT='', STATUS_ORDER=N'Đang tìm kiếm NV' WHERE ID="+idOrder+"";
                                Statement stmt = connect.createStatement();
                                stmt.executeQuery(query);
                                finish();
                            }else {
                                String query = "UPDATE ORDER_COOK SET USER_SUBMIT='', DATE_SUBMIT='', STATUS_ORDER=N'Đang tìm kiếm NV' WHERE ID="+idOrder+"";
                                Statement stmt = connect.createStatement();
                                stmt.executeQuery(query);
                                finish();
                            }
                        }
                        connect.close();
                    }
                    catch (Exception ex){

                    }
                }else if (orderType.trim().equals("Định kỳ")){
                    checkCancel();
                }else {
                    try {
                        com.example.coosinstaff.connection.ConnectionDB conStr=new com.example.coosinstaff.connection.ConnectionDB();
                        connect =conStr.CONN();
                        // Connect to database
                        if (connect == null){checkConnectDialog();}
                        else {
                            if (account.trim().equals(userSubmit)){
                                String query = "UPDATE ORDER_OVERVIEW SET USER_SUBMIT1='"+userSubmit2+"', USER_SUBMIT2='"+userSubmit3+"',USER_SUBMIT3='' WHERE ID="+idOrder+"";
                                Statement stmt = connect.createStatement();
                                stmt.executeQuery(query);
                            }else if (account.trim().equals(userSubmit2)){
                                String query = "UPDATE ORDER_OVERVIEW SET USER_SUBMIT2='"+userSubmit3+"',USER_SUBMIT3='' WHERE ID="+idOrder+"";
                                Statement stmt = connect.createStatement();
                                stmt.executeQuery(query);
                            }else {
                                String query = "UPDATE ORDER_OVERVIEW SET USER_SUBMIT3='' WHERE ID="+idOrder+"";
                                Statement stmt = connect.createStatement();
                                stmt.executeQuery(query);
                            }
                        }
                        connect.close();
                    }
                    catch (Exception ex){

                    }
                }
                //notification
                NOTIFICATION_TITLE = "CoOsin NV báo nghỉ đến ["+userOrder+"]";
                NOTIFICATION_MESSAGE ="Nhân viên "+accountName+" đã báo nghỉ ca làm "+txtMDH.getText().toString();

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
        });
        builder.setPositiveButton("Không", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();

        TextView messageText = alertDialog.findViewById(android.R.id.message);
        messageText.setGravity(Gravity.CENTER);
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
                        Toast.makeText(SubmitOrderActivity.this, "Request error", Toast.LENGTH_LONG).show();
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
