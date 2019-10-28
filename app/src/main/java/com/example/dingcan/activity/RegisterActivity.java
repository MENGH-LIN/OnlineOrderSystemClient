package com.example.dingcan.activity;

import android.content.Intent;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.example.dingcan.R;
import com.example.dingcan.entity.User;
import com.example.dingcan.util.HttpUtil;
import com.example.dingcan.util.MD5Util2;
import okhttp3.*;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class RegisterActivity extends AppCompatActivity {

    private EditText ename;
    private EditText epassword1;
    private EditText epassword2;
    private EditText eaddress;
    private EditText etelephone;
    private Button register;
    private Button cancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        register = (Button)findViewById(R.id.register_btn_sure);
        cancel = (Button)findViewById(R.id.register_btn_cancel);
        ename = (EditText)findViewById(R.id.register_edit_name);
        epassword1 = (EditText)findViewById(R.id.register_edit_password_1);
        epassword2 = (EditText)findViewById(R.id.register_edit_password_2);
        eaddress = (EditText)findViewById(R.id.register_edit_address);
        etelephone = (EditText)findViewById(R.id.register_edit_telephone);

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String name = ename.getText().toString().trim();
                final String password1 = epassword1.getText().toString().trim();
                String password2 = epassword2.getText().toString().trim();
                final String address = eaddress.getText().toString().trim();
                final String telephone = etelephone.getText().toString().trim();

                if(name.equals("")||password1.equals("")||password2.equals("")||address.equals("")||telephone.equals("")) {
                    Toast mToast = Toast.makeText(getApplicationContext(), null, Toast.LENGTH_SHORT);
                    mToast.setText("任意输入框不能为空");
                    mToast.show();
                    return;
                }else if(!password1.equals(password2)){
                    Toast mToast = Toast.makeText(getApplicationContext(), null, Toast.LENGTH_SHORT);
                    mToast.setText("两次密码不一致！");
                    mToast.show();
                    return;
                }else if(!telephone.matches("^(1[3-9])\\d{9}$")){
                    Toast mToast = Toast.makeText(getApplicationContext(), null, Toast.LENGTH_SHORT);
                    mToast.setText("请输入正确的手机号码！");
                    mToast.show();
                    return;
                } else if(password1.length()<6){
                    Toast mToast = Toast.makeText(getApplicationContext(), null, Toast.LENGTH_SHORT);
                    mToast.setText("密码应大于6位");
                    mToast.show();
                    return;
                } else {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                OkHttpClient client = new OkHttpClient();
                                RequestBody requestBody= new FormBody.Builder()
                                        .add("name",name)
                                        .add("password",password1)
                                        .add("address",address)
                                        .add("phone",telephone)
                                        .build();
                                Request request = new Request.Builder()
                                        .url(HttpUtil.registerUrl)
                                        .post(requestBody)
                                        .build();
                                Response response = client.newCall(request).execute();
                                String responseData = response.body().string();

                                System.out.println(responseData);

                                JSONObject jsonObject = new JSONObject(responseData);
                                int status = jsonObject.getInt("status");
                                System.out.println(status);

                                if(status==1) {
                                    Looper.prepare();
                                    Toast mToast = Toast.makeText(getApplicationContext(), null, Toast.LENGTH_SHORT);
                                    mToast.setText("注册成功！将返回登录界面");
                                    mToast.show();
                                    finish();
                                    Looper.loop();
                                }else if(status==-1){
                                    Looper.prepare();
                                    Toast mToast = Toast.makeText(getApplicationContext(), null, Toast.LENGTH_SHORT);
                                    mToast.setText("该用户名已注册，换一个试试吧！");
                                    mToast.show();
                                    Looper.loop();
                                }
                                else {
                                    Looper.prepare();
                                    Toast mToast = Toast.makeText(getApplicationContext(), null, Toast.LENGTH_SHORT);
                                    mToast.setText("后台IO异常，请稍后再试！");
                                    mToast.show();
                                    Looper.loop();
                                    return;
                                }
                            } catch (IOException | JSONException e) {
                                e.printStackTrace();
                                Looper.prepare();
                                Toast mToast = Toast.makeText(getApplicationContext(), null, Toast.LENGTH_SHORT);
                                mToast.setText("服务器异常，请检查网络！");
                                mToast.show();
                                Looper.loop();
                            }
                        }
                    }).start();
                }
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
