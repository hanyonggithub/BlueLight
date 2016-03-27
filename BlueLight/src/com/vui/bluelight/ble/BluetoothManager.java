package com.vui.bluelight.ble;

import java.util.UUID;

import com.vui.bluelight.MainActivity;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothProfile;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

public class BluetoothManager {

	protected static final String TAG = "BluetoothManager";
	private Context context;
	public boolean isScanning;
	private int index;

	private Handler handler = new Handler();
	// 10秒后停止查找搜索.
	private static final long SCAN_PERIOD = 10000;

	private static BluetoothAdapter btaAdapter = BluetoothAdapter
			.getDefaultAdapter();
	// public static BluetoothGatt mBluetoothGatt;
	private int disconStatus;
	public final static String ACTION_GATT_CONNECTED = "com.example.bluetooth.le.ACTION_GATT_CONNECTED";
	public final static UUID UUID_HEART_RATE_MEASUREMENT = UUID
			.fromString(SampleGattAttributes.HEART_RATE_MEASUREMENT);
	private static BluetoothManager instance;

	private BluetoothManager() {

	}

	public static BluetoothManager getInstance() {

		if (instance == null) {
			synchronized (BluetoothManager.class) {
				if (instance == null) {
					instance = new BluetoothManager();
				}
			}
		}
		return instance;
	}

	public void setContext(Context mContext) {
		context = mContext;
	}

	private final BluetoothGattCallback mGattCallback = new BluetoothGattCallback() {
		@Override
		public void onConnectionStateChange(BluetoothGatt gatt, int status,
				int newState) {
			if (newState == BluetoothProfile.STATE_CONNECTED) {

				Log.d(TAG, "Connected to GATT server.");
				// Attempts to discover services after successful connection.
				Log.d(TAG,
						"Attempting to start service discovery:"
								+ gatt.discoverServices());

			} else if (newState == BluetoothProfile.STATE_DISCONNECTED) {
				Log.d(TAG, "Disconnected from GATT server.");
				if (disconStatus == 129) {
					close(gatt);
					Log.d(TAG, "----129----");
					btaAdapter.disable();
					try {
						Thread.sleep(2500);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					btaAdapter.enable();
				}
			}
		}

		@Override
		public void onServicesDiscovered(BluetoothGatt gatt, int status) {
			if (status == BluetoothGatt.GATT_SUCCESS) {
				if (gatt == null)
					return;
				// writeData(gatt, new byte[] { 0x11, 0x12 });
				// displayGattServices(gatt.getServices());
				Log.d(TAG, "BluetoothGatt.GATT_SUCCESS");
			} else {
				Log.d(TAG, "onServicesDiscovered received: " + status);
				disconStatus = status;
			}
		}

		@Override
		public void onCharacteristicRead(BluetoothGatt gatt,
				BluetoothGattCharacteristic characteristic, int status) {
			if (status == BluetoothGatt.GATT_SUCCESS) {
				// broadcastUpdate(ACTION_DATA_AVAILABLE, characteristic);
				Log.d(TAG, "onCharacteristicRead");
				// Toast.makeText(BluetoothLeService.this,
				// "onCharacteristicRead successfully", 100).show();
			}
		}

		@Override
		public void onCharacteristicChanged(BluetoothGatt gatt,
				BluetoothGattCharacteristic characteristic) {
			// broadcastUpdate(ACTION_DATA_AVAILABLE, characteristic);
			Log.d(TAG, "onCharacteristicChanged");
		}
	};

	public void writeData(BluetoothGatt gatt, byte[] datas) {
		if (/* btaAdapter == null || */gatt == null) {
			Log.w(TAG, "BluetoothGatt is null  can not writeData.");
			return;
		}
		BluetoothGattService gattService = gatt.getService(UUID
				.fromString(SampleGattAttributes.HEART_RATE_MEASUREMENT));
		if (gattService == null) {
			Log.w(TAG,
					"the gattService for BluetoothGattService is null,can not write data");
			return;
		}
		BluetoothGattCharacteristic characteristic = gattService
				.getCharacteristic(UUID
						.fromString(SampleGattAttributes.CLIENT_CHARACTERISTIC_CONFIG));
		if (characteristic == null) {
			Log.w(TAG,
					"the uuid for BluetoothGattCharacteristic is null,can not write data");
			return;
		}
		characteristic.setValue(datas);
		// gatt.writeCharacteristic(characteristic);
		Log.i(TAG,
				"writeCharacteristic"
						+ gatt.writeCharacteristic(characteristic));
	}

	/**
	 * Enables or disables notification on a give characteristic.
	 * 
	 * @param characteristic
	 *            Characteristic to act on.
	 * @param enabled
	 *            If true, enable notification. False otherwise.
	 */
	public void setCharacteristicNotification(
			BluetoothGattCharacteristic characteristic, boolean enabled) {
		BluetoothGatt gatt = BleUtils.mList.get(index).getmBluetoothGatt();
		if (btaAdapter == null || gatt == null) {
			Log.w(TAG, "BluetoothAdapter not initialized");
			return;
		}
		gatt.setCharacteristicNotification(characteristic, enabled);

		// This is specific to Heart Rate Measurement.
		if (UUID_HEART_RATE_MEASUREMENT.equals(characteristic.getUuid())) {
			BluetoothGattDescriptor descriptor = characteristic
					.getDescriptor(UUID
							.fromString(SampleGattAttributes.CLIENT_CHARACTERISTIC_CONFIG));
			descriptor
					.setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);
			gatt.writeDescriptor(descriptor);
		}
	}

