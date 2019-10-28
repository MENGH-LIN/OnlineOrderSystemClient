package com.example.dingcan.myfragment;


import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.dingcan.R;
import com.example.dingcan.activity.LoginActivity;
import com.example.dingcan.activity.MainActivity;
import com.example.dingcan.adapter.GoodAdapter;
import com.example.dingcan.adapter.OrderAdapter;
import com.example.dingcan.entity.Order;
import com.example.dingcan.entity.OrderFS;
import com.example.dingcan.entity.OrderItemFS;
import com.example.dingcan.util.HttpUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import okhttp3.*;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class dingdanFragment extends Fragment {

    private View view;
    private LinearLayout nologin;
    private RecyclerView recyclerView;
    private Button tologin;
    private List<Order> orderList = new ArrayList<>();
    private Handler handler;
    private int flag;
    private int id;
    private String phone;

    private List<OrderFS> orderFSList;

    public dingdanFragment() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_dingdan, container, false);
        init(view);
        return view;
    }

    public void init(View view){
        nologin = view.findViewById(R.id.dingdan_nologin);
        recyclerView = view.findViewById(R.id.dingdan_recycle_view);
        recyclerView.setItemViewCacheSize(100);
        tologin = view.findViewById(R.id.dingdan_btn_login);

        tologin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                startActivity(intent);
            }
        });

        LinearLayoutManager layoutManager = new LinearLayoutManager(this.getActivity());
        recyclerView.setLayoutManager(layoutManager);

        Bundle bundle = getArguments();
        flag = bundle.getInt("flag");
        if(flag==1) {
            id = bundle.getInt("id");
            getData();
        }
        else{
            nologin.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        }

        handler = new Handler(){
            public void handleMessage(Message message){
                super.handleMessage(message);
                switch (message.what){
                    case 1:
                        Collections.sort(orderList, new Comparator<Order>() {
                            @Override
                            public int compare(Order o1, Order o2) {
                                return o2.getTime().compareTo(o1.getTime());
                            }
                        });
                        OrderAdapter orderAdapter = new OrderAdapter(getActivity(),orderList,orderFSList,phone);
                        recyclerView.setAdapter(orderAdapter);
                        break;
                    default:
                        break;
                }
            }
        };
    }

    public void getData(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    OkHttpClient client = new OkHttpClient();
                    RequestBody requestBody = new FormBody.Builder()
                            .add("id", String.valueOf(id))
                            .build();
                    Request request = new Request.Builder()
                            .url(HttpUtil.showCustomerAllOrder)
                            .post(requestBody)
                            .build();
                    Response response = client.newCall(request).execute();
                    String responseData = response.body().string();
                    System.out.println(responseData);
                    JSONObject jsonObject = new JSONObject(responseData);
                    int status = jsonObject.getInt("status");
                    System.out.println(status);
                    if (status == 1) {
                        phone = jsonObject.getString("phone");
                        String orderJson = jsonObject.getString("orderList");
                        Gson gson = new Gson();
                        List<OrderItemFS> orderItemFSList;
                        orderFSList = gson.fromJson(orderJson,new TypeToken<List<OrderFS>>(){}.getType());
                        System.out.println(orderFSList);
                        for(OrderFS orderFS:orderFSList){
                            orderItemFSList = orderFS.getOrderItems();
                            Order order = new Order();
                            order.setId(orderFS.getId());
                            order.setNumber(orderFS.getCode());
                            order.setTotalprice("￥"+orderFS.getTotal());
                            order.setDes(orderItemFSList.get(0).getProduct().getName()+" 共"+orderFS.getTotalNumber()+"份");
                            switch (orderFS.getStatus()){
                                case 0:
                                    order.setStatus("未接单");
                                    break;
                                case 1:
                                    order.setStatus("已接单");
                                    break;
                                case 2:
                                    order.setStatus("已送出");
                                    break;
                                case 3:
                                    order.setStatus("已送达");
                                    break;
                                default:
                                    order.setStatus("未知");
                                    break;
                            }
                            order.setTime(updateDate(orderFS.getCode()));
                            orderList.add(order);
                        }
                        Message message = new Message();
                        message.what = 1;
                        handler.sendMessage(message);
                    }else if(status == -1){
                        Looper.prepare();
                        Toast mToast = Toast.makeText(getActivity(), null, Toast.LENGTH_SHORT);
                        mToast.setText("您暂无订单");
                        mToast.show();
                        Looper.loop();
                        return;
                    }else{
                        Looper.prepare();
                        Toast mToast = Toast.makeText(getActivity(), null, Toast.LENGTH_SHORT);
                        mToast.setText("后台异常！");
                        mToast.show();
                        Looper.loop();
                        return;
                    }

                } catch (JSONException | IOException | ParseException e) {
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

    public String updateDate(String string) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        Date date = sdf.parse(string.substring(0,14));
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return simpleDateFormat.format(date);
    }
}