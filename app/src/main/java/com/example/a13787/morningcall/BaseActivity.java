package com.example.a13787.morningcall;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.baidu.mapapi.SDKInitializer;

public abstract class BaseActivity extends AppCompatActivity
{
    private ForceOffLineReceiver receiver;
    boolean enableForceOffLineReceiver = false;
    boolean enableForceOffLineReceiverTest = true;
    String userEmail="admin@stu.ecnu.edu.cn";
    String userIP="127.0.0.1";
    void setUserEmail(String userEmail)
    {
        this.userEmail = userEmail;
    }
    void setUserIP(String userIP)
    {
        this.userIP=userIP;
    }
    void setEnableForceOffLineReceiver(boolean enable)
    {
        this.enableForceOffLineReceiver = enable;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        Log.d("BaseActivity", getClass().getSimpleName());
        String ip=Util.getLocalIpAddress();
        setUserIP(ip);
        Log.d("IpAddress", ip);
        ActivityCollector.addActivity(this);
        setContentView(initLayout());
        initView();
    }
    @Override
    protected void onResume()
    {
        super.onResume();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("com.example.a13787.morningcall.FORCE_OFFLINE");
        receiver = new ForceOffLineReceiver();
        registerReceiver(receiver,intentFilter);
    }
    @Override
    protected void onPause()
    {
        super.onPause();
        if (receiver!=null)
        {
            unregisterReceiver(receiver);
            receiver = null;
        }
    }
    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        ActivityCollector.removeActivity(this);
    }
    protected abstract void initView();
    protected abstract void initData();
    protected abstract void initListener();
    protected abstract int initLayout();
    class ForceOffLineReceiver extends BroadcastReceiver
    {
        @Override
        public void onReceive(final Context context, Intent intent)
        {
            if (enableForceOffLineReceiver == true)
            {
                String curEmail = intent.getStringExtra("Email");
                String curIP = intent.getStringExtra("IP");
                Log.d("curEmail", curEmail);
                Log.d("curIP", curIP);
                Log.d("userEmail", userEmail);
                Log.d("userIP", userIP);
                if (curEmail.equals(userEmail) && !curIP.equals(userIP))
                {
                    Log.d("Broadcast", "catch");
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setTitle("Warning");
                    builder.setMessage("You are forced to be offline. Please try to login again.");
                    builder.setCancelable(false);
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener(){
                        @Override
                        public void onClick(DialogInterface dialog, int which)
                        {
                            ActivityCollector.finishAll();
                            Intent intent = new Intent(context, LoginActivity.class);
                            context.startActivity(intent);
                        }
                    });
                    builder.show();
                }
            }
            else if (enableForceOffLineReceiverTest == true)
            {
                String curEmail = intent.getStringExtra("Email");
                String curIP = intent.getStringExtra("IP");
                Log.d("curEmail", curEmail);
                Log.d("curIP", curIP);
                Log.d("userEmail", userEmail);
                Log.d("userIP", userIP);
                if (curEmail.equals(userEmail) && curIP.equals(userIP))
                {
                    Log.d("Broadcast", "catch");
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setTitle("Warning");
                    builder.setMessage("You are forced to be offline. Please try to login again.");
                    builder.setCancelable(false);
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener(){
                        @Override
                        public void onClick(DialogInterface dialog, int which)
                        {
                            ActivityCollector.finishAll();
                            Intent intent = new Intent(context, LoginActivity.class);
                            context.startActivity(intent);
                        }
                    });
                    builder.show();
                }
            }
        }
    }
}
