package com.example.lukas.tabbedapp;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothSocket;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.SystemClock;
import android.support.v4.app.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;

import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class tab1_layout extends Fragment{

    public int green =0;
    public int red=0;
    public int blue=0;

    public SeekBar greenSeek;
    public SeekBar redSeek;
    public SeekBar blueSeek;

    private static BluetoothSocket bluetoothsocket;
    private static OutputStream outputstream;
    private static InputStream inputstream;
    private  static BluetoothAdapter bluetoothadapter;




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.tab1_layout, container, false);

        final TextView  currentColor=  rootView.findViewById(R.id.currentColor);
        final TextView hexcolor= (TextView) rootView.findViewById(R.id.hexcolor);
        final TextView deccolor = (TextView) rootView.findViewById(R.id.deccolor);

        final TextView redpercent = (TextView) rootView.findViewById(R.id.redpercent);
        final TextView redvalue = (TextView) rootView.findViewById(R.id.redvalue);

        final TextView greenpercent = (TextView) rootView.findViewById(R.id.greenpercent);
        final TextView greenvalue = (TextView) rootView.findViewById(R.id.greenvalue);

        final TextView bluepercent = (TextView) rootView.findViewById(R.id.bluepercent);
        final TextView bluevalue = (TextView) rootView.findViewById(R.id.bluevalue);

        getBtData();

        greenSeek = (SeekBar) rootView.findViewById(R.id.seekBar_green);
        blueSeek = (SeekBar) rootView.findViewById(R.id.seekBar_blue);
        redSeek = (SeekBar) rootView.findViewById(R.id.seekBar_red);

        redvalue.setText(String.valueOf(redSeek.getProgress()));
        greenvalue.setText(String.valueOf(greenSeek.getProgress()));
        bluevalue.setText(String.valueOf(blueSeek.getProgress()));

        //currentColor.setBackgroundColor(Color.rgb(0,0,0));
        hexcolor.setText("DEC: "+Integer.toHexString(red).toUpperCase()+"/"+Integer.toHexString(green).toUpperCase()+"/"+Integer.toHexString(blue).toUpperCase());
        deccolor.setText("HEX: "+String.valueOf(red)+"/"+String.valueOf(green)+"/"+String.valueOf(blue));

        redpercent.setText("0%");
        greenpercent.setText("0%");
        bluepercent.setText("0%");

        //Seekbar listeners
        greenSeek.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int value, boolean b)
            {
                green=value;
                float greenfloat = (float) value;

                currentColor.setBackgroundColor(Color.rgb(red,green,blue));
                greenvalue.setText(String.valueOf(green));
                greenpercent.setText(String.valueOf(Math.round((greenfloat)/greenSeek.getMax()*100))+"%");

                hexcolor.setText("HEX: "+Integer.toHexString(red).toUpperCase()+"/"+Integer.toHexString(green).toUpperCase()+"/"+Integer.toHexString(blue).toUpperCase());
                deccolor.setText("DEC: "+String.valueOf(red)+"/"+String.valueOf(green)+"/"+String.valueOf(blue));
                getBtData(); //load current bluetooth connection data
                bluetoothWrite(Integer.toHexString(red)+"/"+Integer.toHexString(green)+"/"+Integer.toHexString(blue)+",");

                if((red+green+blue)<=200)
                {
                    hexcolor.setTextColor(Color.WHITE);
                    deccolor.setTextColor(Color.WHITE);
                }
                else
                {
                    hexcolor.setTextColor(Color.BLACK);
                    deccolor.setTextColor(Color.BLACK);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        blueSeek.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int value, boolean b) {
                blue=value;
                float bluefloat=(float) value;
                currentColor.setBackgroundColor(Color.rgb(red,green,blue));
                bluevalue.setText(String.valueOf(blueSeek.getProgress()));
                bluepercent.setText(String.valueOf(Math.round((bluefloat)/blueSeek.getMax()*100))+"%");

                hexcolor.setText("HEX: "+Integer.toHexString(red).toUpperCase()+"/"+Integer.toHexString(green).toUpperCase()+"/"+Integer.toHexString(blue).toUpperCase());
                deccolor.setText("DEC: "+String.valueOf(red)+"/"+String.valueOf(green)+"/"+String.valueOf(blue));
                getBtData();
                bluetoothWrite(Integer.toHexString(red)+"/"+Integer.toHexString(green)+"/"+Integer.toHexString(blue)+",");

                if((red+green+blue)<=200)
                {
                    hexcolor.setTextColor(Color.WHITE);
                    deccolor.setTextColor(Color.WHITE);
                }
                else
                {
                    hexcolor.setTextColor(Color.BLACK);
                    deccolor.setTextColor(Color.BLACK);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        redSeek.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int value, boolean b) {
                red=value;
                float redfloat=(float) value;
                currentColor.setBackgroundColor(Color.rgb(red,green,blue));
                redpercent.setText(String.valueOf(Math.round(redfloat/ redSeek.getMax()*100))+"%");
                redvalue.setText(String.valueOf(red));

                hexcolor.setText("HEX: "+Integer.toHexString(red).toUpperCase()+"/"+Integer.toHexString(green).toUpperCase()+"/"+Integer.toHexString(blue).toUpperCase());
                deccolor.setText("DEC: "+String.valueOf(red)+"/"+String.valueOf(green)+"/"+String.valueOf(blue));
                getBtData();
                bluetoothWrite(Integer.toHexString(red)+"/"+Integer.toHexString(green)+"/"+Integer.toHexString(blue)+",");

                if((red+green+blue)<=200)
                {
                    hexcolor.setTextColor(Color.WHITE);
                    deccolor.setTextColor(Color.WHITE);
                }
                else
                {
                    hexcolor.setTextColor(Color.BLACK);
                    deccolor.setTextColor(Color.BLACK);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        return rootView;


    }

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
                SystemClock.sleep(31);
            }
            catch (IOException e){
                //message("Error");
            }
        }
    }

    public void message(String message){
        Toast.makeText(getContext(),message,Toast.LENGTH_SHORT).show();
    }

}