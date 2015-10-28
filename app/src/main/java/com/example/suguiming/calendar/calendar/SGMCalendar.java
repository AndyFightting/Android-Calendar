package com.example.suguiming.calendar.calendar;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.suguiming.calendar.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by suguiming on 15/10/28.
 */
public class SGMCalendar extends RelativeLayout implements View.OnClickListener{

    public enum MoveFlag{
        NONE,LEFT,RIGHT
    }

    private LayoutInflater inflater;
    private TextView titleTv;
    private TextView preMonthBt;
    private TextView nextMonthBt;
    private ViewPager calendarPager;
    List<CalendarView> calendarViewList;

    MoveFlag moveFlag;
    private int centerYear,centerMonth;
    private int preMonth,preMonthYear;
    private int nextMonth,nextMonthYear;

    public SGMCalendar(Context context,AttributeSet attributeSet){
        super(context,attributeSet);
        inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.calendar_main, this);//设置布局xml

        centerYear = DateUtil.getCurrentYear();
        centerMonth = DateUtil.getCurrentMonth();
        resetPreAndNextMonth();

        calendarViewList = new ArrayList<>();
        for (int i=0;i<3;i++){
            CalendarView calendarView = (CalendarView)inflater.inflate(R.layout.calendar,null);
            if(i==0){
                calendarView.setYearMonthDay(preMonthYear, preMonth, 1);
            }else if(i==1){
                calendarView.setYearMonthDay(centerYear, centerMonth, DateUtil.getCurrentDay());
            }else {
                calendarView.setYearMonthDay(nextMonthYear, nextMonth, 1);
            }
            calendarViewList.add(calendarView);

            //上个月或下个月的day item 点击监听
            calendarView.setOnPreOrNextMonthTapListener(new CalendarView.OnPreOrNextMonthTapListener() {
                @Override
                public void preMonthDayTap(int day) {
                    doMoveRight();
                    centerCalendarTapedAtDay(day);
                }
                @Override
                public void nextMonthDayTap(int day) {
                    doMoveLeft();
                    centerCalendarTapedAtDay(day);
                }
            });

        }

        calendarPager = (ViewPager)view.findViewById(R.id.count_pager);
        calendarPager.setAdapter(new CalendarPagerAdapter(calendarViewList));
        calendarPager.setCurrentItem(1);

        calendarPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }
            @Override
            public void onPageSelected(int position) {
                if (position == 0) {
                    moveFlag = SGMCalendar.MoveFlag.RIGHT;
                }
                if (position == 2) {
                    moveFlag = SGMCalendar.MoveFlag.LEFT;
                }
            }
            @Override
            public void onPageScrollStateChanged(int state) {
                if (state == 0) {//滚动结束了
                    if (moveFlag == SGMCalendar.MoveFlag.RIGHT) {
                        doMoveRight();//手指往右滑动
                    } else if (moveFlag == SGMCalendar.MoveFlag.LEFT) {
                        doMoveLeft();//手指往左边滑动
                    }
                }
            }
        });

        titleTv = (TextView)view.findViewById(R.id.title_tv);
        titleTv.setText(String.format("%d年%d月", centerYear, centerMonth));

        preMonthBt = (TextView)view.findViewById(R.id.pre_month_bt);
        nextMonthBt = (TextView)view.findViewById(R.id.next_month_bt);
        preMonthBt.setOnClickListener(this);
        nextMonthBt.setOnClickListener(this);
    }

    private void doMoveRight() {//手指往右边滑动,上个月
        moveFlag = SGMCalendar.MoveFlag.NONE;
        preMonthTaped();

        CalendarView calendar0 = calendarViewList.get(0);
        CalendarView calendar1 = calendarViewList.get(1);
        CalendarView tmpCalendar = calendarViewList.get(2);//处理后移到0位
        tmpCalendar.setYearMonthDay(preMonthYear, preMonth, 1);

        calendarViewList.set(0, tmpCalendar);
        calendarViewList.set(1,calendar0);
        calendarViewList.set(2,calendar1);

        calendarPager.setAdapter(new CalendarPagerAdapter(calendarViewList));
        calendarPager.setCurrentItem(1);

    }
    private void doMoveLeft() {//手指往左边滑动,下个月
        moveFlag = SGMCalendar.MoveFlag.NONE;
        nextMonthTaped();

        CalendarView tmpCalendar = calendarViewList.get(0);//处理后移到2位
        CalendarView calendar1 = calendarViewList.get(1);
        CalendarView calendar2 = calendarViewList.get(2);
        tmpCalendar.setYearMonthDay(nextMonthYear, nextMonth, 1);

        calendarViewList.set(0,calendar1);
        calendarViewList.set(1,calendar2);
        calendarViewList.set(2,tmpCalendar);

        calendarPager.setAdapter(new CalendarPagerAdapter(calendarViewList));
        calendarPager.setCurrentItem(1);
    }
    public void tabClicked(){

    }

    @Override
    public void onClick(View view){
        switch (view.getId()){
            case R.id.pre_month_bt:{//点击上个月
                doMoveRight();
                break;
            }
            case R.id.next_month_bt:{//点击下个月
                doMoveLeft();
                break;
            }
        }
    }

    private void preMonthTaped(){
        centerMonth = preMonth;
        centerYear = preMonthYear;
        resetPreAndNextMonth();
    }
    private void nextMonthTaped(){
        centerMonth = nextMonth;
        centerYear = nextMonthYear;

        resetPreAndNextMonth();
    }


    private void resetPreAndNextMonth() {
        preMonth = centerMonth - 1;
        preMonthYear = centerYear;
        if (preMonth == 0) {
            preMonth = 12;
            preMonthYear = centerYear - 1;
        }

        nextMonth = centerMonth + 1;
        nextMonthYear = centerYear;
        if (nextMonth == 13) {
            nextMonth = 1;
            nextMonthYear = centerYear + 1;
        }
        if (titleTv != null){
            titleTv.setText(String.format("%d年%d月", centerYear, centerMonth));
        }
    }

    private void centerCalendarTapedAtDay(int day){
        CalendarView centerCalendar = calendarViewList.get(1);
        centerCalendar.clearLastSelectedStyle();
        centerCalendar.setCenterCalendarTapDay(day);
    }
}
