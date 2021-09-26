package com.example.hbp_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.security.cert.PolicyNode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    //    private Button search_button; //메인 내 dialog 창 띄우는 버튼
    private RecyclerView recyclerView2;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private DatabaseReference SellItemList;
    private DatabaseReference mData;

    private ArrayList<SearchItemList> item; // recyclerview에 적용할 아이템 리스트
    private ArrayList<SearchItemList> itemList2;  //전송할 리스트

    private HashMap sell1 = new HashMap();


    public boolean makeLabel = false;
    SearchItemList sellItemList;
    private FloatingActionButton search_button;
    private ScrollView scrollView_searchItem;
    private Button button1;
    private Button button2;


    //하단바
    private LinearLayout contractBtn;
    private LinearLayout homeBtn;
    private LinearLayout chatBtn;
    private LinearLayout mypageBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SearchDialogActivity dialog = new SearchDialogActivity(MainActivity.this);

        search_button = findViewById(R.id.search_button);
        search_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.showDialog();
            }
        });


        //하단바
        homeBtn = findViewById(R.id.homeBtn);
        homeBtn.setBackgroundColor(Color.parseColor("#B1E3FF"));


        chatBtn = findViewById(R.id.chatBtn);
        chatBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), FragmentActivity2.class);
                startActivity(intent);
                finish();
            }
        });

        mypageBtn = findViewById(R.id.mypageBtn);
        mypageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MyPageMain.class);
                startActivity(intent);
                finish();
            }
        });

        contractBtn = findViewById(R.id.contractBtn);
        contractBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), WritingActivity.class);
                startActivity(intent);
                finish();
            }
        });

        item = new ArrayList<>();
        item.clear();



        recyclerView2 = findViewById(R.id.recyclerview2);


        int numberOfColumns = 2; //컬럼 2개로
        int spacing = 30;
        boolean includeEdge = true;
        recyclerView2.setLayoutManager(new GridLayoutManager(this, numberOfColumns));
        recyclerView2.addItemDecoration(new GridSpacingItemDecoration(numberOfColumns, spacing, includeEdge));

        adapter = new SearchAdapter(item, this);
        recyclerView2.setAdapter(adapter);

    }



    @Override
    public void onResume() {
        super.onResume();
        item.clear();
        getWish();
        adapter.notifyDataSetChanged();
    }

    public void getWish() {
        DatabaseReference mData = FirebaseDatabase.getInstance().getReference(); //firebase 연결
        SellItemList = mData.child("Sell");
        SellItemList.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshoted) {
                sell1 = (HashMap) snapshoted.getValue();
                sell1.size();
                for (int i = sell1.size(); i > 0; i--) {
                    for (DataSnapshot snap : snapshoted.getChildren()) {
                        String sell = snap.getKey();
                        String num = sell.replace("sell", "");
                        Integer num2 = Integer.valueOf(num);
                        System.out.println(num);
                        System.out.println(i);
                        System.out.println("----------");
                        Log.d("ㅇㅇ", "ㄴㅁㅇ");
                        if (num2 == i) {

                            sellItemList = snap.getValue(SearchItemList.class);
                            item.add(sellItemList);
                            break;

                        }
                    }
                }
                adapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}


