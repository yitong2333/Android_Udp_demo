package com.yt.IntelligentPigsystem;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.yt.IntelligentPigsystem.db.MySqlHelper;

public class SigninActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_signin);
        EditText etName = findViewById(R.id.et_sign_name);
        EditText etPhone = findViewById(R.id.et_sign_phone);
        EditText etPass = findViewById(R.id.et_sign_pass);
        Button btnNew = findViewById(R.id.btn_sign_sign);
        btnNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String Phone = etPhone.getText().toString().trim();
                String Name = etName.getText().toString().trim();
                String Pass = etPass.getText().toString().trim();
                MySqlHelper.SignHelper signHelper = new MySqlHelper.SignHelper(SigninActivity.this);
                if (Phone.equals("")&&Name.equals("")&&Pass.equals("")){
                    Toast.makeText(getApplicationContext(),"用户名、手机号、密码不能为空",Toast.LENGTH_SHORT).show();
                }
                else{
                    signHelper.SignUp(Phone,Name,Pass);
                    finish();
                }
            }
        });
    }

}
