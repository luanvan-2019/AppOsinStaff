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
                String query = "select * from CUSTOMER";
                Statement stmt = connect.createStatement();
                ResultSet rs = stmt.executeQuery(query);
                while (rs.next()){
                    name.add(rs.getString("FULL_NAME"));
                    phone.add(rs.getString("PHONE_NUM"));
                    id.add(rs.getInt("ID"));
                }
                name.add("ADMIN");
                id.add(9999);
                phone.add("Quản trị viên");

                nameArr = new String[name.size()];
                nameArr = name.toArray(nameArr);
                idArr = new Integer[id.size()];
                idArr = id.toArray(idArr);
                phoneArr = new String[phone.size()];
                phoneArr = phone.toArray(phoneArr);
                for (int i = 0; i < name.size();i++){
                    listUserChats.add(new ListUserChat(nameArr[i],phoneArr[i],idArr[i]));
                }
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
