package com.example.chatapp;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chatapp.adapter.MessageAdapter;
import com.example.chatapp.model.Message;

import java.util.ArrayList;
import java.util.List;

public class ChatActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private MessageAdapter messageAdapter;
    private List<Message> messageList = new ArrayList<>();
    private EditText inputMessage;
    private Button btnSend;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        recyclerView = findViewById(R.id.recyclerView);
        inputMessage = findViewById(R.id.inputMessage);
        btnSend = findViewById(R.id.btnSend);

        // Настройка адаптера и LayoutManager для RecyclerView
        messageAdapter = new MessageAdapter(messageList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(messageAdapter);

        // Обработчик нажатия на кнопку "Отправить"
        btnSend.setOnClickListener(view -> sendMessage());
    }

    private void sendMessage() {
        String messageText = inputMessage.getText().toString().trim();

        if (!TextUtils.isEmpty(messageText)) {
            // Создаём сообщение и добавляем его в список
            Message message = new Message(messageText, "User", System.currentTimeMillis());
            messageList.add(message);

            // Уведомляем адаптер об изменении данных и скроллим к последнему сообщению
            messageAdapter.notifyItemInserted(messageList.size() - 1);
            recyclerView.scrollToPosition(messageList.size() - 1);

            // Очищаем поле ввода
            inputMessage.setText("");
        }
    }
}
