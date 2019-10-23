package com.example.coosinstaff.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.coosinstaff.R;
import com.example.coosinstaff.adapter.ListAdapter;
import com.example.coosinstaff.adapter.ListAdapterNauAn;
import com.example.coosinstaff.model.ListOrder;
import com.example.coosinstaff.model.ListOrderNauAn;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

public class FragmentNauAn extends Fragment {

    View view;
    RecyclerView recyclerView;
    ArrayList<ListOrderNauAn> listOrders;
    ListAdapterNauAn orderListAdapter;
    String phone_num;
    Connection connect;

    ArrayList<String> address = new ArrayList<String>();
    ArrayList<String> date = new ArrayList<String>();
    ArrayList<String> time = new ArrayList<String>();

    ArrayList<String> peopleAmount = new ArrayList<String>();
    ArrayList<String> dishAmount = new ArrayList<String>();
    ArrayList<String> taste = new ArrayList<String>();
    ArrayList<String> fruit = new ArrayList<String>();
    ArrayList<String> market = new ArrayList<String>();
    ArrayList<Integer> marketPrice = new ArrayList<Integer>();

    ArrayList<String> create_at = new ArrayList<String>();
    ArrayList<Integer> price = new ArrayList<Integer>();
    ArrayList<Integer> seen = new ArrayList<Integer>();
    String[] addressArr,dateArr,timeArr,create_atArr,peopleAmountArr,dishAmountArr,tasteArr,fruitArr,marketArr;
    Integer[] priceArr,seenArr,marketPriceArr;

    public FragmentNauAn() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.nauan_fragment,container,false);
        recyclerView = view.findViewById(R.id.recyclerview_nauan);


        listOrders = new ArrayList<>();
        address.clear();
        date.clear();
        time.clear();
        peopleAmount.clear();
        dishAmount.clear();
        taste.clear();
        fruit.clear();
        market.clear();
        marketPrice.clear();
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
                String query = "select * from ORDER_COOK";
                Statement stmt = connect.createStatement();
                ResultSet rs = stmt.executeQuery(query);
                while (rs.next())
                {
                    address.add(rs.getString("ADDRESS_ORDER"));
                    date.add(rs.getString("DATE_WORK"));
                    time.add(rs.getString("TIME_WORK"));

                    peopleAmount.add(rs.getString("PEOPLE_AMOUNT"));
                    dishAmount.add(rs.getString("DISH_AMOUNT"));
                    taste.add(rs.getString("TASTE"));
                    fruit.add(rs.getString("FRUIT"));
                    market.add(rs.getString("MARKET"));
                    marketPrice.add(rs.getInt("MAX_MARKET_PRICE"));

                    price.add(rs.getInt("TOTAL_PRICE"));
                    create_at.add(rs.getString("CREATE_AT"));
                    seen.add(rs.getInt("SEEN"));
                }
                addressArr = new String[address.size()];
                addressArr = address.toArray(addressArr);
                dateArr = new String[date.size()];
                dateArr = date.toArray(dateArr);
                timeArr = new String[time.size()];
                timeArr = time.toArray(timeArr);

                peopleAmountArr = new String[peopleAmount.size()];
                peopleAmountArr = peopleAmount.toArray(peopleAmountArr);
                dishAmountArr = new String[dishAmount.size()];
                dishAmountArr = dishAmount.toArray(dishAmountArr);
                tasteArr = new String[taste.size()];
                tasteArr = taste.toArray(tasteArr);
                fruitArr = new String[fruit.size()];
                fruitArr = fruit.toArray(fruitArr);
                marketArr = new String[market.size()];
                marketArr = market.toArray(marketArr);
                marketPriceArr = new Integer[marketPrice.size()];
                marketPriceArr = marketPrice.toArray(marketPriceArr);

                priceArr = new Integer[price.size()];
                priceArr = price.toArray(priceArr);
                create_atArr = new String[create_at.size()];
                create_atArr = create_at.toArray(create_atArr);
                seenArr = new Integer[seen.size()];
                seenArr = seen.toArray(seenArr);
                for (int i = 0; i < address.size();i++){
                    listOrders.add(new ListOrderNauAn("Nấu ăn",priceArr[i],addressArr[i],"2",dateArr[i],timeArr[i],
                            peopleAmountArr[i],dishAmountArr[i],tasteArr[i],fruitArr[i],marketArr[i],marketPriceArr[i],create_atArr[i],seenArr[i]));
                }
            }
        }
        catch (Exception ex)
        {
            Toast.makeText(getContext(), ex.getMessage(), Toast.LENGTH_LONG).show();
        }
        orderListAdapter = new ListAdapterNauAn(listOrders);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getActivity()));
        recyclerView.setAdapter(orderListAdapter);
        return view;
    }
}