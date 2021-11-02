package com.example.hbp_app;


import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Map;

public class PopupDialogActivity extends Activity {
    private Dialog popupDialog;
    public Context dialogContext;
    public Context activityContext; //다이얼로그가 열린 activity의 context

    private Button yesBtn; //검색 버튼
    private Button closeBtn;


    public PopupDialogActivity(Context context) {
        popupDialog = new Dialog(context);
        popupDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        popupDialog.setContentView(R.layout.explain_popup);
        activityContext = context;
        dialogContext = popupDialog.getOwnerActivity();
    }

    public void showDialog() {
        popupDialog.show();

        closeBtn = popupDialog.findViewById(R.id.popup_finish);
        closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupDialog.dismiss(); // 다이얼로그 닫기
            }
        });
    }



}