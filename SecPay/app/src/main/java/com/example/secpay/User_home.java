package com.example.secpay;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;

import com.google.android.material.navigation.NavigationView;

public class User_home extends AppCompatActivity {
    Button b1;
    NavigationView navigationView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_home);

        navigationView = (NavigationView) findViewById(R.id.nav);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                if(id==R.id.home)
                {
                    Intent i4=new Intent(User_home.this,User_home.class);
                    startActivity(i4);

                }

                else if(id==R.id.selectp)
                {
//
                    Intent i3=new Intent(User_home.this,Select_Product.class);
                    startActivity(i3);
//                    Toast.makeText(getApplicationContext(),"You clicked View my Bucket",Toast.LENGTH_SHORT).show();

                }
                else if(id==R.id.generateqr)
                {
                    Intent i2=new Intent(User_home.this,Generate_qr.class);
                    startActivity(i2);
//                    Toast.makeText(getApplicationContext(),"You clicked Profile",Toast.LENGTH_SHORT).show();

                }
                else if(id==R.id.pstatus)
                {
                    Intent i2 = new Intent(User_home.this, User_Payment_Status.class);
                    startActivity(i2);
//                    Toast.makeText(getApplicationContext(),"You clicked Profile",Toast.LENGTH_SHORT).show();

                }
                else if(id==R.id.logout)
                {
                    Intent i1=new Intent(User_home.this,LoginPage.class);
                    startActivity(i1);
                }


                return true;
            }
        });

    }
}