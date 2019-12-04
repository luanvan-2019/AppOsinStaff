package com.example.coosinstaff.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.coosinstaff.R;
import com.example.coosinstaff.model.AccountAvatar;
import com.example.coosinstaff.model.ListUserChat;
import com.example.coosinstaff.model.OnItemClickListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
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
    public void onBindViewHolder(@NonNull final ListSelectUserChatAdapter.ListSelectUserChatHolder holder, int position) {
        final ListUserChat listUserChat = mangListUserChat.get(position);
        holder.txtName.setText(listUserChat.getName());
        holder.txtPhone.setText(listUserChat.getPhone());
        if (listUserChat.getName().equals("ADMIN")){
            holder.txtID.setText("");
        }else {holder.txtID.setText("KH"+listUserChat.getId());}
        DatabaseReference reference1 = FirebaseDatabase.getInstance().getReference("avatars");
        reference1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    AccountAvatar accountAvatar = snapshot.getValue(AccountAvatar.class);
                    if (accountAvatar.getAcountPhone().equals(listUserChat.getPhone())) {
                        Picasso.get().load(accountAvatar.getImageUrl()).into(holder.imgAvatar);
                    }
                    if (accountAvatar.getAcountPhone().equals("admin")) {
                        if (listUserChat.getPhone().equals("Quản trị viên")){
                            Picasso.get().load(accountAvatar.getImageUrl()).into(holder.imgAvatar);
                        }
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

    }

    @Override
    public int getItemCount() {
        return mangListUserChat.size() > 0 ? mangListUserChat.size() : 0;
    }

    class ListSelectUserChatHolder extends RecyclerView.ViewHolder {

        TextView txtName, txtID, txtPhone;
        ImageView imgAvatar;
        //truyen item view vao va anh xa
        public ListSelectUserChatHolder(@NonNull View itemView) {
            super(itemView);

            txtName = itemView.findViewById(R.id.txt_select_user_name);
            txtID = itemView.findViewById(R.id.txt_select_user_id);
            txtPhone = itemView.findViewById(R.id.txt_select_user_phone);
            imgAvatar = itemView.findViewById(R.id.select_user_image);

            PushDownAnim.setPushDownAnimTo(itemView).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mOnItemClickListener.onClick(view, getLayoutPosition());
                }
            });

        }
    }
}
