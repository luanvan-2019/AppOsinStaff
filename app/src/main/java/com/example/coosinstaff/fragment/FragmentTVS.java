package com.example.coosinstaff.fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.coosinstaff.HomeActivity;
import com.example.coosinstaff.R;
import com.example.coosinstaff.SubmitOrderActivity;
import com.example.coosinstaff.adapter.ListAdapter;
import com.example.coosinstaff.adapter.ListAdapterTVS;
import com.example.coosinstaff.model.ListOrder;
import com.example.coosinstaff.model.ListOrderTVS;
import com.example.coosinstaff.model.OnItemClickListener;
import com.example.coosinstaff.notifications.MySingleton;

import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class FragmentTVS extends Fragment {

    View view;
    RecyclerView recyclerView;
    ArrayList<ListOrderTVS> listOrders;
    ListAdapterTVS orderListAdapter;
    String account;
    Integer account_status;
    Connection connect;

    ArrayList<String> cusPhone = new ArrayList<String>();

    ArrayList<String> address = new ArrayList<String>();
    ArrayList<String> userSubmitAmount = new ArrayList<String>();
    ArrayList<String> date = new ArrayList<String>();
    ArrayList<String> time = new ArrayList<String>();
    ArrayList<String> create_at = new ArrayList<String>();
    ArrayList<Integer> price = new ArrayList<Integer>();
    ArrayList<Integer> seen = new ArrayList<Integer>();
    ArrayList<Integer> area = new ArrayList<Integer>();
    ArrayList<Integer> id = new ArrayList<Integer>();
    String[] addressArr,dateArr,timeArr,create_atArr,userSubmitAmountArr,cusPhoneArr;
    Integer[] priceArr,seenArr,areaArr,idArr;
    String arena_type,accountName;

    SwipeRefreshLayout swipeRefreshLayout;

    final private String FCM_API = "https://fcm.googleapis.com/fcm/send";
    final private String serverKey = "key=" + "AAAAZNB3xoU:APA91bGxISuo_YVJ7-142Aua8xMYvafuhaZvNIf01IeOzVrZ1hEypTqdP53X3pMZg_Mx3XkkVJOdiiDCMnHp00ytrTJxLDaozcdVpEXc1AsciThWq5ZkiDOHswqSHsLEskoVXOpC8SZC";
    final private String contentType = "application/json";
    final String TAG = "NOTIFICATION TAG";
    String NOTIFICATION_TITLE;
    String NOTIFICATION_MESSAGE;

    public FragmentTVS() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.tvs_fragment,container,false);
        recyclerView = view.findViewById(R.id.recyclerview_tvs);
        swipeRefreshLayout = view.findViewById(R.id.swipe_tvs);

        //lay so dien thoai da luu khi dang nhap
        SharedPreferences SP = getContext().getSharedPreferences("PHONE",0);
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
            Toast.makeText(getContext(), ex.getMessage(), Toast.LENGTH_LONG).show();
        }

        todo();

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                todo();
                swipeRefreshLayout.setRefreshing(false);
            }
        });

        return view;
    }

    public void todo(){
        listOrders = new ArrayList<>();
        address.clear();
        date.clear();
        time.clear();
        price.clear();
        create_at.clear();
        seen.clear();
        area.clear();
        userSubmitAmount.clear();

        try
        {
            com.example.coosinstaff.connection.ConnectionDB conStr=new com.example.coosinstaff.connection.ConnectionDB();
            connect =conStr.CONN();        // Connect to database
            if (connect == null)
            {
                Toast.makeText(getContext(), "Không có kết nối mạng!", Toast.LENGTH_LONG).show();
            }
            else
            {
                // Change below query according to your own database.
                String query = "select * from ORDER_OVERVIEW where USER_SUBMIT1 !='"+account+"' AND USER_SUBMIT2 !='"+account+"' AND USER_SUBMIT3!='"+account+"'" +
                        " OR AREA_TYPE=1 AND USER_SUBMIT2='' AND AREA_TYPE=2 AND USER_SUBMIT3='' ORDER BY ID DESC";
                Statement stmt = connect.createStatement();
                ResultSet rs = stmt.executeQuery(query);
                while (rs.next())
                {
                    if (rs.getInt("AREA_TYPE")==1){
                        if (rs.getString("USER_SUBMIT1").trim().equals("")){
                            userSubmitAmount.add("0/2 đã nhận");
                        }else if (!rs.getString("USER_SUBMIT1").trim().equals("") && rs.getString("USER_SUBMIT2").trim().equals("")) {
                            userSubmitAmount.add("1/2 đã nhận");
                        }
                    }else {
                        if (rs.getString("USER_SUBMIT1").trim().equals("")){
                            userSubmitAmount.add("0/3 đã nhận");
                        }else if (!rs.getString("USER_SUBMIT1").trim().equals("") && rs.getString("USER_SUBMIT2").trim().equals("")) {
                            userSubmitAmount.add("1/3 đã nhận");
                        }else if (!rs.getString("USER_SUBMIT1").trim().equals("") && !rs.getString("USER_SUBMIT2").trim().equals("")
                                && rs.getString("USER_SUBMIT3").trim().equals("")) {
                            userSubmitAmount.add("2/3 đã nhận");
                        }
                    }
                    address.add(rs.getString("ADDRESS_ORDER"));
                    date.add(rs.getString("DATE_WORK"));
                    time.add(rs.getString("TIME_START"));
                    area.add(rs.getInt("AREA_TYPE"));
                    price.add(rs.getInt("TOTAL_PRICE"));
                    create_at.add(rs.getString("CREATE_AT"));
                    seen.add(rs.getInt("SEEN"));
                    id.add(rs.getInt("ID"));
                    cusPhone.add(rs.getString("USER_ORDER"));

                }
                cusPhoneArr = new String[cusPhone.size()];
                cusPhoneArr = cusPhone.toArray(cusPhoneArr);

                addressArr = new String[address.size()];
                addressArr = address.toArray(addressArr);
                dateArr = new String[date.size()];
                dateArr = date.toArray(dateArr);
                timeArr = new String[time.size()];
                timeArr = time.toArray(timeArr);
                areaArr = new Integer[area.size()];
                areaArr = area.toArray(areaArr);
                priceArr = new Integer[price.size()];
                priceArr = price.toArray(priceArr);
                create_atArr = new String[create_at.size()];
                create_atArr = create_at.toArray(create_atArr);
                seenArr = new Integer[seen.size()];
                seenArr = seen.toArray(seenArr);
                userSubmitAmountArr = new String[userSubmitAmount.size()];
                userSubmitAmountArr = userSubmitAmount.toArray(userSubmitAmountArr);
                idArr = new Integer[id.size()];
                idArr = id.toArray(idArr);
                for (int i = 0; i < address.size();i++){
                    if (areaArr[i] == 1){
                        arena_type = "80m\u00B2 2 người 4h";
                    }else if (areaArr[i] == 2){
                        arena_type = "100m\u00B2 3 người 3h";
                    }else arena_type = "150m\u00B2 3 người 4h";
                    listOrders.add(new ListOrderTVS("Tổng vệ sinh",priceArr[i],addressArr[i],"2km",dateArr[i],timeArr[i],arena_type,
                            create_atArr[i],seenArr[i],userSubmitAmountArr[i]));
                }
                //check account
                String query1 = "select * from EMPLOYEE where PHONE_NUM='"+account+"'";
                Statement stmt1 = connect.createStatement();
                ResultSet rs1 = stmt1.executeQuery(query1);
                if (rs1.next()){
                    account_status = rs1.getInt("USER_STATUS");
                }
                connect.close();
            }
        }
        catch (Exception ex)
        {
            Toast.makeText(getContext(), ex.getMessage(), Toast.LENGTH_LONG).show();
        }
        orderListAdapter = new ListAdapterTVS(listOrders);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getActivity()));
        recyclerView.setAdapter(orderListAdapter);

        if (recyclerView.getAdapter() != null) {
            ((ListAdapterTVS) recyclerView.getAdapter()).setOnItemClickListener(new OnItemClickListener() {
                @Override
                public void onClick(View v, @NonNull int position) {
                    if (account_status != 2){
                        checkAccount();
                    }else {
                        checkAvailable(idArr[position]);

                        try
                        {
                            com.example.coosinstaff.connection.ConnectionDB conStr=new com.example.coosinstaff.connection.ConnectionDB();
                            connect =conStr.CONN();        // Connect to database
                            if (connect == null){checkConnectDialog();return;}
                            String query = "select * from ORDER_OVERVIEW where ID=" + idArr[position] + "";
                            Statement stmt = connect.createStatement();
                            ResultSet rs = stmt.executeQuery(query);
                            if (rs.next()) {
                                if (rs.getInt("AREA_TYPE")==1){
                                    if (rs.getString("USER_SUBMIT1").trim().equals("")){
                                        String query1 = "UPDATE ORDER_OVERVIEW SET USER_SUBMIT1='"+account+"'," +
                                                "ORDER_STATUS=N'Đã tìm được NV' WHERE ID="+idArr[position]+"";
                                        Statement stmt1 = connect.createStatement();
                                        stmt1.executeQuery(query1);
                                    }else if (!rs.getString("USER_SUBMIT1").trim().equals("")){
                                        String query1 = "UPDATE ORDER_OVERVIEW SET USER_SUBMIT2='"+account+"' WHERE ID="+idArr[position]+"";
                                        Statement stmt1 = connect.createStatement();
                                        stmt1.executeQuery(query1);
                                    }
                                }else {
                                    if (rs.getString("USER_SUBMIT1").trim().equals("")){
                                        String query1 = "UPDATE ORDER_OVERVIEW SET USER_SUBMIT1='"+account+"'," +
                                                "ORDER_STATUS=N'Đã tìm được NV' WHERE ID="+idArr[position]+"";
                                        Statement stmt1 = connect.createStatement();
                                        stmt1.executeQuery(query1);
                                    }else if (!rs.getString("USER_SUBMIT1").trim().equals("") && rs.getString("USER_SUBMIT2").trim().equals("")){
                                        String query1 = "UPDATE ORDER_OVERVIEW SET USER_SUBMIT2='"+account+"' WHERE ID="+idArr[position]+"";
                                        Statement stmt1 = connect.createStatement();
                                        stmt1.executeQuery(query1);
                                    }else {
                                        String query1 = "UPDATE ORDER_OVERVIEW SET USER_SUBMIT3='"+account+"' WHERE ID="+idArr[position]+"";
                                        Statement stmt1 = connect.createStatement();
                                        stmt1.executeQuery(query1);
                                    }
                                }
                            }
                            connect.close();
                        }
                        catch (Exception ex)
                        {
                            //notification
                            NOTIFICATION_TITLE = "CoOsin thông báo ["+cusPhoneArr[position]+"]";
                            NOTIFICATION_MESSAGE ="Nhân viên "+accountName+" đã nhận ca TVS"+idArr[position];

                            JSONObject notification = new JSONObject();
                            JSONObject notifcationBody = new JSONObject();
                            try {
                                notifcationBody.put("title", NOTIFICATION_TITLE);
                                notifcationBody.put("message", NOTIFICATION_MESSAGE);

                                notification.put("to", "/topics/nhanlich"+cusPhoneArr[position]);

                                notification.put("data", notifcationBody);
                            } catch (JSONException e) {
                                Log.e(TAG, "onCreate: " + e.getMessage());
                            }
                            sendNotification(notification);
                            Log.d("BBB",ex.getMessage());
                        }
                        Intent detail = new Intent(getActivity(), SubmitOrderActivity.class);
                        detail.putExtra("idOrder",idArr[position]);
                        detail.putExtra("orderType","Tổng vệ sinh");
                        startActivity(detail);
                    }
                }

                @Override
                public void onLongClick(View v, @NonNull int position) {

                }
            });
        }
    }

    public void checkAccount(){

        TextView title = new TextView(this.getContext());
        // You Can Customise your Title here
        title.setText("THÔNG BÁO");
        //title.setBackgroundColor(Color.DKGRAY);
        title.setPadding(10, 30, 10, 10);
        title.setGravity(Gravity.CENTER);
        title.setTextSize(20);
        title.setTextColor(Color.RED);

        AlertDialog.Builder builder = new AlertDialog.Builder(this.getContext());
        builder.setCustomTitle(title);
        builder.setMessage(R.string.checkaccount);
        builder.setCancelable(false);
        builder.setNegativeButton("Để sau", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        builder.setPositiveButton("Gọi ngay", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String s = "tel:" + "0921895314";
                Log.d("BBB",s);
                Intent intent = new Intent(Intent.ACTION_CALL);
                intent.setData(Uri.parse(s));
                startActivity(intent);
                dialogInterface.dismiss();
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
        TextView messageText = alertDialog.findViewById(android.R.id.message);
        messageText.setGravity(Gravity.CENTER);
    }

    public void checkConnectDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this.getActivity());
        builder.setTitle("Thông báo");
        builder.setMessage("Không có kết nối mạng!");
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

    private void checkAvailable(int id){
        try {
            // Change below query according to your own database.
            String query = "select * from ORDER_OVERVIEW where ID="+id+"";
            Statement stmt = connect.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            if (rs.next()){
                if (rs.getInt("AREA_TYPE")==1){
                    if (!rs.getString("USER_SUBMIT2").trim().equals("")){
                        AlertDialog.Builder builder = new AlertDialog.Builder(this.getActivity());
                        builder.setTitle("Thông báo");
                        builder.setMessage("Lịch này đã đủ người!");
                        builder.setCancelable(false);
                        builder.setNegativeButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                                Intent intent = new Intent(getActivity(), HomeActivity.class);
                                startActivity(intent);
                                getActivity().finish();
                            }
                        });
                        AlertDialog alertDialog = builder.create();
                        alertDialog.show();
                    }
                }else {
                    if (!rs.getString("USER_SUBMIT3").trim().equals("")){
                        AlertDialog.Builder builder = new AlertDialog.Builder(this.getActivity());
                        builder.setTitle("Thông báo");
                        builder.setMessage("Lịch này đã đủ người!");
                        builder.setCancelable(false);
                        builder.setNegativeButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                                Intent intent = new Intent(getActivity(), HomeActivity.class);
                                startActivity(intent);
                                getActivity().finish();
                            }
                        });
                        AlertDialog alertDialog = builder.create();
                        alertDialog.show();
                    }
                }
            }
        }
        catch (Exception e){

        }
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
                        Toast.makeText(getContext(), "Request error", Toast.LENGTH_LONG).show();
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
        MySingleton.getInstance(getContext()).addToRequestQueue(jsonObjectRequest);
    }
}