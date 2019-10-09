package com.example.coosinstaff.connection;

import android.annotation.SuppressLint;
import android.os.StrictMode;
import android.util.Log;

import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionDB {

    @SuppressLint("NewApi")
    public java.sql.Connection CONN() {
        {
            String ip = "115.84.182.60";
            String db = "hcmunrec_appOsin";
            String DBUserNameStr = "thai";
            String DBPasswordStr = "Luanvan2019@";

            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
            java.sql.Connection connection = null;
            String ConnectionURL = null;
            try {
                Class.forName("net.sourceforge.jtds.jdbc.Driver");
                ConnectionURL = "jdbc:jtds:sqlserver://" + ip + ";databaseName=" + db + ";user=" + DBUserNameStr + ";password=" + DBPasswordStr + ";";
                connection = DriverManager.getConnection(ConnectionURL);
            } catch (SQLException se) {
                Log.e("error here 1 : ", se.getMessage());
            } catch (ClassNotFoundException e) {
                Log.e("error here 2 : ", e.getMessage());
            } catch (Exception e) {
                Log.e("error here 3 : ", e.getMessage());
            }
            return connection;
        }
    }
}
