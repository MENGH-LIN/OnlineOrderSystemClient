package com.example.dingcan.myfragment;

import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.dingcan.R;
import com.example.dingcan.activity.MainActivity;
import com.example.dingcan.adapter.GoodAdapter;
import com.example.dingcan.entity.*;
import com.example.dingcan.util.HttpUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import okhttp3.*;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class dingcanFragment extends Fragment {
    private View view;
    private RecyclerView recyclerView;
    private List<Good> goodList = new ArrayList<>();

    private int flag;
    private User user;

    private Handler handler;

    public dingcanFragment() {
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        //mxl文件
        view=inflater.inflate(R.layout.fragment_dingcan, container, false);
        init(view);
        return view;
    }

    public void init(View view){

        Bundle bundle = getArguments();
        flag = bundle.getInt("flag");
        if(flag==1)
            user = (User) bundle.getSerializable("user");

        recyclerView = view.findViewById(R.id.dingcan_recycle_view);
        recyclerView.setItemViewCacheSize(100);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this.getActivity());
        recyclerView.setLayoutManager(layoutManager);


//        for(int i=0;i<30;i++){
//            Good good = new Good();
//            good.setId(i);
//            good.setType("tao"+i);
//            good.setSell("月售 "+i);
//            good.setName("jiangtao"+i);
//            good.setDes("hahahahaha");
//            good.setImg("meiyou");
//            good.setPrice("￥"+i);
//            goodList.add(good);
//        }
        getData();

        handler = new Handler(){
            public void handleMessage(Message message){
                super.handleMessage(message);
                switch (message.what){
                    case 1:
                        GoodAdapter goodAdapter = new GoodAdapter(view,goodList,flag,user,getActivity());
                        recyclerView.setAdapter(goodAdapter);
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
                    Request request = new Request.Builder()
                            .url(HttpUtil.showAllGoods)
                            .build();
                    Response response = client.newCall(request).execute();
                    String responseData = response.body().string();

                    System.out.println(responseData);

                    Gson gson = new Gson();
                    List<Product> productList = gson.fromJson(responseData,new TypeToken<List<Product>>(){}.getType());
                    for(Product product:productList){
                        Good good = new Good();
                        good.setId(product.getId());
                        good.setType(product.getCategory().getName());
                        good.setSell("月售 "+product.getNumber());
                        good.setName(product.getName());
                        good.setDes(product.getMiaoshu());
                        good.setImg(product.getImageurl());
                        good.setPrice("￥"+product.getPrice());
                        goodList.add(good);
                    }
                    Message message = new Message();
                    message.what = 1;
                    handler.sendMessage(message);
                } catch (IOException e) {
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
