package com.example.secpay.RecyclerAdaptor;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.secpay.Merchant_home;
import com.example.secpay.R;
import com.example.secpay.User_home;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class User_generate_recy extends RecyclerView.Adapter<User_generate_recy.MyViewHolder>
{
    private static final String TAG = "RecyclerNews";
    private final Context context;
    private final JSONArray array;
    private static final String fsts ="0";
    Activity act;



    public User_generate_recy(Context applicationContext, JSONArray jsonArray, Activity a) {
        this.context = applicationContext;
        this.array = jsonArray;
        this.act = a;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.my_accepted_request_list, null);
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

            b1 =(Button)itemView.findViewById(R.id.generate);
            b1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String p_id = tv4.getText().toString();

                    RequestQueue requestQueue= Volley.newRequestQueue(context);
                    StringRequest requ=new StringRequest(Request.Method.POST, "http://192.168.21.253:8000/generate_qr_code/", new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response)
                        {

                            try {
                                JSONObject o = new JSONObject(response);
                                String dat = o.getString("msg");
                                Log.e("hai",dat);
                                if(dat.equals("QR code generated successfully in cwd"))
                                {
                                    Toast.makeText(context,"QR code generated successfully in cwd",Toast.LENGTH_SHORT).show();

                                    Intent i1= new Intent(context, User_home.class);
                                    context.startActivity(i1);

                                }
                                else {
                                    Log.e("hai","hai");
                                    Toast.makeText(context, "Error Occured! ", Toast.LENGTH_LONG).show();
                                }
                            }
                            catch (Exception e){
                                e.printStackTrace();

                            }

                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
//                Log.e(TAG,error.getMessage());
                            error.printStackTrace();
                        }
                    }){
                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {
                            Map<String,String> m=new HashMap<>();
                            m.put("p_id",p_id);

                            return m;
                        }
                    };
                    requestQueue.add(requ);
                }
            });
        }
    }

}

