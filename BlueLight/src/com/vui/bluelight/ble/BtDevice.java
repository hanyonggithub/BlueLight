package com.vui.bluelight.ble;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattService;

public class BtDevice {

	private String address;
	private String name;
	private BluetoothGatt mBluetoothGatt;
	private int connectedState;
	private BluetoothGattService mGattService;

	public int getConnectedState() {
		return connectedState;
	}

	public void setConnectedState(int connectedState) {
		this.connectedState = connectedState;
	}

	public BluetoothGattService getmGattService() {
		return mGattService;
	}

	public void setmGattService(BluetoothGattService mGattService) {
		this.mGattService = mGattService;
	}

	public BtDevice(String address, String name) {
		this.address=address;
		this.name=name;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public BluetoothGatt getmBluetoothGatt() {
		return mBluetoothGatt;
	}

	public void setmBluetoothGatt(BluetoothGatt mBluetoothGatt) {
		this.mBluetoothGatt = mBluetoothGatt;
	}

	@Override
	public boolean equals(Object o) {
		if (o == null) {
			return false;
		}
		if (o == this) {
			return true;
		}
		if (getClass() != o.getClass()) {
			return false;
		}
		BtDevice e = (BtDevice) o;
		return (this.address.equals(e.address));
	}

	@Override
	public int hashCode() {
		final int PRIME = 31;
		int result = 1;
		result = PRIME * result + address.hashCode();
		return result;
	}

}
