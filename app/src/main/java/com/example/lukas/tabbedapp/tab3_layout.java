package com.example.lukas.tabbedapp;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Set;

import java.util.UUID;


public class tab3_layout extends tab1_layout {

    public BluetoothAdapter BA;
    public Set<BluetoothDevice> pairedDevices;

    //Bluetoothconnection
    BluetoothConnection btConnection;
    BluetoothSocket bluetoothSocket;
    OutputStream bluetoothOutputStream;
    InputStream bluetoothInputStream;

    BroadcastReceiver mReceiver;

    ArrayList list;

    boolean btsocket_manual_close = false;

    final UUID MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    boolean isBluetoothConnected = false;

    ListView lv;
    String address;
    public TextView connectionstate;

    Button b1,b3,b4;
    Button disconnect;

    public View rootView;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.tab3_layout, container, false);

        BA = BluetoothAdapter.getDefaultAdapter(); //if BA is null, there is no bluetoothadapter available
        lv = (ListView)rootView.findViewById(R.id.listView);
        btConnection = new BluetoothConnection();

        b1 = (Button) rootView.findViewById(R.id.on);
        b3=(Button)rootView.findViewById(R.id.list);
        b4=(Button)rootView.findViewById(R.id.off);
        disconnect = (Button)rootView.findViewById(R.id.disconnect);

        connectionstate = (TextView) rootView.findViewById(R.id.connectionstate);

        //mom
        connectionstate.setText("");
        //mom





        if (BA == null) {
            message("No Bluetooth Adapter");
        }

        //on click listener for the on button
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!BA.isEnabled()) {
                    Intent turnOn = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                    startActivityForResult(turnOn, 0);
                    message("Turned on");
                } else {
                    message("Already on");
                }
            }
        });

        //on click listener for the list button
        b3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                pairedDevices = BA.getBondedDevices();

                list = new ArrayList();
                ArrayAdapter adapter;



                list.add("paired devices");

                if (pairedDevices.size()>0) {
                    for(BluetoothDevice bt : pairedDevices) { //pipe the bluetoothdevices in the listview
                        list.add(bt.getName() + "\n" + bt.getAddress());
                    }
                }
                else {
                    Toast.makeText(getContext(), "No Paired Bluetooth Devices Found.", Toast.LENGTH_LONG).show();
                }
                Toast.makeText(getContext(), "Showing Paired Devices", Toast.LENGTH_SHORT).show();displayListOfFoundDevices();

                list.add("search for devices");

                displayListOfFoundDevices();

                adapter = new ArrayAdapter(getContext(), android.R.layout.simple_list_item_1, list);

                lv.setAdapter(adapter);
                lv.setOnItemClickListener(onItemClickListener); //set on item click listener

            }
        });

        //off button click listener
        b4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BA.disable();
                message("Turned Off");



            }
        });

        disconnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(BA.isEnabled()) {

                    if(!btsocket_manual_close) {
                        try {
                            bluetoothOutputStream.close();
                            bluetoothInputStream.close();
                            bluetoothSocket.close();
                            btsocket_manual_close = true;
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    if (bluetoothSocket != null) {
                        bluetoothSocket = null;
                    }
                    if (bluetoothInputStream != null) {
                        bluetoothInputStream = null;
                    }

                    if (bluetoothOutputStream != null) {

                        bluetoothOutputStream = null;
                    }

                }
                message("Disconnected");
            }
        });

        return rootView;
    }

    private void displayListOfFoundDevices()
    {

        // start looking for bluetooth devices
        BA.startDiscovery();

        // Discover new devices
        // Create a BroadcastReceiver for ACTION_FOUND
        mReceiver = new BroadcastReceiver()
        {
            @Override
            public void onReceive(Context context, Intent intent)
            {
                String action = intent.getAction();
                // When discovery finds a device
                if (BluetoothDevice.ACTION_FOUND.equals(action))
                {
                    BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                    // Add the name and address to an array adapter to show in a ListView
                    list.add(device.getName() + "\n" + device.getAddress());
                }
            }
        };
        // Register the BroadcastReceiver
        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        getContext().registerReceiver(mReceiver, filter);

    }

    //function for pop-up message
    public void message(String message){
        Toast.makeText(getContext(),message,Toast.LENGTH_SHORT).show();
    }


    //list view on item click listener
    public AdapterView.OnItemClickListener onItemClickListener = new AdapterView.OnItemClickListener(){
        public void onItemClick (AdapterView<?> av, View v, int arg2, long arg3){
            String info = ((TextView) v).getText().toString();
            address = info.substring(info.length() - 17);

            btConnection = new BluetoothConnection();
            btConnection.execute();
        }
    };

    //BluetoothConnection Class
    public class BluetoothConnection extends AsyncTask<Void, Void, Void> {
        private boolean ConnectSuccess = true;
        ProgressDialog progress;
        BluetoothDevice bluetoothDevice;

        @Override
        protected void onPreExecute() {
            progress = ProgressDialog.show(getContext(), "Connecting...", "Please wait!!!");
        }

        @Override
        protected Void doInBackground(Void... devices) {
            try {
                if (bluetoothSocket == null || !bluetoothSocket.isConnected()) {
                    bluetoothDevice = BA.getRemoteDevice(address);
                    bluetoothSocket = bluetoothDevice.createInsecureRfcommSocketToServiceRecord(MY_UUID);
                    bluetoothOutputStream = bluetoothSocket.getOutputStream();
                    bluetoothInputStream = bluetoothSocket.getInputStream();
                    BA.cancelDiscovery();
                    bluetoothSocket.connect();
                    btsocket_manual_close = false;
                }
            } catch (IOException e) {
                ConnectSuccess = false;
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);

            if (!ConnectSuccess) {
                message("Connection Failed. Is it a SPP Bluetooth? Try again.");

                new AlertDialog.Builder(rootView.getContext())
                        .setTitle("Connection failed")
                        .setMessage("Do you want to retry?")
                        .setNegativeButton("cancel", null)
                        .setPositiveButton("Retry", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                                try {
                                    bluetoothSocket.close();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }

                                if(bluetoothSocket != null)
                                {
                                    bluetoothSocket = null;
                                }

                                if(bluetoothInputStream != null)
                                {
                                    bluetoothInputStream =null;
                                }

                                if(bluetoothOutputStream != null)
                                {
                                    bluetoothOutputStream =null;
                                }

                                btConnection = new BluetoothConnection();
                                btConnection.execute();
                            }
                        }).create().show();

            } else {
                message("Connected.");
                isBluetoothConnected = true;
                DataStorage.saveBt(bluetoothSocket,bluetoothOutputStream,bluetoothInputStream,BA);
            }
            progress.dismiss();
        }
    }

}