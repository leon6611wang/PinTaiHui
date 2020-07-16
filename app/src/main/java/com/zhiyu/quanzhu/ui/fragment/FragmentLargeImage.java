package com.zhiyu.quanzhu.ui.fragment;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.shizhefei.view.largeimage.LargeImageView;
import com.zhiyu.quanzhu.R;

public class FragmentLargeImage extends Fragment {
    private View view;
    private LargeImageView largeImageView;
    private String url;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_large_image, null);
        Bundle bundle = getArguments();
        url = bundle.getString("url");
        initViews();
        return view;
    }

    private void initViews() {
        largeImageView = view.findViewById(R.id.largeImageView);
        largeImageView.setEnabled(true);
        largeImageView.setHorizontalScrollBarEnabled(false);
        largeImageView.setVerticalScrollBarEnabled(false);
        largeImageView.setBackgroundColor(getResources().getColor(R.color.text_color_black));
        Glide.with(this).asBitmap().load(url)
                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                        largeImageView.setImage(resource);
                    }
                });
        largeImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });
    }
}
