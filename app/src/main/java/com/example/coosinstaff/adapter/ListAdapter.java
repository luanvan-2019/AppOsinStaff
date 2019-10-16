package com.example.coosinstaff.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.coosinstaff.R;
import com.example.coosinstaff.model.ListOrder;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class ListAdapter extends RecyclerView.Adapter<ListAdapter.ListHolder> {

    ArrayList<ListOrder> mangListorder;

    HandelClick handelClick;

    public ListAdapter(ArrayList<ListOrder> mangListorder) {
        this.mangListorder = mangListorder;

    }

    @NonNull
    @Override
    public ListHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.list_order_item, null);
        return new ListHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ListHolder holder, int position) {
        ListOrder listorder = mangListorder.get(position);
        holder.itemView.setTag(position);
        holder.mOrderType.setText(listorder.getOrderType());
        DecimalFormat decimalFormat = new DecimalFormat("#,###");
        String totalGiaString = decimalFormat.format(listorder.getPrice());
        holder.mOrderPrice.setText(totalGiaString+" đ");
        holder.mOrderLocation.setText(listorder.getLocation());
        holder.mNearby.setText(listorder.getNearby());
        holder.mOrderDate.setText(listorder.getDate());
        holder.mOrderTime.setText(listorder.getTime());
        holder.mOrderCreateAt.setText(listorder.getCreateAt());
        holder.mOrderSeen.setText(listorder.getSeenCount()+" người");
    }

    @Override
    public int getItemCount() {
        return mangListorder.size() > 0 ? mangListorder.size() : 0;
    }
    class ListHolder extends RecyclerView.ViewHolder {

        private TextView mOrderType, mOrderPrice, mOrderLocation, mNearby, mOrderDate, mOrderTime,
                mOrderCreateAt, mOrderSeen;

        public ListHolder(@NonNull View itemView) {
            super(itemView);
            mOrderType = itemView.findViewById(R.id.order_type);
            mOrderPrice = itemView.findViewById(R.id.order_price);
            mOrderLocation = itemView.findViewById(R.id.order_location);
            mNearby = itemView.findViewById(R.id.txt_nearby);
            mOrderDate = itemView.findViewById(R.id.txt_date);
            mOrderTime = itemView.findViewById(R.id.time);
            mOrderCreateAt = itemView.findViewById(R.id.time_gui);
            mOrderSeen = itemView.findViewById(R.id.songuoixem);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
//                    handelClick.onClick(view,(int) view.getTag());
                }
            });
            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    handelClick.onLongClick(view, (Integer) view.getTag());
                    return true;
                }
            });
        }
    }
//    public void addList(ListOrder listOrder){
//        mangListorder.add(listOrder);
//        notifyDataSetChanged();
//    }
//    public void listenClick(HandelClick handelClick){
//        this.handelClick = handelClick;
//    }
}
