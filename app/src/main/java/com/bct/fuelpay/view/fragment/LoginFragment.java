package com.bct.fuelpay.view.fragment;

import static com.bct.fuelpay.utils.FragmentUtils.TRANSITION_NONE;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bct.fuelpay.R;
import com.bct.fuelpay.constant.AppConstant;
import com.bct.fuelpay.databinding.FragmentLoginBinding;
import com.bct.fuelpay.utils.FragmentUtils;
import com.bct.fuelpay.utils.SavePrefs;
import com.bct.fuelpay.view.activity.HomeActivity;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;


public class LoginFragment extends Fragment {

    FragmentLoginBinding binding;
    String ServerConnection = "";
    String NACKServerConnection = "NO";
    Receiver receiver;
    Sender messageSender;
    static boolean CanRead;
    private Socket client;
    private PrintWriter printwriter;
    DataInputStream din;
    String whichPump,nozzle,grade;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_login,container,false);
        binding.btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getActivity(), HomeActivity.class);
                startActivity(i);
                getActivity().finish();
            }
        });


        return binding.getRoot();
    }

    private void connectToFCC(){
        String strServerIp = SavePrefs.getString(AppConstant.prefName, getActivity(), AppConstant.ipPref, null);
        String strServerPort = SavePrefs.getString(AppConstant.prefName, getActivity(), AppConstant.portPref, null);
        try {
            if (strServerIp == null & strServerPort == null) {
                FragmentUtils.replaceFragment((AppCompatActivity) getActivity(),new FccSettingFragment(),
                        R.id.fragContainer,false, TRANSITION_NONE);
            }else {
                CanRead = true;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                    new ChatOperator().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                } else {
                    new ChatOperator().execute();
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("EXCEPTION","exception");
        }
    }

    private class ChatOperator extends AsyncTask<Void, Void, Void> {


        @SuppressWarnings("unused")
        ProgressDialog pDialog;

        public ChatOperator() {
        }

        @SuppressWarnings("unused")
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = ProgressDialog.show(getActivity(), "",
                    "Please wait...\nConnecting to Server", true);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @SuppressWarnings("unused")
        @Override
        protected Void doInBackground(Void... arg0) {
            try {
                ServerConnection = "";
                NACKServerConnection = "NO";
                String strServerIp = SavePrefs.getString(AppConstant.prefName, getActivity(), AppConstant.ipPref, null);
                String strServerPort = SavePrefs.getString(AppConstant.prefName, getActivity(), AppConstant.portPref, null);

                if (strServerIp == null) {
//                    getResources().getString(R.string.string_ip);//default ip address
                }
                if (strServerPort == null) {
//                    strServerPort = getResources().getString(R.string.string_port_request); //default port
                }
                client = new Socket(strServerIp, Integer.parseInt(strServerPort));

                // Creating the server socket.
                // client = new Socket(getResources().getString(R.string.string_ip), Integer.parseInt(getResources().getString(R.string.string_port_request))); // Creating the server socket.

                if (client != null) {
                    printwriter = new PrintWriter(client.getOutputStream(), true);
                    //InputStreamReader inputStreamReader = new InputStreamReader(client.getInputStream());
                    din = new DataInputStream(client.getInputStream());
                    //bufferedReader = new BufferedReader(inputStreamReader);
                } else {
                    //Log.e("Server has not bean started on port ", "" + Integer.parseInt(getResources().getString(R.string.string_port_request)));
                    Log.e("Server port ", "" + SavePrefs.getString(AppConstant.prefName,
                            getActivity(), AppConstant.portPref, null));

                }
            } catch (UnknownHostException e) {
                ServerConnection = "FAIL";
                // Log.e("Faild to connect server ", "" + getResources().getString(R.string.string_ip));
                Log.e("Faild to connect", "" + SavePrefs.getString(AppConstant.prefName, getActivity(),
                        AppConstant.ipPref, null));

                e.printStackTrace();
            } catch (IOException e) {
                ServerConnection = "FAIL";
                Log.e("Faild to connect", "" + SavePrefs.getString(AppConstant.prefName,
                        getActivity(), AppConstant.ipPref, null));

                e.printStackTrace();

            }
            return null;
        }

        /**
         * Following method is executed at the end of doInBackground method.
         */
        @SuppressWarnings("unused")
        @Override
        protected void onPostExecute(Void result) {
            pDialog.dismiss();
            if (ServerConnection.equals("FAIL")) {

                Log.e("Server Conn", "Fail");
                //  showServerConfigDialog();
            } else {
                if (CanRead) {
                    try {
                        messageSender = new Sender("LOGN"); // Initialize chat sender AsyncTask.
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                            messageSender.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                        } else {
                            messageSender.execute();
                        }


                        receiver = new Receiver(); // Initialize chat receiver AsyncTask.

                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
                            receiver.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                        else {
                            receiver.execute();
                        }

                    } catch (Exception e) {
                    }
                }
            }
        }

    }

    private class Sender extends AsyncTask<Void, Void, Void> {

        @SuppressWarnings("unused")
        private String message;
        @SuppressWarnings("unused")
        String stat;
        @SuppressWarnings("unused")
        ProgressDialog pDialog;


        @SuppressWarnings("unused")
        public Sender(String stat) {
            this.stat = stat;
        }

        @SuppressWarnings("unused")
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = ProgressDialog.show(getActivity(), "",
                    "Please wait...", true);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @SuppressWarnings("unused")
        @Override
        protected Void doInBackground(Void... params) {
            try {

                if (stat.equals("STAT|0") || stat.equals("STAT")) {
                    // message = "<STAT|" + 0 + ">";
                    message = "<STAT|" + whichPump + ">";

                } else if (stat.equals("LOGN")) {

                    String strServerUsename = SavePrefs.getString(AppConstant.prefName, getActivity(), AppConstant.userNamePref, null);

                    String strServerPassword = SavePrefs.getString(AppConstant.prefName, getActivity(), AppConstant.passwordPref, null);

                    message = "<LOGN|" + strServerUsename + "|" + strServerPassword + ">";
                    //  message = "<LOGN|admin|AdmiN@123!>";
                } else if (stat.equalsIgnoreCase("GRAD")) {
                    message = "<GRAD>";

                }
                printwriter.write(message);
                printwriter.flush();
            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }

        @SuppressWarnings("unused")
        @Override
        protected void onPostExecute(Void result) {
            Log.e("Client: ", "" + message);
            pDialog.dismiss();
        }
    }

    class Receiver extends AsyncTask<Void, Void, Void> {

        @SuppressWarnings("unused")
        private String result;
        @SuppressWarnings("unused")
        byte[] result1;
        @SuppressWarnings("unused")
        ProgressDialog pDialog;

        @SuppressWarnings("unused")
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = ProgressDialog.show(getActivity(), "",
                    "Please wait...", true);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @SuppressWarnings("unused")
        @Override
        protected Void doInBackground(Void... params) {

            while (CanRead) {
                try {
                    if (din.available() > 0) {
                        result1 = new byte[client.getReceiveBufferSize()];
                        din.read(result1);
                        result = new String(result1);

                        Log.e("SERVER", "" + result);

                        publishProgress(null);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            return null;
        }

        @SuppressWarnings("unused")
        @Override
        protected void onProgressUpdate(Void... values) {
//            Log.e("SERVER",""+result);
            if (result.contains("NACK")) {

                pDialog.dismiss();
                NACKServerConnection = "YES";
                Toast.makeText(getActivity(), result.replace("�", "").replace("<", "")
                        .replace(">", "").split("\\|")[2].toLowerCase().trim(), Toast.LENGTH_LONG).show();

                Log.e("ERROR", "" + result.replace("�", "").replace("<", "")
                        .replace(">", "").split("\\|")[2].toLowerCase());
            } else if (result.contains("ACK")) {
                messageSender = new Sender("GRAD"); // Initialize chat sender AsyncTask.
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                    messageSender.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                } else {
                    messageSender.execute();
                }
            } else if (result.contains("GRAD")){
                Log.e("re",""+result);
                pDialog.dismiss();

            }
        }
    }

}