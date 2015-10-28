package com.example.suguiming.calendar.calendar;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by suguiming on 15/10/27.
 */
public class CalendarPagerAdapter extends PagerAdapter {
    List<CalendarView> pages;
    public CalendarPagerAdapter(List<CalendarView> views){
        this.pages = views;
    }

    @Override
    public int getCount() {
        return pages.size();
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        //container.removeView(pages.get(position));
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View view = pages.get(position);

        container.removeView(view);
        container.addView(view);

        return view;
    }

    @Override
    public boolean isViewFromObject(View view, Object o) {
        return view == o;
    }
}
