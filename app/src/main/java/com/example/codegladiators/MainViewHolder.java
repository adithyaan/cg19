package com.example.codegladiators;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

/**
 * Created by ADITHYA AN on 01-06-2018.
 */

class MainViewHolder extends RecyclerView.ViewHolder
{

    TextView name,address,date,amount;
    Button start_bid;
    CardView cv;


    public MainViewHolder(View itemView)
    {
        super(itemView);
        name=itemView.findViewById(R.id.name);
        date=itemView.findViewById(R.id.date);
        address=itemView.findViewById(R.id.address);
        amount=itemView.findViewById(R.id.amount);

    }

    public void inflate(final NewsFeedData model, final Context context)
    {
        Log.e("Data",model.toString());
        name.setText(model.getName());
        date.setText(model.getDate());
        address.setText(model.getAddress());
        amount.setText(model.getAmount());
        date.setText("23/05/2019");
        amount.setText("200");

//        cv.setOnClickListener(new View.OnClickListener()
//        {
//            @Override
//            public void onClick(View v)
//            {
//                Intent intent = new Intent(context,DetailActivity.class);
//                intent.putExtra("instance",model.getGoods_descp());
//                context.startActivity(intent);
//            }
//        });
    }

}
