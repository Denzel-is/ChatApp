package com.example.chatapp.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

;import com.example.chatapp.ChatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        Intent intent = new Intent(MainActivity.this, ChatActivity.class);
//        startActivity(intent);
//        finish();  // Завершаем MainActivity, чтобы не возвращаться сюда
        // Проверяем, залогинен ли пользователь
        if (isUserLoggedIn()) {
            // Если залогинен — переходим в ChatActivity
            Intent intent = new Intent(MainActivity.this, ChatActivity.class);
            startActivity(intent);
            finish();  // Завершаем MainActivity, чтобы не возвращаться сюда
        } else {
            // Если не залогинен — переходим в LoginActivity
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();  // Завершаем MainActivity
        }
    }

    // Пример проверки авторизации (можно использовать SharedPreferences)
    private boolean isUserLoggedIn() {
        try {
            SharedPreferences prefs = getSharedPreferences("ChatAppPrefs", MODE_PRIVATE);
            boolean isLoggedIn = prefs.getBoolean("isLoggedIn", false);
            return isLoggedIn;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

}
