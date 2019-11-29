package com.example.coosinstaff.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.coosinstaff.R;
import com.example.coosinstaff.model.ListOrder;
import com.example.coosinstaff.model.ListOrderNauAn;
import com.example.coosinstaff.model.OnItemClickListener;
import com.thekhaeng.pushdownanim.PushDownAnim;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class ListAdapterNauAn extends RecyclerView.Adapter<ListAdapterNauAn.ListHolder> {

    ArrayList<ListOrderNauAn> mangListorder;

    private OnItemClickListener mOnItemClickListener;

    public void setOnItemClickListener(OnItemClickListener mOnItemClickListener){
        this.mOnItemClickListener = mOnItemClickListener;
    }

    public ListAdapterNauAn(ArrayList<ListOrderNauAn> mangListorder) {
        this.mangListorder = mangListorder;

    }

    @NonNull
    @Override
    public ListHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.list_order_item_nauan, null);
        return new ListHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ListHolder holder, int position) {
        ListOrderNauAn listorder = mangListorder.get(position);
        holder.itemView.setTag(position);
        holder.mOrderType.setText(listorder.getOrderType());
        DecimalFormat decimalFormat = new DecimalFormat("#,###");
        String totalGiaString = decimalFormat.format(listorder.getPrice());
        holder.mOrderPrice.setText(totalGiaString+" đ");
        holder.mOrderLocation.setText(listorder.getLocation());
        holder.mNearby.setText(listorder.getNearby());
        holder.mOrderDate.setText(listorder.getDate());
        holder.mOrderTime.setText(listorder.getTime());

        holder.mOrderPeopleAmount.setText(listorder.getPeopleAmount());
        holder.mOrderDishAmount.setText(listorder.getDishAmount());
        holder.mOrderTaste.setText(listorder.getTaste());
        holder.mOrderFruit.setText(listorder.getFruit());
        holder.mOrderMarket.setText(listorder.getMarket());
        String marketPriceString = decimalFormat.format(listorder.getMarketPrice());
        holder.mOrderMarketPrice.setText(marketPriceString+" đ");

        holder.mOrderCreateAt.setText(listorder.getCreateAt());
        holder.mOrderSeen.setText(listorder.getSeenCount()+" người");
    }

    @Override
    public int getItemCount() {
        return mangListorder.size() > 0 ? mangListorder.size() : 0;
    }
    class ListHolder extends RecyclerView.ViewHolder {

        private TextView mOrderType, mOrderPrice, mOrderLocation, mNearby, mOrderDate, mOrderTime,
                mOrderPeopleAmount,mOrderDishAmount,mOrderTaste,mOrderFruit,mOrderMarket,
                mOrderMarketPrice,mOrderCreateAt, mOrderSeen;
        private Button btnSubmit;

        public ListHolder(@NonNull View itemView) {
            super(itemView);
            mOrderType = itemView.findViewById(R.id.order_type);
            mOrderPrice = itemView.findViewById(R.id.order_price);
            mOrderLocation = itemView.findViewById(R.id.order_location);
            mNearby = itemView.findViewById(R.id.txt_nearby);
            mOrderDate = itemView.findViewById(R.id.txt_date);
            mOrderTime = itemView.findViewById(R.id.time);

            mOrderPeopleAmount = itemView.findViewById(R.id.txt_songuoian);
            mOrderDishAmount = itemView.findViewById(R.id.txt_somon);
            mOrderTaste = itemView.findViewById(R.id.txt_khauvi);
            mOrderFruit = itemView.findViewById(R.id.txt_traicay);
            mOrderMarket = itemView.findViewById(R.id.txt_dicho);
            mOrderMarketPrice = itemView.findViewById(R.id.txt_tiendicho);
            btnSubmit = itemView.findViewById(R.id.btn_nhanlich);

            mOrderCreateAt = itemView.findViewById(R.id.time_gui);
            mOrderSeen = itemView.findViewById(R.id.songuoixem);

            PushDownAnim.setPushDownAnimTo(btnSubmit).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mOnItemClickListener.onClick(view, getLayoutPosition());
                }
            });
            PushDownAnim.setPushDownAnimTo(btnSubmit).setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    mOnItemClickListener.onLongClick(view, getLayoutPosition());
                    return true;
                }
            });
        }
    }
}
