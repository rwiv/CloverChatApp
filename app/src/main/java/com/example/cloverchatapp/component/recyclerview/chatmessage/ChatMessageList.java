package com.example.cloverchatapp.component.recyclerview.chatmessage;

import android.view.ViewGroup;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cloverchatapp.MainActivity;
import com.example.cloverchatapp.R;
import com.example.cloverchatapp.web.domain.chat.ResponseStompChatMessage;

import java.util.ArrayList;
import java.util.List;

public class ChatMessageList {

    private final MainActivity activity;
    private final ViewGroup rootView;

    RecyclerView recyclerView;
    ChatMessageAdapter adapter;
    List<ResponseStompChatMessage> itemList;

    public ChatMessageList(MainActivity activity, ViewGroup rootView, List<ResponseStompChatMessage> chatMessages) {
        this.activity = activity;
        this.rootView = rootView;

        init(chatMessages);
    }

    private void init(List<ResponseStompChatMessage> chatMessages) {
        recyclerView = rootView.findViewById(R.id.rv_list);

        itemList = new ArrayList<>();
        itemList.addAll(chatMessages);

        adapter = new ChatMessageAdapter(itemList, activity);

        LinearLayoutManager layoutManager = new LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        recyclerView.scrollToPosition(adapter.getItemCount() - 1);
    }

    public void addItem(ResponseStompChatMessage chatMessage) {
        activity.runOnUiThread(() -> {
            itemList.add(chatMessage);
            activity.curChatMessages.add(chatMessage);
            recyclerView.scrollToPosition(adapter.getItemCount() - 1);
        });
    }

    public void clearList() {
        itemList.clear();
    }
}
