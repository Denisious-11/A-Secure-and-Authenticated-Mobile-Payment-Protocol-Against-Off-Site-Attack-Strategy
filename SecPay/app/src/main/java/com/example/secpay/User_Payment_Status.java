package com.example.secpay;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.secpay.RecyclerAdaptor.Merchant_payment_recy;
import com.example.secpay.RecyclerAdaptor.User_payment_recy;
import com.google.android.material.navigation.NavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class User_Payment_Status extends AppCompatActivity
{
    RecyclerView recv;
    NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_payment_status);

        SharedPreferences prefs = getSharedPreferences("userdetails", MODE_PRIVATE);
        String username=prefs.getString("username", "");

        recv=(RecyclerView)findViewById(R.id.recyclerView3);
        RequestQueue requestQueue= Volley.newRequestQueue(getApplicationContext());
        StringRequest requ=new StringRequest(Request.Method.POST, "http://192.168.21.253:8000/fetch_user_payment_status/", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject object = new JSONObject(response.trim());

                    JSONArray jsonArray = object.getJSONArray("data");
                    User_payment_recy ordrec = new User_payment_recy(User_Payment_Status.this, jsonArray, User_Payment_Status.this);

                    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(User_Payment_Status.this);
                    linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                    DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recv.getContext(),
                            linearLayoutManager.getOrientation());
                    recv.addItemDecoration(dividerItemDecoration);
                    recv.setLayoutManager(linearLayoutManager);
                    recv.setAdapter(ordrec);


                } catch (JSONException e) {
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
                m.put("username",username);

                return m;
            }
        };
        requestQueue.add(requ);

        navigationView = (NavigationView) findViewById(R.id.nav);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                if(id==R.id.home)
                {
                    Intent i4=new Intent(User_Payment_Status.this,User_home.class);
                    startActivity(i4);

                }

                else if(id==R.id.selectp)
                {
//
                    Intent i3=new Intent(User_Payment_Status.this,Select_Product.class);
                    startActivity(i3);
//                    Toast.makeText(getApplicationContext(),"You clicked View my Bucket",Toast.LENGTH_SHORT).show();

                }
                else if(id==R.id.generateqr)
                {
                    Intent i2=new Intent(User_Payment_Status.this,Generate_qr.class);
                    startActivity(i2);
//                    Toast.makeText(getApplicationContext(),"You clicked Profile",Toast.LENGTH_SHORT).show();

                }
                else if(id==R.id.pstatus)
                {
                    Intent i2 = new Intent(User_Payment_Status.this, User_Payment_Status.class);
                    startActivity(i2);
//                    Toast.makeText(getApplicationContext(),"You clicked Profile",Toast.LENGTH_SHORT).show();

                }
                else if(id==R.id.logout)
                {
                    Intent i1=new Intent(User_Payment_Status.this,LoginPage.class);
                    startActivity(i1);
                }


                return true;
            }
        });
    }
}