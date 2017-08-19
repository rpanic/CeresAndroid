package org.rpanic1308.transmission.listeners;

import java.util.List;

import org.rpanic1308.ceres.MainFeedActivity;
import org.rpanic1308.feed.FeedItem;
import org.rpanic1308.transmission.ServerListener;
import org.rpanic1308.transmission.ServerToAppInfo;
import org.rpanic1308.transmission.SocketHolder;

/**
 * Created by Team_ on 01.04.2017.
 */

public class FeedResponseListener implements ServerListener{
    @Override
    public void onRecieve(ServerToAppInfo info, SocketHolder holder) {
        System.out.println("FeedResponseLIstener: " + info.toString());
        if(info.getType() == ServerToAppInfo.TransType.FeedItems)

            if (info.getData() instanceof List) {

                List<FeedItem> list = (List<FeedItem>) info.getData();
                for (FeedItem item : list) {
                    MainFeedActivity.mainActivity.addFeedItem(item);
                }

            } else {
                return;
            }
    }
}
