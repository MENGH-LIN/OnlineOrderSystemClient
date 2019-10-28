package com.example.dingcan.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.dingcan.R;
import com.example.dingcan.entity.Order;
import com.example.dingcan.entity.ShoppingCart;

import java.util.List;

public class SubmitAdapter extends RecyclerView.Adapter<SubmitAdapter.ViewHolder>{

    private List<ShoppingCart> shoppingCartList;

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView orderName;
        TextView orderNum;
        TextView orderPrice;

        public ViewHolder(View view) {
            super(view);

            orderName = view.findViewById(R.id.order_name);
            orderNum = view.findViewById(R.id.order_num);
            orderPrice = view.findViewById(R.id.order_price);

        }
    }

    public SubmitAdapter(List<ShoppingCart> shoppingCarts) {
        shoppingCartList = shoppingCarts;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.order_item, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ShoppingCart shoppingCart = shoppingCartList.get(position);
        holder.orderName.setText(shoppingCart.getsName());
        holder.orderNum.setText("X"+shoppingCart.getsNum());
        holder.orderPrice.setText("￥"+shoppingCart.getsTotalPrice());
    }
    @Override
    public int getItemCount() {
        return shoppingCartList.size();
    }
}
