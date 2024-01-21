package lk.chalitha.agriguide;

import java.util.ArrayList;

public class NotificationData {


    private int id;
    private String notifyTitle;

    private String notifyType;
    private String notifyCategory;
    private String notifyDate;

    public NotificationData(int id, String notifyTitle, String notifyType, String notifyCategory, String notifyDate) {
        this.id = id;
        this.notifyTitle = notifyTitle;
        this.notifyType = notifyType;
        this.notifyCategory = notifyCategory;
        this.notifyDate = notifyDate;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNotifyTitle() {
        return notifyTitle;
    }

    public void setNotifyTitle(String notifyTitle) {
        this.notifyTitle = notifyTitle;
    }

    public String getNotifyType() {
        return notifyType;
    }

    public void setNotifyType(String notifyType) {
        this.notifyType = notifyType;
    }

    public String getNotifyCategory() {
        return notifyCategory;
    }

    public void setNotifyCategory(String notifyCategory) {
        this.notifyCategory = notifyCategory;
    }

    public String getNotifyDate() {
        return notifyDate;
    }

    public void setNotifyDate(String notifyDate) {
        this.notifyDate = notifyDate;
    }

    public  static ArrayList<NotificationData> getNotifyDataList(){

        ArrayList<NotificationData> notifyDataList  = new ArrayList<>();

        notifyDataList.add(new NotificationData(1,"New Report Notification","Problems","Vegetables","2023/08/26"));
        notifyDataList.add(new NotificationData(2,"New Guide Notification","information","Fruits","2023/08/26"));
        notifyDataList.add(new NotificationData(3,"Lorem Ipsum is simply dummy text of the printing and typesetting industr","Problems","Seed","2023/08/26"));
        notifyDataList.add(new NotificationData(4,"New Report Notification","information","Vegetables","2023/08/26"));
        notifyDataList.add(new NotificationData(5,"New Guide Notification","Problems","Vegetables","2023/08/26"));


        return notifyDataList;

    }
}
