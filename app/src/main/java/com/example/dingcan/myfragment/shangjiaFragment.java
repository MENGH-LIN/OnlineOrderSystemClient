package com.example.dingcan.myfragment;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.TextView;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.dingcan.R;
import com.example.dingcan.util.ShangjiaUtil;

public class shangjiaFragment extends Fragment {

    private View view;
    private TextView name;
    private TextView type;
    private TextView address;
    private TextView phone;
    private TextView time;
    private TextView message;

    public void init(View view){
        name = view.findViewById(R.id.shangjia_text_name);
        type = view.findViewById(R.id.shangjia_text_type);
        address = view.findViewById(R.id.shangjia_text_address);
        phone = view.findViewById(R.id.shangjia_text_phone);
        time = view.findViewById(R.id.shangjia_text_time);
        message = view.findViewById(R.id.shangjia_text_message);

        name.setText(ShangjiaUtil.shangjiaName);
        type.setText(ShangjiaUtil.shangjiaType);
        address.setText(ShangjiaUtil.shangjiaAddress);
        phone.setText(ShangjiaUtil.shangjiaPhone);
        time.setText(ShangjiaUtil.shangjiaTime);
        message.setText(ShangjiaUtil.shangjiaMessage);

        phone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:"+ShangjiaUtil.shangjiaPhone));
                startActivity(intent);
            }
        });
    }

    public shangjiaFragment() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_shangjia, container, false);
        init(view);
        return view;
    }
}
