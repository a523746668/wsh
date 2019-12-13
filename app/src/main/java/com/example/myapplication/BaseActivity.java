package com.example.myapplication;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public  abstract class BaseActivity extends AppCompatActivity {

    public List<Activity> activities=new ArrayList<>();
    public  String getStr(int resId){
        return  getResources().getString(resId);
    }

    public MyApplication getMyApplication(){
        return (MyApplication) getApplication();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getMyApplication().activities.add(this);
        Log.i("activity","size:"+getMyApplication().activities.size()+"addname:"+getLocalClassName());
    }

    @Override
    protected void onDestroy() {
        getMyApplication().activities.remove(this);
        Log.i("activity","size:"+getMyApplication().activities.size()+"removename:"+getLocalClassName());
        super.onDestroy();

    }

    public void finishApp(){
        List<BaseActivity> list=getMyApplication().activities;
        for(BaseActivity activity:list){
            activity.finish();
        }
    }
}
