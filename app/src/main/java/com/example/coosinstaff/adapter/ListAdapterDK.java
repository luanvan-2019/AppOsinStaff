package com.example.coosinstaff.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.coosinstaff.R;
import com.example.coosinstaff.model.ListOrder;
import com.example.coosinstaff.model.ListOrderDK;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class ListAdapterDK extends RecyclerView.Adapter<ListAdapterDK.ListHolder> {

    ArrayList<ListOrderDK> mangListorder;

    HandelClick handelClick;

    public ListAdapterDK(ArrayList<ListOrderDK> mangListorder) {
        this.mangListorder = mangListorder;

    }

    @NonNull
    @Override
    public ListHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.item_list_order_dk, null);
        return new ListHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ListHolder holder, int position) {
        ListOrderDK listorder = mangListorder.get(position);
        holder.itemView.setTag(position);
        holder.mOrderType.setText(listorder.getOrderType());
        DecimalFormat decimalFormat = new DecimalFormat("#,###");
        String totalGiaString = decimalFormat.format(listorder.getPrice());
        holder.mOrderPrice.setText(totalGiaString+" đ");
        holder.mOrderLocation.setText(listorder.getLocation());
        holder.mNearby.setText(listorder.getNearby());
        holder.mOrderDateStart.setText(listorder.getDateStart());
        holder.mOrderDateEnd.setText(listorder.getDateEnd());
        holder.mOrderSchedule.setText(listorder.getSchedule());
        holder.mOrderTime.setText(listorder.getTime());
        holder.mOrderCreateAt.setText(listorder.getCreateAt());
        holder.mOrderSeen.setText(listorder.getSeenCount()+" người");
    }

    @Override
    public int getItemCount() {
        return mangListorder.size() > 0 ? mangListorder.size() : 0;
    }
    class ListHolder extends RecyclerView.ViewHolder {

        private TextView mOrderType, mOrderPrice, mOrderLocation, mNearby, mOrderDateStart, mOrderDateEnd, mOrderSchedule, mOrderTime,
                mOrderCreateAt, mOrderSeen;

        public ListHolder(@NonNull View itemView) {
            super(itemView);
            mOrderType = itemView.findViewById(R.id.order_type);
            mOrderPrice = itemView.findViewById(R.id.order_price);
            mOrderLocation = itemView.findViewById(R.id.order_location);
            mNearby = itemView.findViewById(R.id.txt_nearby);
            mOrderDateStart = itemView.findViewById(R.id.txt_date_start);
            mOrderDateEnd = itemView.findViewById(R.id.txt_date_end);
            mOrderSchedule = itemView.findViewById(R.id.schedule);
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
    public void addList(ListOrderDK listOrder){
        mangListorder.add(listOrder);
        notifyDataSetChanged();
    }
    public void listenClick(HandelClick handelClick){
        this.handelClick = handelClick;
    }
}
