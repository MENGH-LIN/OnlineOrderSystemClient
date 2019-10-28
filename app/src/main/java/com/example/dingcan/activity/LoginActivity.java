package com.example.dingcan.activity;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Looper;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import com.example.dingcan.R;
import com.example.dingcan.entity.User;
import com.example.dingcan.util.MD5Util2;
import okhttp3.*;
import org.json.JSONException;
import org.json.JSONObject;
import com.example.dingcan.util.HttpUtil;

import java.io.IOException;

public class LoginActivity extends AppCompatActivity {
    private EditText ename;
    private EditText epassword;
    private Button login;
    private Button register;
    private Button nologin;
    private TextView forget;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_login);

        login = (Button) findViewById(R.id.login_btn_login);
        register = (Button) findViewById(R.id.login_btn_register);
        nologin = (Button) findViewById(R.id.login_btn_nologin);
        forget = (TextView) findViewById(R.id.login_text_forgetpwd);
        ename = (EditText) findViewById(R.id.login_edit_name);
        epassword = (EditText) findViewById(R.id.login_edit_password);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String name = ename.getText().toString().trim();
                final String password = epassword.getText().toString().trim();
                if (name.equals("") || password.equals("")) {
                    Toast mToast = Toast.makeText(getApplicationContext(), null, Toast.LENGTH_SHORT);
                    mToast.setText("用户名或密码不能为空");
                    mToast.show();
                    return;
                }else if(password.length()<6){
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
                                        .add("password",password)
                                        .build();
                                Request request = new Request.Builder()
                                        .url(HttpUtil.loginUrl)
                                        .post(requestBody)
                                        .build();
                                Response response = client.newCall(request).execute();
                                String responseData = response.body().string();

                                System.out.println(responseData);

                                JSONObject jsonObject = new JSONObject(responseData);
                                int status = jsonObject.getInt("status");
                                System.out.println(status);

                                if(status==1) {
                                    String customer = jsonObject.getString("customer");
                                    System.out.println(customer);
                                    JSONObject userInfo = new JSONObject(customer);
                                    int id = userInfo.getInt("id");
                                    String name = userInfo.getString("name");
                                    String password = userInfo.getString("password");
                                    String address = userInfo.getString("address");
                                    String telephone = userInfo.getString("phone");
                                    User user = new User();
                                    user.setId(id);
                                    user.setName(name);
                                    user.setPassword(password);
                                    user.setAddress(address);
                                    user.setTelephone(telephone);
                                    System.out.println(id + name + password  + address + telephone);
                                    Looper.prepare();
                                    Toast mToast = Toast.makeText(getApplicationContext(), null, Toast.LENGTH_SHORT);
                                    mToast.setText("登录成功！");
                                    mToast.show();
                                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                    Bundle bundle = new Bundle();
                                    bundle.putSerializable("user",user);
                                    bundle.putInt("flag",1);
                                    intent.putExtras(bundle);
                                    startActivity(intent);
                                    finish();
                                    Looper.loop();
                                }else if(status==-1){
                                    Looper.prepare();
                                    Toast mToast = Toast.makeText(getApplicationContext(), null, Toast.LENGTH_SHORT);
                                    mToast.setText("用户不存在或密码错误，请检查后再试！");
                                    mToast.show();
                                    Looper.loop();
                                    return;
                                }else {
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

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });

        forget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, ForgetActivity.class);
                startActivity(intent);
            }
        });

        nologin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                Bundle bundle = new Bundle();
                bundle.putInt("flag",0);
                intent.putExtras(bundle);
                startActivity(intent);
                finish();
            }
        });
    }
}

//    private void sendRequestWithOkHttp() {
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                try {
//                    Looper.prepare();//增加部分
//                    OkHttpClient client = new OkHttpClient();
//                    RequestBody requestBody= new FormBody.Builder()
//                            .add("username",name.getText().toString())
//                            .add("password",password.getText().toString())
//                            .build();
//                    Request request = new Request.Builder()
//                            .url(HttpUtil.loginUrl)
//                            .post(requestBody)
//                            .build();
//                    Response response = client.newCall(request).execute();
//                    String responseData = response.body().string();
//
//                    JSONObject jsonObject = new JSONObject(responseData);
//                    String status = jsonObject.getString("status");
//                    System.out.println(status);
//                    JSONObject userInfo = jsonObject.getJSONObject("user");
//                    int id = userInfo.getInt("id");
//                    String name = userInfo.getString("name");
//                    String password = userInfo.getString("password");
//                    String sex = userInfo.getString("sex");
//                    String address = userInfo.getString("address");
//                    String telephone = userInfo.getString("telephone");
//                    User user = new User();
//                    user.setId(id);
//                    user.setName(name);
//                    user.setPassword(password);
//                    user.setSex(sex);
//                    user.setAddress(address);
//                    user.setTelephone(telephone);
//                    System.out.println(id+name+password+sex+address+telephone);
//                    Toast.makeText(getApplicationContext(),status,Toast.LENGTH_LONG).show();
//                    Intent intent = new Intent(LoginActivity.this,Main2Activity.class);
//                    intent.putExtra("user",user);
//                    startActivity(intent);
//                    Looper.loop();//增加部分
//                } catch (IOException | JSONException e) {
//                    e.printStackTrace();
//                }
//            }
//        }).start();
//    }
//}
