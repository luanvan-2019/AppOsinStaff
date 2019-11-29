package com.example.coosinstaff.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.coosinstaff.R;
import com.example.coosinstaff.model.ListUserChat;
import com.example.coosinstaff.model.OnItemClickListener;
import com.thekhaeng.pushdownanim.PushDownAnim;

import java.util.ArrayList;

public class ListSelectUserChatAdapter extends RecyclerView.Adapter<ListSelectUserChatAdapter.ListSelectUserChatHolder> {

    ArrayList<ListUserChat> mangListUserChat;
    private OnItemClickListener mOnItemClickListener;

    public void setOnItemClickListener(OnItemClickListener mOnItemClickListener){
        this.mOnItemClickListener = mOnItemClickListener;
    }

    public ListSelectUserChatAdapter(ArrayList<ListUserChat> mangListUserChat) {
        this.mangListUserChat = mangListUserChat;
    }

    @NonNull
    @Override
    public ListSelectUserChatAdapter.ListSelectUserChatHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater layoutInflater= LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.select_user_chat_item,null);
        return new ListSelectUserChatHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ListSelectUserChatAdapter.ListSelectUserChatHolder holder, int position) {
        ListUserChat listUserChat = mangListUserChat.get(position);
        holder.txtName.setText(listUserChat.getName());
        holder.txtPhone.setText(listUserChat.getPhone());
        holder.txtID.setText("MKH: KH"+listUserChat.getId());

    }

    @Override
    public int getItemCount() {
        return mangListUserChat.size() > 0 ? mangListUserChat.size() : 0;
    }

    class ListSelectUserChatHolder extends RecyclerView.ViewHolder {

        TextView txtName, txtID, txtPhone;
        ImageView imgHinhanh;
        //truyen item view vao va anh xa
        public ListSelectUserChatHolder(@NonNull View itemView) {
            super(itemView);

            txtName = itemView.findViewById(R.id.txt_select_user_name);
            txtID = itemView.findViewById(R.id.txt_select_user_id);
            txtPhone = itemView.findViewById(R.id.txt_select_user_phone);

            PushDownAnim.setPushDownAnimTo(itemView).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mOnItemClickListener.onClick(view, getLayoutPosition());
                }
            });

        }
    }
}
