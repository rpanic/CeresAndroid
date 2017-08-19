package org.rpanic1308.ceres;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.ms.square.android.expandabletextview.ExpandableTextView;

import java.util.List;

import rpanic1308.ceres.R;

/**
 * Created by rpanic on 11.11.2016.
 */
public class FeedListAdapter extends ArrayAdapter<Object> {

    Context c;
    List<Object> list;

    public FeedListAdapter(Context context, int resource, List<Object> objects) {
        super(context, resource, objects);
        list = objects;
        c = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View v;
        LayoutInflater inflater = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        v = inflater.inflate(R.layout.layout_expandable_textview, parent, false); //TODO schauen wie das besser geht
        
        ExpandableTextView expTv1 = (ExpandableTextView) v.findViewById(R.id.expand_text_view)
                .findViewById(R.id.expand_text_view);

        // IMPORTANT - call setText on the ExpandableTextView to set the text content to display
        expTv1.setText(c.getString(R.string.loremIpsumSmall));

        //v.setLayoutParams(new GridView.LayoutParams(GridView.AUTO_FIT, 500));

        return v;
    }

}
