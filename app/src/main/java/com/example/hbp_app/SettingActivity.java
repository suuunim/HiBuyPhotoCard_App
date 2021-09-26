package com.example.hbp_app;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SettingActivity extends AppCompatActivity {

    private List<String> list;          // 데이터를 넣은 리스트변수
    private ListView listView;          // 검색을 보여줄 리스트변수
    private EditText editSearch;        // 검색어를 입력할 Input 창
    private SearchAdapter adapter;      // 리스트뷰에 연결할 아답터
    private ArrayList<String> arraylist;
    private FirebaseAuth mAuth;
    int i = 0;
    private DatabaseReference SellItemList;

    DatabaseReference ref;
    private Dialog searchDialog;
    private Button search_button;
    private FirebaseDatabase database = FirebaseDatabase.getInstance();

    private DatabaseReference databaseReference = database.getReference();
    private DatabaseReference mDatabase;
    private DatabaseReference groupDB;
    private DatabaseReference fandomDB;
    private FirebaseDatabase mData;
    private DatabaseReference memberDB;

    private AutoCompleteTextView search_Group;
    private AutoCompleteTextView search_Album;
    private AutoCompleteTextView search_Member;

    private List<String> group_list = new ArrayList<String>();
    private List<String> album_list = new ArrayList<String>();
    private List<String> member_list = new ArrayList<String>();
    private ArrayList<SearchItemList> item; // recyclerview에 적용할 아이템 리스트
    private ArrayList<SearchItemList> itemList2;  //전송할 리스트


    SearchItemList sellItemList;

    private HashMap sell1 = new HashMap();
    private String selectMember ="";
    private LinearLayout group_label;
    private LinearLayout album_label;
    private LinearLayout member_label;
    private String selectGroup ="";
    private List<String> searchKeywordGroup = new ArrayList<String>();
    private List<String> AllGroup = new ArrayList<String>();
    private List<String> searchKeywordAlbum = new ArrayList<String>();
    private List<String> searchKeywordMember = new ArrayList<String>();
    private DatabaseReference photocardDB;
    private String dataGroup;
    private final int GET_GALLERY_IMAGE = 200;
    private ImageView imageview;
    private FirebaseStorage storage;
    Uri selectedImageUri;
    EditText nicknameText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profilesetting);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);



        ActionBar actionBar = getSupportActionBar();
        actionBar.hide(); // actionBar 숨기기


        imageview = (ImageView) findViewById(R.id.profile_ficture);
        imageview.setBackground(new ShapeDrawable(new OvalShape()));


        storage = FirebaseStorage.getInstance();
        imageview.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setDataAndType(android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                startActivityForResult(intent, GET_GALLERY_IMAGE);

            }
        });

        nicknameText = findViewById(R.id.signup_nickname);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        Intent intent = getIntent();
        String email = intent.getStringExtra("emailtext");

        group_label = findViewById(R.id.group_label);

        member_label = findViewById(R.id.member_label);

        search_Group = findViewById(R.id.signup_favorit);

        search_Member = findViewById(R.id.signup_love);

        mDatabase = FirebaseDatabase.getInstance().getReference();
        mData=FirebaseDatabase.getInstance();
        groupDB = mDatabase.child("idol");
        fandomDB=mDatabase.child("fandom");

        Button Finish_button = findViewById(R.id.button_profile_finish);

        Finish_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                databaseReference.child("id_list").child(nicknameText.getText().toString()).child("name").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        String value = snapshot.getValue(String.class);

                        if(value!=null){
                            Toast.makeText(getApplicationContext(),"중복되는 닉네임 입니다",Toast.LENGTH_SHORT).show();//토스메세지 출력
                        }
                        else{

                            FirebaseDatabase.getInstance().getReference().child("users").child(mAuth.getUid()).child("profileImageUrl").setValue(selectedImageUri.toString());
                            FirebaseDatabase.getInstance().getReference().child("users").child(mAuth.getUid()).child("uid").setValue(mAuth.getUid());
                            FirebaseDatabase.getInstance().getReference().child("users").child(mAuth.getUid()).child("userName").setValue(nicknameText.getText().toString());
                            databaseReference.child("id_list").child(nicknameText.getText().toString()).child("email").setValue(email);
                            databaseReference.child("id_list").child(nicknameText.getText().toString()).child("name").setValue(nicknameText.getText().toString());
                            databaseReference.child("id_list").child(nicknameText.getText().toString()).child("group").setValue(searchKeywordGroup);


                            String fandomname;
                            fandomDB.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    Map<String,String> fandom = (Map<String, String>)snapshot.getValue();
                                    String groupname=searchKeywordGroup.get(0);

                                    databaseReference.child("id_list").child(nicknameText.getText().toString()).child("mypage").child("fandom").setValue(fandom.get(groupname));


                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });






                            databaseReference.child("id_list").child(nicknameText.getText().toString()).child("member").setValue(searchKeywordMember);
                            databaseReference.child("id_list").child(nicknameText.getText().toString()).child("image").setValue(selectedImageUri.toString());
                            databaseReference.child("id_list").child(nicknameText.getText().toString()).child("mypage").child("deliveryScore").setValue(30);
                            databaseReference.child("id_list").child(nicknameText.getText().toString()).child("mypage").child("itemScore").setValue(30);
                            databaseReference.child("id_list").child(nicknameText.getText().toString()).child("mypage").child("mannerScore").setValue(30);


                            Intent intent = new Intent(SettingActivity.this, MainActivity.class);
                            intent.putExtra("itemList",item);
                            startActivity(intent);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        // 디비를 가져오던중 에러 발생 시
                        //Log.e("MainActivity", String.valueOf(databaseError.toException())); // 에러문 출력
                    }
                });


            }
        });
        //firebase에서 데이터 불러오기




        groupDB.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Map<String,Object> group = (Map<String, Object>)snapshot.getValue();
                Object [] groupList = group.keySet().toArray();
                ArrayAdapter groupAdapter = new ArrayAdapter(SettingActivity.this,
                        android.R.layout.simple_list_item_1,groupList);
                search_Group.setAdapter(groupAdapter);

                search_Group.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


                        DatabaseReference mData = FirebaseDatabase.getInstance().getReference(); //firebase 연결
                        SellItemList = mData.child("Sell");

                        item = new ArrayList<>(); //검색 결과화면으로 넘길 리스트
                        item.clear();


                        SellItemList.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshoted) {
                                sell1 = (HashMap) snapshoted.getValue();
                                sell1.size();
                                for(int i= sell1.size();i>0;i--){
                                    for(DataSnapshot snap :snapshoted.getChildren() ){
                                        String sell = snap.getKey();
                                        String num = sell.replace("sell","");
                                        Integer num2= Integer.valueOf(num);
                                        System.out.println(num);
                                        System.out.println(i);
                                        System.out.println("----------");
                                        Log.d("ㅇㅇ","ㄴㅁㅇ");
                                        if(num2 == i){

                                            sellItemList = snap.getValue(SearchItemList.class);
                                            item.add(sellItemList);
                                            break;

                                        }
                                    }
                                }
//                                     for(DataSnapshot snap :snapshoted.getChildren() ){
////
//                                         String member = snap.child("memberTag").getValue(String.class);
////                                   if(member.equals(((TextView)view).getText().toString())){
//
//                                             sellItemList = snap.getValue(SearchItemList.class);
////                                             item.add(sellItemList);
//                                             item.add(0,sellItemList); //모든 판매글 저장 //최애 맴버의 글만 보여줌
//
//
//
////
//                    }


                            }
                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });


                        String dataGroup = parent.getAdapter().getItem(position).toString();
                        if (selectGroup.equals("")){
                            selectGroup=dataGroup;
                            createTextView(dataGroup,group_label,"group",searchKeywordGroup);
                            selectOther(searchKeywordGroup);
                            search_Group.setText("");
                        }
                        else{
                            Toast.makeText(SettingActivity.this,
                                    "하나만 선택해주세요.", Toast.LENGTH_SHORT).show();

                        }
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });





    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GET_GALLERY_IMAGE && resultCode == RESULT_OK && data != null && data.getData() != null) {

            selectedImageUri = data.getData();
            imageview.setImageURI(selectedImageUri);
            StorageReference storageRef = storage.getReference();
            StorageReference riversRef=storageRef.child(selectedImageUri.toString());
            UploadTask uploadTask = riversRef.putFile(selectedImageUri);


        }

    }

    public void selectOther(List group) {

        for (int i=0; i <group.size();i++) {
            memberDB = mDatabase.child("idol").child(group.get(i).toString()).child("member");
            memberDB.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    ArrayList member = (ArrayList) snapshot.getValue();
                    ArrayAdapter memberAdapter = new ArrayAdapter(SettingActivity.this,
                            android.R.layout.simple_list_item_1, member);
                    search_Member.setAdapter(memberAdapter);
                    search_Member.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {





                            String dataMemeber = parent.getAdapter().getItem(position).toString();
                            if (selectMember.equals("")) {
                                selectMember = dataMemeber;
                                createTextView(dataMemeber, member_label, "member",searchKeywordMember);
                            } else {
                                Toast.makeText(SettingActivity.this,
                                        "하나만 선택해주세요.", Toast.LENGTH_SHORT).show();
                            }
                            search_Member.setText("");
                        }
                    });
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }

    }
    private void createTextView(String text, LinearLayout layout, String type, List list){
        TextView label = new TextView(getApplicationContext());
        label.setText(text);
        label.setSingleLine(true);
        label.setEllipsize(TextUtils.TruncateAt.END);
        label.setPadding(15,8,15,8);
        label.setTextSize(12);
        label.setTextColor(Color.parseColor("#ffffff"));
        label.setTypeface(null, Typeface.NORMAL);
        label.setId(0);
        label.setLinksClickable(true);
        label.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                layout.removeView(v);
                int index = list.indexOf(text);
                list.remove(index);
                Log.d("데이터",list.toString());
            }
        });

        GradientDrawable label_drawable = (GradientDrawable) ContextCompat.getDrawable(this,R.drawable.custom_label);
        label_drawable.setStroke(4,Color.parseColor(selectStrokeColor(type)),8,5);
        label_drawable.setColor(Color.parseColor(selectColor(type)));
        label.setBackground(getResources().getDrawable(R.drawable.custom_label));

        LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        param.rightMargin =20;

        label.setLayoutParams(param);
        layout.addView(label);
        list.add(text);
        Log.d("데이터",list.toString());

    }


    private String selectColor(String type){
        if(type.equals("group"))
            return "#FFC8C8";
        else if(type.equals("member"))
            return "#FFE790";
        return "#565656";
    }
    private String selectStrokeColor(String type){
        if(type.equals("group"))
            return "#FFB8B8";
        else if(type.equals("member"))
            return "#FCD54C";
        return "#565656";
    }


}