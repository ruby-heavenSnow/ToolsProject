package fragment.ruby.toolsproject.views;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Point;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.NumberPicker;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import fragment.ruby.toolsproject.R;

public class DateTimePickerDialog extends Dialog
{
    private static final String TAG = DateTimePickerDialog.class.getSimpleName();
    private Context mContext;
    private NumberPicker yearNP;
    private NumberPicker monthNP;
    private NumberPicker dayNP;
//    private NumberPicker hourNP;
//    private NumberPicker minuteNP;
    private Button mEnterButton;
    private Button mClearButton;


    private String[] years = new String[201];

    public DateTimePickerDialog(Context context)
    {
//        super(context, R.style.MDialogStyle);
        super(context, R.style.AppTheme);
        this.mContext = context;
        setContentView(R.layout.dialog_date_time);
        WindowManager wm = getWindow().getWindowManager();
        Display display = wm.getDefaultDisplay();
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        Point p = new Point();
        display.getSize(p);
        lp.width = (int)(p.x*0.95);//设置对话框的宽
        lp.height =(int)(p.y*0.6);//设置对话框的高
        getWindow().setAttributes(lp);
        initView();
    }

    private void initView()
    {
        yearNP = (NumberPicker) findViewById(R.id.year);
        yearNP.setEnabled(true);
//        yearNP.setEditEnabled(false);
        monthNP = (NumberPicker) findViewById(R.id.month);
        monthNP.setMaxValue(11);
        monthNP.setMinValue(0);
        monthNP.setEnabled(true);
//        monthNP.setEditEnabled(false);
        dayNP = (NumberPicker) findViewById(R.id.day);
        dayNP.setEnabled(true);
//        dayNP.setEditEnabled(false);
//        hourNP = (NumberPicker) findViewById(R.id.hour);
//        hourNP.setMaxValue(23);
//        hourNP.setMinValue(0);
//        hourNP.setEnabled(true);
//        hourNP.setEditEnabled(false);
//        minuteNP = (NumberPicker) findViewById(R.id.minute);
//        minuteNP.setMaxValue(59);
//        minuteNP.setMinValue(0);
//        minuteNP.setEnabled(true);
//        minuteNP.setEditEnabled(false);

        mEnterButton = (Button) findViewById(R.id.dialog_date_time_enter);
        mClearButton = (Button) findViewById(R.id.dialog_date_time_clear);
//        initdata();
        initListener();
    }

    /**
     * 根据当前的时间得到，年月日时分的集合
     */
    private void initdata()
    {
        Calendar calendar = Calendar.getInstance();
        int curYear = calendar.get(Calendar.YEAR);

        int k=0;
        for(int i =curYear-100;i<=curYear+100;i++)
        {
            years[k++]=i+"年";
        }
        yearNP.setMinValue(0);
        yearNP.setMaxValue(200);
        yearNP.setDisplayedValues(years);
        yearNP.setValue(100);

        int curmonth = calendar.get(Calendar.MONTH);
        Log.i(TAG, "curyear" + curYear);
        Log.i(TAG, "curmonth" + curmonth);
        String[] months = new String[12];
        for(int i =0;i<12;i++)
        {
            months[i]=(i+1)+"月";
        }
        monthNP.setDisplayedValues(months);
        monthNP.setValue(curmonth);


        int curday = calendar.get(Calendar.DATE);
        Log.i(TAG, "curday" + curday);
        int max = getdaycounts(curYear,curmonth);
        String[] days = new String[max];
        for(int i=0;i<max;i++)
        {
            days[i] = (i+1)+"日";
        }
        dayNP.setMinValue(0);
        dayNP.setMaxValue(max - 1);
        dayNP.setDisplayedValues(days);
        dayNP.setValue(curday - 1);

//        int curhour = calendar.get(Calendar.HOUR_OF_DAY);
//        String[] hours = new String[24];
//        for(int i =0;i<24;i++)
//        {
//            hours[i] = i+"时";
//        }
//        hourNP.setDisplayedValues(hours);
//        hourNP.setValue(curhour);
//
//        int curminute = calendar.get(Calendar.MINUTE);
//        String[] minutes = new String[60];
//        for(int i =0;i<60;i++)
//        {
//            minutes[i] = i+"分";
//        }
//        minuteNP.setDisplayedValues(minutes);
//        minuteNP.setValue(curminute);

    }

