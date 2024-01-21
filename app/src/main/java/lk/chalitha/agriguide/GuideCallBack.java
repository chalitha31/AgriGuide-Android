package lk.chalitha.agriguide;

import java.util.ArrayList;

import lk.chalitha.agriguide.model.Guide_post_model;

public interface GuideCallBack {

    void onCallback(ArrayList<Guide_post_model> reportDataList);
}