	private boolean isPrepared;

	public void scanLeDevice(final boolean enable,
			final BluetoothAdapter.LeScanCallback mLeScanCallback) {
		if (!isPrepared) {
			isPrepared = true;
			Looper.prepare();
		}
		if (enable) {
			// Stops scanning after a pre-defined scan period.
			handler.postDelayed(new Runnable() {
				@Override
				public void run() {
					isScanning = false;
					btaAdapter.stopLeScan(mLeScanCallback);
					Log.d(TAG, "STOP....");
				}
			}, SCAN_PERIOD);
			isScanning = true;
			btaAdapter.startLeScan(mLeScanCallback);
		} else {
			isScanning = false;
			btaAdapter.stopLeScan(mLeScanCallback);
		}
	}

	/**
	 * After using a given BLE device, the app must call this method to ensure
	 * resources are released properly.
	 */
	public void close(BluetoothGatt gatt) {
		if (gatt == null) {
			return;
		}
		gatt.close();
		gatt = null;
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
	public boolean connect(final String address, int index) {
		this.index = index;
		if (address == null) {
			Log.w(TAG, " unspecified address.");
			return false;
		}

		final BluetoothDevice device = btaAdapter.getRemoteDevice(address);
		if (device == null) {
			Log.w(TAG, "Device not found.  Unable to connect.");
			return false;
		}
		// We want to directly connect to the device, so we are setting the
		// autoConnect
		// parameter to false.

		// MainActivity.mList.get(index).setmBluetoothGatt(gatt);
		if (!BleUtils.gattMap.containsKey(BleUtils.mList.get(index)
				.getAddress())) {
			BluetoothGatt gatt = device.connectGatt(context, true,
					mGattCallback);
			BleUtils.gattMap.put(BleUtils.mList.get(index).getAddress(),
					gatt);
			Log.d(TAG, "Trying to create a new connection."
					+ BleUtils.gattMap.size());
		} else{
			Log.d(TAG, "allready connected." + BleUtils.gattMap.size());
		}
		
		return true;
	}

	/**
	 * Disconnects an existing connection or cancel a pending connection. The
	 * disconnection result is reported asynchronously through the
	 * {@code BluetoothGattCallback#onConnectionStateChange(android.bluetooth.BluetoothGatt, int, int)}
	 * callback.
	 */
	public void disconnect(int index) {
		if (btaAdapter == null) {
			Log.w(TAG, "BluetoothAdapter not initialized");
			return;
		}
		BleUtils.mList.get(index).getmBluetoothGatt().disconnect();
	}
	
	
	
	public interface gattStateCallback{
		public void onConnected();
		public void onDisconnected();
	}
}
