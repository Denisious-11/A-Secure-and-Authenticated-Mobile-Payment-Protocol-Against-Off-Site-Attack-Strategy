package com.example.secpay;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.navigation.NavigationView;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Scan_Page extends AppCompatActivity implements View.OnClickListener
{
    Button scanBtn,b1;
    TextView messageText;
    NavigationView navigationView;
    String get_username;
    String hash_value;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_page);

        SharedPreferences prefs = getSharedPreferences("userdetails", MODE_PRIVATE);
        String username=prefs.getString("username", "");
        get_username=username;
        b1=(Button)findViewById(R.id.get_med_info);
        scanBtn = (Button) findViewById(R.id.scanBtn);
        messageText = findViewById(R.id.textContent);


        // adding listener to the button
        scanBtn.setOnClickListener(this);

        b1.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                RequestQueue requestQueue= Volley.newRequestQueue(getApplicationContext());
                StringRequest requ=new StringRequest(Request.Method.POST, "http://192.168.21.253:8000/verify_decode/", new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response)
                    {

                        Log.e("Response is: ", response.toString());
                        try
                        {
                            JSONObject o = new JSONObject(response);
                            String dat = o.getString("msg");
                            if (dat.equals("Invalid payment token. Payment validation failed."))
                            {
                                Toast.makeText(Scan_Page.this, "Invalid payment token. Payment validation failed.", Toast.LENGTH_SHORT).show();
                                Intent i1=new Intent(Scan_Page.this,Merchant_home.class);
                                startActivity(i1);
                            }
                            else if(dat.equals("Signature verification failed. The QR code may have been tampered with."))
                            {
                                Toast.makeText(Scan_Page.this, "Signature verification failed. The QR code may have been tampered with.", Toast.LENGTH_SHORT).show();
                                Intent i1=new Intent(Scan_Page.this,Merchant_home.class);
                                startActivity(i1);
                            }
                            else if(dat.equals("Payment successful"))
                            {
                                Toast.makeText(Scan_Page.this, "Payment successful", Toast.LENGTH_SHORT).show();
                                Intent i1=new Intent(Scan_Page.this,Merchant_home.class);
                                startActivity(i1);
                            }
                            else
                            {
                                Toast.makeText(Scan_Page.this, "Payment Failed", Toast.LENGTH_SHORT).show();
                                Intent i1=new Intent(Scan_Page.this,Merchant_home.class);
                                startActivity(i1);
                            }
                        }
                        catch (Exception e)
                        {
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
                        m.put("value", hash_value);
                        m.put("username",get_username);

                        return m;
                    }

                };
                requestQueue.add(requ);
            }
        });
    }

    @Override
    public void onClick(View v) {
        // we need to create the object
        // of IntentIntegrator class
        // which is the class of QR library
        IntentIntegrator intentIntegrator = new IntentIntegrator(this);
        intentIntegrator.setPrompt("Scan a barcode or QR Code");
        intentIntegrator.setOrientationLocked(true);
        intentIntegrator.initiateScan();
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        IntentResult intentResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        // if the intentResult is null then
        // toast a message as "cancelled"
        if (intentResult != null)
        {
            if (intentResult.getContents() == null)
            {
                Toast.makeText(getBaseContext(), "Cancelled", Toast.LENGTH_SHORT).show();
            } else
            {
                // if the intentResult is not null we'll set
                // the content and format of scan message
                hash_value=intentResult.getContents();
//                messageText.setText(intentResult.getContents());
                messageText.setText("Scanned Successfully");//


            }
        }
        else
        {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

}