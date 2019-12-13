package com.example.myapplication;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.callback.JsonCallback;
import com.example.myapplication.model.InformationModel;
import com.example.myapplication.zxing.android.CaptureActivity;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;

import butterknife.BindView;
import butterknife.ButterKnife;
import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.RuntimePermissions;

@RuntimePermissions
public class GiftCodeActivity extends BaseActivity {
    @BindView(R.id.ev1)
    EditText ev1;
    private TextView smTV, hxTV;
    private static final int REQUEST_CODE_SCAN = 0x0000;
    private static final String DECODED_CONTENT_KEY = "codedContent";
    private static final String DECODED_BITMAP_KEY = "codedBitmap";
    private String mUrl="http://wx.weibenh5.com/2019/Oct/XFL/api/Appapi.php?a=putPrzCode";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gift_code);
        ButterKnife.bind(this);
        initview();
    }

    private void initview() {
        smTV = findViewById(R.id.smtv1);
        hxTV = findViewById(R.id.tv1);
        smTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GiftCodeActivityPermissionsDispatcher.goScanWithPermissionCheck(GiftCodeActivity.this);
            }
        });
        hxTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getCodeInfomation();
            }
        });

    }

    private void getCodeInfomation() {
        String code="";
        if(!TextUtils.isEmpty(ev1.getText())){
            code=ev1.getText().toString();
        }else {
            Toast.makeText(this,"请输入礼品卷码",Toast.LENGTH_SHORT).show();
            return;
        }


        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        String finalCode = code;
        builder.setTitle("温馨提示")
                .setMessage("您核销的礼品劵号码为"+code+",是否确认核销？")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog,
                                        int which) {
                        OkGo.<InformationModel>post(mUrl)
                                .params("PrzCode" , finalCode)
                                .params("token",getMyApplication().mToken)
                                .execute(new JsonCallback<InformationModel>(GiftCodeActivity.this) {
                                    @Override
                                    public void onSuccess(Response<InformationModel> response) {
                                        InformationModel model=response.body();
                                        if(model!=null&&model.error==0){
                                            goInformation(model.data);
                                        }else {
                                            Toast.makeText(GiftCodeActivity.this,model.info,Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog,
                                        int which) {

                    }
                })
                .show();




    }

    /**
     * 跳转到扫码界面扫码
     */
    @NeedsPermission(Manifest.permission.CAMERA)
    public void goScan() {
        Intent intent = new Intent(GiftCodeActivity.this, CaptureActivity.class);
        startActivityForResult(intent, REQUEST_CODE_SCAN);
    }

    /**
     * 跳转到信息界面
     */
    public void goInformation(InformationModel.DataBean data) {
        Intent intent = new Intent(GiftCodeActivity.this, InfomationActivity.class);
        intent.putExtra("data",data);
        startActivity(intent);
    }

    @SuppressLint("NeedOnRequestPermissionsResult")
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        GiftCodeActivityPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // 扫描二维码/条码回传
        if (requestCode == REQUEST_CODE_SCAN && resultCode == RESULT_OK) {
            if (data != null) {
                //返回的文本内容
                String content = data.getStringExtra(DECODED_CONTENT_KEY);
                //返回的BitMap图像
                Bitmap bitmap = data.getParcelableExtra(DECODED_BITMAP_KEY);
                if(!TextUtils.isEmpty(content))
                ev1.setText( content);
            }
        }
    }
}
