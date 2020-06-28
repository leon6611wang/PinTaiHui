package com.zhiyu.quanzhu.ui.activity;

import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import com.zhiyu.quanzhu.R;
import com.zhiyu.quanzhu.base.BaseActivity;
import com.zhiyu.quanzhu.ui.adapter.MessageListAdapter;
import com.zhiyu.quanzhu.ui.dialog.MessageMenuDownDialog;
import com.zhiyu.quanzhu.ui.dialog.MessageMenuUpDialog;
import com.zhiyu.quanzhu.utils.ScreentUtils;
import java.util.ArrayList;
import java.util.List;

public class MessageListActivity extends BaseActivity implements View.OnTouchListener{
    private ListView listView;
    private MessageListAdapter adapter;
    private List<String> list = new ArrayList<>();
    private MessageMenuDownDialog downDialog;
    private MessageMenuUpDialog upDialog;
    private int menuX,menuY;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_list);

        ScreentUtils.getInstance().setStatusBarLightMode(this, true);
        initViews();
        initDialogs();
    }

    private void initDialogs() {
        downDialog = new MessageMenuDownDialog(this, R.style.dialog);
        upDialog = new MessageMenuUpDialog(this, R.style.dialog, new MessageMenuUpDialog.OnMessageMenuListener() {
            @Override
            public void onTop(int position) {

            }

            @Override
            public void onDelete(int position) {

            }
        });
    }

    private void initViews() {
        for (int i = 0; i < 10; i++) {
            list.add(String.valueOf(i));
        }
        listView = findViewById(R.id.listView);
        adapter = new MessageListAdapter();
        adapter.setList(list);
        listView.setAdapter(adapter);
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                int y = (view.getHeight() + 1) * (position - listView.getFirstVisiblePosition() + 1 / 4);
                upDialog.show();
                upDialog.setLocation(menuX,menuY,position);
                return false;
            }
        });
        listView.setOnTouchListener(this);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            menuX = (int) event.getX();
            menuY = (int) event.getY();
        }
        return false;
    }

}