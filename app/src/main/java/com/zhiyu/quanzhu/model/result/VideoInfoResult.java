package com.zhiyu.quanzhu.model.result;

import com.zhiyu.quanzhu.base.BaseResult;
import com.zhiyu.quanzhu.model.data.VideoInfoData;

public class VideoInfoResult extends BaseResult {
    private VideoInfoData data;

    public VideoInfoData getData() {
        return data;
    }

    public void setData(VideoInfoData data) {
        this.data = data;
    }
}
