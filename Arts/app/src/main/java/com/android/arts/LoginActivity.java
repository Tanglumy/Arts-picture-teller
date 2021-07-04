package com.android.arts;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;



/**
 * 登录页面
 */
public class LoginActivity extends AppCompatActivity {
    MySqliteOpenHelper helper = null;
    private EditText etAccount;//手机号
    private EditText etPassword;//密码
    private Button btnRegister;//注册
    private Button btnLogin;//登录按钮
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        helper = new MySqliteOpenHelper(this);
        etAccount = findViewById(R.id.et_account);//获取手机号
        etPassword=findViewById(R.id.et_password);//获取密码
        btnRegister=findViewById(R.id.tv_register);//获取注册
        btnLogin=findViewById(R.id.btn_login);//获取登录
        //手机号注册
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //跳转到注册页面
                Intent intent=new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });
        //设置点击按钮
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //关闭虚拟键盘
                InputMethodManager inputMethodManager= (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(v.getWindowToken(),0);
                //获取请求参数
                String account= etAccount.getText().toString();
                String password=etPassword.getText().toString();
                if ("".equals(account)){//账号不能为空
                    Toast.makeText(LoginActivity.this,"账号不能为空", Toast.LENGTH_LONG).show();
                    return;
                }
                if ("".equals(password)){//密码为空
                    Toast.makeText(LoginActivity.this,"密码为空", Toast.LENGTH_LONG).show();
                    return;
                }
                User mUser = null;
                //查询用户是否存在
                String sql = "select * from user where account = ?";
                SQLiteDatabase db = helper.getWritableDatabase();
                Cursor cursor = db.rawQuery(sql, new String[]{account});
                if (cursor != null && cursor.getColumnCount() > 0) {
                    while (cursor.moveToNext()) {
                        String dbId = cursor.getString(0);
                        String dbAccount = cursor.getString(1);
                        String dbName = cursor.getString(2);
                        String dbPassword = cursor.getString(3);
                        mUser = new User(dbId, dbAccount,dbName,dbPassword);
                    }
                }
                db.close();
                if (mUser != null) {
                    if (!password.equals(mUser.getPassword())) {
                        Toast.makeText(LoginActivity.this, "密码错误", Toast.LENGTH_SHORT).show();
                    }else{
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    }
                }else{
                    Toast.makeText(LoginActivity.this, "账号不存在", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
