/**
 * Este programa permite enviar información obtenida de arduino a una computadora mediante TCP.
 *
 * @author  Portugués Castellanos Mauricio.
 * @version 1.0
 * @since   2017-02-20
 */

package com.cm.reactstudios.climatemodule;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Set;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {

    private BluetoothAdapter        myBluetooth;
    private BluetoothSocket         btSocket;
    private boolean                 isBtConnected   =   false;
    private boolean                 socketTCP       =   false;
    private Button                  buttonCBluetooth, buttonDBluetooth, buttonCServer;
    private Button                  buttonDServer;
    private ConnectedThread         connectedThread;
    private Handler                 bluetoothIn;
    final int                       handlerState    =   0;
    private ObjectOutput            s;
    private Set<BluetoothDevice>    pairedDevices;
    private String[]                socketData;
    private static final String     BTADDRESS       =   "20:16:09:05:32:00";
    private StringBuilder           recDataString   =   new StringBuilder();
    private TCPThread               tcpThread;
    private TextView                textHumidity, textLight, textTemperature, textPressure;
    static final                    UUID myUUID     =   UUID.fromString("00001101-0000-1000-8000-" +
            "00805F9B34FB");

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar     =   (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        textHumidity        =   (TextView)findViewById(R.id.textHumidity);
        textLight           =   (TextView)findViewById(R.id.textLight);
        textTemperature     =   (TextView)findViewById(R.id.textTemperature);
        textPressure        =   (TextView)findViewById(R.id.textPressure);
        buttonCBluetooth    =   (Button)findViewById(R.id.buttonCBluetooth);
        buttonDBluetooth    =   (Button)findViewById(R.id.buttonDBluetooth);
        buttonDBluetooth.setVisibility(View.GONE);
        buttonDServer.setVisibility(View.GONE);
        bluetoothIn         =   new Handler() {

            public void handleMessage(android.os.Message msg) {
                Weather dato;

                Thread thread = new Thread(new Runnable() {

                    @Override
                    public void run() {
                        try {
                            Enviar.enviarGET();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });

                if (msg.what == handlerState) {
                    String readMessage = (String)msg.obj;
                    recDataString.append(readMessage);
                    int endOfLineIndex = recDataString.indexOf("~");
                    if  (endOfLineIndex > 0) {
                        String dataInPrint = recDataString.substring(0, endOfLineIndex);
                        textTemperature.setText(dataInPrint);
                        if  (recDataString.charAt(0) == '#') {
                            String sensor0 = recDataString.substring(1, 5);
                            String sensor1 = recDataString.substring(6, 9);
                            String sensor2 = recDataString.substring(10, 14);
                            String sensor3 = recDataString.substring(15, 21);
                            textHumidity.setText("Humedad relativa:\t"+sensor0+"%");
                            textLight.setText("Luminosidad:\t"+sensor1);
                            textTemperature.setText("Temperatura:\t"+sensor2+" °C");
                            textPressure.setText("Presión:\t"+sensor3+"hPa");
                            dato = new Weather(sensor0, sensor1, sensor2, sensor3);
                            Enviar.setDato(dato);
                            thread.start();
                        }
                        recDataString.delete(0, recDataString.length());
                    }
                }

            }
        };
        myBluetooth = BluetoothAdapter.getDefaultAdapter();
        buttonCBluetooth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if  (!myBluetooth.isEnabled()) {
                    Intent turnBTOn =   new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                    startActivityForResult(turnBTOn, 1);
                }
                pairedDevices       =   myBluetooth.getBondedDevices();
                if  (pairedDevices.size() > 0) {
                    for (BluetoothDevice bt : pairedDevices) {
                        if  (BTADDRESS.equals(bt.getAddress())) {
                            connectBT();
                        }
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "No se encontró el módulo BT.",
                            Toast.LENGTH_LONG).show();
                }

            }
        });
        buttonDBluetooth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                disconnectBT();
            }
        });
        buttonCServer.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){

                AlertDialog.Builder         alertDialog = new AlertDialog.Builder(MainActivity.this);
                LinearLayout.LayoutParams   lp          = new LinearLayout.LayoutParams(LinearLayout
                        .LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);

                alertDialog.setTitle("Conexión TCP");
                alertDialog.setMessage("Dirección ip y puerto del host:");
                final EditText input=new EditText(MainActivity.this);
                input.setLayoutParams(lp);
                alertDialog.setView(input);
                alertDialog.setIcon(R.drawable.secnet);
                alertDialog.setPositiveButton("Continuar", new DialogInterface.OnClickListener(){

                    public void onClick(DialogInterface dialog, int which){

                        socketData=input.getText().toString().split(":");
                        if(socketData.length!=0) {
                            tcpThread=new TCPThread(socketData[0], Integer.parseInt(socketData[1]
                                    .toString()));
                            tcpThread.start();
                        }

                    }

                });
                alertDialog.setNegativeButton("Cancelar", new DialogInterface.OnClickListener(){

                    public void onClick(DialogInterface dialog, int which) {

                        dialog.cancel();

                    }

                });
                alertDialog.setCancelable(false).show();

            }
        });
        buttonDServer.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                disconnectTCP();

            }

        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if  (item.getItemId() == R.id.action_settings) {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri
                    .parse("https://www.dropbox.com/home/Calidad%20y%20Pruebas%2017-i/RhinoSoft"));
            startActivity(browserIntent);
            return true;
        }
        return super.onOptionsItemSelected(item);

    }

    public void connectBT() {

        try {
            if  (btSocket == null && !isBtConnected) {
                myBluetooth                 =   BluetoothAdapter.getDefaultAdapter();
                BluetoothDevice dispositivo =   myBluetooth.getRemoteDevice(BTADDRESS);
                btSocket                    =   dispositivo
                        .createInsecureRfcommSocketToServiceRecord(myUUID);
                BluetoothAdapter.getDefaultAdapter().cancelDiscovery();
                btSocket.connect();
                connectedThread             =   new ConnectedThread(btSocket);
                connectedThread.start();
                buttonCBluetooth.setVisibility(View.GONE);
                buttonDBluetooth.setVisibility(View.VISIBLE);
                Toast.makeText(getApplicationContext(), "Conectado", Toast.LENGTH_LONG).show();
                isBtConnected               =   true;
            }
        } catch(IOException e) {
            Toast.makeText(getApplicationContext(), "Receptor no está disponible", Toast
                    .LENGTH_LONG).show();
        }

    }

    private class ConnectedThread extends Thread {

        private final InputStream entrada;

        public ConnectedThread(BluetoothSocket socket) {

            InputStream tmpIn   =   null;
            try {
                tmpIn           =   socket.getInputStream();
            }   catch(IOException e) {

            }
            entrada             =   tmpIn;

        }

        public void run() {

            byte[]  buffer  =   new byte[256];
            int     bytes;

            while(true) {
                try {
                    bytes               =   entrada.read(buffer);
                    String readMessage  =   new String(buffer, 0, bytes);
                    bluetoothIn.obtainMessage(handlerState, bytes, -1, readMessage).sendToTarget();
                    if(socketTCP) tcpThread.run();
                    SystemClock.sleep(2000);
                } catch(IOException e) {
                    break;
                }
            }

        }

    }

    private void disconnectBT() {

        try {
            btSocket.close();
            btSocket        =   null;
            isBtConnected   =   false;
            buttonCBluetooth.setVisibility(View.VISIBLE);
            buttonDBluetooth.setVisibility(View.GONE);
        } catch(IOException e) {
            Toast.makeText(getApplicationContext(), "No se pudo desconectar", Toast.LENGTH_LONG)
                    .show();
        }

    }

    private class TCPThread extends Thread {

        public TCPThread(String host, int port) {

            try {
                Socket sc       = new Socket(InetAddress.getByName(host), port);
                /*OutputStream ostream  = sc.getOutputStream();
                s                       = new ObjectOutputStream(ostream);*/
                buttonCServer.setVisibility(View.GONE);
                buttonDServer.setVisibility(View.VISIBLE);
                socketTCP               = true;
                Toast.makeText(getApplicationContext(), "Conectado", Toast.LENGTH_LONG).show();
            } catch(Exception e) {
                Toast.makeText(getApplicationContext(), "No se logró conectar TCP", Toast
                        .LENGTH_LONG).show();
            }

        }

        public void run() {

            while(socketTCP) {
                try {
                    /*s.writeObject(new Weather(textHumidity.toString(), textLight.toString(),
                            textTemperature.toString(), textPressure.toString()));
                    s.flush();*/
                    Toast.makeText(getApplicationContext(), "Se envió el objeto al socket TCP",
                            Toast.LENGTH_LONG).show();
                } catch(Exception e) {
                    Toast.makeText(getApplicationContext(), "Error al enviar datos a socket TCP",
                            Toast.LENGTH_LONG).show();
                }
                SystemClock.sleep(2000);
            }

        }

    }

    private void disconnectTCP() {

        socketTCP   =   false;
        tcpThread   =   null;
        buttonCServer.setVisibility(View.VISIBLE);
        buttonDServer.setVisibility(View.GONE);

    }

}