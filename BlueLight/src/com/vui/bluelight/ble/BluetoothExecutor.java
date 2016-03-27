package com.vui.bluelight.ble;

import java.util.Set;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Semaphore;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import android.util.Log;

public class BluetoothExecutor {

	private static final String TAG = "TcpExecutor";
	private static BluetoothExecutor instance = null;
	private ThreadPoolExecutor executor;
	private Semaphore semp;

	private BluetoothManager manager;

	private BluetoothExecutor() {
		executor = new ThreadPoolExecutor(1, 7, 500, TimeUnit.MILLISECONDS,
				new ArrayBlockingQueue<Runnable>(20));
		semp = new Semaphore(1);
		manager = BluetoothManager.getInstance();
	}

	public static BluetoothExecutor getInstans() {
		if (instance == null) {
			synchronized (BluetoothExecutor.class) {
				if (instance == null)
					instance = new BluetoothExecutor();
			}
		}
		return instance;
	}

	public void execute(final byte[] chars) {
		Log.d(TAG, "execute start" + executor.getActiveCount());
		try {
			semp.acquire();

			final Set<String> set = BleUtils.gattMap.keySet();
			Log.d(TAG, "Set<String>" + set);
			for (int i = 0; i < BleUtils.gattMap.size(); i++) {

				executor.execute(new Runnable() {
					@Override
					public void run() {
						manager.writeData(
								BleUtils.gattMap.get(set.iterator().next()),
								chars);
					}
				});
			}

			while (executor.getActiveCount() != 0)
				;
			semp.release();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}