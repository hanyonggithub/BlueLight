package com.vui.bluelight.ble;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.vui.bluelight.utils.DataFormatUtils;
import com.vui.bluelight.utils.LogUtils;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothProfile;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

public class BleUtils2 {

	private static final String TAG = "BleUtils2";

	public static List<BtDevice> mList = new ArrayList<BtDevice>();// 所有扫描到设备
	
	public static Map<String, List<BtDevice>> groupMap = new HashMap<String, List<BtDevice>>();// 分组设备

	public static List<BtDevice> selectDevices = new ArrayList<BtDevice>();// 选中要连接的设备

	public static Map<String, BtDevice> selectedMap = new HashMap<String, BtDevice>();;// 保存连接设备信息

	private Context context;
	public BluetoothManager manager;
	private BluetoothAdapter mBluetoothAdapter;

	private boolean mScanning;
	private Handler mHandler;
	private static final long SCAN_PERIOD = 10000;

	public static final String SER_UUID = "00001000-0000-1000-8000-00805f9b34fb";
	public static final String CHAR_UUID = "00001002-0000-1000-8000-00805f9b34fb";
	public final static UUID UUID_HEART_RATE_MEASUREMENT = UUID.fromString(SampleGattAttributes.HEART_RATE_MEASUREMENT);

	private static final int STATE_DISCONNECTED = 0;
	private static final int STATE_CONNECTING = 1;
	private static final int STATE_CONNECTED = 2;

	private static BleUtils2 instance;

	private BleUtils2() {
	}

	public static BleUtils2 getInstance() {
		if (instance == null) {
			synchronized (BleUtils2.class) {
				if (instance == null) {
					instance = new BleUtils2();
				}
			}
		}
		return instance;
	}

