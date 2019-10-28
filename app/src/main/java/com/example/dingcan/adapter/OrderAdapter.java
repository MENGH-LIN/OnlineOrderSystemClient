package com.example.dingcan.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.dingcan.activity.OrderActivity;
import com.example.dingcan.entity.*;
import com.example.dingcan.R;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.ViewHolder>{

    private List<Order> orderList;
    private Context context;
    private String phone;

    private List<OrderFS> orderFSList;

    static class ViewHolder extends RecyclerView.ViewHolder{
        TextView orderNumber;
        TextView orderDes;
        TextView orderTime;
        TextView orderTotalPrice;
        TextView orderStatus;


        Button orderBtn;

        public ViewHolder(View view){
            super(view);

            orderNumber = (TextView)view.findViewById(R.id.dingdan_text_number);
            orderDes = (TextView)view.findViewById(R.id.dingdan_text_des);
            orderTime = (TextView)view.findViewById(R.id.dingdan_text_time);
            orderTotalPrice = (TextView)view.findViewById(R.id.dingdan_text_totalPrice);
            orderStatus = (TextView)view.findViewById(R.id.dingdan_text_status);
            orderBtn = view.findViewById(R.id.dingdan_btn_xiangqing);

        }
    }

    public OrderAdapter(Context context,List<Order> orders,List<OrderFS> orderFSList,String s){
        phone = s;
        this.context = context;
        orderList = orders;
        this.orderFSList = orderFSList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.dingdan_item,parent,false);
        ViewHolder holder = new ViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Order order= orderList.get(position);
        holder.orderNumber.setText(order.getNumber());
        holder.orderTime.setText(order.getTime());
        holder.orderStatus.setText(order.getStatus());
        holder.orderDes.setText(order.getDes());
        holder.orderTotalPrice.setText(order.getTotalprice());

        holder.orderBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<ShoppingCart> shoppingCartList = new ArrayList<>();
                String address="";
                String aphone="";
                float totalprice = 0;
                for(OrderFS orderFS:orderFSList){
                    if(orderFS.getCode()==holder.orderNumber.getText()){
                        address = orderFS.getAddress();
                        aphone = phone;
                        totalprice = orderFS.getTotal();
                        for(int i=0;i<orderFS.getOrderItems().size();i++){
                            OrderItemFS orderItemFS = orderFS.getOrderItems().get(i);
                            ShoppingCart shoppingCart = new ShoppingCart();
                            shoppingCart.setsId(orderItemFS.getId());
                            shoppingCart.setsName(orderItemFS.getProduct().getName());
                            shoppingCart.setsNum(orderItemFS.getNumber());
                            shoppingCart.setsPrice(orderItemFS.getProduct().getPrice());
                            float a = shoppingCart.getsNum()*shoppingCart.getsPrice();
                            shoppingCart.setsTotalPrice(a);
                            shoppingCartList.add(shoppingCart);
                        }
                        break;
                    }
                }
                Intent intent = new Intent(context, OrderActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("address",address);
                bundle.putString("phone",aphone);
                bundle.putFloat("total",totalprice);
                bundle.putSerializable("item", (Serializable) shoppingCartList);
                System.out.println(address);
                System.out.println(aphone);
                System.out.println(totalprice);
                System.out.println(shoppingCartList);
                intent.putExtras(bundle);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return orderList.size();
    }
}
