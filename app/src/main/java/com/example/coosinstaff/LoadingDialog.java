package com.example.coosinstaff;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Handler;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class LoadingDialog extends AppCompatActivity {
   public void loading(Context context){
       AlertDialog.Builder builder = new AlertDialog.Builder(context);
       builder.setMessage("Đang nạp dữ liệu...");
       builder.setCancelable(false);
       final AlertDialog alertDialog = builder.create();
       alertDialog.show();
       LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
       layoutParams.gravity = Gravity.CENTER;
       layoutParams.setMargins(20,20,20,20);

       TextView messageText = alertDialog.findViewById(android.R.id.message);
       messageText.setGravity(Gravity.CENTER);

       final Handler handler  = new Handler();
       final Runnable runnable = new Runnable() {
           @Override
           public void run() {
               if (alertDialog.isShowing()) {
                   alertDialog.dismiss();
               }
           }
       };

       alertDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
           @Override
           public void onDismiss(DialogInterface dialog) {
               handler.removeCallbacks(runnable);
           }
       });

       handler.postDelayed(runnable, 100);
   }
}