    public void setDate(String calendar)
    {
//        SimpleDateFormat simpleDate = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        SimpleDateFormat simpleDate = new SimpleDateFormat("yyyy-MM-dd");
        Calendar c = Calendar.getInstance();
        try {
            if(calendar.equals(0000-00-00)) {

            }else {
                c.setTime(simpleDate.parse(calendar));
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        int curYear = c.get(Calendar.YEAR);
        int k=0;
        for(int i =curYear-100;i<=curYear+100;i++)
        {
            years[k++]=i+"年";
        }
        yearNP.setMinValue(0);
        yearNP.setMaxValue(200);
        yearNP.setDisplayedValues(years);
        yearNP.setValue(100);

        int curmonth = c.get(Calendar.MONTH);
        Log.i(TAG, "curyear" + curYear);
        Log.i(TAG, "curmonth" + curmonth);
        String[] months = new String[12];
        for(int i =0;i<12;i++)
        {
            months[i]=(i+1)+"月";
        }
        monthNP.setDisplayedValues(months);
        monthNP.setValue(curmonth);


        int curday = c.get(Calendar.DATE);
        Log.i(TAG, "curday" + curday);
        int max = getdaycounts(curYear,curmonth);
        String[] days = new String[max];
        for(int i=0;i<max;i++)
        {
            days[i] = (i+1)+"日";
        }
        dayNP.setMinValue(0);
        dayNP.setMaxValue(max - 1);
        dayNP.setDisplayedValues(days);
        dayNP.setValue(curday - 1);

//        int curhour = c.get(Calendar.HOUR_OF_DAY);
//        String[] hours = new String[24];
//        for(int i =0;i<24;i++)
//        {
//            hours[i] = i+"时";
//        }
//        hourNP.setDisplayedValues(hours);
//        hourNP.setValue(curhour);
//
//        int curminute = c.get(Calendar.MINUTE);
//        String[] minutes = new String[60];
//        for(int i =0;i<60;i++)
//        {
//            minutes[i] = i+"分";
//        }
//        minuteNP.setDisplayedValues(minutes);
//        minuteNP.setValue(curminute);
    }

    /**
     *
     * @param year 年
     * @param month 月
     * @return 本月的天数
     */
    private int getdaycounts(int year,int month)
    {
        int days = 0;
        SimpleDateFormat simpleDate = new SimpleDateFormat("yyyy/MM");
        Calendar c = Calendar.getInstance();
        try {
            c.setTime(simpleDate.parse(year+"/"+(month+1)));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        days = c.getActualMaximum(Calendar.DAY_OF_MONTH);
        return days;
    }

    private void initListener()
    {
        monthNP.setOnScrollListener(new NumberPicker.OnScrollListener() {
            @Override
            public void onScrollStateChange(NumberPicker view, int scrollState)
            {
                if(scrollState == NumberPicker.OnScrollListener.SCROLL_STATE_IDLE)
                {
                    //当前的月份
                    int value = view.getValue();
                    String curyear = years[yearNP.getValue()];
                    int selyear = Integer.valueOf(curyear.replace("年", ""));
                    int selday = dayNP.getValue();

                    int days = getdaycounts(selyear,value);
                    Log.i(TAG,selyear+"年"+(value+1)+"月"+days+"天");
                    String[] dayss = new String[days];
                    for(int i=0;i<days;i++)
                    {
                        dayss[i] = (i + 1) + "日";
                    }
                    dayNP.setValue(4);

                    dayNP.setMaxValue(days-1);
                    dayNP.setMinValue(0);
                    dayNP.setDisplayedValues(dayss);


                    if(selday>days-1)
                    {
                        dayNP.setValue(days-1);
                    }else
                    {
                        dayNP.setValue(selday);
                    }

                }

            }
        });

        yearNP.setOnScrollListener(new NumberPicker.OnScrollListener()
        {
            @Override
            public void onScrollStateChange(NumberPicker view, int scrollState)
            {
                if(scrollState == NumberPicker.OnScrollListener.SCROLL_STATE_IDLE)
                {
                    //已选择的年
                    String s_selyear = years[yearNP.getValue()];
                    int selyear = Integer.valueOf(s_selyear.replace("年", ""));

                    //已选择的月
                    int month = monthNP.getValue();
                    int selday = dayNP.getValue();
                    int days = getdaycounts(selyear,month);
                    Log.i(TAG,selyear+"年"+(month+1)+"月"+days+"天");
                    String[] dayss = new String[days];
                    for(int i=0;i<days;i++)
                    {
                        dayss[i] = (i + 1) + "日";
                    }
                    dayNP.setValue(4);

                    dayNP.setMaxValue(days-1);
                    dayNP.setMinValue(0);
                    dayNP.setDisplayedValues(dayss);


                    if(selday>days-1)
                    {
                        dayNP.setValue(days-1);
                    }else
                    {
                        dayNP.setValue(selday);
                    }
                }
            }
        });
    }

    /**
     *
     * @param listener  点击事件
     */
    public void setOnEnterButtonClick(View.OnClickListener  listener)
    {
        mEnterButton.setOnClickListener(listener);
    }

    public void setOnClearButtonClick(View.OnClickListener listener)
    {
        mClearButton.setOnClickListener(listener);
    }

    public String getTime()
    {
        int yearid = yearNP.getValue();
        int monthid = monthNP.getValue()+1;
        int dayid = dayNP.getValue()+1;
//        int timeid = hourNP.getValue();
//        int minuteid = minuteNP.getValue();
        String year = years[yearid].replace("年", "");
//        SimpleDateFormat simpleDate = new SimpleDateFormat("yyyy/MM/dd HH:mm");
        SimpleDateFormat simpleDate = new SimpleDateFormat("yyyy/MM/dd");
        Calendar c = Calendar.getInstance();
        try {
//            c.setTime(simpleDate.parse(year+"/"+monthid+"/"+dayid+" "+timeid+":"+minuteid));
            c.setTime(simpleDate.parse(year+"/"+monthid+"/"+dayid));
        } catch (ParseException e) {
            e.printStackTrace();
        }

//        return new SimpleDateFormat("yy-MM-dd HH:mm").format(c.getTime());
        return new SimpleDateFormat("yyyy-MM-dd").format(c.getTime());
    }
}
