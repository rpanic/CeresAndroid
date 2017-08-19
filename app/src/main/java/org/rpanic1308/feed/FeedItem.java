package org.rpanic1308.feed;

import java.io.Serializable;
import java.util.Comparator;

public class FeedItem implements Serializable{

    private static final long serialVersionUID = 1L;

    private long timeStamp;

    private Object content;
    //private ActionListener onClick;
    private String id;
    private String type;
    private int priority = 0;

    //public abstract Object displayItem();  //nur für Android

    //public void setOnClickListener(ActionListener listener){
     //   onClick = listener;
    //}

    public void setContent(Object content){
        this.content = content;
    }

    public long getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(long timeStamp) {
        this.timeStamp = timeStamp;
    }

    //public ActionListener getOnClick() {
    //    return onClick;
    //}

    //public void setOnClick(ActionListener onClick) {
    //    this.onClick = onClick;
    //}

    public Object getContent() {
        return content;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public boolean equals(FeedItem i, FeedItem i2){
        if(i.getId() != null && i.getId() != null){
            return i.getId().equals(i2.getId());
        }
        return i.getTimeStamp() == i2.getTimeStamp();
    }

    public static class FeedComparator implements Comparator<FeedItem>{

        @Override
        public int compare(FeedItem o1, FeedItem o2) {
            return Long.compare(o2.getTimeStamp(), o1.getTimeStamp());
        }
    }

    public String toCSVString(){
        return Long.toString(timeStamp) + "~" + content.toString() + "~" + id + "~" + type;
    }

    public static FeedItem fromCSVString(String s){
        FeedItem item = new FeedItem();
        String[] arr = s.split("~");
        for(int i = 0 ; i < arr.length ; i++){
            if(arr[i].equals("null")){
                arr[i] = null;
            }
        }
        if(arr[0].equals("")){
            arr[0] = Long.toString(System.currentTimeMillis());
        }
        item.setTimeStamp(Long.parseLong(arr[0]));
        item.setContent(arr[1]);
        item.setId(arr[2]);
        item.setType(arr[3]);
        return item;
    }
}
