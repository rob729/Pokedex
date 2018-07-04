package com.example.robin.pokedex;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.roger.catloadinglibrary.CatLoadingView;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    Button prev,search,next;
    EditText edt;
    String purl = "https://pokeapi.co/api/v2/pokemon/";
    OkHttpClient okHttpClient;
    Request request;
    Gson gson;
    String result;
    Result apiResponse;
    TextView name,weight,height,exp,rank;
    ImageView img;
    int b=0;
    CatLoadingView mview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mview = new CatLoadingView();
        search = findViewById(R.id.bn);
        next  =findViewById(R.id.next);
        edt = findViewById(R.id.edt);
        name = findViewById(R.id.name);
        weight = findViewById(R.id.weight);
        height = findViewById(R.id.height);
        img = findViewById(R.id.img);
        exp = findViewById(R.id.exp);
        rank = findViewById(R.id.rank);
        prev = findViewById(R.id.prev);
        prev.setText("<");

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String a = edt.getText().toString();
                b = Integer.parseInt(a);
                if((b>=1)&&(b<=802)) {
                    mview.show(getSupportFragmentManager(),"");
                    String x = purl + a + "/";
                    makeNetworkCall(x);
                }


            }
        });
        prev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if((--b>=1)&&(b<=802)) {
                    mview.show(getSupportFragmentManager(),"");
                    String x = purl + b + "/";
                    makeNetworkCall(x);
                }
            }
        });
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mview.show(getSupportFragmentManager(),"");
                ++b;
                if(b<803) {
                    String x = purl + b + "/";
                    makeNetworkCall(x);
                }
            }

        });
    }
    private void makeNetworkCall(String url) {

        okHttpClient = new OkHttpClient();

        request = new Request.Builder()
                .url(url)
                .build();

        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                result = response.body().string();
                gson = new Gson();
                apiResponse = gson.fromJson(result, Result.class);

                // Log.e("TAG", "onResponse: " + apiResponse.getItems().toString() );

                (MainActivity.this).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Picasso.get().load(apiResponse.getSprites().getFront_default()).into(img);
                        name.setText(apiResponse.getName());
                        weight.setText("Weight: "+ apiResponse.getWeight());
                        height.setText("Height: "+apiResponse.getHeight());
                        exp.setText("Exp: "+apiResponse.getBase_experience());
                        rank.setText("Rank: "+b);
                        edt.setText("");
                        mview.dismiss();



//                        String firstUserToJson = gson.toJson(firstUser);

                    }
                });
            }
        });
    }
}
