package com.maxus376.kostromafastfooddelivery;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    String apiUrl = "http://172.20.10.7/kostroma-fastfood/kostromafastfood/public/api/users/delivery?email=";
    SharedPreferences sharedPreferences;
    ArrayList<ListDataOrder> arrayList;
    String order_id, address;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sharedPreferences = getSharedPreferences("KostromaFastFoodDelivery", MODE_PRIVATE);
        if (sharedPreferences.getString("login", "false").equals("false")) {
            Intent intent = new Intent(getApplicationContext(), Login.class);
            startActivity(intent);
            finish();
        }
        checkPermissions();
        fetchData();
        arrayList = new ArrayList<>();



        /*buttonSuccess.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                markOrderStatus("http://192.168.1.110/food-delivery-application/fooddeliveryapp/public/api/users/delivery/success?order_id=" + order_id,
                        "Доставлено");
            }
        });


        buttonLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(MainActivity.this, "Укажите приложение для показа адреса на карте", Toast.LENGTH_SHORT).show();
                Uri mapUri = Uri.parse("geo:0,0?q=" + Uri.encode(address));
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, mapUri);
                mapIntent.setPackage("com.google.android.apps.maps");
                startActivity(mapIntent);
            }
        });*/

    }
    public void onClickStatus() {
        markOrderStatus("http://172.20.10.7/kostroma-fastfood/kostromafastfood/public/api/users/delivery/success?order_id=" + order_id,
                "Доставлено");
    }

    public void onClickLocation() {
        Uri mapUri = Uri.parse("geo:0,0?q=" + Uri.encode(address));
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, mapUri);
        mapIntent.setPackage("com.google.android.apps.maps");
        startActivity(mapIntent);
    }

    public void checkPermissions() {
        if (ContextCompat.checkSelfPermission(MainActivity.this,
                android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this,
                    android.Manifest.permission.ACCESS_FINE_LOCATION)) {
                ActivityCompat.requestPermissions(MainActivity.this,
                        new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            } else {
                ActivityCompat.requestPermissions(MainActivity.this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            }
        }
    }

    public void markOrderStatus(String url, String status) {
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response.equals("success")) {

                            Toast.makeText(MainActivity.this, status, Toast.LENGTH_SHORT).show();

                        } else Toast.makeText(MainActivity.this, "Ошибка подключения",
                                Toast.LENGTH_SHORT).show();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });
        queue.add(stringRequest);
    }

    public void parseJSON(String data) {
        try {
            JSONArray jsonArray = new JSONArray(data);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject stu = jsonArray.getJSONObject(i);
                order_id = stu.getString("id");
                address = stu.getString("destination_address");
                String time = "23";
                String sum_price = stu.getString("sum_price");
                arrayList.add(new ListDataOrder("0", address, sum_price, time));
                RecyclerView recyclerView = findViewById(R.id.recyclerView);
                RecyclerViewAdapterOrder adapter = new RecyclerViewAdapterOrder(arrayList, this);
                //recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(new LinearLayoutManager(this));
                recyclerView.setAdapter(adapter);

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void fetchData() {
        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.GET,
                apiUrl + sharedPreferences.getString("email", ""),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        parseJSON(response);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        queue.add(stringRequest);
    }
}