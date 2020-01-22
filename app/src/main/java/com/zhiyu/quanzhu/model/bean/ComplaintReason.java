package com.zhiyu.quanzhu.model.bean;

/**
 * 投诉理由
 */
public class ComplaintReason {
    private String reason;
    private boolean isSelected;

    public ComplaintReason(String reason) {
        this.reason = reason;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}
