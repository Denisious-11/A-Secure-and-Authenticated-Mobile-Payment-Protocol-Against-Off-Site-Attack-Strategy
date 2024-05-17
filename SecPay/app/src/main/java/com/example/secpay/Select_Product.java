package com.example.secpay;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Select_Product extends AppCompatActivity
{
    String[] products = { "Coca Cola","Fanta",};
    String ptype="Coca Cola";

    String[] quantities = { "1","2","3","4","5",};
    String qtype="1";
    Spinner spin1;
    Spinner spin2;
    Button b1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_product);
        b1=(Button)findViewById(R.id.searchbtn);
        SharedPreferences prefs = getSharedPreferences("userdetails", MODE_PRIVATE);
        String username=prefs.getString("username", "");

        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
                    StringRequest requ = new StringRequest(Request.Method.POST, "http://192.168.21.253:8000/add_purchase/", new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                            Log.e("Response is: ", response.toString());
                            try {
                                JSONObject o = new JSONObject(response);
                                String dat = o.getString("msg");
                                if(dat.equals("yes"))
                                {
                                    Toast.makeText(Select_Product.this, "Purchase request sent successfully", Toast.LENGTH_LONG).show();
                                    Intent i1=new Intent(Select_Product.this,User_home.class);
                                    startActivity(i1);
                                }
                                else if(dat.equals("Purchase request is in pending list"))
                                {
                                    Toast.makeText(Select_Product.this, "Purchase request is in pending list", Toast.LENGTH_LONG).show();
                                    Intent i1=new Intent(Select_Product.this,User_home.class);
                                    startActivity(i1);
                                }
                                else
                                {
                                    Toast.makeText(Select_Product.this, "Error Happened!!!", Toast.LENGTH_LONG).show();
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
                    }) {
                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {
                            Map<String, String> m = new HashMap<>();

                            m.put("productname", ptype);
                            m.put("quantity", qtype );
                            m.put("username",username);
                            return m;
                        }
                    };
                    requestQueue.add(requ);
                }
        });
        spin1 = (Spinner) findViewById(R.id.spinner3);
        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, products);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spin1.setAdapter(adapter);
        spin1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l)
            {
                ptype=products[i];
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        spin2 = (Spinner) findViewById(R.id.spinner2);
        ArrayAdapter adapter1 = new ArrayAdapter(this, android.R.layout.simple_spinner_item, quantities);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spin2.setAdapter(adapter1);
        spin2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int j, long l)
            {
                qtype=quantities[j];
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }
}