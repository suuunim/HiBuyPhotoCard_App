package com.example.hibuyphotocard;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    private FragmentManager manager=getSupportFragmentManager();
    private FragmentTransaction transaction;
    BottomNavigationView bottomNavigationView;
    Menu menu;
    private Fragment1 fragment1 = new Fragment1();
    private Fragment2 fragment2 = new Fragment2();
    private Fragment3 fragment3 = new Fragment3();
    private Fragment4 fragment4 = new Fragment4();
    private TextView textView_signIn;
    private TextView textView_find;
    private Button login_btn;
    private FirebaseAuth mAuth;
    private EditText textView_passwd;
    private EditText textView_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FragmentTransaction transaction = manager.beginTransaction();


        bottomNavigationView=findViewById(R.id.navigation);
        menu=bottomNavigationView.getMenu();

        bottomNavigationView.setOnNavigationItemSelectedListener(new ItemSelectedListener());
        bottomNavigationView.setSelectedItemId(R.id.house);  //선택된 아이템 지정

    }


    class ItemSelectedListener implements BottomNavigationView.OnNavigationItemSelectedListener{
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
            FragmentTransaction transaction = manager.beginTransaction();
            switch(menuItem.getItemId())
            {
                case R.id.write:

                    menu.findItem(R.id.house).setIcon(R.drawable.house);
                    menu.findItem(R.id.my_page).setIcon(R.drawable.logo_icon);
                    menu.findItem(R.id.chat).setIcon(R.drawable.chat);
                    transaction.replace(R.id.frameLayout, fragment1).commitAllowingStateLoss();
                    break;

                case R.id.house:

                    menu.findItem(R.id.write).setIcon(R.drawable.contract);
                    menu.findItem(R.id.my_page).setIcon(R.drawable.logo_icon);
                    menu.findItem(R.id.chat).setIcon(R.drawable.chat);
                    transaction.replace(R.id.frameLayout, fragment2).commitAllowingStateLoss();
                    break;

                case R.id.my_page:
                    menu.findItem(R.id.house).setIcon(R.drawable.house);
                    menu.findItem(R.id.write).setIcon(R.drawable.contract);
                    menu.findItem(R.id.chat).setIcon(R.drawable.chat);
                    transaction.replace(R.id.frameLayout, fragment4).commitAllowingStateLoss();

                    break;

                case R.id.chat:
                    menu.findItem(R.id.house).setIcon(R.drawable.house);
                    menu.findItem(R.id.write).setIcon(R.drawable.contract);
                    menu.findItem(R.id.my_page).setIcon(R.drawable.logo_icon);
                    transaction.replace(R.id.frameLayout, fragment3).commitAllowingStateLoss();
            }// switch()..
            return true;
        }
    }// ItemSelectedListener class..

}