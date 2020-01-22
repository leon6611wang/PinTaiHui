package com.zhiyu.quanzhu.planet;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.zhiyu.quanzhu.R;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_soul_main);

        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.flContainer, new MainFragment())
                .commitAllowingStateLoss();
    }
}