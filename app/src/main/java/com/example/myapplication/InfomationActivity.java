package com.example.myapplication;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.callback.JsonCallback;
import com.example.myapplication.model.InformationModel;
import com.example.myapplication.model.Model;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;

import butterknife.BindView;
import butterknife.ButterKnife;

public class InfomationActivity extends BaseActivity {

    @BindView(R.id.ev1)
    TextView ev1;
    @BindView(R.id.ev2)
    TextView ev2;
    @BindView(R.id.tvPhone)
    TextView tvPhone;
    @BindView(R.id.tvCode)
    TextView tvCode;
    @BindView(R.id.sureTv)
    TextView sureTv;

    private InformationModel.DataBean data;
    private String mUrl = "http://wx.weibenh5.com/2019/Oct/XFL/api/Appapi.php?a=confirm";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_infomation);
        ButterKnife.bind(this);
        data = (InformationModel.DataBean) getIntent().getSerializableExtra("data");
        if (data == null) {
            finish();
            return;
        }

        initData();
    }

    private void initData() {
        ev1.setText(data.name);
        ev2.setText(data.shop);
        tvPhone.setText(data.phone);
        tvCode.setText(data.drivingID);

        sureTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Sure();
            }
        });
    }


    public void Sure() {
        OkGo.<Model>post(mUrl)
                .params("token", getMyApplication().mToken)
                .execute(new JsonCallback<Model>(InfomationActivity.this) {
                    @Override
                    public void onSuccess(Response<Model> response) {
                        Model model = response.body();
                        if (model != null && model.error == 0) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(InfomationActivity.this);
                            builder.setTitle("")
                                    .setMessage(model.info)
                                    .setPositiveButton("返回", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog,
                                                            int which) {
                                            finish();
                                        }
                                    })

                                    .show();
                        } else {
                            Toast.makeText(InfomationActivity.this, model.info, Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }
}
