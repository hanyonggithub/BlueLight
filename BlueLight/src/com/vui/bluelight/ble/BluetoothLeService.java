/*
 * Copyright (C) 2013 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.vui.bluelight.ble;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.vui.bluelight.utils.DataFormatUtils;
import com.vui.bluelight.utils.LogUtils;

import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothProfile;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

/**
 * Service for managing connection and data communication with a GATT server
 * hosted on a given Bluetooth LE device.
 */
public class BluetoothLeService extends Service {
	private final static String TAG = BluetoothLeService.class.getSimpleName();

	private BluetoothManager mBluetoothManager;
	private BluetoothAdapter mBluetoothAdapter;
	private String mBluetoothDeviceAddress;
	private BluetoothGatt mBluetoothGatt;
	private int mConnectionState = STATE_DISCONNECTED;

	private static final int STATE_DISCONNECTED = 0;
	private static final int STATE_CONNECTING = 1;
	private static final int STATE_CONNECTED = 2;

	public final static String ACTION_GATT_CONNECTED = "com.example.bluetooth.le.ACTION_GATT_CONNECTED";
	public final static String ACTION_GATT_DISCONNECTED = "com.example.bluetooth.le.ACTION_GATT_DISCONNECTED";
	public final static String ACTION_GATT_SERVICES_DISCOVERED = "com.example.bluetooth.le.ACTION_GATT_SERVICES_DISCOVERED";
	public final static String ACTION_DATA_AVAILABLE = "com.example.bluetooth.le.ACTION_DATA_AVAILABLE";
	public final static String EXTRA_DATA = "com.example.bluetooth.le.EXTRA_DATA";

	public final static UUID UUID_HEART_RATE_MEASUREMENT = UUID.fromString(SampleGattAttributes.HEART_RATE_MEASUREMENT);
	Handler mHandler = new Handler();

	// Implements callback methods for GATT events that the app cares about. For
	// example,
	// connection change and services discovered.
	private final BluetoothGattCallback mGattCallback = new BluetoothGattCallback() {
		@Override
		public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
			String intentAction;
			if (newState == BluetoothProfile.STATE_CONNECTED) {
				intentAction = ACTION_GATT_CONNECTED;
				/*
				 * mConnectionState = STATE_CONNECTED;
				 * BleUtils.gattMap.put(gatt.getDevice().getAddress(), gatt);
				 */
				if (BleUtils.selectedMap.containsKey(gatt.getDevice().getAddress())) {
					BleUtils.selectedMap.get(gatt.getDevice().getAddress()).setConnectedState(STATE_CONNECTED);
					BleUtils.selectedMap.get(gatt.getDevice().getAddress()).setmBluetoothGatt(gatt);
				} else {
					BtDevice device = new BtDevice(gatt.getDevice().getAddress(), gatt.getDevice().getName());
					device.setmBluetoothGatt(gatt);
					device.setConnectedState(STATE_CONNECTED);
					BleUtils.selectedMap.put(gatt.getDevice().getAddress(), device);
				}

				// 连接开始将集合清空，没连接一次，添加一个，连接设备数等于，要连接设备数，即为都连接好了，这个时候可以发送连接完毕的广播
				// 设置timeout 时间，如果有一个设备一直未连接上，要能找到未连接的设备，尝试再次连接，多连接几次
				if (BleUtils.selectedMap.size() == BleUtils.selectDevices.size()) {
					// 设备已经都连接上了
					broadcastUpdate(intentAction);
				}

				Log.e(TAG, "Connected to GATT server.");
				// Attempts to discover services after successful connection.
				Log.e(TAG, "Attempting to start service discovery:" + gatt.discoverServices());

			} else if (newState == BluetoothProfile.STATE_DISCONNECTED) {
				intentAction = ACTION_GATT_DISCONNECTED;
				if (BleUtils.selectedMap.containsKey(gatt.getDevice().getAddress())) {
					BleUtils.selectedMap.get(gatt.getDevice().getAddress()).setConnectedState(STATE_DISCONNECTED);
					BleUtils.selectedMap.get(gatt.getDevice().getAddress()).getmBluetoothGatt().close();
					BleUtils.selectedMap.get(gatt.getDevice().getAddress()).setmBluetoothGatt(null);
					BleUtils.selectedMap.remove(gatt.getDevice().getAddress());
				}
				gatt.close();
				gatt = null;
				Log.e(TAG, "Disconnected from GATT server.");

				broadcastUpdate(intentAction);

			}
		}

