package com.example.lukas.tabbedapp;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothSocket;

import java.io.InputStream;
import java.io.OutputStream;

public class DataStorage {

    private static BluetoothSocket storebluetoothsocket;
    private static OutputStream storeoutputstream;
    private static InputStream storeinputstream;
    private  static BluetoothAdapter storebluetoothadapter;

    public static void saveBt(BluetoothSocket btsckt, OutputStream out, InputStream in, BluetoothAdapter ba)
    {
        storebluetoothsocket = btsckt;
        storeoutputstream = out;
        storeinputstream = in;
        storebluetoothadapter = ba;
    }

    public static BluetoothSocket getBluetoothSocket()
    {
        return storebluetoothsocket;
    }
    public static OutputStream getOutputStream()
    {
        return storeoutputstream;
    }
    public static InputStream getInputStream()
    {
        return storeinputstream;
    }
    public static BluetoothAdapter getBluetoothAdapter()
    {
        return storebluetoothadapter;
    }
}
