package com.example.a13787.morningcall;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;

import java.util.ArrayList;
import java.util.List;

public class MapActivity extends BaseActivity
{
    private Marker markerOnMap = null;
    private MyDataBase eventOnMap = null;
    public LocationClient mLocationClient;
    private MapView mapView;
    private BaiduMap baiduMap;
    private boolean isFirstLocate = true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SDKInitializer.initialize(getApplicationContext());
        super.onCreate(savedInstanceState);
        //隐藏状态栏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }
    @Override
    protected void initView()
    {
        mLocationClient=new LocationClient(getApplicationContext());
        mLocationClient.registerLocationListener(new MyLocationListener());
        mapView=(MapView)findViewById(R.id.bmapView);
        baiduMap = mapView.getMap();
        baiduMap.setMyLocationEnabled(true);
        List<String> permissionList=new ArrayList<>();
        if(ContextCompat.checkSelfPermission(MapActivity.this, Manifest.permission.ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED){
            permissionList.add(Manifest.permission.ACCESS_FINE_LOCATION);
        }
        if(ContextCompat.checkSelfPermission(MapActivity.this, Manifest.permission.READ_PHONE_STATE)!= PackageManager.PERMISSION_GRANTED){
            permissionList.add(Manifest.permission.READ_PHONE_STATE);
        }
        if(ContextCompat.checkSelfPermission(MapActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED){
            permissionList.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        if(!permissionList.isEmpty()){
            String[] permissions=permissionList.toArray(new String[permissionList.size()]);
            ActivityCompat.requestPermissions(MapActivity.this,permissions,1);
        }
        else {
            requestLocation();
            initData();   //模拟数据
            //对"+"按钮的监听button_add
		/*Button button_add=(Button)findViewById(R.id.button_add);
		button_add.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				Intent intent = new Intent(MapActivity.this,AddActivity.class);
				startActivityForResult(intent, 1);
			}
		)};*/
            baiduMap.setOnMarkerClickListener(new BaiduMap.OnMarkerClickListener()
            {
                @Override
                public boolean onMarkerClick(Marker marker)
                {
                    final MyDataBase mydataBase = (MyDataBase) marker.getExtraInfo().get("DataBase");
                    if (mydataBase.isClickable()==false)
                        return true;
                    if (markerOnMap!=null)
                        markerOnMap.remove();
                    if (eventOnMap!=null)
                        updateOverlay(eventOnMap);
                    eventOnMap=mydataBase;
                    marker.remove();
                    LinearLayout layout=(LinearLayout)findViewById(R.id.layout_infotop);
                    layout.setVisibility(View.VISIBLE);
                    LatLng ll = new LatLng(mydataBase.getLatitude(),mydataBase.getLongtitude());
                    TextView infoUsername = (TextView)findViewById(R.id.info_username);
                    infoUsername.setText("Name: " + mydataBase.getUsername());
                    TextView infoDepartment = (TextView)findViewById(R.id.info_department);
                    infoDepartment.setText("Department: " + mydataBase.getDepartment());
                    TextView infoTime = (TextView)findViewById(R.id.info_time);
                    infoTime.setText("Time: " + mydataBase.getStartTime() + " - " + mydataBase.getEndTime());
                    String color;
                    if (mydataBase.getType().equals("study"))
                    {
                        BitmapDescriptor bitmap = BitmapDescriptorFactory.fromResource(R.drawable.icon_study_mark);
                        MyDataBase temp  = new MyDataBase();
                        temp.setClickable(false);
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("DataBase",temp);
                        OverlayOptions option = new MarkerOptions().position(ll).extraInfo(bundle).icon(bitmap);
                        markerOnMap = (Marker) baiduMap.addOverlay(option);
                        color="#ff0000";
                    }
                    else if (mydataBase.getType().equals("food"))
                    {
                        BitmapDescriptor bitmap = BitmapDescriptorFactory.fromResource(R.drawable.icon_food_mark);
                        MyDataBase temp  = new MyDataBase();
                        temp.setClickable(false);
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("DataBase",temp);
                        OverlayOptions option = new MarkerOptions().position(ll).extraInfo(bundle).icon(bitmap);
                        markerOnMap = (Marker) baiduMap.addOverlay(option);
                        color="#00ff00";
                    }
                    else if (mydataBase.getType().equals("sport"))
                    {
                        BitmapDescriptor bitmap = BitmapDescriptorFactory.fromResource(R.drawable.icon_sport_mark);
                        MyDataBase temp  = new MyDataBase();
                        temp.setClickable(false);
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("DataBase",temp);
                        OverlayOptions option = new MarkerOptions().position(ll).extraInfo(bundle).icon(bitmap);
                        markerOnMap = (Marker) baiduMap.addOverlay(option);
                        color="#0000ff";
                    }
                    else
                    {
                        BitmapDescriptor bitmap = BitmapDescriptorFactory.fromResource(R.drawable.icon_enjoyment_mark);
                        MyDataBase temp  = new MyDataBase();
                        temp.setClickable(false);
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("DataBase",temp);
                        OverlayOptions option = new MarkerOptions().position(ll).extraInfo(bundle).icon(bitmap);
                        markerOnMap = (Marker) baiduMap.addOverlay(option);
                        color="#ff00ff";
                    }
                    infoUsername.setTextColor(Color.parseColor(color));
                    infoDepartment.setTextColor(Color.parseColor(color));
                    infoTime.setTextColor(Color.parseColor(color));
                    //Add InfoWindow
                    baiduMap.hideInfoWindow();
                    Button button = new Button(getApplicationContext());
                    button.setText(mydataBase.getTitle());
                    button.setAllCaps(false);
                    InfoWindow mInfoWindow = new InfoWindow(button, ll, -170);
                    baiduMap.showInfoWindow(mInfoWindow);
                    return true;
                }
            });
        }
    }
    @Override
    protected int initLayout()
    {
        return R.layout.activity_map;
    }
    @Override
    protected void initData()
    {
        MyDataBase mydataBase= new MyDataBase();
        mydataBase.setLongtitude(121.40658068154808);
        mydataBase.setLatitude(31.228386048773345);
        mydataBase.setUsername("cyq");
        mydataBase.setSchoolName("ECNU");
        mydataBase.setDepartment("SE");
        mydataBase.setClickable(true);
        mydataBase.setType("study");
        mydataBase.setTitle("Prepare for exam");
        mydataBase.setLocation("ecnu library");
        mydataBase.setInfo("I want to study for my comming Examination.");
        mydataBase.setStartTime("2019.3.9 15:00");
        mydataBase.setEndTime("2019.3.9 17:00");
        mydataBase.setSex("all");
        updateOverlay(mydataBase);
        mydataBase= new MyDataBase();
        mydataBase.setLongtitude(121.40440721093566);
        mydataBase.setLatitude(31.228679705989208);
        mydataBase.setUsername("cyq");
        mydataBase.setSchoolName("ECNU");
        mydataBase.setDepartment("SE");
        mydataBase.setClickable(true);
        mydataBase.setType("sport");
        mydataBase.setTitle("Running");
        mydataBase.setLocation("ecnu playground");
        mydataBase.setInfo("I want to run for an hour.");
        mydataBase.setStartTime("2019.3.9 18:00");
        mydataBase.setEndTime("2019.3.9 19:00");
        mydataBase.setSex("all");
        updateOverlay(mydataBase);
        mydataBase= new MyDataBase();
        mydataBase.setLongtitude(121.40319750967011);
        mydataBase.setLatitude(31.230346081244303);
        mydataBase.setUsername("cyq");
        mydataBase.setSchoolName("ECNU");
        mydataBase.setDepartment("SE");
        mydataBase.setClickable(true);
        mydataBase.setType("food");
        mydataBase.setTitle("Breakfast");
        mydataBase.setLocation("ecnu Hexi canteen");
        mydataBase.setInfo("I hope to have breakfast everyday.");
        mydataBase.setStartTime("2019.3.9 6:00");
        mydataBase.setEndTime("2019.3.9 8:00");
        mydataBase.setSex("all");
        updateOverlay(mydataBase);
        mydataBase = new MyDataBase();
        mydataBase.setLongtitude(121.40449750967011);
        mydataBase.setLatitude(31.231446081244303);
        mydataBase.setUsername("cyq");
        mydataBase.setSchoolName("ECNU");
        mydataBase.setDepartment("SE");
        mydataBase.setClickable(true);
        mydataBase.setType("enjoyment");
        mydataBase.setTitle("PlayUNO");
        mydataBase.setLocation("ecnu Fifth dorm");
        mydataBase.setInfo("Let's play UNO.");
        mydataBase.setStartTime("2019.3.9 20:00");
        mydataBase.setEndTime("2019.3.9 22:00");
        mydataBase.setSex("all");
        updateOverlay(mydataBase);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        switch (requestCode)
        {
            case 1:
                if (resultCode==RESULT_OK)
                {
                    updateOverlay((MyDataBase) data.getSerializableExtra("DataBase"));
                }
        }
    }
    private void updateOverlay(MyDataBase mydataBase)
    {
        LatLng point = new LatLng(mydataBase.getLatitude(), mydataBase.getLongtitude());
        //构建Marker图标
        BitmapDescriptor bitmap = null;
        if (mydataBase.getType().equals("study"))
            bitmap = BitmapDescriptorFactory.fromResource(R.drawable.icon_study);
        else if (mydataBase.getType().equals("food"))
            bitmap = BitmapDescriptorFactory.fromResource(R.drawable.icon_food);
        else if (mydataBase.getType().equals("sport"))
            bitmap = BitmapDescriptorFactory.fromResource(R.drawable.icon_sport);
        else bitmap = BitmapDescriptorFactory.fromResource(R.drawable.icon_enjoyment);
        Bundle bundle = new Bundle();
        bundle.putSerializable("DataBase",mydataBase);
        //构建MarkerOption，用于在地图上添加
        MarkerOptions option = new MarkerOptions().position(point).extraInfo(bundle).icon(bitmap);
        //在地图上添加Marker，并显示
        Log.d("overlay", "add");
        baiduMap.addOverlay(option);
    }
    private void navigateTo(BDLocation location)
    {
        if (isFirstLocate)
        {
            LatLng ll = new LatLng(location.getLatitude(),location.getLongitude());
            MapStatusUpdate update = MapStatusUpdateFactory.newLatLng(ll);
            baiduMap.animateMapStatus(update);
            update = MapStatusUpdateFactory.zoomTo(16f);
            baiduMap.animateMapStatus(update);
            isFirstLocate = false;
        }
        Log.d("Latitude",""+location.getLatitude());
        Log.d("Longitude",""+location.getLongitude());
        MyLocationData.Builder locationBulider=new MyLocationData.Builder();
        locationBulider.latitude(location.getLatitude());
        locationBulider.longitude(location.getLongitude());
        MyLocationData locationData=locationBulider.build();
        baiduMap.setMyLocationData(locationData);

    }

    private void requestLocation()
    {
        initLocation();
        mLocationClient.start();
    }

    private void initLocation(){
        LocationClientOption option=new LocationClientOption();
        option.setCoorType("bd09ll");
        option.setIsNeedAddress(true);
        option.setOpenGps(true);
        option.setScanSpan(1000);
        option.setIsNeedLocationPoiList(true);
        option.setPriority(LocationClientOption.GpsFirst);
        mLocationClient.setLocOption(option);
    }
    @Override
    protected void initListener()
    {

    }
    @Override
    protected void  onResume(){
        super.onResume();
        mapView.onResume();
    }
    @Override
    protected void  onPause(){
        super.onPause();
        mapView.onPause();
    }
    @Override
    protected void  onDestroy(){
        super.onDestroy();
        mLocationClient.stop();
        mapView.onDestroy();
        baiduMap.setMyLocationEnabled(false);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,String[] permissions,int[] grantResults)
    {
        switch(requestCode){
            case 1:
                if (grantResults.length>0)
                {
                    for (int result: grantResults)
                    {
                        if (result!= PackageManager.PERMISSION_GRANTED)
                        {
                            Toast.makeText(this, "You must grant all the permissions",Toast.LENGTH_SHORT).show();
                            finish();
                            return;
                        }
                    }
                    requestLocation();
                }
                else
                {
                    Toast.makeText(this, "Error occur",Toast.LENGTH_SHORT).show();
                    finish();
                }
                break;
            default:
        }
    }
    public class MyLocationListener implements BDLocationListener
    {
        @Override
        public void onReceiveLocation(final BDLocation location)
        {
            if (location.getLocType() == BDLocation.TypeGpsLocation || location.getLocType()==BDLocation.TypeNetWorkLocation){
                navigateTo(location);

            }
        }
    }
}
