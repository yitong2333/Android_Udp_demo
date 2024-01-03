package com.yt.IntelligentPigsystem;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.yt.IntelligentPigsystem.db.MySqlHelper;

public class ForgetActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_forget);
        EditText etPhone = findViewById(R.id.et_forget_phone);
        EditText etName = findViewById(R.id.et_forget_name);
        EditText etPass = findViewById(R.id.et_forget_pass);
        Button btnConfirm = findViewById(R.id.btn_forget_sign);
        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String tel = etPhone.getText().toString();
                String name = etName.getText().toString();
                String pass = etPass.getText().toString();
                MySqlHelper.SignHelper signHelper = new MySqlHelper.SignHelper(ForgetActivity.this);
                if (tel.equals("")&&name.equals("")&&pass.equals("")) {
                    Toast.makeText(getApplicationContext(),"用户名、手机号、新密码不能为空",Toast.LENGTH_SHORT).show();
                }
                else{
                    signHelper.Forget(tel,name,pass);
                    finish();
                }

            }
        });
    }
}
