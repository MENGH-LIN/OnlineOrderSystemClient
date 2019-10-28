package com.example.dingcan.activity;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import com.example.dingcan.R;
import com.example.dingcan.entity.Good;
import com.example.dingcan.entity.Order;
import com.example.dingcan.entity.Product;
import com.example.dingcan.entity.User;
import com.example.dingcan.myfragment.*;
import com.example.dingcan.util.HttpUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import okhttp3.Call;
import okhttp3.Response;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity{

    private RadioGroup radioGroup;
    private RadioButton radioButton;
    private dingcanFragment dingcan;
    private dingdanFragment dingdan;
    private myselfFragment myself;
    private shangjiaFragment shangjia;

    private int flag;
    private User user;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);

        //从login中获得的Bundle解析
        Bundle bundle = getIntent().getExtras();
        flag = bundle.getInt("flag");
        if(flag==1)
            user = (User) bundle.getSerializable("user");

        //传参给Fragment
        dingcan = new dingcanFragment();
        dingcan.setArguments(bundle);
        replaceFragment(dingcan);

        //设置切换Fragment
        radioGroup = (RadioGroup)findViewById(R.id.radiogroup);
        RadioGroupList radigGroupList = new RadioGroupList();
        radioGroup.setOnCheckedChangeListener(radigGroupList);

        //设置默认按钮为选中状态
        radioButton =(RadioButton) findViewById(R.id.btn_dingcan);
        radioButton.setChecked(true);
    }

    public class RadioGroupList implements RadioGroup.OnCheckedChangeListener
    {

        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId)
        {
            if(group.getId() == R.id.radiogroup)
            {
                switch (checkedId)
                {
                    case R.id.btn_dingcan:
                        dingcan = new dingcanFragment();
                        //传参给Fragment
                        Bundle toFrag1 = new Bundle();
                        toFrag1.putInt("flag",flag);
                        if(flag==1)
                            toFrag1.putSerializable("user",user);
                        dingcan.setArguments(toFrag1);
                        replaceFragment(dingcan);
                        Log.d("订餐", "提示");
                        break;
                    case R.id.btn_dingdan:
                        dingdan = new dingdanFragment();
                        //传参给Fragment
                        Bundle toFrag2 = new Bundle();
                        toFrag2.putInt("flag",flag);
                        if(flag==1)
                            toFrag2.putInt("id",user.getId());
                        dingdan.setArguments(toFrag2);
                        replaceFragment(dingdan);
                        Log.d("订单", "提示");
                        break;
                    case R.id.btn_shangjia:
                        shangjia = new shangjiaFragment();
                        replaceFragment(shangjia);
                        Log.d("商家", "提示");
                        break;
                    case R.id.btn_myself:
                        myself = new myselfFragment();
                        //传参给Fragment
                        Bundle toFrag4 = new Bundle();
                        toFrag4.putInt("flag",flag);
                        if(flag==1)
                            toFrag4.putSerializable("user",user);
                        myself.setArguments(toFrag4);
                        replaceFragment(myself);
                        Log.d("我的", "提示");
                        break;
                    default :
                        break;
                }
            }
        }
    }

    private void replaceFragment(Fragment fragment){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.fragment,fragment);
        transaction.commit();
    }

//    public List<Order> getOrder(){
//        List<Order> orderList = new ArrayList<>();
//        for(int i = 0;i<20;i++){
//            Order order = new Order();
//            order.setId(i);
//            order.setNumber("20191022"+i);
//            order.setDes("腐竹炒肉 等"+(i+1)+"份");
//            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");
//            order.setTime(df.format(new Date()));
//            order.setTotalprice("￥"+i);
//            order.setStatus("已送达");
//            orderList.add(order);
//        }
//        return orderList;
//    }
}