		@Override
		public void onServicesDiscovered(BluetoothGatt gatt, int status) {
			if (status == BluetoothGatt.GATT_SUCCESS) {
				Log.e(TAG, "onService discovered status=" + status);
				BluetoothGattService targetService = gatt.getService(UUID.fromString(BleUtils.SER_UUID));
				if (targetService != null) {
					LogUtils.e("获取指定uuid service");
					if (BleUtils.selectedMap.containsKey(gatt.getDevice().getAddress())) {
						BleUtils.selectedMap.get(gatt.getDevice().getAddress()).setmGattService(targetService);
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
				broadcastUpdate(ACTION_DATA_AVAILABLE, characteristic);
			}
		}

		@Override
		public void onCharacteristicChanged(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic) {
			broadcastUpdate(ACTION_DATA_AVAILABLE, characteristic);
		}

		@Override
		public void onCharacteristicWrite(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {

		}
	};

	private void broadcastUpdate(final String action) {
		final Intent intent = new Intent(action);
		sendBroadcast(intent);
	}

	private void broadcastUpdate(final String action, final BluetoothGattCharacteristic characteristic) {
		final Intent intent = new Intent(action);

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
			intent.putExtra(EXTRA_DATA, String.valueOf(heartRate));
		} else {
			// For all other profiles, writes the data formatted in HEX.
			final byte[] data = characteristic.getValue();
			if (data != null && data.length > 0) {
				final StringBuilder stringBuilder = new StringBuilder(data.length);
				for (byte byteChar : data)
					stringBuilder.append(String.format("%02X ", byteChar));
				LogUtils.e("recieve data:"+new String(data));
				intent.putExtra(EXTRA_DATA, new String(data) + "\n" + stringBuilder.toString());
			}
		}
		sendBroadcast(intent);
	}

	public class LocalBinder extends Binder {
		public BluetoothLeService getService() {
			return BluetoothLeService.this;
		}
	}

	@Override
	public IBinder onBind(Intent intent) {
		return mBinder;
	}

	@Override
	public boolean onUnbind(Intent intent) {
		// After using a given device, you should make sure that
		// BluetoothGatt.close() is called
		// such that resources are cleaned up properly. In this particular
		// example, close() is
		// invoked when the UI is disconnected from the Service.
		try {
			close();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return super.onUnbind(intent);
	}

	private final IBinder mBinder = new LocalBinder();

	/**
	 * Initializes a reference to the local Bluetooth adapter.
	 *
	 * @return Return true if the initialization is successful.
	 */
	public boolean initialize() {
		// For API level 18 and above, get a reference to BluetoothAdapter
		// through
		// BluetoothManager.
		if (mBluetoothManager == null) {
			mBluetoothManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
			if (mBluetoothManager == null) {
				Log.e(TAG, "Unable to initialize BluetoothManager.");
				return false;
			}
		}

		mBluetoothAdapter = mBluetoothManager.getAdapter();
		if (mBluetoothAdapter == null) {
			Log.e(TAG, "Unable to obtain a BluetoothAdapter.");
			return false;
		}

		return true;
	}

	Thread connectionThread;
	int curConnIndex;

	public void connect(final List<BtDevice> devices) {
		curConnIndex = 0;
		if (connectionThread == null) {
			connectionThread = new Thread(new Runnable() {
				@Override
				public void run() {
					while (curConnIndex <= devices.size() - 1) {
						mBluetoothAdapter.getRemoteDevice(devices.get(curConnIndex).getAddress())
								.connectGatt(BluetoothLeService.this, false, mGattCallback);
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
					if (BleUtils.selectedMap.size() < BleUtils.selectDevices.size()) {
						for (BtDevice dev : BleUtils.selectDevices) {
							if (!BleUtils.selectedMap.containsKey(dev.getAddress())) {
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

	/**
	 * Connects to the GATT server hosted on the Bluetooth LE device.
	 *
	 * @param address
	 *            The device address of the destination device.
	 *
	 * @return Return true if the connection is initiated successfully. The
	 *         connection result is reported asynchronously through the
	 *         {@code BluetoothGattCallback#onConnectionStateChange(android.bluetooth.BluetoothGatt, int, int)}
	 *         callback.
	 */
	public boolean connect(final String address) {
		if (mBluetoothAdapter == null || address == null) {
			Log.w(TAG, "BluetoothAdapter not initialized or unspecified address.");
			return false;
		}

		BluetoothGatt gatt;
		if (BleUtils.gattMap.containsKey(address)) {
			gatt = BleUtils.gattMap.get(address);
			if (gatt != null) {
				Log.e(TAG, "try to use the  exist gatt");
				if (gatt.connect()) {
					return true;
				} else {
					gatt.disconnect();
					gatt.close();
					BleUtils.gattMap.remove(address);
				}
			}
		}

		final BluetoothDevice device = mBluetoothAdapter.getRemoteDevice(address);

		if (device == null) {
			Log.w(TAG, "Device not found.  Unable to connect.");
			return false;
		}
		// We want to directly connect to the device, so we are setting the
		// autoConnect
		// parameter to false.
		Log.e(TAG, "Trying to create a new connection.");
		gatt = device.connectGatt(this, false, mGattCallback);
		// BleUtils.gattMap.put(address, gatt);
		// mBluetoothDeviceAddress = address;
		mConnectionState = STATE_CONNECTING;
		return true;
	}

	byte[] WriteBytes = new byte[20];
	Thread writeThread = null;

	public void write(final String uuid, final String content) {
		if (BleUtils.selectedMap != null && BleUtils.selectedMap.size() > 0) {

			if (writeThread == null) {
				writeThread = new Thread(new Runnable() {
					@Override
					public void run() {
						Iterator<Map.Entry<String, BtDevice>> iterator = BleUtils.selectedMap.entrySet().iterator();
						LogUtils.e("map.size="+BleUtils.selectedMap.size());
						while (iterator.hasNext()) {
							Map.Entry<String, BtDevice> entry=iterator.next();
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
											setCharacteristicNotification(entry.getValue().getmBluetoothGatt(),character, true);
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
	 * Disconnects an existing connection or cancel a pending connection. The
	 * disconnection result is reported asynchronously through the
	 * {@code BluetoothGattCallback#onConnectionStateChange(android.bluetooth.BluetoothGatt, int, int)}
	 * callback.
	 */
	public void disconnect() {
		if (mBluetoothAdapter == null || mBluetoothGatt == null) {
			Log.e(TAG, "BluetoothAdapter not initialized");
			return;
		}
		Log.e(TAG, "disconnect");
		mBluetoothGatt.disconnect();
	}

	/**
	 * After using a given BLE device, the app must call this method to ensure
	 * resources are released properly.
	 */
	public void close() {
		Log.e(TAG, "close");
		if (mBluetoothGatt == null) {
			return;
		}
		mBluetoothGatt.close();
		mBluetoothGatt = null;
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
	public void readCharacteristic(BluetoothGattCharacteristic characteristic) {
		if (mBluetoothAdapter == null || mBluetoothGatt == null) {
			Log.w(TAG, "BluetoothAdapter not initialized");
			return;
		}
		mBluetoothGatt.readCharacteristic(characteristic);
	}

	public void writeCharacteristic(BluetoothGattCharacteristic characteristic) {
		if (mBluetoothAdapter == null || mBluetoothGatt == null) {
			Log.w(TAG, "BluetoothAdapter not initialized");
			return;
		}
		mBluetoothGatt.writeCharacteristic(characteristic);
	}

	/**
	 * Enables or disables notification on a give characteristic.
	 *
	 * @param characteristic
	 *            Characteristic to act on.
	 * @param enabled
	 *            If true, enable notification. False otherwise.
	 */
	public void setCharacteristicNotification(BluetoothGatt gatt,BluetoothGattCharacteristic characteristic, boolean enabled) {
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

	/**
	 * Retrieves a list of supported GATT services on the connected device. This
	 * should be invoked only after {@code BluetoothGatt#discoverServices()}
	 * completes successfully.
	 *
	 * @return A {@code List} of supported services.
	 */
	public List<BluetoothGattService> getSupportedGattServices() {
		if (mBluetoothGatt == null)
			return null;
		return mBluetoothGatt.getServices();
	}
}
