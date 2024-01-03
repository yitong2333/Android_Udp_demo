package com.yt.IntelligentPigsystem;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.yt.IntelligentPigsystem.db.MySqlHelper;

public class LoginActivity extends AppCompatActivity {
    SQLiteDatabase db;
    MySqlHelper helper;
    //轻量存储库
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_login);
        EditText etAccount = findViewById(R.id.et_login_account);
        EditText etPassword = findViewById(R.id.et_login_password);
        Button btnLogin = findViewById(R.id.btn_login_login);
        Button btnSignIn = findViewById(R.id.btn_login_sign);
        Button btnForget = findViewById(R.id.btn_login_forget);
        helper = new MySqlHelper(getApplicationContext());
        db = helper.getReadableDatabase();
        sharedPreferences = getSharedPreferences("User",MODE_PRIVATE);
        editor = sharedPreferences.edit();
        checkIfUserLoggedIn();
        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this,SigninActivity.class);
                startActivity(intent);
            }
        });
        btnForget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this,ForgetActivity.class);
                startActivity(intent);
            }
        });
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String Tel = etAccount.getText().toString().trim();
                String Pass = etPassword.getText().toString().trim();
                MySqlHelper.SignHelper signHelper = new MySqlHelper.SignHelper(getApplicationContext());
                boolean isLogin = signHelper.Login(db,Tel,Pass);
                if (isLogin){
                    editor.putString("Phone",Tel);
                    editor.apply();
                    Intent intent = new Intent(LoginActivity.this,MainActivity.class);
                    startActivity(intent);
                }
            }
        });


    }
    private void checkIfUserLoggedIn() {
        String userPhone = sharedPreferences.getString("Phone", null);
        if (userPhone != null) {
            // 用户已登录，直接跳转到主界面
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
            finish(); // 关闭登录界面
        }
        // 否则，用户未登录，保持在登录界面
    }
}
