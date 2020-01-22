package com.zhiyu.quanzhu.planet;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.zhiyu.quanzhu.R;
import com.zhiyu.quanzhu.planet.adapter.TestAdapter;
import com.zhiyu.quanzhu.planet.view.SoulPlanetsView;


/**
 * ================================================
 * 作    者：Herve、Li
 * 创建日期：2019/9/24
 * 描    述：
 * 修订历史：
 * ================================================
 */
public class MainFragment extends Fragment {

    private View root;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_soul_main, container, false);
        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        final SoulPlanetsView soulPlanet = root.findViewById(R.id.soulPlanetView);
        soulPlanet.setAdapter(new TestAdapter());

        root.findViewById(R.id.clBackground).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "数据更新中", Toast.LENGTH_SHORT).show();
                soulPlanet.setAdapter(new TestAdapter());
            }
        });


        soulPlanet.setOnTagClickListener(new SoulPlanetsView.OnTagClickListener() {
            @Override
            public void onItemClick(ViewGroup parent, View view, int position) {
                Toast.makeText(getContext(), String.valueOf(position), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
