package com.example.myapplication.callback;

import android.content.Context;
import android.graphics.Color;

import com.example.myapplication.R;
import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import com.lzy.okgo.callback.AbsCallback;
import com.lzy.okgo.request.base.Request;
import com.zyao89.view.zloading.ZLoadingDialog;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import okhttp3.Response;
import okhttp3.ResponseBody;

import static com.zyao89.view.zloading.Z_TYPE.DOUBLE_CIRCLE;

public  abstract class  JsonCallback<T>  extends AbsCallback<T> {

    public boolean mShowLoading=true;  //是否显示加载动画，默认显示
    public Context mContext;
    private ZLoadingDialog mDialog;

    public JsonCallback(Context mContext) {
        this.mContext = mContext;
        if(mShowLoading){
            initSpotsDialog();
        }
    }



    public JsonCallback(boolean mShowLoading, Context mContext) {
        this.mShowLoading = mShowLoading;
        this.mContext = mContext;
        if(mShowLoading){
            initSpotsDialog();
        }
    }

    @Override
    public T convertResponse(Response response) throws Throwable {

        ResponseBody body=response.body();
        if(body==null) return  null;

        T data=null;
        Gson gson=new Gson();
        JsonReader jsonReader=new JsonReader(body.charStream());
        Type genType=getClass().getGenericSuperclass();
        Type type=((ParameterizedType)genType).getActualTypeArguments()[0];
        data=gson.fromJson(jsonReader,type);
        return data;
    }

    @Override
    public void onError(com.lzy.okgo.model.Response<T> response) {
        super.onError(response);
        dismissLoading();
    }

    @Override
    public void onFinish() {
        dismissLoading();
    }


    @Override
    public void onStart(Request<T, ? extends Request> request) {

        super.onStart(request);
        showLoading();
    }

    private void initSpotsDialog() {
        if(mDialog==null){
            mDialog= new  ZLoadingDialog(mContext);
            mDialog.setLoadingBuilder(DOUBLE_CIRCLE)//设置类型
                    .setLoadingColor(mContext.getResources().getColor(R.color.colorDarkBlue))//颜色
                    .setHintText("Loading...")
                    .setHintTextSize(16) // 设置字体大小 dp
                    .setHintTextColor(Color.WHITE)// 设置字体颜色
                    .setCancelable(false)
                    .setCanceledOnTouchOutside(false);

        }
    }

    private void showLoading(){
        if(mDialog!=null){
            mDialog.show();
        }
    }



    private void dismissLoading(){
        if(mDialog!=null){
            mDialog.dismiss();
            mDialog=null;
        }
    }
}
