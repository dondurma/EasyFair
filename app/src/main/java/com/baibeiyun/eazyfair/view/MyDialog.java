package com.baibeiyun.eazyfair.view;

import android.app.Dialog;
import android.content.Context;

import com.baibeiyun.eazyfair.R;


public class MyDialog {
    private Context context;
    private Dialog progressDialog;

    public MyDialog(Context contexts) {
        this.context = contexts;
        progressDialog = new Dialog(context, R.style.progress_dialog);
    }

    public void dialogShow() {
        progressDialog.setContentView(R.layout.dialog);
        progressDialog.setCancelable(true);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        progressDialog.show();
    }

    public void dialogDismiss() {
        progressDialog.dismiss();
    }
}
