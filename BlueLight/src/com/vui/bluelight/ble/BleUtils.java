package com.vui.bluelight.ble;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.vui.bluelight.utils.DataFormatUtils;
import com.vui.bluelight.utils.LogUtils;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

public class BleUtils {
	private static final String TAG = "BleUtils";
	public static Map<String,BluetoothGatt> gattMap=new HashMap<String, BluetoothGatt>();
	public static List<BtDevice> mList=new ArrayList<BtDevice>();
	public static Map<String,List<BtDevice>> groupMap=new HashMap<String, List<BtDevice>>();
	public int currentIndex=0;
	public String curAddress="";
	
	public BluetoothManager manager;
	private Context context;
	
	private BluetoothAdapter mBluetoothAdapter;
	private boolean mScanning;
	private Handler mHandler;
	private static final long SCAN_PERIOD = 10000;
	
	
	private BluetoothLeService mBluetoothLeService;
	private BluetoothGattService mGattService;
	private BluetoothGattCharacteristic mCharacteristic;
	
	private static final String SER_UUID = "0000fff0-0000-1000-8000-00805f9b34fb";
	private static final String CHAR_UUID = "0000fff6-0000-1000-8000-00805f9b34fb";

	
	private static BleUtils instance;

	private BleUtils(){
	}

	public static BleUtils getInstance() {

		if (instance == null) {
			synchronized (BleUtils.class) {
				if (instance == null) {
					instance = new BleUtils();
				}
			}
		}
		return instance;
	}
	
	public void initBt(Context context) {
		mList.clear();
		gattMap.clear();
		groupMap.clear();
		manager = BluetoothManager.getInstance();
		manager.setContext(context);
		mHandler=new Handler();
		this.context=context;
		
		if (!context.getPackageManager().hasSystemFeature(
				PackageManager.FEATURE_BLUETOOTH_LE)) {
			Toast.makeText(context, "您的手机不支持BLE", Toast.LENGTH_SHORT).show();
			((Activity)context).finish();
		}
		mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
		if (!mBluetoothAdapter.isEnabled()) {
			mBluetoothAdapter.enable();
		}
		bindservice();
		registerReciever();
		if (!manager.isScanning){
//			new ScanLeTask().execute();
			scanLeDevice(true);
		}

	}
	
	public void destroyBt(){
		unbindService();
		unregisterReciever();
	}
	
	
	public void scanLeDevice(final boolean enable) {
		if (enable) {
			// 针对扫描不到当前已连接设备
			Log.e(TAG, "开始扫描设备-----");
			mHandler.postDelayed(new Runnable() {
				@Override
				public void run() {
					mScanning = false;
					mBluetoothAdapter.stopLeScan(mLeScanCallback);
				}
			}, SCAN_PERIOD);

			mScanning = true;
			mBluetoothAdapter.startLeScan(mLeScanCallback);
		} else {
			Log.e(TAG, "停止扫描设备-----");
			mScanning = false;
			mBluetoothAdapter.stopLeScan(mLeScanCallback);
			;
		}

	}

public class ScanLeTask extends AsyncTask<Void, Void, Void> {

