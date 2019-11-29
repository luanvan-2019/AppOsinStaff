package com.example.coosinstaff.fragment;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.coosinstaff.ChatActivity;
import com.example.coosinstaff.LoadingDialog;
import com.example.coosinstaff.R;
import com.example.coosinstaff.adapter.ListSelectUserChatAdapter;
import com.example.coosinstaff.model.ListUserChat;
import com.example.coosinstaff.model.OnItemClickListener;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class ChatFragment extends Fragment {

    View view;
    RecyclerView recyclerView;
    ArrayList<ListUserChat> listUserChats;
    ListSelectUserChatAdapter listSelectUserChatAdapter;
    String account;
    Connection connect;

    ArrayList<String> name = new ArrayList<String>();
    ArrayList<Integer> id = new ArrayList<Integer>();
    ArrayList<String> phone = new ArrayList<String>();
    String[] nameArr,phoneArr;
    Integer[] idArr;


    public ChatFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_chat, container, false);
        recyclerView = view.findViewById(R.id.recyclerview_select_chat);

        //lay so dien thoai
        SharedPreferences SP = getActivity().getSharedPreferences("PHONE",0);
        account = SP.getString("phone_num",null);

        listUserChats = new ArrayList<>();
        name.clear();
        phone.clear();
        id.clear();

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
                String b="";
                // Change below query according to your own database.
                String query = "select * from ORDER_SINGLE where USER_SUBMIT='"+ account +"'";
                Statement stmt = connect.createStatement();
                ResultSet rs = stmt.executeQuery(query);
                while (rs.next())
                {
                    String a =rs.getString("USER_ORDER");
                    String query1 = "select * from CUSTOMER where PHONE_NUM='"+ rs.getString("USER_ORDER") +"'";
                    Statement stmt1 = connect.createStatement();
                    ResultSet rs1 = stmt1.executeQuery(query1);
                    if (rs1.next()){
                        if (!a.equals(b)){
                            name.add(rs1.getString("FULL_NAME"));
                            phone.add(rs1.getString("PHONE_NUM"));
                            id.add(rs1.getInt("ID"));
                        }
                        b=rs.getString("USER_ORDER");
                    }
                }
                nameArr = new String[name.size()];
                nameArr = name.toArray(nameArr);
                idArr = new Integer[id.size()];
                idArr = id.toArray(idArr);
                phoneArr = new String[phone.size()];
                phoneArr = phone.toArray(phoneArr);
                for (int i = 0; i < name.size();i++){
                    listUserChats.add(new ListUserChat(nameArr[i],phoneArr[i],idArr[i]));
                }

