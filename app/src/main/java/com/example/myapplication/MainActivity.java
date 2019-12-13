package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.Util.Helper;
import com.example.myapplication.callback.JsonCallback;
import com.example.myapplication.model.LoginModel;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;

import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.RuntimePermissions;

@RuntimePermissions
public class MainActivity extends BaseActivity {
     private EditText mAcountET,mPassWordEt;
     private TextView mLoginTv;

     private String mLoginUrl="http://wx.weibenh5.com/2019/Oct/XFL/api/Appapi.php?a=login";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initview();
    }

    private void initview() {
        mAcountET=findViewById(R.id.ev1);
        mPassWordEt=findViewById(R.id.ev2);
        mLoginTv=findViewById(R.id.tv1);
        mLoginTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(TextUtils.isEmpty(mAcountET.getText())){
                    Toast.makeText(MainActivity.this, "账号不能为空",Toast.LENGTH_SHORT).show();
                     return;
                }

                if(TextUtils.isEmpty(mPassWordEt.getText())){
                    Toast.makeText(MainActivity.this, "密码不能为空",Toast.LENGTH_SHORT).show();
                     return;
                }
                MainActivityPermissionsDispatcher.loginWithPermissionCheck(MainActivity.this, mAcountET.getText().toString(),mPassWordEt.getText().toString() );

            }
        });
    }

    @NeedsPermission(Manifest.permission.INTERNET)
    public  void login(String user, String password) {
        String userMd5= Helper.md5Decode32(user);
        String passwordMd5=Helper.md5Decode32(password);
        Log.i("tmd", userMd5+"----"+passwordMd5);
        OkGo.<LoginModel>post(mLoginUrl)
                .params("userID", userMd5, false)
                .params("pwd",passwordMd5,false)
                .execute(new JsonCallback<LoginModel>(MainActivity.this) {
                    @Override
                    public void onSuccess(Response<LoginModel> response) {
                           LoginModel model=response.body();
                           if(model!=null){
                              if(model.error==0){
                                  getMyApplication().setmToken(model.data.token);
                                  gotoGIFT();
                              }else {
                                  Toast.makeText(MainActivity.this,model.info,Toast.LENGTH_SHORT).show();
                              }
                           }else {
                               Toast.makeText(MainActivity.this,"登录失败",Toast.LENGTH_SHORT).show();
                           }
                    }

                    @Override
                    public void onError(Response<LoginModel> response) {
                        super.onError(response);
                         Toast.makeText(MainActivity.this,"登录失败",Toast.LENGTH_SHORT).show();
                     }
                });
    }

    private void gotoGIFT(){
        Intent intent=new Intent(MainActivity.this,GiftCodeActivity.class);
        startActivity(intent);
    }

    @SuppressLint("NeedOnRequestPermissionsResult")
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        MainActivityPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
    }
}
