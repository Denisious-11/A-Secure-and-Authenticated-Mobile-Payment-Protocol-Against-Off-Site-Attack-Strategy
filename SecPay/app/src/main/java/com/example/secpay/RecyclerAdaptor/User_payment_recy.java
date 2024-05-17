package com.example.secpay.RecyclerAdaptor;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.secpay.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class User_payment_recy extends RecyclerView.Adapter<User_payment_recy.MyViewHolder>
{
    private static final String TAG = "RecyclerNews";
    private final Context context;
    private final JSONArray array;
    private static final String fsts ="0";
    Activity act;



    public User_payment_recy(Context applicationContext, JSONArray jsonArray, Activity a) {
        this.context = applicationContext;
        this.array = jsonArray;
        this.act = a;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.my_user_payment_list, null);
        return new MyViewHolder(view);
    }

    @SuppressLint("ResourceType")
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        try {
            JSONObject jsonObject = array.getJSONObject(position);

            holder.tv1.setText(jsonObject.getString("product_name"));
            holder.tv2.setText(jsonObject.getString("quantity"));
            holder.tv3.setText(jsonObject.getString("total_amount"));
            holder.tv4.setText(jsonObject.getString("p_id"));
            holder.tv5.setText(jsonObject.getString("status"));

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @Override
    public int getItemCount() {
        return array.length();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder
    {
        TextView tv1,tv2,tv3,tv4,tv5,tv6;
        ImageView iv1;
        CardView cv;
        Button b1;
        public MyViewHolder(@NonNull View itemView)
        {
            super(itemView);
            tv1 = (TextView) itemView.findViewById(R.id.pname);
            tv2 = (TextView) itemView.findViewById(R.id.qty);
            tv3 = (TextView) itemView.findViewById(R.id.tamount);
            tv4 = (TextView) itemView.findViewById(R.id.p_id);
            tv5 = (TextView) itemView.findViewById(R.id.status);


            cv=(CardView)itemView.findViewById(R.id.card_view) ;

        }
    }

}

