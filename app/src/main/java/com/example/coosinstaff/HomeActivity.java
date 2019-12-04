package com.example.coosinstaff;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.coosinstaff.fragment.ChatFragment;
import com.example.coosinstaff.fragment.DaotaoFragment;
import com.example.coosinstaff.fragment.HotroFragment;
import com.example.coosinstaff.fragment.LichlamviecFragment;
import com.example.coosinstaff.fragment.ThongbaoFragment;
import com.example.coosinstaff.fragment.VideogioithieuFragment;
import com.example.coosinstaff.model.AccountAvatar;
import com.example.coosinstaff.model.CheckLogined;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import de.hdodenhof.circleimageview.CircleImageView;

public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    String account,userName="",userType="";
    Toolbar toolbar;
    private DrawerLayout drawer;
    Connection connect;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        //lay so dien thoai da luu khi dang nhap
        SharedPreferences SP = getApplicationContext().getSharedPreferences("PHONE",0);
        account = SP.getString("phone_num",null);

//        FirebaseMessaging.getInstance().subscribeToTopic("lich");

        try {
            com.example.coosinstaff.connection.ConnectionDB conStr = new com.example.coosinstaff.connection.ConnectionDB();
            connect = conStr.CONN();
            String query = "select * from EMPLOYEE where PHONE_NUM= '" + account+"'";
            Statement stmt = connect.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            if(rs.next())
            {
                if (rs.getInt("USER_STATUS")==0){
                    AlertDialog.Builder builder = new AlertDialog.Builder(HomeActivity.this);
                    builder.setTitle("Không thể đăng nhập");
                    builder.setMessage("Tài khoản của bạn hiện đang bị khóa, thắc mắc xin liên hệ 0921895314 !");
                    builder.setCancelable(false);
                    builder.setNegativeButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                            Intent login = new Intent(HomeActivity.this,MainActivity.class);
                            login.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            CheckLogined.SharedPrefesSAVE(getApplicationContext(),getIntent().getStringExtra(null));
                            startActivity(login);

                        }
                    });
                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();
                }
                userName = rs.getString("FULL_NAME");
                if (rs.getInt("EMP_TYPE")==1){
                    userType="Nhân viên vệ sinh";
                }else if (rs.getInt("EMP_TYPE")==2){
                    userType="Nhân viên nấu ăn";
                }else userType = "Nhân viên đa năng";
                connect.close();

            }
        }
        catch (Exception e){

        }
        NavigationView navigationView =  findViewById(R.id.nav_view);
        View headerView = navigationView.getHeaderView(0);
        TextView navUsername =  headerView.findViewById(R.id.txt_account_name);
        TextView navUserType =  headerView.findViewById(R.id.txt_account_emp_type);
        final CircleImageView imgAvatar = headerView.findViewById(R.id.imgAvatar);
        navUsername.setText(userName);
        navUserType.setText(userType);

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("avatars");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    AccountAvatar accountAvatar = snapshot.getValue(AccountAvatar.class);
                    if (accountAvatar.getAcountPhone().equals(account)){
                        Picasso.get().load(accountAvatar.getImageUrl()).into(imgAvatar);
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Lịch làm việc");
        setSupportActionBar(toolbar);

        drawer = findViewById(R.id.drawer_layout);
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
        }else {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Xác nhận");
            builder.setMessage("Bạn muốn đóng ứng dụng?");
            builder.setCancelable(false);
            builder.setPositiveButton("Ở lại", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                }
            });
            builder.setNegativeButton("Thoát", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    Intent intent = new Intent(Intent.ACTION_MAIN);
                    intent.addCategory(Intent.CATEGORY_HOME);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                }
            });
            AlertDialog alertDialog = builder.create();
            alertDialog.show();
        }
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
                break;
            case R.id.nav_lichlamviec:
                LoadingDialog loadingDialog = new LoadingDialog();
                loadingDialog.loading(HomeActivity.this);
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
                String s = "tel:" + "0921895314";
                Intent intent = new Intent(Intent.ACTION_CALL);
                intent.setData(Uri.parse(s));
                startActivity(intent);
                break;
            case R.id.nav_chat:
                toolbar.setTitle("Tin nhắn");
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new ChatFragment()).commit();
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
                LoadingDialog loadingDialog = new LoadingDialog();
                loadingDialog.loading(this);
                Intent submited = new Intent(HomeActivity.this, SubmitedActivity.class);
                startActivity(submited);
                return true;
            case R.id.history:
                LoadingDialog loadingDialog1 = new LoadingDialog();
                loadingDialog1.loading(this);
                Intent history = new Intent(HomeActivity.this, HistoryActivity.class);
                startActivity(history);
                return true;
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
