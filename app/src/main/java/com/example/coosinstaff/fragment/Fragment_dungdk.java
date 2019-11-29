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

import com.example.coosinstaff.R;
import com.example.coosinstaff.SubmitOrderActivity;
import com.example.coosinstaff.adapter.ListAdapter;
import com.example.coosinstaff.adapter.ListAdapterDK;
import com.example.coosinstaff.model.ListOrderDK;
import com.example.coosinstaff.model.OnItemClickListener;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class Fragment_dungdk extends Fragment {

    View view;
    RecyclerView recyclerView;
    ArrayList<ListOrderDK> listOrders;
    ListAdapterDK orderListAdapter;
    Connection connect;

    ArrayList<String> address = new ArrayList<String>();
    ArrayList<String> dateStart = new ArrayList<String>();
    ArrayList<String> dateEnd = new ArrayList<String>();
    ArrayList<String> schedule = new ArrayList<String>();
    ArrayList<String> time = new ArrayList<String>();
    ArrayList<String> create_at = new ArrayList<String>();
    ArrayList<Integer> price = new ArrayList<Integer>();
    ArrayList<Integer> seen = new ArrayList<Integer>();
    ArrayList<Integer> id = new ArrayList<Integer>();
    String[] addressArr,timeArr,create_atArr,dateStartArr,dateEndArr,scheduleArr;
    Integer[] priceArr,seenArr,idArr;
    String account;
    Integer account_status;

    SwipeRefreshLayout swipeRefreshLayout;

    public Fragment_dungdk() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.dungdk_fragment,container,false);
        recyclerView = view.findViewById(R.id.recyclerview_dk);
        swipeRefreshLayout = view.findViewById(R.id.swipe_dinhky);

        //lay so dien thoai da luu khi dang nhap
        SharedPreferences SP = getContext().getSharedPreferences("PHONE",0);
        account = SP.getString("phone_num",null);

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
        dateStart.clear();
        dateEnd.clear();
        schedule.clear();
        time.clear();
        price.clear();
        create_at.clear();
        seen.clear();

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
                String query = "select * from ORDER_MULTI WHERE ORDER_STATUS=N'Đang tìm kiếm NV'";
                Statement stmt = connect.createStatement();
                ResultSet rs = stmt.executeQuery(query);
                while (rs.next())
                {
                    address.add(rs.getString("ADDRESS_ORDER"));
                    dateStart.add(rs.getString("DATE_START"));
                    dateEnd.add(rs.getString("DATE_END"));
                    schedule.add(rs.getString("SCHEDULE"));
                    time.add(rs.getString("TIME_WORK"));
                    price.add(rs.getInt("TOTAL_PRICE"));
                    create_at.add(rs.getString("CREATE_AT"));
                    seen.add(rs.getInt("SEEN"));
                    id.add(rs.getInt("ID"));
                }
                addressArr = new String[address.size()];
                addressArr = address.toArray(addressArr);
                dateStartArr = new String[dateStart.size()];
                dateStartArr = dateStart.toArray(dateStartArr);
                dateEndArr = new String[dateEnd.size()];
                dateEndArr = dateEnd.toArray(dateEndArr);
                scheduleArr = new String[schedule.size()];
                scheduleArr = schedule.toArray(scheduleArr);
                timeArr = new String[time.size()];
                timeArr = time.toArray(timeArr);
                priceArr = new Integer[price.size()];
                priceArr = price.toArray(priceArr);
                create_atArr = new String[create_at.size()];
                create_atArr = create_at.toArray(create_atArr);
                seenArr = new Integer[seen.size()];
                seenArr = seen.toArray(seenArr);
                idArr = new Integer[id.size()];
                idArr = id.toArray(idArr);
                for (int i = 0; i < address.size();i++){
                    listOrders.add(new ListOrderDK("Định kỳ",priceArr[i],addressArr[i],"2km",dateStartArr[i],dateEndArr[i],
                            scheduleArr[i],timeArr[i],create_atArr[i],seenArr[i]));
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
        orderListAdapter = new ListAdapterDK(listOrders);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getActivity()));
        recyclerView.setAdapter(orderListAdapter);

        if (recyclerView.getAdapter() != null) {
            ((ListAdapterDK) recyclerView.getAdapter()).setOnItemClickListener(new OnItemClickListener() {
                @Override
                public void onClick(View v, @NonNull int position) {
                    DateFormat formatter = new SimpleDateFormat("HH:mm dd/MM/yyyy");
                    Date date = new Date();
                    String create_at = formatter.format(date);
                    if (account_status != 2){
                        checkAccount();
                    }

                    checkAvailable(idArr[position]);

                    com.example.coosinstaff.connection.ConnectionDB conStr=new com.example.coosinstaff.connection.ConnectionDB();
                    connect =conStr.CONN();
                    try
                    {
                        // Connect to database
                        if (connect == null){checkConnectDialog();}
                        else {
                            // Change below query according to your own database.
                            String query = "UPDATE ORDER_MULTI SET USER_SUBMIT='"+account+"', DATE_SUBMIT='"+create_at+"'" +
                                    ",ORDER_STATUS=N'Đã tìm được NV' WHERE ID="+idArr[position]+"";
                            Log.d("BBB",query);
                            Statement stmt = connect.createStatement();
                            stmt.executeQuery(query);
                        }
                        connect.close();
                    }
                    catch (Exception ex)
                    {

                    }
                    Intent detail = new Intent(getActivity(), SubmitOrderActivity.class);
                    detail.putExtra("idOrder",idArr[position]);
                    detail.putExtra("orderType","Định kỳ");
                    startActivity(detail);
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
                Intent intent = new Intent(Intent.ACTION_CALL);
                intent.setData(Uri.parse(s));
                startActivity(intent);
                dialogInterface.dismiss();
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
            String query = "select * from ORDER_MULTI where ID="+id+"";
            Statement stmt = connect.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            if (rs.next()){
                if (rs.getString("ORDER_STATUS").trim().equals("Đã tìm được NV")){
                    AlertDialog.Builder builder = new AlertDialog.Builder(this.getActivity());
                    builder.setTitle("Thông báo");
                    builder.setMessage("Lịch này đã có người nhận!");
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
        }
        catch (Exception e){

        }
        return;
    }
}