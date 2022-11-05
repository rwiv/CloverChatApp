package com.example.cloverchatapp.web.client;

import com.example.cloverchatapp.MainActivity;
import com.example.cloverchatapp.component.recyclerview.chatmessage.ChatMessageList;
import com.example.cloverchatapp.util.Constants;
import com.example.cloverchatapp.web.domain.board.ResponseChatRoom;
import com.example.cloverchatapp.web.domain.chat.RequestStompChatMessage;
import com.example.cloverchatapp.web.domain.chat.ResponseStompChatMessage;
import com.google.gson.Gson;

import java.util.HashMap;

import io.reactivex.functions.Consumer;
import ua.naiksoftware.stomp.Stomp;
import ua.naiksoftware.stomp.StompClient;
import ua.naiksoftware.stomp.dto.LifecycleEvent;
import ua.naiksoftware.stomp.dto.StompMessage;

public class WebSocketClient {

    private final MainActivity activity;
    private final ChatMessageList chatMessageList;

    private final ResponseChatRoom chatRoom;

    public HttpClient httpClient;
    public StompClient stompClient;

    private final String wsRequestUrl;
    public final String subPath;
    private final String sendPath;
    private final String jSessionValue;

    public WebSocketClient(MainActivity activity, ChatMessageList chatMessageList, ResponseChatRoom chatRoom) {
        this.activity = activity;
        this.chatMessageList = chatMessageList;
        this.chatRoom = chatRoom;

        this.httpClient = new HttpClient(activity.authStorage);

        this.wsRequestUrl = Constants.SERVER_URL + "/stomp/websocket";
        this.subPath = "/sub/" + chatRoom.id;
        this.sendPath = "/pub/" + chatRoom.id;
        this.jSessionValue = "JSESSIONID=" + activity.authStorage.sessionId;
    }

    public void connect() {
        HashMap<String, String> headers = new HashMap<>();
        headers.put("Cookie", jSessionValue);

        stompClient = Stomp.over(Stomp.ConnectionProvider.OKHTTP, wsRequestUrl, headers);

        setLifecycleListener();
        stompClient.connect();

        subscribeChatRoom();
    }

    public void disconnect() {
        stompClient.disconnect();
    }

    public void send(String content) {
        RequestStompChatMessage requestStompChatMessage = new RequestStompChatMessage(
                chatRoom.id,
                activity.authStorage.me.id,
                content
        );

        String json = new Gson().toJson(requestStompChatMessage);
        stompClient.send(sendPath, json).subscribe();
    }

    private void setLifecycleListener() {
        stompClient.lifecycle()
                .subscribe(lifecycleHandle());
    }

    private Consumer<LifecycleEvent> lifecycleHandle() {
        return (LifecycleEvent lifecycleEvent) -> {
            switch (lifecycleEvent.getType()) {
                case OPENED:
                    System.out.println("opened");
                    httpClient.createChatUser(chatRoom.id, res -> {});
                    break;
                case ERROR:
                    Exception ex = lifecycleEvent.getException();
                    System.out.println(ex.getMessage());
                    ex.printStackTrace();
                    httpClient.deleteChatUser(chatRoom.id, res -> {});
                    break;
                case CLOSED:
                    System.out.println("closed");
                    httpClient.deleteChatUser(chatRoom.id, res -> {});
                    break;
            }
        };
    }

    private void subscribeChatRoom() {
        stompClient.topic(subPath)
                .subscribe(subscribeHandle());
    }

    private Consumer<StompMessage> subscribeHandle() {
        return (StompMessage topicMessage) -> {
            ResponseStompChatMessage chatMsg = new Gson().fromJson(topicMessage.getPayload(), ResponseStompChatMessage.class);

            chatMessageList.addItem(chatMsg);
        };
    }
}
