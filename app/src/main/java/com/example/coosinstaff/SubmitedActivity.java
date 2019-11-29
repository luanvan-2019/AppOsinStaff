package com.example.coosinstaff;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.coosinstaff.adapter.ListAdapter;
import com.example.coosinstaff.adapter.ListAdapterSubmited;
import com.example.coosinstaff.model.ListSubmited;
import com.example.coosinstaff.model.OnItemClickListener;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class SubmitedActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    ArrayList<ListSubmited> listSubmiteds;
    ListAdapterSubmited adapterSubmited;
    String account;
    ArrayList<String> address = new ArrayList<String>();
    ArrayList<String> orderType = new ArrayList<String>();
    ArrayList<String> date = new ArrayList<String>();
    ArrayList<String> time = new ArrayList<String>();
    ArrayList<Integer> price = new ArrayList<Integer>();
    ArrayList<Integer> id = new ArrayList<Integer>();
    String[] addressArr,dateArr,timeArr,orderTypeArr;
    Integer[] priceArr,idArr;
    Connection connect;
    TextView txtTotal;

    SwipeRefreshLayout swipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_submited);

        recyclerView = findViewById(R.id.recyclerview_submited);
        swipeRefreshLayout = findViewById(R.id.swipe_submited);
        txtTotal = findViewById(R.id.txt_total_submited);

        //back button
        Toolbar toolbar = findViewById(R.id.toolbar_submited);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        //lay so dien thoai da luu khi dang nhap
        SharedPreferences SP = this.getSharedPreferences("PHONE",0);
        account = SP.getString("phone_num",null);

        todo();

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                todo();
                swipeRefreshLayout.setRefreshing(false);
            }
        });

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

    public void todo(){
        listSubmiteds = new ArrayList<>();
        address.clear();
        date.clear();
        time.clear();
        price.clear();
        id.clear();
        orderType.clear();

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
                String query = "select * from ORDER_SINGLE where USER_SUBMIT= '" + account  + "' AND STATUS_ORDER!='Hoàn thành'";
                Statement stmt = connect.createStatement();
                ResultSet rs = stmt.executeQuery(query);
                while (rs.next())
                {
                    address.add(rs.getString("ADDRESS_ORDER"));
                    date.add(rs.getString("DATE_WORK"));
                    time.add(rs.getString("TIME_WORK"));
                    price.add(rs.getInt("TOTAL_PRICE"));
                    id.add(rs.getInt("ID"));
                    orderType.add("Dùng lẻ");
                }
                addressArr = new String[address.size()];
                addressArr = address.toArray(addressArr);
                orderTypeArr = new String[orderType.size()];
                orderTypeArr = orderType.toArray(orderTypeArr);
                dateArr = new String[date.size()];
                dateArr = date.toArray(dateArr);
                timeArr = new String[time.size()];
                timeArr = time.toArray(timeArr);
                priceArr = new Integer[price.size()];
                priceArr = price.toArray(priceArr);
                idArr = new Integer[id.size()];
                idArr = id.toArray(idArr);
                for (int i = 0; i < address.size();i++){
                    listSubmiteds.add(new ListSubmited(orderTypeArr[i],dateArr[i],timeArr[i],addressArr[i],
                            idArr[i],priceArr[i]));
                }

                //load dinh ky da nhan
                int a = address.size();
                String query1 = "select * from ORDER_MULTI where USER_SUBMIT= '" + account  + "' AND ORDER_STATUS!='Hoàn thành'";
                Statement stmt1 = connect.createStatement();
                ResultSet rs1 = stmt1.executeQuery(query1);
                while (rs1.next())
                {
                    address.add(rs1.getString("ADDRESS_ORDER"));
                    date.add(rs1.getString("DATE_START")+" - "+rs1.getString("DATE_END"));
                    time.add(rs1.getString("TIME_WORK"));
                    price.add(rs1.getInt("TOTAL_PRICE"));
                    id.add(rs1.getInt("ID"));
                    orderType.add("Định kỳ");
                }
                addressArr = new String[address.size()];
                addressArr = address.toArray(addressArr);
                orderTypeArr = new String[orderType.size()];
                orderTypeArr = orderType.toArray(orderTypeArr);
                dateArr = new String[date.size()];
                dateArr = date.toArray(dateArr);
                timeArr = new String[time.size()];
                timeArr = time.toArray(timeArr);
                priceArr = new Integer[price.size()];
                priceArr = price.toArray(priceArr);
                idArr = new Integer[id.size()];
                idArr = id.toArray(idArr);
                for (int i = a; i < address.size();i++){
                    listSubmiteds.add(new ListSubmited(orderTypeArr[i],dateArr[i],timeArr[i],addressArr[i],
                            idArr[i],priceArr[i]));
                }

                //load tong ve sinh da nhan
                int b = address.size();
                String query2 = "select * from ORDER_OVERVIEW where USER_SUBMIT1= '"+account+"' OR USER_SUBMIT2= '"+account+"' OR USER_SUBMIT3='"+account+"'" +
                        "AND ORDER_STATUS!='Hoàn thành'";
                Statement stmt2 = connect.createStatement();
                ResultSet rs2 = stmt2.executeQuery(query2);
                while (rs2.next())
                {
                    address.add(rs2.getString("ADDRESS_ORDER"));
                    date.add(rs2.getString("DATE_WORK"));
                    time.add("Bắt đầu: "+rs2.getString("TIME_START"));
                    price.add(rs2.getInt("TOTAL_PRICE"));
                    id.add(rs2.getInt("ID"));
                    orderType.add("Tổng vệ sinh");
                }
                addressArr = new String[address.size()];
                addressArr = address.toArray(addressArr);
                orderTypeArr = new String[orderType.size()];
                orderTypeArr = orderType.toArray(orderTypeArr);
                dateArr = new String[date.size()];
                dateArr = date.toArray(dateArr);
                timeArr = new String[time.size()];
                timeArr = time.toArray(timeArr);
                priceArr = new Integer[price.size()];
                priceArr = price.toArray(priceArr);
                idArr = new Integer[id.size()];
                idArr = id.toArray(idArr);
                for (int i = b; i < address.size();i++){
                    listSubmiteds.add(new ListSubmited(orderTypeArr[i],dateArr[i],timeArr[i],addressArr[i],
                            idArr[i],priceArr[i]));
                }

                //load tong ve sinh da nhan
                int c = address.size();
                String query3 = "select * from ORDER_COOK where USER_SUBMIT='"+account+"' AND ORDER_STATUS != 'Hoàn thành'";
                Statement stmt3 = connect.createStatement();
                ResultSet rs3 = stmt3.executeQuery(query3);
                while (rs3.next())
                {
                    address.add(rs3.getString("ADDRESS_ORDER"));
                    date.add(rs3.getString("DATE_WORK"));
                    time.add("Giờ ăn: "+rs3.getString("TIME_WORK"));
                    price.add(rs3.getInt("TOTAL_PRICE"));
                    id.add(rs3.getInt("ID"));
                    orderType.add("Nấu ăn");
                }
                addressArr = new String[address.size()];
                addressArr = address.toArray(addressArr);
                orderTypeArr = new String[orderType.size()];
                orderTypeArr = orderType.toArray(orderTypeArr);
                dateArr = new String[date.size()];
                dateArr = date.toArray(dateArr);
                timeArr = new String[time.size()];
                timeArr = time.toArray(timeArr);
                priceArr = new Integer[price.size()];
                priceArr = price.toArray(priceArr);
                idArr = new Integer[id.size()];
                idArr = id.toArray(idArr);
                for (int i = c; i < address.size();i++){
                    listSubmiteds.add(new ListSubmited(orderTypeArr[i],dateArr[i],timeArr[i],addressArr[i],
                            idArr[i],priceArr[i]));
                }
            }
            connect.close();
        }
        catch (Exception ex)
        {
            Log.d("BBB",ex.getMessage());
        }
        txtTotal.setText("Tổng: "+listSubmiteds.size());
        adapterSubmited = new ListAdapterSubmited(listSubmiteds);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapterSubmited);

        if (recyclerView.getAdapter() != null) {
            ((ListAdapterSubmited) recyclerView.getAdapter()).setOnItemClickListener(new OnItemClickListener() {
                @Override
                public void onClick(View v, @NonNull int position) {
                    LoadingDialog loadingDialog = new LoadingDialog();
                    loadingDialog.loading(SubmitedActivity.this);
                    Intent detail = new Intent(SubmitedActivity.this, SubmitOrderActivity.class);
                    detail.putExtra("idOrder",idArr[position]);
                    detail.putExtra("orderType",orderTypeArr[position]);
                    startActivity(detail);
                }

                @Override
                public void onLongClick(View v, @NonNull int position) {

                }
            });
        }
    }

    //back button
    @Override
    public boolean onSupportNavigateUp() {
        LoadingDialog loadingDialog = new LoadingDialog();
        loadingDialog.loading(this);
        onBackPressed();
        return true;
    }
}