package com.example.dingcan.myfragment;


import android.content.Intent;
import android.os.Bundle;
//import android.app.Fragment;
import android.os.Looper;
import android.widget.*;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import com.example.dingcan.R;
import com.example.dingcan.activity.LoginActivity;
import com.example.dingcan.activity.MainActivity;
import com.example.dingcan.entity.User;
import com.example.dingcan.util.HttpUtil;
import com.example.dingcan.util.MD5Util2;
import okhttp3.*;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class myselfFragment extends Fragment {

    private View view;
    private LinearLayout nologin;
    private Button tologin;
    private LinearLayout login;
    private TextView tname;
    private EditText epassword;
    private EditText eaddress;
    private EditText etelephone;
    private Button update;
    private Button cancel;
    private int id;

    private int flag;
    private User user;

    private void init(View view) {

        nologin = view.findViewById(R.id.myself_nologin);
        login = view.findViewById(R.id.myself_login);
        tologin = view.findViewById(R.id.myself_btn_login);
        tname = view.findViewById(R.id.myself_text_name);
        epassword = view.findViewById(R.id.myself_edit_password);
        eaddress = view.findViewById(R.id.myself_edit_address);
        etelephone = view.findViewById(R.id.myself_edit_telephone);
        update = view.findViewById(R.id.myself_btn_update);
        cancel = view.findViewById(R.id.myself_btn_cancel);

        Bundle bundle = getArguments();
        flag = bundle.getInt("flag");
        if (flag==1) {
            user = (User) bundle.getSerializable("user");
            tname.setText(user.getName());
            eaddress.setText(user.getAddress());
            etelephone.setText(user.getTelephone());
            id = user.getId();
        }else {
            nologin.setVisibility(View.VISIBLE);
            login.setVisibility(View.GONE);
        }


        tologin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                startActivity(intent);
            }
        });
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String name = tname.getText().toString().trim();
                final String password = epassword.getText().toString().trim();
                final String address = eaddress.getText().toString().trim();
                final String telephone = etelephone.getText().toString().trim();

                if(password.equals("")||address.equals("")||telephone.equals("")) {
                    Toast mToast = Toast.makeText(getActivity(), null, Toast.LENGTH_SHORT);
                    mToast.setText("任意输入框不能为空");
                    mToast.show();
                    return;
                }else if(!telephone.matches("^(1[3-9])\\d{9}$")){
                    Toast mToast = Toast.makeText(getActivity(), null, Toast.LENGTH_SHORT);
                    mToast.setText("请输入正确的手机号码！");
                    mToast.show();
                    return;
                } else if(password.length()<6){
                    Toast mToast = Toast.makeText(getActivity(), null, Toast.LENGTH_SHORT);
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
                                        .add("id", String.valueOf(id))
                                        .add("name",name)
                                        .add("password",MD5Util2.getStringMD5(password))
                                        .add("address",address)
                                        .add("phone",telephone)
                                        .build();
                                Request request = new Request.Builder()
                                        .url(HttpUtil.updateUrl)
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
                                    Toast mToast = Toast.makeText(getActivity(), null, Toast.LENGTH_SHORT);
                                    mToast.setText("修改成功，将退出账号，请重新登录！");
                                    mToast.show();
                                    Intent intent = new Intent(getActivity(),LoginActivity.class);
                                    startActivity(intent);
                                    getActivity().finish();
                                    Looper.loop();
                                }else {
                                    Looper.prepare();
                                    Toast mToast = Toast.makeText(getActivity(), null, Toast.LENGTH_SHORT);
                                    mToast.setText("后台IO异常，请稍后再试！");
                                    mToast.show();
                                    Looper.loop();
                                    return;
                                }
                            } catch (IOException | JSONException e) {
                                e.printStackTrace();
                                Looper.prepare();
                                Toast mToast = Toast.makeText(getActivity(), null, Toast.LENGTH_SHORT);
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
                Intent intent = new Intent(getActivity(),LoginActivity.class);
                startActivity(intent);
                getActivity().finish();
            }
        });
    }

    public myselfFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_myself, container, false);
        init(view);
        return view;
    }
}
