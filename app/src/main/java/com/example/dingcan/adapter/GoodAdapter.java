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
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;
import com.example.dingcan.activity.SubmitActivity;
import com.example.dingcan.entity.Good;
import com.example.dingcan.R;
import com.example.dingcan.entity.ShoppingCart;
import com.example.dingcan.entity.User;

import java.io.Serializable;
import java.util.*;

public class GoodAdapter extends RecyclerView.Adapter<GoodAdapter.ViewHolder>{

    private View parentView;
    private Context context;
    private List<Good> goodList;
    private List<ShoppingCart> shoppingCartList;

    private int flag;
    private User user;

    TextView shoppingNum;
    TextView shoppingPrice;
    TextView shoppingEmpty;
    TextView shoppingCheck;
    static class ViewHolder extends RecyclerView.ViewHolder{
        ImageView goodImage;
        TextView goodName;
        TextView goodDes;
        TextView goodSell;
        TextView goodPrice;
        TextView goodType;

        int goodId;

        ImageView goodAdd;
        ImageView goodDel;
        TextView goodSum;




        public ViewHolder(View view){
            super(view);

            goodImage = view.findViewById(R.id.good_img_icon);
            goodName = view.findViewById(R.id.good_text_name);
            goodDes = view.findViewById(R.id.good_text_des);
            goodSell = view.findViewById(R.id.good_text_sell);
            goodPrice = view.findViewById(R.id.good_text_price);
            goodType = view.findViewById(R.id.good_text_type);

            goodAdd = view.findViewById(R.id.good_img_add);
            goodDel = view.findViewById(R.id.good_img_delete);
            goodSum = view.findViewById(R.id.good_text_num);


        }
    }

    public GoodAdapter(View view, List<Good> goods, int flag, User user,Context context){
        this.context = context;
        parentView = view;
        goodList=goods;
        this.flag = flag;
        this.user = user;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.good_item,parent,false);
        ViewHolder holder = new ViewHolder(view);

        shoppingCartList = new ArrayList<>();

        shoppingNum = parentView.findViewById(R.id.gouwuche_text_num);
        shoppingEmpty = parentView.findViewById(R.id.gouwuche_text_empty);
        shoppingPrice = parentView.findViewById(R.id.gouwuche_text_totalPrice);
        shoppingCheck = parentView.findViewById(R.id.gouwuche_text_check);


