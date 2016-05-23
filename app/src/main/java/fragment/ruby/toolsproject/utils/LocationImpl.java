package fragment.ruby.toolsproject.utils;

import android.text.TextUtils;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;

import net.iaf.framework.app.BaseApplication;
import net.iaf.framework.util.Loger;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by qincunrong on 2015/8/3.
 */
public class LocationImpl {

    public static LocationImpl mLocationImpl=null;

    public LocationClient mLocationClient;
    public MyLocationListener mMyLocationListener;
    private int mLocationDuration = 0;
    private List<OnLocationListener> mOnLocationListenerList;


    public static LocationImpl getInstance() {
        if(mLocationImpl==null){
            mLocationImpl=new LocationImpl();
        }
        return mLocationImpl;
    }

    private LocationImpl() {
        initLocation();
        initLocationOption();
    }

    private void initLocation() {
        mLocationClient = new LocationClient(BaseApplication.getContext());
        mMyLocationListener = new MyLocationListener();
        mLocationClient.registerLocationListener(mMyLocationListener);
        mOnLocationListenerList = new ArrayList<OnLocationListener>();
    }
    private void initLocationOption(){
        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
//        option.setCoorType("gcj02");
        option.setCoorType("bd09ll");
        option.setScanSpan(mLocationDuration);
        option.setIsNeedAddress(true);
        option.setIgnoreKillProcess(true);
        option.setNeedDeviceDirect(false);
        option.setOpenGps(true);
        option.setTimeOut(10000);
//        option.setPoiExtraInfo(true);
//        option.setPoiDistance(1500);
//        option.setLocationNotify(true);
//        option.setEnableSimulateGps(false);
//        option.setIsNeedLocationDescribe(true);
//        option.setIsNeedLocationPoiList(true);
        mLocationClient.setLocOption(option);
    }


    public void startLocation() {
        Loger.i("开始定位。。。。");
        if (!mLocationClient.isStarted()) {
            mLocationClient.start();
        } else {
            int result = mLocationClient.requestLocation();
            Loger.i("result:" + result);
        }
    }


    public void addOnLocationListener(OnLocationListener listener) {
        if (listener != null && !mOnLocationListenerList.contains(listener)) {
            mOnLocationListenerList.add(listener);
        }
    }

    public void removeOnLocationListener(OnLocationListener listener) {
        if (listener != null && mOnLocationListenerList.contains(listener)) {
            mOnLocationListenerList.remove(listener);
        }
    }

    public interface OnLocationListener {
        void onLocationSuccess();

        void onLocationException();
    }

    /**
     * 实现实时位置回调监听
     */
    public class MyLocationListener implements BDLocationListener {

        @Override
        public void onReceiveLocation(BDLocation location) {
            //Receive Location
            parseLocationInfo(location);

        }
    }

    private void parseLocationInfo(BDLocation location) {
        int loactionResult = location.getLocType();
        Loger.i("location result:" + loactionResult);
        if (61 != loactionResult && 68 != loactionResult
                && 161 != loactionResult) {
            Loger.e("定位失败!");
            //将数据清除
//            SP_AppStatus.setCurrentLng("");
//            SP_AppStatus.setCurrentLat("");
//            SP_AppStatus.setCurrentDistrict("");
//            SP_AppStatus.setCurrentCityName("");
//            SP_AppStatus.setCurrentAddress("");
            for (OnLocationListener listener : mOnLocationListenerList) {
                if (listener != null) {
                    listener.onLocationException();
                }
            }
            return;
        }

        // 保存当前定位信息
//        SP_AppStatus.setCurrentLng(String.valueOf(location
//                .getLongitude()));
//        SP_AppStatus.setCurrentLat(String.valueOf(location
//                .getLatitude()));
//        SP_AppStatus.setCurrentDistrict(location.getDistrict());

        //get city name
        String cityName = location.getCity();
        if (!TextUtils.isEmpty(cityName)) {
            if (cityName.endsWith("市")) {
                cityName = cityName.substring(0, cityName.length() - 1);
            }
//            SP_AppStatus.setCurrentCityName(cityName);
        }

        //get address
        String address = "";
//        if (!TextUtils.isEmpty(location.getDistrict())) {
//            address += location.getDistrict();
//            if (!TextUtils.isEmpty(location.getStreet())) {
//                address += location.getStreet();
//            }
//
//            if (!TextUtils.isEmpty(location.getStreetNumber())) {
//                address += location.getStreetNumber();
//            }
//        }
//
//        if (!TextUtils.isEmpty(address) && !TextUtils.isEmpty(cityName)) {
//            SP_AppStatus.setCurrentAddress(cityName + address);
//
//        } else if (!TextUtils.isEmpty(address) && TextUtils.isEmpty(cityName)) {
//            SP_AppStatus.setCurrentAddress(address);
//
//        } else if (!TextUtils.isEmpty(cityName)) {
//            SP_AppStatus.setCurrentAddress(cityName);
//        }

        Loger.i("定位成功!");
        //call callback listeners
        for (OnLocationListener listener : mOnLocationListenerList) {
            if (listener != null) {
                listener.onLocationSuccess();
            }
        }

        Loger.i("lng:" + location.getLongitude());
        Loger.i("lat:" + location.getLatitude());
        Loger.i("city:" + location.getCity());
        Loger.i("street:" + location.getStreet());
        Loger.i("district:" + location.getDistrict());
        Loger.i("address:" + address);
        Loger.i("addr:" + location.getAddrStr());
    }




}
