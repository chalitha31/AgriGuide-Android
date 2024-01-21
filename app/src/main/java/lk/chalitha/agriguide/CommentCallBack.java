package lk.chalitha.agriguide;

import java.util.ArrayList;

import lk.chalitha.agriguide.model.commentModel;

public interface CommentCallBack {

    void onCallback(ArrayList<commentModel> reportDataList);
}
