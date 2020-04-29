package com.zhiyu.quanzhu.model.bean;

import com.zhiyu.quanzhu.R;

public class BuyVipIndex {
    private int icon;
    private boolean isCurrent;

    public BuyVipIndex(boolean isCurrent) {
        this.isCurrent = isCurrent;
    }

    public int getIcon() {
        if (isCurrent) {
            icon = R.mipmap.buy_vip_index_current;
        } else {
            icon = R.mipmap.buy_vip_index_normal;
        }
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public boolean isCurrent() {
        return isCurrent;
    }

    public void setCurrent(boolean current) {
        isCurrent = current;
    }
}