//                int a = name.size();
//                // Change below query according to your own database.
//                String query1 = "select * from ORDER_MULTI where USER_SUBMIT='"+ account +"'";
//                Statement stmt1 = connect.createStatement();
//                ResultSet rs1 = stmt1.executeQuery(query1);
//                while (rs1.next())
//                {
//                    String query2 = "select * from CUSTOMER where PHONE_NUM='"+ rs1.getString("USER_ORDER") +"'";
//                    Statement stmt2 = connect.createStatement();
//                    ResultSet rs2 = stmt2.executeQuery(query2);
//                    if (rs2.next()){
//                        name.add(rs2.getString("FULL_NAME"));
//                        phone.add(rs1.getString("USER_ORDER"));
//                    }
//                }
//                nameArr = new String[name.size()];
//                nameArr = name.toArray(nameArr);
//                idArr = new String[id.size()];
//                idArr = id.toArray(idArr);
//                phoneArr = new String[phone.size()];
//                phoneArr = phone.toArray(phoneArr);
//                for (int i = a; i < name.size();i++){
//                    listUserChats.add(new ListUserChat(nameArr[i],phoneArr[i],idArr[i]));
//                }
//
//                int b = name.size();
//                // Change below query according to your own database.
//                String query2 = "select * from ORDER_OVERVIEW where USER_SUBMIT1='"+ account +"' OR USER_SUBMIT2='"+ account +"' OR" +
//                        "USER_SUBMIT3='"+ account +"'";
//                Statement stmt2 = connect.createStatement();
//                ResultSet rs2 = stmt2.executeQuery(query2);
//                while (rs2.next())
//                {
//                    String query3 = "select * from CUSTOMER where PHONE_NUM='"+ rs2.getString("USER_ORDER") +"'";
//                    Statement stmt3 = connect.createStatement();
//                    ResultSet rs3 = stmt3.executeQuery(query3);
//                    if (rs3.next()){
//                        name.add(rs3.getString("FULL_NAME"));
//                        phone.add(rs2.getString("USER_ORDER"));
//                    }
//                    if (account.trim().equals(rs2.getString("USER_SUBMIT1"))){
//                        String query4 = "select * from EMPLOYEE where PHONE_NUM='"+ rs2.getString("USER_ORDER2") +"'";
//                        Statement stmt4 = connect.createStatement();
//                        ResultSet rs4 = stmt4.executeQuery(query4);
//                        if (rs4.next()){
//                            name.add(rs4.getString("FULL_NAME"));
//                            phone.add(rs4.getString("PHONE_NUM"));
//                        }
//                        String query5 = "select * from EMPLOYEE where PHONE_NUM='"+ rs2.getString("USER_ORDER3") +"'";
//                        Statement stmt5 = connect.createStatement();
//                        ResultSet rs5 = stmt5.executeQuery(query5);
//                        if (rs5.next()){
//                            name.add(rs5.getString("FULL_NAME"));
//                            phone.add(rs5.getString("PHONE_NUM"));
//                        }
//                    }else if (account.trim().equals(rs2.getString("USER_SUBMIT2"))){
//                        String query4 = "select * from EMPLOYEE where PHONE_NUM='"+ rs2.getString("USER_ORDER1") +"'";
//                        Statement stmt4 = connect.createStatement();
//                        ResultSet rs4 = stmt4.executeQuery(query4);
//                        if (rs4.next()){
//                            name.add(rs4.getString("FULL_NAME"));
//                            phone.add(rs4.getString("PHONE_NUM"));
//                        }
//                        String query5 = "select * from EMPLOYEE where PHONE_NUM='"+ rs2.getString("USER_ORDER3") +"'";
//                        Statement stmt5 = connect.createStatement();
//                        ResultSet rs5 = stmt5.executeQuery(query5);
//                        if (rs5.next()){
//                            name.add(rs5.getString("FULL_NAME"));
//                            phone.add(rs5.getString("PHONE_NUM"));
//                        }
//                    }else {
//                        String query4 = "select * from EMPLOYEE where PHONE_NUM='"+ rs2.getString("USER_ORDER1") +"'";
//                        Statement stmt4 = connect.createStatement();
//                        ResultSet rs4 = stmt4.executeQuery(query4);
//                        if (rs4.next()){
//                            name.add(rs4.getString("FULL_NAME"));
//                            phone.add(rs4.getString("PHONE_NUM"));
//                        }
//                        String query5 = "select * from EMPLOYEE where PHONE_NUM='"+ rs2.getString("USER_ORDER2") +"'";
//                        Statement stmt5 = connect.createStatement();
//                        ResultSet rs5 = stmt5.executeQuery(query5);
//                        if (rs5.next()){
//                            name.add(rs5.getString("FULL_NAME"));
//                            phone.add(rs5.getString("PHONE_NUM"));
//                        }
//                    }
//                }
//                nameArr = new String[name.size()];
//                nameArr = name.toArray(nameArr);
//                idArr = new String[id.size()];
//                idArr = id.toArray(idArr);
//                phoneArr = new String[phone.size()];
//                phoneArr = phone.toArray(phoneArr);
//                for (int i = b; i < name.size();i++){
//                    listUserChats.add(new ListUserChat(nameArr[i],phoneArr[i],idArr[i]));
//                }
//                int c =name.size();
//                // Change below query according to your own database.
//                String query4 = "select * from ORDER_COOK where USER_SUBMIT='"+ account +"'";
//                Statement stmt4 = connect.createStatement();
//                ResultSet rs4 = stmt4.executeQuery(query4);
//                while (rs4.next())
//                {
//                    String query5 = "select * from CUSTOMER where PHONE_NUM='"+ rs4.getString("USER_ORDER") +"'";
//                    Statement stmt5 = connect.createStatement();
//                    ResultSet rs5 = stmt5.executeQuery(query5);
//                    if (rs5.next()){
//                        name.add(rs5.getString("FULL_NAME"));
//                        phone.add(rs5.getString("PHONE_NUM"));
//                    }
//                }
//                nameArr = new String[name.size()];
//                nameArr = name.toArray(nameArr);
//                idArr = new String[id.size()];
//                idArr = id.toArray(idArr);
//                phoneArr = new String[phone.size()];
//                phoneArr = phone.toArray(phoneArr);
//                for (int i = c; i < name.size();i++){
//                    listUserChats.add(new ListUserChat(nameArr[i],phoneArr[i],idArr[i]));
//                }
            }
        }
        catch (Exception ex)
        {
            Toast.makeText(getContext(), ex.getMessage(), Toast.LENGTH_LONG).show();
            Log.d("BBB",ex.getMessage());
        }

        listSelectUserChatAdapter = new ListSelectUserChatAdapter(listUserChats);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(listSelectUserChatAdapter);

        if (recyclerView.getAdapter() != null) {
            ((ListSelectUserChatAdapter) recyclerView.getAdapter()).setOnItemClickListener(new OnItemClickListener() {
                @Override
                public void onClick(View v, @NonNull int position) {
                    LoadingDialog loadingDialog = new LoadingDialog();
                    loadingDialog.loading(getActivity());
                    Intent detail = new Intent(getActivity(), ChatActivity.class);
                    detail.putExtra("receiver",phoneArr[position]);
                    startActivity(detail);
                }

                @Override
                public void onLongClick(View v, @NonNull int position) {

                }
            });
        }

        return view;
    }

}
