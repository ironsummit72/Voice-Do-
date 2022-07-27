package com.sumitdev.voicedo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import java.util.List;

public class PagerAdapter extends androidx.viewpager.widget.PagerAdapter {
    Context context;
    List<IntroScreenItem> lists;

    public PagerAdapter(Context context, List<IntroScreenItem> lists) {
        this.context = context;
        this.lists = lists;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {


        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layoutScreen = inflater.inflate(R.layout.permissionlayout, null);
        ImageView imgSlide = layoutScreen.findViewById(R.id.permissionimage);
        TextView title = layoutScreen.findViewById(R.id.permissiontitle);
        TextView description = layoutScreen.findViewById(R.id.permissiondescription);
        //  Button grant=layoutScreen.findViewById(R.id.grantbutton);
        title.setText(lists.get(position).getTitle());
        description.setText(lists.get(position).getDescription());
        imgSlide.setImageResource(lists.get(position).getScreenimage());
        container.addView(layoutScreen);

        return layoutScreen;

    }

    @Override
    public int getCount() {
        return lists.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {

        container.removeView((View) object);

    }

}
