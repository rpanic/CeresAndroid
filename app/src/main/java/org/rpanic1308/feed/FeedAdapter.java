package org.rpanic1308.feed;

import android.content.Context;
import android.content.res.Resources;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.Interpolator;
import android.view.animation.OvershootInterpolator;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.ocpsoft.prettytime.PrettyTime;
import org.rpanic1308.Views.ExpandableTextView;

import rpanic1308.ceres.R;

/**
 * Created by Team_ on 19.03.2017.
 */

public class FeedAdapter extends ArrayAdapter<FeedItem>{

    Context context;
    List<FeedItem> list;

    public FeedAdapter(Context context, int resource, List<FeedItem> objects) {
        super(context, resource, objects);
        this.context = context;
        Collections.sort(objects, new FeedComparator());
        this.list = objects;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        FeedItem item = list.get(position);

        LayoutInflater inflater = null;
        if(context instanceof AppCompatActivity){
            inflater = ((AppCompatActivity)context).getLayoutInflater();
        }

        View v = null;
    try {
        switch (item.getType()) {
            case "text":
                v = inflater.inflate(R.layout.feeditem_text, null);
                TextView headline = (TextView) v.findViewById(R.id.headline);

                headline.setText(item.getContent().toString().split(";")[0]);
                final ExpandableTextView text = (ExpandableTextView) v.findViewById(R.id.text);

                text.setText(item.getContent().toString().split(";")[1]);

                final ImageView image = (ImageView) v.findViewById(R.id.imageView);

                initEvents(text, image, v);
                break;
            case "image":
                v = inflater.inflate(R.layout.feeditem_text, null);

                ImageView iv = new ImageView(context);
                iv.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                //iv.setImageDrawable(context.getDrawable(R.drawable.palinski));

                ViewGroup gr = (ViewGroup) v;
                gr.addView(iv);

                final View v3 = v;

                TextView headline3 = (TextView) v.findViewById(R.id.headline);

                headline3.setText(item.getContent().toString().split(";")[0]);
                final ExpandableTextView text2 = (ExpandableTextView) v.findViewById(R.id.text);

                text2.setText(item.getContent().toString().split(";")[1]);

                final ImageView image2 = (ImageView) v.findViewById(R.id.imageView);

                initEvents(text2, image2, v);
                break;

            //Text 2 ohne headline
            case "text2":

                v = inflater.inflate(R.layout.feeditem_text2, null);
                ExpandableTextView text3 = (ExpandableTextView) v.findViewById(R.id.text);

                text3.setText(item.getContent().toString());

                final ImageView image3 = (ImageView) v.findViewById(R.id.imageView);

                initEvents(text3, image3, v);
                break;

            case "weather":

                v = inflater.inflate(R.layout.feeditem_weather, null);

                String[] arr = item.getContent().toString().split(";");

                ImageView image4 = (ImageView) v.findViewById(R.id.imageView);
                Picasso.with(context).load(arr[2]).into(image4);

                TextView textView = (TextView) v.findViewById(R.id.textViewCity);
                textView.setText(arr[1]);

                textView = (TextView) v.findViewById(R.id.textViewDegree);

                if (!(arr.length < 6 || arr[5] == null || arr[5].equals(""))) {  //Wenn beide Temperaturen vorhanden sind
                    textView.setText(arr[4] + "°C / " + arr[5] + "°C");
                } else {
                    textView.setText(arr[4] + "°C");
                }

                //textView.setText(arr[5]);

                textView = (TextView) v.findViewById(R.id.textViewDesc);
                textView.setText(arr[3]);

                textView = (TextView) v.findViewById(R.id.textViewTime);
                String time = new PrettyTime().format(new Date(Long.parseLong(arr[0])));
                textView.setText(time);

                break;

        }
    }catch(Exception e){
        e.printStackTrace();
    }

        if(item.getPriority() > 0){
            v.setBackgroundColor(ContextCompat.getColor(context, R.color.colorPrimaryDark));
        }

        return v;
    }

    public void initEvents(final ExpandableTextView text, final ImageView image, final View parent){
        text.post(new Runnable() {
            @Override
            public void run() {

                if(text.getLineCount() <= text.getMaxLines()){
                    image.setVisibility(View.GONE);
                }else {

                    //text.setInterpolator(new OvershootInterpolator(2F));
                    image.setVisibility(View.VISIBLE);
                    text.setOnExpandListener(new ExpandableTextView.OnExpandListener() {
                        @Override
                        public void onExpand(ExpandableTextView view) {
                            Animation animation = AnimationUtils.loadAnimation(context, R.anim.rotation_animation);

                            image.startAnimation(animation);

                            image.setImageDrawable(context.getDrawable(R.drawable.ic_collapse_small_holo_light));

                        }

                        @Override
                        public void onCollapse(ExpandableTextView view) {
                            Animation animation = AnimationUtils.loadAnimation(context, R.anim.rotation_animation_back);
                            image.startAnimation(animation);

                            image.setImageDrawable(context.getDrawable(R.drawable.ic_expand_small_holo_light));

                        }
                    });

                    parent.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                        text.toggle();
                        }
                    });
                }
                //text.setInterpolator(new OvershootInterpolator(2F/*getTensionFromLines(text.getLineCount())*/));
            }
        });
    }


    public static float getTensionFromLines(int lines){
        double i = Math.log(lines);
        return (float)i;
    }
}