        holder.goodAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                float price = Float.parseFloat(holder.goodPrice.getText().toString().replace("￥","").trim());
                float shopprice = Float.parseFloat(shoppingPrice.getText().toString().replace("￥","").trim());
                int num = Integer.parseInt(holder.goodSum.getText().toString().trim());
                int shopnum = Integer.parseInt(shoppingNum.getText().toString().trim());
                if(num==0){
                    num++;
                    holder.goodSum.setText(String.valueOf(num));
                    holder.goodSum.setVisibility(View.VISIBLE);
                    holder.goodDel.setVisibility(View.VISIBLE);
                    if(shopnum==0){
                        shoppingNum.setVisibility(View.VISIBLE);
                        shoppingNum.setText(String.valueOf(num));
                        shoppingEmpty.setVisibility(View.GONE);
                        shoppingPrice.setVisibility(View.VISIBLE);;
                        shoppingPrice.setText("￥"+price);
                    }else {
                        shopnum++;
                        shoppingNum.setText(String.valueOf(shopnum));
                        shoppingPrice.setText("￥"+(shopprice+price));
                    }
                }else{
                    num++;
                    holder.goodSum.setText(String.valueOf(num));
                    shopnum++;
                    shoppingNum.setText(String.valueOf(shopnum));
                    shoppingPrice.setText("￥"+(shopprice+price));
                }
                ShoppingCart shoppingCart = new ShoppingCart();
                shoppingCart.setsId(holder.goodId);
                shoppingCart.setsName(holder.goodName.getText().toString());
                shoppingCart.setsPrice(Float.parseFloat(holder.goodPrice.getText().toString().replace("￥","").trim()));
                boolean isOnly = true;
                if(shoppingCartList==null){
                    shoppingCart.setsNum(1);
                    shoppingCart.setsTotalPrice(shoppingCart.getsPrice());
                    shoppingCartList.add(shoppingCart);
                }else {
                    for (ShoppingCart shoppingCart1 : shoppingCartList) {
                        if (shoppingCart.getsId() == shoppingCart1.getsId()) {
                            shoppingCart1.setsNum(shoppingCart1.getsNum() + 1);
                            shoppingCart1.setsTotalPrice(shoppingCart1.getsTotalPrice() + shoppingCart1.getsPrice());
                            isOnly = false;
                            break;
                        }
                    }
                    if (isOnly) {
                        shoppingCart.setsNum(1);
                        shoppingCart.setsTotalPrice(shoppingCart.getsPrice());
                        shoppingCartList.add(shoppingCart);
                    }
                }
            }
        });

        holder.goodDel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                float price = Float.parseFloat(holder.goodPrice.getText().toString().replace("￥", "").trim());
                float shopprice = Float.parseFloat(shoppingPrice.getText().toString().replace("￥", "").trim());
                int num = Integer.parseInt(holder.goodSum.getText().toString().trim());
                int shopnum = Integer.parseInt(shoppingNum.getText().toString().trim());
                if (num == 1) {
                    num--;
                    holder.goodSum.setText(String.valueOf(num));
                    holder.goodSum.setVisibility(View.INVISIBLE);
                    holder.goodDel.setVisibility(View.INVISIBLE);
                    if (shopnum == 1) {
                        shoppingNum.setVisibility(View.INVISIBLE);
                        shoppingNum.setText(String.valueOf(num));
                        shoppingEmpty.setVisibility(View.VISIBLE);
                        shoppingPrice.setVisibility(View.GONE);
                        ;
                        shoppingPrice.setText("￥" + (shopprice - price));
                    } else {
                        shopnum--;
                        shoppingNum.setText(String.valueOf(shopnum));
                        shoppingPrice.setText("￥" + (shopprice - price));
                    }
                } else {
                    num--;
                    holder.goodSum.setText(String.valueOf(num));
                    shopnum--;
                    shoppingNum.setText(String.valueOf(shopnum));
                    shoppingPrice.setText("￥" + (shopprice - price));
                }
                Iterator<ShoppingCart> iterator = shoppingCartList.iterator();
                while (iterator.hasNext()){
                    ShoppingCart shoppingCart = iterator.next();
                    if(shoppingCart.getsId()==holder.goodId){
                        shoppingCart.setsNum(shoppingCart.getsNum()-1);
                        shoppingCart.setsTotalPrice(shoppingCart.getsTotalPrice()-shoppingCart.getsPrice());
                        if(shoppingCart.getsNum()==0)
                            iterator.remove();
                        break;
                    }
                }
            }
        });

        shoppingCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int shopnum = Integer.parseInt(shoppingNum.getText().toString().trim());
                if(shopnum==0){
                    Toast mToast = Toast.makeText(context, null, Toast.LENGTH_SHORT);
                    mToast.setText("购物车内没有信息，请添加！");
                    mToast.show();
                }else {
                    float totalprice = Float.parseFloat(shoppingPrice.getText().toString().replace("￥","").trim());
                    Bundle bundle = new Bundle();
                    bundle.putInt("flag",flag);
                    if (flag==1)
                        bundle.putSerializable("user", user);
                    bundle.putSerializable("shopCartList", (Serializable) shoppingCartList);
                    bundle.putFloat("totalprice",totalprice);
                    Intent intent = new Intent(context, SubmitActivity.class);
                    intent.putExtras(bundle);
                    context.startActivity(intent);
                }
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Good good = goodList.get(position);
        holder.goodName.setText(good.getName());
        holder.goodDes.setText(good.getDes());
        holder.goodSell.setText(good.getSell());
        holder.goodPrice.setText(good.getPrice());
        holder.goodImage.setImageResource(R.drawable.icon_logo_image_default);
        holder.goodType.setText(good.getType());
        holder.goodId = good.getId();
    }

    @Override
    public int getItemCount() {
        return goodList.size();
    }
}