	@Override
	protected Void doInBackground(Void... params) {
		if(instance!=null){
			instance.scanLeDevice(true);
		}
		
		return null;
	}

}

// Device scan callback.
private BluetoothAdapter.LeScanCallback mLeScanCallback = new BluetoothAdapter.LeScanCallback() {

	@Override
	public void onLeScan(final BluetoothDevice device, int rssi,
			byte[] scanRecord) {
		((Activity)context).runOnUiThread(new Runnable() {

			@Override
			public void run() {
			
				BtDevice mDevice = new BtDevice(device.getAddress(),
						device.getName());
				if (!mList.contains(mDevice)) {
					mList.add(mDevice);
					
					/*manager.connect(mDevice.getAddress(),
							mList.size()-1);*/
					Log.d(TAG, "deviceName="+device.getName()+",deviceAddress="+device.getAddress()+",size="+mList.size());
					//读设备特征值，分组信息
					
				}
			}
		});
	}
};

//蓝牙连接回调

private final BroadcastReceiver mGattUpdateReceiver = new BroadcastReceiver() {
	@Override
	public void onReceive(Context context, Intent intent) {
		final String action = intent.getAction();
		if (BluetoothLeService.ACTION_GATT_CONNECTED.equals(action)) {
			// Toast.makeText(MainActivity2.this, "connected",
			// Toast.LENGTH_LONG).show();
			Log.e(TAG, "mGattUpdateReceiver ---设备已连接--begin");

			Log.e(TAG, "mGattUpdateReceiver ---设备已连接--end");
		} else if (BluetoothLeService.ACTION_GATT_DISCONNECTED.equals(action)) {
			Log.e(TAG, "mGattUpdateReceiver ---设备连接断开");

		} else if (BluetoothLeService.ACTION_GATT_SERVICES_DISCOVERED.equals(action)) {
			Log.e(TAG, "action discovered");
			if (mBluetoothLeService == null) {
				return;
			}
			List<BluetoothGattService> gattServices = mBluetoothLeService.getSupportedGattServices();

			if (gattServices != null) {
				for (BluetoothGattService service : gattServices) {
					Log.e(TAG, "service uuid=" + service.getUuid());
					if (service.getUuid().toString().equals(SER_UUID)) {
						mGattService = service;
						mCharacteristic = mGattService.getCharacteristic(UUID.fromString(CHAR_UUID));
						
						Log.e(TAG, "获取目标设备 charater");
					}
				}
			}

			if (mCharacteristic != null) {
			}
			// List<BluetoothGattCharacteristic> gattCharacteristics =
			// gattService.getCharacteristics();

		} else if (BluetoothLeService.ACTION_DATA_AVAILABLE.equals(action)) {
			// 解析数据 指令码 校验值
			LogUtils.e("data:"+intent.getStringExtra(BluetoothLeService.EXTRA_DATA));
			read(intent.getStringExtra(BluetoothLeService.EXTRA_DATA));
		}
	}
};

private final ServiceConnection mServiceConnection = new ServiceConnection() {

	@Override
	public void onServiceConnected(ComponentName componentName, IBinder service) {
		Log.e(TAG, "service connected");
		mBluetoothLeService = ((BluetoothLeService.LocalBinder) service).getService();
		if (mBluetoothLeService == null) {
			Log.e(TAG, "service is null 重新绑定---");
			
		} else {
			if (!mBluetoothLeService.initialize()) {
				Log.e(TAG, "Unable to initialize Bluetooth");
			}
			// Automatically connects to the device upon successful start-up
			// initialization.
			
			/*if (!TextUtils.isEmpty(curAddress)) {
				Log.e(TAG, "service 已连接，connect 设备 adrres=" + curAddress);
				mBluetoothLeService.connect(curAddress);
			}*/
			Log.e(TAG, "service is bund!");
		}

	}

	@Override
	public void onServiceDisconnected(ComponentName componentName) {
		Log.e(TAG, "service disconnected");
		mBluetoothLeService = null;
	}
};

public void registerReciever(){
	context.registerReceiver(mGattUpdateReceiver, makeGattUpdateIntentFilter());
}
public void unregisterReciever(){
	context.unregisterReceiver(mGattUpdateReceiver);
}

public void unbindService(){
	
}
public void bindservice(){
	Intent gattServiceIntent = new Intent(context, BluetoothLeService.class);
	context.bindService(gattServiceIntent, mServiceConnection, Context.BIND_AUTO_CREATE);
}

private static IntentFilter makeGattUpdateIntentFilter() {
	final IntentFilter intentFilter = new IntentFilter();
	intentFilter.addAction(BluetoothLeService.ACTION_GATT_CONNECTED);
	intentFilter.addAction(BluetoothLeService.ACTION_GATT_DISCONNECTED);
	intentFilter.addAction(BluetoothLeService.ACTION_GATT_SERVICES_DISCOVERED);
	intentFilter.addAction(BluetoothLeService.ACTION_DATA_AVAILABLE);
	return intentFilter;
}

public void connect(String address){ 
	
	if(mBluetoothLeService!=null){
		mBluetoothLeService.connect(address);
	}

}

byte[] WriteBytes = new byte[20];
private BluetoothGattCharacteristic mNotifyCharacteristic;

public boolean write(String str) {
	Log.e(TAG, "write:" + str);
	if (mCharacteristic != null) {
		final int charaProp = mCharacteristic.getProperties();
		if ((charaProp | BluetoothGattCharacteristic.PROPERTY_WRITE) > 0) {
			byte[] value = new byte[20];
			value[0] = (byte) 0x00;
			if (str != null && str.length() > 0) {
				WriteBytes = DataFormatUtils.hex2byte(str.getBytes());
			}
			mCharacteristic.setValue(value[0], BluetoothGattCharacteristic.FORMAT_UINT8, 0);
			mCharacteristic.setValue(WriteBytes);
			if (mBluetoothLeService != null) {
				mBluetoothLeService.writeCharacteristic(mCharacteristic);

				if ((charaProp | BluetoothGattCharacteristic.PROPERTY_NOTIFY) > 0) {
					mNotifyCharacteristic = mCharacteristic;
					mBluetoothLeService.setCharacteristicNotification(mCharacteristic, true);
				}
				return true;
			}
		}
	} else {
		Log.e(TAG, "当前设备未连接！");
	}
	return false;
}
public void read(String result){
	
	
}
public void handle(){
	
}



	

}
