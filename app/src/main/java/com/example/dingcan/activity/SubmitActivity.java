package com.example.dingcan.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.dingcan.R;
import com.example.dingcan.adapter.GoodAdapter;
import com.example.dingcan.adapter.SubmitAdapter;
import com.example.dingcan.entity.CustomerFS;
import com.example.dingcan.entity.ShoppingCart;
import com.example.dingcan.entity.User;
import com.example.dingcan.util.HttpUtil;
import com.google.gson.Gson;
import okhttp3.*;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SubmitActivity extends AppCompatActivity {

    private EditText address;
    private EditText phone;
    private Button submit;
    private RecyclerView recyclerView;
    private List<ShoppingCart> shoppingCartList;
    private int flag;
    private User user;
    private float totalprice;
    private TextView price;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_submit);

        address = findViewById(R.id.submit_edit_address);
        phone = findViewById(R.id.submit_edit_phone);
        recyclerView = findViewById(R.id.submit_recycle_view);
        submit = findViewById(R.id.submit_btn_submit);
        price = findViewById(R.id.submit_text_totalprice);
        shoppingCartList = new ArrayList<>();

        Bundle bundle = getIntent().getExtras();
        flag = bundle.getInt("flag");
        if(flag==1) {
            user = (User) bundle.getSerializable("user");
            address.setText(user.getAddress());
            phone.setText(user.getTelephone());
        }
        shoppingCartList = (List<ShoppingCart>) bundle.getSerializable("shopCartList");
        totalprice = bundle.getFloat("totalprice");
        price.setText("￥"+totalprice);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String addressstr = address.getText().toString().trim();
                String phonestr = phone.getText().toString().trim();
                if (addressstr.equals("") || phonestr.equals("")) {
                    Toast mToast = Toast.makeText(getApplicationContext(), null, Toast.LENGTH_SHORT);
                    mToast.setText("输入项不能为空！");
                    mToast.show();
                    return;
                } else if (!phonestr.matches("^(1[3-9])\\d{9}$")){
                    Toast mToast = Toast.makeText(getApplicationContext(), null, Toast.LENGTH_SHORT);
                    mToast.setText("请输入正确的手机号码！");
                    mToast.show();
                    return;
                } else {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                String idstr = "";
                                String numstr = "";
                                int userid;
                                if (flag == 1)
                                    userid = user.getId();
                                else
                                    userid = -1;
                                for (int i = 0; i < shoppingCartList.size(); i++) {
                                    idstr = idstr + shoppingCartList.get(i).getsId() + ",";
                                    numstr = numstr + shoppingCartList.get(i).getsNum() + ",";
                                }
                                idstr = idstr.substring(0, idstr.length() - 1);
                                numstr = numstr.substring(0, numstr.length() - 1);

                                System.out.println("idstr:" + idstr);
                                System.out.println("numstr:" + numstr);
                                System.out.println("userid:" + String.valueOf(userid));
                                System.out.println("address:" + addressstr);
                                System.out.println("phonestr:" + phonestr);

                                OkHttpClient client = new OkHttpClient();
                                RequestBody requestBody = new FormBody.Builder()
                                        .add("id", idstr)
                                        .add("number", numstr)
                                        .add("customerId", String.valueOf(userid))
                                        .add("address", addressstr)
                                        .add("phone", phonestr)
                                        .build();
                                Request request = new Request.Builder()
                                        .url(HttpUtil.submitOrderUrl)
                                        .post(requestBody)
                                        .build();
                                Response response = client.newCall(request).execute();
                                String responseData = response.body().string();
                                System.out.println(responseData);
                                JSONObject jsonObject = new JSONObject(responseData);
                                int status = jsonObject.getInt("status");
                                if(status==1){
                                    if(flag==1){
                                        Looper.prepare();
                                        Toast mToast = Toast.makeText(getApplicationContext(), null, Toast.LENGTH_SHORT);
                                        mToast.setText("提交订单成功！");
                                        mToast.show();
                                        finish();
                                        Looper.loop();
                                    }else {
                                        Gson gson = new Gson();
                                        CustomerFS customerFS = gson.fromJson(jsonObject.getString("customer"),CustomerFS.class);
                                        String name = customerFS.getName();
                                        System.out.println(customerFS.toString());
                                        Looper.prepare();
                                        AlertDialog.Builder builder = new AlertDialog.Builder(SubmitActivity.this);
                                        builder.setTitle("自动注册");
                                        builder.setMessage("订单提交成功，已为您自动创建账号\n用户名："+name+"\n密码为您的手机号");
                                        builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                finish();
                                            }
                                        });
                                        builder.show();
                                        Looper.loop();
                                    }
                                }else {
                                    Looper.prepare();
                                    Toast mToast = Toast.makeText(getApplicationContext(), null, Toast.LENGTH_SHORT);
                                    mToast.setText("后台IO异常！");
                                    mToast.show();
                                    Looper.loop();
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

        recyclerView.setItemViewCacheSize(100);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        SubmitAdapter submitAdapter = new SubmitAdapter(shoppingCartList);
        recyclerView.setAdapter(submitAdapter);
    }

}
