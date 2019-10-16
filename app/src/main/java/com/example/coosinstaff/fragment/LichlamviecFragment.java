package com.example.coosinstaff.fragment;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.example.coosinstaff.R;
import com.example.coosinstaff.adapter.OrderPagerAdapter;
import com.google.android.material.tabs.TabLayout;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class LichlamviecFragment extends Fragment {

    private TabLayout tabLayout;
    private ViewPager viewPager;
    View view;
    Connection connect;
    String phone_num;
    int empType = 0;
    Boolean checkk = false;
    OrderPagerAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_lichlamviec,container,false);

        tabLayout = view.findViewById(R.id.tab_layout);
        viewPager = view.findViewById(R.id.view_pager);

        adapter = new OrderPagerAdapter(getChildFragmentManager());

        //lay so dien thoai
        SharedPreferences SP = getContext().getSharedPreferences("PHONE",0);
        phone_num = SP.getString("phone_num",null);

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
                String query = "select * from EMPLOYEE where PHONE_NUM= '"+phone_num+"'";
                Statement stmt = connect.createStatement();
                ResultSet rs = stmt.executeQuery(query);
                if (rs.next())
                {
                    empType = rs.getInt("EMP_TYPE");
                }
            }
        }
        catch (Exception ex)
        {
            Toast.makeText(getContext(), ex.getMessage(), Toast.LENGTH_LONG).show();
        }

        if (empType == 1){
            adapter.addFragment(new FragmentDungle(),"Dùng lẻ");
            adapter.addFragment(new Fragment_dungdk(),"Dùng định kỳ");
//            adapter.addFragment(new Fragment_tongvs(),"Tổng vệ sinh");
            tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        }else if (empType == 2){
//            adapter.addFragment(new Fragment_dvnauan(),"DV nấu ăn");
            tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        }else {
            adapter.addFragment(new FragmentDungle(),"Dùng lẻ");
            adapter.addFragment(new Fragment_dungdk(),"Dùng định kỳ");
//            adapter.addFragment(new Fragment_tongvs(),"Tổng vệ sinh");
//            adapter.addFragment(new Fragment_dvnauan(),"DV nấu ăn");
            tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
        }

        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
        checkk = true;

        return view;
    }
}
