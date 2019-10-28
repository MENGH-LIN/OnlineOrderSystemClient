package com.example.dingcan.activity;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.dingcan.R;
import com.example.dingcan.adapter.SubmitAdapter;
import com.example.dingcan.entity.OrderFS;
import com.example.dingcan.entity.ShoppingCart;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class OrderActivity extends AppCompatActivity {

    private Button cancel;
    private TextView address;
    private TextView phone;
    private TextView price;
    private RecyclerView recyclerView;
    private List<ShoppingCart> shoppingCartList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);

        cancel = findViewById(R.id.order_btn_cancel);
        address = findViewById(R.id.order_text_address);
        phone = findViewById(R.id.order_text_phone);
        price = findViewById(R.id.order_text_price);
        recyclerView = findViewById(R.id.order_recycle_view);
        shoppingCartList = new ArrayList<>();


        Bundle bundle = getIntent().getExtras();
        address.setText(bundle.getString("address"));
        phone.setText(bundle.getString("phone"));
        price.setText("￥"+bundle.getFloat("total"));
        shoppingCartList = (List<ShoppingCart>) bundle.getSerializable("item");

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        recyclerView.setItemViewCacheSize(100);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        SubmitAdapter submitAdapter = new SubmitAdapter(shoppingCartList);
        recyclerView.setAdapter(submitAdapter);
    }
}
