package fragment.ruby.toolsproject.utils;

import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ListAdapter;
import android.widget.ListView;

import java.text.SimpleDateFormat;
import java.util.Date;

public class ToolUtil
{
    /**
     * 得到listView的实际高度
     * @param listView
     * @return
     */
    public static ViewGroup.LayoutParams setListViewHeight(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            return null;
        }

        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
//            listItem.measure(
//                    View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
//                    View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight
                + (listView.getDividerHeight() * (listAdapter.getCount() - 1));

        return params;
    }

    public static ViewGroup.LayoutParams setGirdViewHeight(GridView gridview)
    {
        ListAdapter listAdapter = gridview.getAdapter();
        if(listAdapter == null)
        {
            return null;
        }

        int totalHeight = 0;
//        int cols = gridview.getNumColumns();
        int cols = 3;
        for(int i =0;i<listAdapter.getCount();i+=cols)
        {
            View listItem = listAdapter.getView(i,null,gridview);
            listItem.measure(0,0);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = gridview.getLayoutParams();
        params.height = totalHeight;
        return params;
    }

    /** 地球半径 **/
    public static final double EARTH_RADIUS = 6378.137;

    /**
     * 根据指定的两个经纬度得到距离
     * @param lat1   120.380025,31.49437
     * @param lng1
     * @param lat2   120.362404,31.550974
     * @param lng2
     * @return
     */
    public static double getDistance(double lat1,double lng1,double lat2,double lng2)
    {
        double radLat1 = rad(lat1);
        double radLat2 = rad(lat2);
        double a = radLat1 - radLat2;
        double b = rad(lng1) - rad(lng2);

        double s = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a / 2), 2) +
                Math.cos(radLat1) * Math.cos(radLat2) * Math.pow(Math.sin(b / 2), 2)));
        s = s * EARTH_RADIUS;
        s = Math.round(s * 10000) / 10000;
        return s;
    }

    private static double rad(double d)
    {
        return d* Math.PI/180.0;
    }

    public static String getCurrentTime(){

        SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        return format.format(new Date());
    }

    public static String getFileName(String fullName){
        int lastIndex = fullName.lastIndexOf("/") + 1;
        if(lastIndex>=0&&lastIndex<=fullName.length()){
            return fullName.substring(lastIndex).replace(".jpg","");
        }
        return "";
    }

    public static String getFileType(String fullName){
        int lastpoint = fullName.lastIndexOf(".") + 1;
        if(lastpoint>=0&&lastpoint<=fullName.length()){
            return fullName.substring(lastpoint);
        }
        return "";
    }

    public static long parseTime(String formTime) {
        //example:"/Date(1419778800000+0800)/"
        if (formTime == null) {
            return 0;
        }
        int startIndex = formTime.indexOf("(") + 1;
        int endIndex = formTime.indexOf("+");
        if (endIndex > startIndex && startIndex >= 0 && endIndex <= formTime.length()) {
            return Long.parseLong(formTime.substring(startIndex, endIndex));
        }
        return 0;
    }

    /**
     *
     * @param reg 正则
     * @param str 需要匹配的字符串
     * @return
     */
    public static boolean isMatch(String reg, String str) {
        return str.matches(reg);
    }

}
