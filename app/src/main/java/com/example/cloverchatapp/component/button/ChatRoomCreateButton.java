package com.example.cloverchatapp.component.button;

import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import com.example.cloverchatapp.MainActivity;
import com.example.cloverchatapp.R;
import com.example.cloverchatapp.fragment.FragmentEnum;
import com.example.cloverchatapp.web.domain.board.RequestChatRoomCreateForm;
import com.example.cloverchatapp.web.domain.board.ChatRoomType;

public class ChatRoomCreateButton {

    private final MainActivity activity;

    private final EditText inputPassword;
    private final EditText inputTitle;
    private final CheckBox isPrivateChkBox;

    private final Button targetButton;

    public ChatRoomCreateButton(MainActivity activity, ViewGroup rootView, EditText inputPassword, EditText inputTitle, CheckBox isPrivateChkBox) {
        this.activity = activity;
        this.inputPassword = inputPassword;
        this.inputTitle = inputTitle;
        this.isPrivateChkBox = isPrivateChkBox;

        this.targetButton = rootView.findViewById(R.id.createChatRoomBtn);

        setOnClickListener();
    }

    private void setOnClickListener() {
        targetButton.setOnClickListener((View v) -> {
            RequestChatRoomCreateForm requestChatRoomCreateForm = new RequestChatRoomCreateForm(
                    activity.authStorage.me.id,
                    getPassword(),
                    inputTitle.getText().toString(),
                    getCurChatRoomType()
            );

            activity.httpClient.createChatRoom(requestChatRoomCreateForm, res -> {
                activity.navigator.navigate(FragmentEnum.INDEX);
            });
        });
    }

    private String getPassword() {
        if (isPrivateChkBox.isChecked()) {
            return inputPassword.getText().toString();
        }

        return null;
    }

    private ChatRoomType getCurChatRoomType() {
        if (isPrivateChkBox.isChecked()) {
            return ChatRoomType.PRIVATE;
        }

        return ChatRoomType.PUBLIC;
    }
}
