package com.baibeiyun.eazyfair.fragment;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.baibeiyun.eazyfair.R;


@SuppressLint("ValidFragment")
public class PictrueFragment extends Fragment {

    private Bitmap resId;

    @SuppressLint("ValidFragment")
    public PictrueFragment(Bitmap resId) {

        this.resId = resId;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.scale_pic_item, null);
        initView(view);

        return view;
    }

    private void initView(View view) {
        ImageView imageView = (ImageView) view.findViewById(R.id.scale_pic_item);

        imageView.setImageBitmap(resId);

    }

}
