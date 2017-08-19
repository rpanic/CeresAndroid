package org.rpanic1308.feed;

import java.util.Comparator;

/**
 * Created by Team_ on 07.07.2017.
 */

public class FeedComparator implements Comparator<FeedItem> {

    @Override
    public int compare(FeedItem o1, FeedItem o2) {
        return Integer.compare(o1.getPriority(), o2.getPriority());
    }

}
