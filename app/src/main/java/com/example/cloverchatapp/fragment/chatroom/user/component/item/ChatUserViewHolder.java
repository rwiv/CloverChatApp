package com.example.cloverchatapp.fragment.chatroom.user.component.item;

import android.view.View;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cloverchatapp.R;

public class ChatUserViewHolder extends RecyclerView.ViewHolder {

    public final CardView card;
    public final TextView nickname;
    public final TextView email;

    public ChatUserViewHolder(View itemView) {
        super(itemView);

        this.card = itemView.findViewById(R.id.user_card_view);
        this.nickname = itemView.findViewById(R.id.user_tv_nickname);
        this.email = itemView.findViewById(R.id.user_tv_email);
    }
}