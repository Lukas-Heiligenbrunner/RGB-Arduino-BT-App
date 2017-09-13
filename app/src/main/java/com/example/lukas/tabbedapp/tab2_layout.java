package com.example.lukas.tabbedapp;


import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothSocket;
import android.graphics.Color;
import android.os.SystemClock;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.Button;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class tab2_layout extends Fragment {

    Button red,blue,green;
    Button yellow, purple, cyan;
    Button orange, pink, grey;
    Button white,black;

    private static BluetoothSocket bluetoothsocket;
    private static OutputStream outputstream;
    private static InputStream inputstream;
    private  static BluetoothAdapter bluetoothadapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.tab2_layout, container, false);

        red = (Button) rootView.findViewById(R.id.btn_red);
        blue = (Button) rootView.findViewById(R.id.btn_blue);
        green = (Button) rootView.findViewById(R.id.btn_green);

        yellow = (Button) rootView.findViewById(R.id.btn_yellow);
        purple = (Button) rootView.findViewById(R.id.btn_purple);
        cyan = (Button) rootView.findViewById(R.id.btn_cyan);

        orange = (Button) rootView.findViewById(R.id.btn_orange);
        pink = (Button) rootView.findViewById(R.id.btn_pink);
        grey = (Button) rootView.findViewById(R.id.btn_grey);

        white = (Button) rootView.findViewById(R.id.btn_white);
        black = (Button) rootView.findViewById(R.id.btn_black);

        red.setOnClickListener(MyOnClickListener);
        blue.setOnClickListener(MyOnClickListener);
        green.setOnClickListener(MyOnClickListener);

        yellow.setOnClickListener(MyOnClickListener);
        purple.setOnClickListener(MyOnClickListener);
        cyan.setOnClickListener(MyOnClickListener);

        orange.setOnClickListener(MyOnClickListener);
        pink.setOnClickListener(MyOnClickListener);
        grey.setOnClickListener(MyOnClickListener);

        white.setOnClickListener(MyOnClickListener);
        black.setOnClickListener(MyOnClickListener);

        return rootView;
    }

    public View.OnClickListener MyOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            getBtData();

            switch (v.getId()) {
                case R.id.btn_red:

                    bluetoothWrite("255/0/0");
                    message("set color to red");

                    break;
                case R.id.btn_blue:

                    bluetoothWrite("0/0/255");
                    message("set color to blue");

                    break;
                case R.id.btn_green:

                    bluetoothWrite("0/255/0");
                    message("set color to greem");

                    break;
                case R.id.btn_yellow:

                    bluetoothWrite("255/255/0");
                    message("set color to yellow");

                    break;
                case R.id.btn_purple:

                    bluetoothWrite("255/0/255");
                    message("set color to purple");

                    break;
                case R.id.btn_cyan:

                    bluetoothWrite("0/255/255");
                    message("set color to cyan");

                    break;
                case R.id.btn_orange:

                    bluetoothWrite("255/127/0");
                    message("set color to orange");

                    break;
                case R.id.btn_pink:

                    bluetoothWrite("255/0/127");
                    message("set color to pink");

                    break;
                case R.id.btn_grey:

                    bluetoothWrite("127/127/127");
                    message("set color to grey");

                    break;
                case R.id.btn_white:

                    bluetoothWrite("255/255/255");
                    message("set color to white");

                    break;
                case R.id.btn_black:

                    bluetoothWrite("0/0/0");
                    message("turned off");

                    break;
                default:
                    break;
            }
        }
    };

    public void getBtData()
    {
        bluetoothadapter = DataStorage.getBluetoothAdapter();
        bluetoothsocket = DataStorage.getBluetoothSocket();
        inputstream = DataStorage.getInputStream();
        outputstream = DataStorage.getOutputStream();
    }

    public void bluetoothWrite(String data) {
        if (bluetoothsocket !=null){
            try {
                outputstream.write(data.getBytes());
                SystemClock.sleep(30);
            }
            catch (IOException e){
                message("Error");
            }
        }
    }

    public void message(String message){
        Toast.makeText(getContext(),message,Toast.LENGTH_SHORT).show();
    }

}