	public void initBt(Context context) {

		if (selectDevices != null) {
			selectDevices.clear();
		} else {
			selectDevices = new ArrayList<BtDevice>();
		}
		if (selectedMap != null) {
			selectedMap.clear();
		} else {
			selectedMap = new HashMap<String, BtDevice>();
		}

		manager = BluetoothManager.getInstance();
		manager.setContext(context);
		mHandler = new Handler();
		this.context = context;

		if (!context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
			Toast.makeText(context, "您的手机不支持BLE", Toast.LENGTH_SHORT).show();
			((Activity) context).finish();
		}
		mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
		if (!mBluetoothAdapter.isEnabled()) {
			mBluetoothAdapter.enable();
		}
		if (!manager.isScanning) {
			scanLeDevice(true);
		}

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

	// Device scan callback.
	private BluetoothAdapter.LeScanCallback mLeScanCallback = new BluetoothAdapter.LeScanCallback() {

		@Override
		public void onLeScan(final BluetoothDevice device, int rssi, byte[] scanRecord) {
			((Activity) context).runOnUiThread(new Runnable() {

				@Override
				public void run() {

					BtDevice mDevice = new BtDevice(device.getAddress(), device.getName());
					if (!mList.contains(mDevice)) {
						mList.add(mDevice);
						Log.d(TAG, "deviceName=" + device.getName() + ",deviceAddress=" + device.getAddress() + ",size="
								+ mList.size());
						// 读设备特征值，分组信息

						// 如需自动连接，则连接，考虑用队列

					}
				}
			});
		}
	};

	private BluetoothGattCallback mGattCallback = new BluetoothGattCallback() {
		@Override
		public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
			String intentAction;
			if (newState == BluetoothProfile.STATE_CONNECTED) {

				/*
				 * mConnectionState = STATE_CONNECTED;
				 * BleUtils.gattMap.put(gatt.getDevice().getAddress(), gatt);
				 */
				if (BleUtils2.selectedMap.containsKey(gatt.getDevice().getAddress())) {
					BleUtils2.selectedMap.get(gatt.getDevice().getAddress()).setConnectedState(STATE_CONNECTED);
					BleUtils2.selectedMap.get(gatt.getDevice().getAddress()).setmBluetoothGatt(gatt);
				} else {
					BtDevice device = new BtDevice(gatt.getDevice().getAddress(), gatt.getDevice().getName());
					device.setmBluetoothGatt(gatt);
					device.setConnectedState(STATE_CONNECTED);
					BleUtils2.selectedMap.put(gatt.getDevice().getAddress(), device);
				}

				// 连接开始将集合清空，没连接一次，添加一个，连接设备数等于，要连接设备数，即为都连接好了，这个时候可以发送连接完毕的广播
				// 设置timeout 时间，如果有一个设备一直未连接上，要能找到未连接的设备，尝试再次连接，多连接几次
				if (BleUtils2.selectedMap.size() == BleUtils2.selectDevices.size()) {
					// 设备已经都连接上了
				}

				Log.e(TAG, "Connected to GATT server.");
				// Attempts to discover services after successful connection.
				Log.e(TAG, "Attempting to start service discovery:" + gatt.discoverServices());

			} else if (newState == BluetoothProfile.STATE_DISCONNECTED) {
				if (BleUtils2.selectedMap.containsKey(gatt.getDevice().getAddress())) {
					BleUtils2.selectedMap.get(gatt.getDevice().getAddress()).setConnectedState(STATE_DISCONNECTED);
					BleUtils2.selectedMap.get(gatt.getDevice().getAddress()).getmBluetoothGatt().close();
					BleUtils2.selectedMap.get(gatt.getDevice().getAddress()).setmBluetoothGatt(null);
					BleUtils2.selectedMap.remove(gatt.getDevice().getAddress());
				}
				gatt.close();
				gatt = null;
				Log.e(TAG, "Disconnected from GATT server.");

			}
		}

		@Override
		public void onServicesDiscovered(BluetoothGatt gatt, int status) {
			if (status == BluetoothGatt.GATT_SUCCESS) {
				Log.e(TAG, "onService discovered status=" + status);
				BluetoothGattService targetService = gatt.getService(UUID.fromString(BleUtils2.SER_UUID));
				if (targetService != null) {
					LogUtils.e("获取指定uuid service");
					if (BleUtils2.selectedMap.containsKey(gatt.getDevice().getAddress())) {
						BleUtils2.selectedMap.get(gatt.getDevice().getAddress()).setmGattService(targetService);
					}
				}
			} else {
				Log.e(TAG, "onServicesDiscovered received: " + status);
				// 是否要没有发现就置为null
				/*
				 * if(BleUtils.selectedMap.containsKey(gatt.getDevice().
				 * getAddress())){
				 * BleUtils.selectedMap.get(gatt.getDevice().getAddress()).
				 * setmGattService(null); }
				 */

			}
		}

		@Override
		public void onCharacteristicRead(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
			if (status == BluetoothGatt.GATT_SUCCESS) {
				handleData(gatt,characteristic);
			}
		}

		@Override
		public void onCharacteristicChanged(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic) {
			handleData(gatt,characteristic);
		}

		@Override
		public void onCharacteristicWrite(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {

		}
	};

	private void handleData(BluetoothGatt gatt,BluetoothGattCharacteristic characteristic) {

		// This is special handling for the Heart Rate Measurement profile. Data
		// parsing is
		// carried out as per profile specifications:
		// http://developer.bluetooth.org/gatt/characteristics/Pages/CharacteristicViewer.aspx?u=org.bluetooth.characteristic.heart_rate_measurement.xml
		if (UUID_HEART_RATE_MEASUREMENT.equals(characteristic.getUuid())) {
			int flag = characteristic.getProperties();
			int format = -1;
			if ((flag & 0x01) != 0) {
				format = BluetoothGattCharacteristic.FORMAT_UINT16;
				Log.d(TAG, "Heart rate format UINT16.");
			} else {
				format = BluetoothGattCharacteristic.FORMAT_UINT8;
				Log.d(TAG, "Heart rate format UINT8.");
			}
			final int heartRate = characteristic.getIntValue(format, 1);
			Log.d(TAG, String.format("Received heart rate: %d", heartRate));
		} else {
			// For all other profiles, writes the data formatted in HEX.
			final byte[] data = characteristic.getValue();
			if (data != null && data.length > 0) {
				final StringBuilder stringBuilder = new StringBuilder(data.length);
				for (byte byteChar : data)
					stringBuilder.append(String.format("%02X ", byteChar));
				
				LogUtils.e("recieve data:" +stringBuilder.toString());
				
				
			}
			
		}
	}

	Thread connectionThread;
	int curConnIndex;

	public void connect(final List<BtDevice> devices) {
		BleUtils2.selectDevices.addAll(devices);
		curConnIndex = 0;
		if (connectionThread == null) {
			connectionThread = new Thread(new Runnable() {
				@Override
				public void run() {
					while (curConnIndex <= devices.size() - 1) {
						mBluetoothAdapter.getRemoteDevice(devices.get(curConnIndex).getAddress()).connectGatt(context,
								false, mGattCallback);
						LogUtils.e("连接第" + curConnIndex + "个设备，address=" + devices.get(curConnIndex).getAddress());
						curConnIndex++;
						try {
							Thread.sleep(250);
						} catch (InterruptedException e) {
						}
					}
					connectionThread.interrupt();
					connectionThread = null;
				}
			});
			connectionThread.start();
			mHandler.postDelayed(new Runnable() {
				@Override
				public void run() {
					if (BleUtils2.selectedMap.size() < BleUtils2.selectDevices.size()) {
						for (BtDevice dev : BleUtils2.selectDevices) {
							if (!BleUtils2.selectedMap.containsKey(dev.getAddress())) {
								// 有设备未连接，在此进行从新连接
								LogUtils.e("有设备，未连接上，重新连接+address=" + dev.getAddress());
								connect(dev.getAddress());
								try {
									Thread.sleep(250);
								} catch (InterruptedException e) {
								}
							}
						}
					}
				}
			}, 5000);

		}
	}

	public boolean connect(final String address) {
		if (mBluetoothAdapter == null || address == null) {
			Log.w(TAG, "BluetoothAdapter not initialized or unspecified address.");
			return false;
		}

		BluetoothGatt gatt;

		final BluetoothDevice device = mBluetoothAdapter.getRemoteDevice(address);

		if (device == null) {
			Log.w(TAG, "Device not found.  Unable to connect.");
			return false;
		}
		// We want to directly connect to the device, so we are setting the
		// autoConnect
		// parameter to false.
		Log.e(TAG, "Trying to create a new connection.");
		gatt = device.connectGatt(context, false, mGattCallback);
		// BleUtils.gattMap.put(address, gatt);
		// mBluetoothDeviceAddress = address;

		return true;
	}

	byte[] WriteBytes = new byte[20];
	Thread writeThread = null;

	public void write(final String uuid, final String content) {
		if (BleUtils2.selectedMap != null && BleUtils2.selectedMap.size() > 0) {

			if (writeThread == null) {
				writeThread = new Thread(new Runnable() {
					@Override
					public void run() {
						Iterator<Map.Entry<String, BtDevice>> iterator = BleUtils2.selectedMap.entrySet().iterator();
						LogUtils.e("map.size=" + BleUtils2.selectedMap.size());
						while (iterator.hasNext()) {
							Map.Entry<String, BtDevice> entry = iterator.next();
							if (entry.getValue().getmGattService() != null) {
								LogUtils.e("从map中获取 对应service");
								BluetoothGattCharacteristic character = entry.getValue().getmGattService()
										.getCharacteristic(UUID.fromString(uuid));
								if (character != null) {
									LogUtils.e("获取目标character---");
									int charaProp = character.getProperties();
									if ((charaProp | BluetoothGattCharacteristic.PROPERTY_WRITE) > 0) {
										byte[] value = new byte[20];
										value[0] = (byte) 0x00;
										if (content != null && content.length() > 0) {
											WriteBytes = DataFormatUtils.hex2byte(content.getBytes());
										}
										character.setValue(value[0], BluetoothGattCharacteristic.FORMAT_UINT8, 0);
										character.setValue(WriteBytes);

										entry.getValue().getmBluetoothGatt().writeCharacteristic(character);

										if ((charaProp | BluetoothGattCharacteristic.PROPERTY_NOTIFY) > 0) {
											setCharacteristicNotification(entry.getValue().getmBluetoothGatt(),
													character, true);
										}
										try {
											Thread.sleep(100);
										} catch (InterruptedException e) {
											e.printStackTrace();
										}
									}
								}
							}
						}
						writeThread.interrupt();
						writeThread = null;
					}
				});
				writeThread.start();

			}

		}

	}

	/**
	 * Request a read on a given {@code BluetoothGattCharacteristic}. The read
	 * result is reported asynchronously through the
	 * {@code BluetoothGattCallback#onCharacteristicRead(android.bluetooth.BluetoothGatt, android.bluetooth.BluetoothGattCharacteristic, int)}
	 * callback.
	 *
	 * @param characteristic
	 *            The characteristic to read from.
	 */
	public void readCharacteristic(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic) {
		if (mBluetoothAdapter == null || gatt == null) {
			Log.w(TAG, "BluetoothAdapter not initialized");
			return;
		}
		gatt.readCharacteristic(characteristic);
	}

	public void writeCharacteristic(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic) {
		if (mBluetoothAdapter == null || gatt == null) {
			Log.w(TAG, "BluetoothAdapter not initialized");
			return;
		}
		gatt.writeCharacteristic(characteristic);
	}

	/**
	 * Enables or disables notification on a give characteristic.
	 *
	 * @param characteristic
	 *            Characteristic to act on.
	 * @param enabled
	 *            If true, enable notification. False otherwise.
	 */
	public void setCharacteristicNotification(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic,
			boolean enabled) {
		if (mBluetoothAdapter == null || gatt == null) {
			Log.w(TAG, "BluetoothAdapter not initialized");
			return;
		}
		gatt.setCharacteristicNotification(characteristic, enabled);

		// This is specific to Heart Rate Measurement.
		if (UUID_HEART_RATE_MEASUREMENT.equals(characteristic.getUuid())) {
			BluetoothGattDescriptor descriptor = characteristic
					.getDescriptor(UUID.fromString(SampleGattAttributes.CLIENT_CHARACTERISTIC_CONFIG));
			descriptor.setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);
			gatt.writeDescriptor(descriptor);
		}
	}

}
