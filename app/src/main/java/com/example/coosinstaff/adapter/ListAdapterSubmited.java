package com.example.coosinstaff.adapter;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.coosinstaff.R;
import com.example.coosinstaff.model.ListSubmited;
import com.example.coosinstaff.model.OnItemClickListener;
import com.thekhaeng.pushdownanim.PushDownAnim;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class ListAdapterSubmited extends RecyclerView.Adapter<ListAdapterSubmited.ListSubmitedHolder> {

    ArrayList<ListSubmited> mangListsubmited;

    private OnItemClickListener mOnItemClickListener;

    public void setOnItemClickListener(OnItemClickListener mOnItemClickListener){
        this.mOnItemClickListener = mOnItemClickListener;
    }

    public ListAdapterSubmited(ArrayList<ListSubmited> mangListsubmited) {
        this.mangListsubmited = mangListsubmited;

    }

    @NonNull
    @Override
    public ListSubmitedHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater layoutInflater= LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.item_list_submited,null);
        return new ListSubmitedHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ListAdapterSubmited.ListSubmitedHolder holder, int position) {
        ListSubmited listSubmited = mangListsubmited.get(position);
        holder.txtOrderType.setText(listSubmited.getOrdertype());
        DecimalFormat decimalFormat = new DecimalFormat("#,###");
        String totalGiaString = decimalFormat.format(listSubmited.getGia());
        holder.gia.setText(totalGiaString + " đ");
        holder.txtCa.setText(listSubmited.getCa());
        holder.txtDate.setText(listSubmited.getDate());
        holder.txtDiadiem.setText(listSubmited.getDiadiem());
        if (listSubmited.getOrdertype().trim().equals("Dùng lẻ")){
            holder.txtMahoadon.setText("MĐH: DL"+listSubmited.getMahoadon());
        }else if (listSubmited.getOrdertype().trim().equals("Định kỳ")){
            holder.txtMahoadon.setText("MĐH: DK"+listSubmited.getMahoadon());
            holder.txtOrderType.setBackgroundResource(R.drawable.bg_text_orange);
            holder.txtOrderType.setTextColor(Color.WHITE);
        }else if (listSubmited.getOrdertype().trim().equals("Nấu ăn")){
            holder.txtMahoadon.setText("MĐH: NA"+listSubmited.getMahoadon());
            holder.txtOrderType.setBackgroundResource(R.drawable.bg_text_pink);
            holder.txtOrderType.setTextColor(Color.WHITE);
        }else {
            holder.txtMahoadon.setText("MĐH: TVS"+listSubmited.getMahoadon());
            holder.txtOrderType.setBackgroundResource(R.drawable.bg_text_ryan);
            holder.txtOrderType.setTextColor(Color.WHITE);
        }

    }

    @Override
    public int getItemCount() {
        return mangListsubmited.size() > 0 ? mangListsubmited.size() : 0;
    }

    class ListSubmitedHolder extends RecyclerView.ViewHolder {

        TextView txtDate, txtCa, txtDiadiem, txtMahoadon, gia,txtOrderType;

        public ListSubmitedHolder(@NonNull View itemView) {
            super(itemView);
            txtOrderType = itemView.findViewById(R.id.txt_order_type);
            txtCa = itemView.findViewById(R.id.ca_order);
            txtDate = itemView.findViewById(R.id.date_order);
            txtDiadiem = itemView.findViewById(R.id.diadiem);
            txtMahoadon = itemView.findViewById(R.id.ma_hoadon);
            gia = itemView.findViewById(R.id.gia);

            PushDownAnim.setPushDownAnimTo(itemView).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mOnItemClickListener.onClick(view, getLayoutPosition());
                }
            });
            PushDownAnim.setPushDownAnimTo(itemView).setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    mOnItemClickListener.onLongClick(view, getLayoutPosition());
                    return true;
                }
            });
        }
    }

}
