package com.example.hbp_app;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.lakue.lakuepopupactivity.PopupActivity;
import com.lakue.lakuepopupactivity.PopupResult;
import com.lakue.lakuepopupactivity.PopupType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class SellPageActivity extends AppCompatActivity {
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference = database.getReference();
    private Intent intent;

    private String itemGroupTag;
    private String itemAlbumTag;
    private String itemMemberTag;
    private String itemDetail;
    private String itemDelivery;
    private String itemUserName;
    private String itemImage;
    private String itemPrice;
    private ImageView imageView;
    private ImageView imagePopup;
    private TextView groupTagView;
    private TextView albumTagView;
    private TextView memberTagView;
    private TextView detailView;
    private TextView deliveryView;
    private TextView userNameView;
    private TextView priceView;
    private ToggleButton sellListFavorite;
    private String sellID;
    private Boolean likeState;

    private ImageView sellListImage;
    private String state;

    private Button backButton;
    private FirebaseUser user;
    private String email;
    private String nickName;
    private ArrayList wish;
    private DatabaseReference mDatabase;
    private DatabaseReference allUsers;
    private DatabaseReference wishListDB;

    private DatabaseReference userDB;
    private TextView deliveryScore;
    private TextView mannerScore;
    private TextView itemScore;

    private Button chatButton;

    private FirebaseStorage storage;
    private ImageView userProfile;
    private DatabaseReference userProfileDB;
    private Uri imgUri;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sell_page); // xml 연결

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        user = FirebaseAuth.getInstance().getCurrentUser();
        if(user!=null) {
            for(UserInfo profile : user.getProviderData()){
                email = profile.getEmail();  // 현재 로그인한 계정 저장
            }
        }
        mDatabase = FirebaseDatabase.getInstance().getReference(); //firebase 연결
        allUsers = mDatabase.child("id_list");

        intent = getIntent();
        itemGroupTag = intent.getStringExtra("groupTag");
        itemAlbumTag = intent.getStringExtra("albumTag");
        itemMemberTag = intent.getStringExtra("memberTag");
        itemDetail = intent.getStringExtra("detail");
        itemDelivery = intent.getStringExtra("delivery");
        itemUserName = intent.getStringExtra("userName");
        itemImage = intent.getStringExtra("imageURI");  // 이미지 URI
        itemPrice = intent.getStringExtra("price");
        sellID = intent.getStringExtra("sellID");
        state = intent.getStringExtra("state");

        sellListFavorite = findViewById(R.id.sellListFavorite);
        likeState = intent.getBooleanExtra("likeState",false);
        Log.d("확인", String.valueOf(likeState));
        if(likeState)
            sellListFavorite.setChecked(true);
        else
            sellListFavorite.setChecked(false);

        deliveryScore = findViewById(R.id.deliveryScore);
        mannerScore = findViewById(R.id.mannerScore);
        itemScore = findViewById(R.id.itemScore);

        userDB = mDatabase.child("id_list").child(itemUserName).child("mypage");
        userDB.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                // 사용자 점수 받아오기
                Map<String,Long> map = (Map<String,Long>)snapshot.getValue();
                deliveryScore.setText(map.get("deliveryScore").toString());
                mannerScore.setText(map.get("mannerScore").toString());
                itemScore.setText(map.get("itemScore").toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        groupTagView = findViewById(R.id.sellListGroupTag);
        albumTagView = findViewById(R.id.sellListAlbumrTag);
        memberTagView = findViewById(R.id.sellListMemberTag);
        detailView = findViewById(R.id.sellListDetail);
        deliveryView = findViewById(R.id.sellListDelivery);
        userNameView = findViewById(R.id.sellListUserName);
        imageView = findViewById(R.id.sellListImage); // 포토카드 이미지
        priceView = findViewById(R.id.sellListPrice);




        userProfile = findViewById(R.id.userProfile);
        userProfileDB = mDatabase.child("id_list").child(itemUserName).child("image");
        userProfileDB.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String profileUri = (String) snapshot.getValue();
                storage =FirebaseStorage.getInstance();
                StorageReference storageReference = storage.getReference();
                StorageReference riverRef = storageReference.child(profileUri);
                riverRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Glide.with(getApplicationContext()).load(uri).into(userProfile);
                    }
                });

                StorageReference pcRef = storageReference.child(itemImage);
                pcRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Glide.with(getApplicationContext()).load(uri).into(imageView);
                        imgUri = uri;
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        groupTagView.setText(itemGroupTag);
        albumTagView.setText(itemAlbumTag);
        memberTagView.setText(itemMemberTag);
        detailView.setText(itemDetail);
        deliveryView.setText(itemDelivery);
        userNameView.setText(itemUserName);
        priceView.setText(String.valueOf(itemPrice));
//        Glide.with(imageView).load(itemImage).into(imageView);

        backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        chatButton = findViewById(R.id.chatButton);
        if(state.equals("예약중")){ //판매글 상태가 예약중인 경우 채팅 X
            chatButton.setText("예약중");
            chatButton.setBackgroundColor(Color.parseColor("#FFC8C8"));
            chatButton.setClickable(false);
        }

       allUsers.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot datas : snapshot.getChildren()) {
                    String user = datas.child("email").getValue(String.class);
                    if(user.equals(email)){
                        nickName = datas.getKey();
                        wishListDB = mDatabase.child("id_list").child(nickName).child("wishList"); //유저의 찜목록 가져오기
                        wishListDB.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                HashMap<String,String> hashMap = (HashMap<String, String>) dataSnapshot.getValue();
                                if(hashMap !=null){
                                    Set<String> keySet = hashMap.keySet();
                                    wish = new ArrayList<String>(keySet);
                                }
                                sellListFavorite.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                                    @Override
                                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                                        if(isChecked){ // 찜 누르면 데이터베이스에 추가
                                           wishListDB.child(String.valueOf(sellID)).setValue(sellID);
                                        }
                                        else { // 취소하면 데이터베이스에서 삭제
                                            wishListDB.child(sellID).removeValue();

                                        }
                                    }
                                });
                            }
                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                            }
                        });
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        //이미지 클릭 시 팝업

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(), PopupActivity.class);
                intent.putExtra("type", PopupType.IMAGE);
                intent.putExtra("title", imgUri.toString()); //Image
                Log.d("확인",imgUri.toString());
                intent.putExtra("buttonLeft", "종료");
                intent.putExtra("buttonRight", "상세보기");
                startActivityForResult(intent, 4);
            }
        });

    }

        @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            //데이터 받기
            if(requestCode == 1){
                PopupResult result = (PopupResult) data.getSerializableExtra("result");
                if(result == PopupResult.CENTER){
                }
            }
            if(requestCode == 2){
                PopupResult result = (PopupResult) data.getSerializableExtra("result");
                if(result == PopupResult.LEFT){

                } else if(result == PopupResult.RIGHT){

                }
            }
            if(requestCode == 3){
                PopupResult result = (PopupResult) data.getSerializableExtra("result");
                if(result == PopupResult.CENTER){
                }
            }
            if(requestCode == 4){
                PopupResult result = (PopupResult) data.getSerializableExtra("result");
                if(result == PopupResult.IMAGE){

                } else if(result == PopupResult.CENTER){


                }
            }
        }
    }
}
