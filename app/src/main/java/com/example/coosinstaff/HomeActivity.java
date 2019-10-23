package com.example.coosinstaff;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.coosinstaff.fragment.DaotaoFragment;
import com.example.coosinstaff.fragment.HotroFragment;
import com.example.coosinstaff.fragment.LichlamviecFragment;
import com.example.coosinstaff.fragment.ThongbaoFragment;
import com.example.coosinstaff.fragment.VideogioithieuFragment;
import com.example.coosinstaff.model.CheckLogined;
import com.google.android.material.navigation.NavigationView;

public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    String account;
    Toolbar toolbar;
    private DrawerLayout drawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        //lay so dien thoai da luu khi dang nhap
        SharedPreferences SP = getApplicationContext().getSharedPreferences("PHONE",0);
        account = SP.getString("phone_num",null);

        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Lịch làm việc");
        setSupportActionBar(toolbar);

        drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,drawer,toolbar,R.string.navigation_drawer_open,
                R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        if (savedInstanceState == null){
            toolbar.setTitle("Lịch làm việc");
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new LichlamviecFragment()).commit();
            navigationView.setCheckedItem(R.id.nav_lichlamviec);
        }
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)){
            drawer.closeDrawer(GravityCompat.START);
        }else super.onBackPressed();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()){
            case R.id.nav_video:
                toolbar.setTitle("Video giới thiệu");
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new VideogioithieuFragment()).commit();
                break;
            case R.id.nav_doisongnhanvien:
                Intent toFacebook = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.facebook.com/daominhthai1997"));
                startActivity(toFacebook);
//                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new FanfageFragment()).commit();
                break;
            case R.id.nav_lichlamviec:
                toolbar.setTitle("Lịch làm việc");
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new LichlamviecFragment()).commit();
                break;
            case R.id.nav_thongbao:
                toolbar.setTitle("Thông báo");
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new ThongbaoFragment()).commit();
                break;
            case R.id.nav_ctdt:
                toolbar.setTitle("Chương trình đào tạo");
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new DaotaoFragment()).commit();
                break;
            case R.id.nav_hotro:
                toolbar.setTitle("Hỗ trợ");
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new HotroFragment()).commit();
                break;
            case R.id.nav_logout:
                dialogLogout();
                break;
        }
        drawer.closeDrawer(GravityCompat.START);
        return  true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()){
            case R.id.cart:
                Intent submited = new Intent(HomeActivity.this, SubmitedActivity.class);
                startActivity(submited);
        }

        return super.onOptionsItemSelected(item);
    }

    public void dialogLogout(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Xác nhận");
        builder.setMessage("Bạn thực sự muốn đăng xuất khỏi tài khoản này?");
        builder.setCancelable(false);
        builder.setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                Intent logout = new Intent(HomeActivity.this,MainActivity.class);
                logout.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                CheckLogined.SharedPrefesSAVE(getApplicationContext(),getIntent().getStringExtra(null));
                startActivity(logout);
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
}
