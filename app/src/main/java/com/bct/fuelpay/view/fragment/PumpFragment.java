package com.bct.fuelpay.view.fragment;

import static com.bct.fuelpay.utils.FragmentUtils.TRANSITION_NONE;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bct.fuelpay.R;
import com.bct.fuelpay.adapter.AuthValueAdapter;
import com.bct.fuelpay.adapter.PumpAdapter;
import com.bct.fuelpay.adapter.PumpAuthAdapter;
import com.bct.fuelpay.constant.AppConstant;
import com.bct.fuelpay.databinding.FragmentPumpBinding;
import com.bct.fuelpay.utils.FragmentUtils;
import com.bct.fuelpay.utils.SavePrefs;
import com.bct.fuelpay.view_model.AuthValueViewModel;
import com.bct.fuelpay.view_model.PumpViewModel;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.snackbar.Snackbar;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;


public class PumpFragment extends Fragment implements PumpAdapter.ItemListener {

    FragmentPumpBinding binding;
    PumpAdapter pumpAdapter;
    PumpViewModel pumpViewModel;
    Bundle b;
    String whichMenu;

    String ServerConnection = "";
    String NACKServerConnection = "NO";
    Receiver receiver;
    Sender messageSender;
    static boolean CanRead;
    private Socket client;
    private PrintWriter printwriter;
    DataInputStream din;
    String whichPump;
    boolean isPauth = false;
    static String pump_no = "", pump_status = "", pump_active_nozzle = "", pump_amount = "", pump_price = "",
            pump_volume = "", pump_nozzle_number = "", pump_nozzle_state = "", retailer_code = "", retailer_name = "", prod_name = "",
            discount = "", cust_mob = "";
    String preAuthAmount, preAuthID, toMobileNumber, preAuthDateTime, preAuthTerminalId, attendant_Name;
    String strServerIp, strServerPort, strServerUsername, strPassword;
    String authType;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_pump, container, false);

        binding.title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });
        pumpViewModel = ViewModelProviders.of(this).get(PumpViewModel.class);
        b = new Bundle();
        b = getArguments();
        whichMenu = b.getString(AppConstant.whichMenu);
        setupGridView();

        return binding.getRoot();
    }

    private void setupGridView() {
        ArrayList items = new ArrayList<>();
        items.add(new PumpViewModel(getActivity(), "Pump 1", getActivity().getResources().
                getColor(R.color.status_green)));
        items.add(new PumpViewModel(getActivity(), "Pump 2", getActivity().getResources().
                getColor(R.color.status_yellow)));
        items.add(new PumpViewModel(getActivity(), "Pump 3", getActivity().getResources().
                getColor(R.color.status_green)));
        items.add(new PumpViewModel(getActivity(), "Pump 4", getActivity().getResources().
                getColor(R.color.status_green)));
        items.add(new PumpViewModel(getActivity(), "Pump 5", getActivity().getResources().
                getColor(R.color.status_light_grey)));
        items.add(new PumpViewModel(getActivity(), "Pump 6", getActivity().getResources().
                getColor(R.color.status_green)));
        items.add(new PumpViewModel(getActivity(), "Pump 7", getActivity().getResources().
                getColor(R.color.status_red)));

        pumpAdapter = new PumpAdapter(getActivity(), items);
        binding.recylerview.setLayoutManager(new GridLayoutManager(getActivity(), 3));
        binding.recylerview.setAdapter(pumpAdapter);
        pumpAdapter.setClickListener(this);

    }

    @Override
    public void onPumpClick(View v, int position) {
        if (whichMenu.equalsIgnoreCase(AppConstant.fuelSale)) {
            b.putString("Pump", String.valueOf(position + 1));
            b.putString("Nozzle", "1");
            b.putString("Grade", "PETROL");
            FragmentUtils.replaceFragment((AppCompatActivity) getActivity(), new FccTransactionFragment(), b,
                    R.id.fragContainer, true, FragmentUtils.TRANSITION_NONE);
        } else if (whichMenu.equalsIgnoreCase(AppConstant.pumpAuthorize)) {
            showAuthorizeButton();
        }
    }

    private void showAuthorizeButton() {

        final BottomSheetDialog bottomSheetDialog1 = new BottomSheetDialog(getActivity());
        bottomSheetDialog1.setContentView(R.layout.authorize_bottomsheet);

        TextView btnAuthorize = bottomSheetDialog1.findViewById(R.id.btnAuthorize);
        btnAuthorize.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bottomSheetDialog1.cancel();
                showAuthDetailFormBottomSheet();
            }
        });

        bottomSheetDialog1.show();
    }

    private void showAuthDetailFormBottomSheet() {

        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(getActivity());
        bottomSheetDialog.setContentView(R.layout.authorize_type_bottomsheet);
        AuthValueAdapter authValueAdapter;
        ArrayList<AuthValueViewModel> arrayList;
        RecyclerView recyclerView = bottomSheetDialog.findViewById(R.id.recylerview);
        arrayList = new ArrayList<>();

        arrayList.add(new AuthValueViewModel("50"));
        arrayList.add(new AuthValueViewModel("100"));
        arrayList.add(new AuthValueViewModel("200"));
        arrayList.add(new AuthValueViewModel("300"));
        arrayList.add(new AuthValueViewModel("500"));

        authValueAdapter = new AuthValueAdapter(getActivity(), arrayList);

        LinearLayoutManager HorizontalLayout
                = new LinearLayoutManager(
                getActivity(),
                LinearLayoutManager.HORIZONTAL,
                false);
        recyclerView.setLayoutManager(HorizontalLayout);
        recyclerView.setAdapter(authValueAdapter);

        EditText etValue = bottomSheetDialog.findViewById(R.id.etValue);
        TextView btnPrice = bottomSheetDialog.findViewById(R.id.btnPrice);
        TextView btnQuantity = bottomSheetDialog.findViewById(R.id.btnQuantity);
        TextView btnTopUp = bottomSheetDialog.findViewById(R.id.btnTopUp);
        btnPrice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                authType = "Amount";
                btnQuantity.setBackgroundResource(R.drawable.grey_rounded_corner);
                btnTopUp.setBackgroundResource(R.drawable.grey_rounded_corner);
                btnPrice.setBackgroundResource(R.drawable.orange_rounded_corner);
                btnPrice.setPadding(10, 10, 10, 10);
                btnTopUp.setPadding(10, 10, 10, 10);
                btnQuantity.setPadding(10, 10, 10, 10);
                arrayList.clear();
                arrayList.add(new AuthValueViewModel("50"));
                arrayList.add(new AuthValueViewModel("100"));
                arrayList.add(new AuthValueViewModel("200"));
                arrayList.add(new AuthValueViewModel("300"));
                arrayList.add(new AuthValueViewModel("500"));
                authValueAdapter.notifyDataSetChanged();
            }
        });

        btnQuantity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                authType = "Volume";
                btnQuantity.setBackgroundResource(R.drawable.orange_rounded_corner);
                btnTopUp.setBackgroundResource(R.drawable.grey_rounded_corner);
                btnPrice.setBackgroundResource(R.drawable.grey_rounded_corner);
                btnPrice.setPadding(10, 10, 10, 10);
                btnTopUp.setPadding(10, 10, 10, 10);
                btnQuantity.setPadding(10, 10, 10, 10);
                arrayList.clear();
                arrayList.add(new AuthValueViewModel("1"));
                arrayList.add(new AuthValueViewModel("5"));
                arrayList.add(new AuthValueViewModel("10"));
                arrayList.add(new AuthValueViewModel("15"));
                arrayList.add(new AuthValueViewModel("20"));
                authValueAdapter.notifyDataSetChanged();
            }
        });

        btnTopUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                authType = "Full Tank";
                btnQuantity.setBackgroundResource(R.drawable.grey_rounded_corner);
                btnTopUp.setBackgroundResource(R.drawable.orange_rounded_corner);
                btnPrice.setBackgroundResource(R.drawable.grey_rounded_corner);
                btnPrice.setPadding(10, 10, 10, 10);
                btnTopUp.setPadding(10, 10, 10, 10);
                btnQuantity.setPadding(10, 10, 10, 10);
            }
        });


        authValueAdapter.setClickListener(new AuthValueAdapter.ItemListener() {
            @Override
            public void onAuthValueClick(View v, int position) {
                etValue.setText(arrayList.get(position).title);
            }
        });

        TextView btnAuthorize = bottomSheetDialog.findViewById(R.id.btnAuthorize);
        btnAuthorize.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String strServerIp = SavePrefs.getString(AppConstant.prefName, getActivity(),
                        AppConstant.ipPref, null);
                String strServerPort = SavePrefs.getString(AppConstant.prefName, getActivity(),
                        AppConstant.portPref, null);
                String strUserName = SavePrefs.getString(AppConstant.prefName, getActivity(),
                        AppConstant.userNamePref, null);
                String strPassword = SavePrefs.getString(AppConstant.prefName, getActivity(),
                        AppConstant.passwordPref, null);

                connectToFCC("1", "10", strServerIp, strServerPort,
                        strUserName, strPassword);
            }
        });

        bottomSheetDialog.show();
    }


    public void connectToFCC(String pump_no, String pump_amount, String serverIp,
                             String serverPort, String username, String passowrd) {
        whichPump = pump_no;
        preAuthAmount = pump_amount;
        strServerIp = serverIp;
        strServerPort = serverPort;
        strServerUsername = username;
        strPassword = passowrd;
//        String strServerIp = SavePrefs.getString(AppConstant.prefName, context, AppConstant.ipPref, null);
//        String strServerPort = SavePrefs.getString(AppConstant.prefName, context, AppConstant.portPref, null);
        try {
            if (strServerIp == null & strServerPort == null) {
                FragmentUtils.replaceFragment((AppCompatActivity) getActivity(), new FccSettingFragment(),
                        R.id.fragContainer, false, TRANSITION_NONE);
            } else {
                CanRead = true;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                    new ChatOperator().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                } else {
                    new ChatOperator().execute();
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("EXCEPTION", "exception");
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
//                String strServerIp = SavePrefs.getString(AppConstant.prefName, context, AppConstant.ipPref, null);
//                String strServerPort = SavePrefs.getString(AppConstant.prefName, context, AppConstant.portPref, null);

                if (strServerIp == null) {
//                    getResources().getString(R.string.string_ip);//default ip address
                }
                if (strServerPort == null) {
//                    strServerPort = getResources().getString(R.string.string_port_request); //default port
                }
                // Creating the server socket.
                client = new Socket(strServerIp, Integer.parseInt(strServerPort));


                if (client != null) {
                    printwriter = new PrintWriter(client.getOutputStream(), true);
                    //InputStreamReader inputStreamReader = new InputStreamReader(client.getInputStream());
                    din = new DataInputStream(client.getInputStream());
                    //bufferedReader = new BufferedReader(inputStreamReader);
                } else {
                    Log.e("port ", "" + strServerPort);
                    ;
                }
            } catch (UnknownHostException e) {
                ServerConnection = "FAIL";
                Log.e("Faild", "" + strServerIp);
//                Toast.makeText(getActivity(),"Server Connection failed",Toast.LENGTH_SHORT).show()
                e.printStackTrace();
            } catch (IOException e) {
                ServerConnection = "FAIL";
                Log.e("Faild", "" + strServerIp);
                e.printStackTrace();
//                Toast.makeText(getActivity(),"Server Connection failed",Toast.LENGTH_SHORT).show();
//                showMessageDialog(getActivity(),"Server Connection failed");

            } catch (Exception e) {

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
                Log.e("Last Tran", "ServerFailed");
                showMessageDialog(getActivity(), "Server Connection failed");
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

//                    String strServerUsename = SavePrefs.getString(AppConstant.prefName, context,
//                            AppConstant.userNamePref, null);
//
//                    String strServerPassword = SavePrefs.getString(AppConstant.prefName, context,
//                            AppConstant.passwordPref, null);

                    message = "<LOGN|" + strServerUsername + "|" + strPassword + ">";
                    //  message = "<LOGN|admin|AdmiN@123!>";
                } else if (stat.equals("PAUT")) {
                    Log.e("pump_active_nozzle", "" + pump_active_nozzle);
                    Log.e("pump_no", "" + pump_no);
                    //message = "<PAUT|" + pump_no + ",1," + pump_active_nozzle + ",2," + preAuthAmount + ",4,,,>";
                    message = "<PAUT|" + pump_no + ",1," + "1" + ",2," + Double.parseDouble(preAuthAmount) + ",4,"
                            + "" + "," + "" + "," + "," + "," + "" + "," + "" + ">";

                } else if (stat.equals("LTRX")) {
                    // message = "<STAT|" + 0 + ">";
                    message = "<LTRX|" + pump_no + ">";

                } else if (stat.equals("STDT")) {

                    message = "<STDT>";

                }
//                }

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
            if (result.contains("PAUT|ACK")) {
                CanRead = true;
                Log.e("PAUTH", "Success");
//                messageSender = new Sender("STAT"); // Initialize chat sender AsyncTask.
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
//                    messageSender.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
//                } else {
//                    messageSender.execute();
//                }
//                doPreAuth(pump_no,"1",preAuthAmount,attendant_Name);
//                Glide.with(getActivity()).load(R.drawable.fueling_gif).into(ivGif);
                pDialog.dismiss();

            } else if (result.contains("PAUT|NACK")) {
                Log.e("PAUTH", "Fail");
                CanRead = false;
//                showMessageDialog(getActivity(),result.replace("�", "").replace("<", "")
//                        .replace(">", "").split("\\|")[2].toLowerCase().trim());
                pDialog.dismiss();

            } else if (result.contains("NACK")) {

                pDialog.dismiss();
                NACKServerConnection = "YES";
//                showMessageDialog(getActivity(),result.replace("�", "").replace("<", "")
//                        .replace(">", "").split("\\|")[2].toLowerCase().trim());
                Log.e("ERROR", "" + result.replace("�", "").replace("<", "")
                        .replace(">", "").split("\\|")[2].toLowerCase());
            } else if (result.contains("ACK")) {
                messageSender = new Sender("STAT|0"); // Initialize chat sender AsyncTask.
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                    messageSender.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                } else {
                    messageSender.execute();
                }
            } else if (result.contains("STAT")) {
//                <STAT|1,6,0,0,121.34,52.53,1/0:2/0:>
                pump_no = result.replace("�", "").replace("<", "").replace(">", "").split("\\|")[1].split("\\,")[0];
                pump_status = result.replace("�", "").replace("<", "").replace(">", "").split("\\|")[1].split("\\,")[1];
                pump_active_nozzle = result.replace("�", "").replace("<", "").replace(">", "").split("\\|")[1].split("\\,")[2];
                pump_volume = result.replace("�", "").replace("<", "").replace(">", "").split("\\|")[1].split("\\,")[3];
                pump_amount = result.replace("�", "").replace("<", "").replace(">", "").split("\\|")[1].split("\\,")[4];
                pump_price = result.replace("�", "").replace("<", "").replace(">", "").split("\\|")[1].split("\\,")[5];
                pump_nozzle_number = result.replace("�", "").replace("<", "").replace(">", "")
                        .split("\\|")[1].split("\\,")[6].split("\\/")[0];
                pump_nozzle_state = result.replace("�", "").replace("<", "").replace(">", "")
                        .split("\\|")[1].split("\\,")[6].split("\\/")[1].split("\\:")[0];

                Log.e("pump_no", "" + pump_no);
                Log.e("pump_status", "" + pump_status);
                Log.e("pump_active_nozzle", "" + pump_active_nozzle);
                Log.e("pump_volume", "" + pump_volume);
                Log.e("pump_amount", "" + pump_amount);
                Log.e("pump_price", "" + pump_price);
                Log.e("pump_nozzle_number", "" + pump_nozzle_number);
                Log.e("pump_nozzle_state", "" + pump_nozzle_state);
                // if (pump_status.equals("1") && pump_active_nozzle.equals("1")) {
                if (pump_status.equals("6")) {
                    messageSender = new Sender("PAUT"); // Initialize chat sender AsyncTask.
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                        messageSender.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                    } else {
                        messageSender.execute();
                    }
                    isPauth = true;

//                    if (isPauth == false) {
//                        messageSender = new Sender("PAUT"); // Initialize chat sender AsyncTask.
//                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
//                            messageSender.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
//                        } else {
//                            messageSender.execute();
//                        }
//                        isPauth = true;
//                    } else {
//                        messageSender = new Sender("STAT"); // Initialize chat sender AsyncTask.
//                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
//                            messageSender.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
//                        } else {
//                            messageSender.execute();
//                        }
//                        //isPauth=false;
//                    }
                } else if (pump_status.equals("1") || pump_status.equals("0")) {

                    messageSender = new Sender("PAUT"); // Initialize chat sender AsyncTask.
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                        messageSender.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                    } else {
                        messageSender.execute();
                    }
                    isPauth = true;

                    /*if (!isPauth) {
                        messageSender = new Sender("PAUT"); // Initialize chat sender AsyncTask.
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                            messageSender.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                        } else {
                            messageSender.execute();
                        }
                        isPauth = true;
                    } else {
                        messageSender = new Sender("STAT"); // Initialize chat sender AsyncTask.
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                            messageSender.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                        } else {
                            messageSender.execute();
                        }
                        //isPauth=false;
                    }*/
                } else if (pump_status.equals("2") || pump_status.equals("3")) {
                    messageSender = new Sender("STAT"); // Initialize chat sender AsyncTask.
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                        messageSender.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                    } else {
                        messageSender.execute();
                    }
                    isPauth = false;
                } else if (pump_status.equals("4")) {
                    messageSender = new Sender("LTRX"); // Initialize chat sender AsyncTask.
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                        messageSender.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                    } else {
                        messageSender.execute();
                    }
//                    CanRead = false;
//                    pDialog.dismiss();
//                    replaceScreen(new Fragment_fetch_transaction(),b);
                } else if (pump_status.equals("11")) {
                    messageSender = new Sender("STAT"); // Initialize chat sender AsyncTask.
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                        messageSender.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                    } else {
                        messageSender.execute();
                    }
                } else {
                    Log.e("pump_status FAILED", "pump_status FAILED");
                    //  pDialog.dismiss();
                    pDialog.dismiss();
                    NACKServerConnection = "YES";
//                    Toast.makeText(getActivity(), "Pump status failed.", Toast.LENGTH_LONG).show();
                    showMessageDialog(getActivity(), "Fetching pump status failed.");
                }

            } else if (result.contains("LTRX")) {
//                b.putString("TXN_ID", result.replace("�", "").replace("<", "").
//                        replace(">", "").split("\\|")[1].split("\\,")[0]);
//                b.putString("TXN_DATE", result.replace("�", "").replace("<", "").
//                        replace(">", "").split("\\|")[1].split("\\,")[1]);
//                b.putString("PRINT_ID", result.replace("�", "").replace("<", "").
//                        replace(">", "").split("\\|")[1].split("\\,")[2]);
//                b.putString("PUMPNO", result.replace("�", "").replace("<", "").
//                        replace(">", "").split("\\|")[1].split("\\,")[3]);
//                b.putString("NOZZLENO", result.replace("�", "").replace("<", "").
//                        replace(">", "").split("\\|")[1].split("\\,")[4]);
//                b.putString("GRADE", result.replace("�", "").replace("<", "").
//                        replace(">", "").split("\\|")[1].split("\\,")[5]);
//                b.putString("VOLUME", result.replace("�", "").replace("<", "").
//                        replace(">", "").split("\\|")[1].split("\\,")[6]);
//                b.putString("AMOUNT", result.replace("�", "").replace("<", "").
//                        replace(">", "").split("\\|")[1].split("\\,")[7]);
//                b.putString("PRICE", result.replace("�", "").replace("<", "").
//                        replace(">", "").split("\\|")[1].split("\\,")[9]);
//                b.putString("attandant",result.replace("�", "").replace("<", "").
//                        replace(">", "").split("\\|")[1].split("\\,")[19]);

                CanRead = false;
                pDialog.dismiss();
//                replaceScreen(new Fragment_fetch_transaction(), b);
            }
            /*else if (result.contains("STDT")) {

                Log.e("New result", "" + result);
//                retailer_code = result.replace("�", "").replace("<", "").replace(">", "")
//                        .split("\\|")[1].split("\\,")[0];
//                retailer_name = result.replace("�", "").split("\\|")[1].split("\\,")[1].split("\\>")[0];
//
////                    retailer_name = retailer_name.split("�")[0];
//
//                CanRead = false;
//
//                Log.e("Retailercode", "" + retailer_code);
//                Log.e("RetailerName", "" + retailer_name);

            }*/


        }
    }


    public void showMessageDialog(Context mContext, String message) {
        androidx.appcompat.app.AlertDialog.Builder builder =
                new androidx.appcompat.app.AlertDialog.Builder(mContext, R.style.MyDialogTheme);
//
        LinearLayout layout = new LinearLayout(mContext);
        layout.setOrientation(LinearLayout.VERTICAL);

        TextView resultMessage = new TextView(mContext);
        resultMessage.setTextSize(15);
        resultMessage.setText(message);
        resultMessage.setGravity(Gravity.CENTER);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        lp.setMargins(0, 20, 0, 0);
        resultMessage.setPadding(0, 55, 0, 35);
        resultMessage.setLayoutParams(lp);
        builder.setView(resultMessage);
        layout.addView(resultMessage);


        View view = new View(mContext);
        LinearLayout.LayoutParams lp1 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                1);
        view.setBackgroundColor(mContext.getResources().getColor(R.color.black));
        view.setLayoutParams(lp1);
        view.setPadding(0, 0, 0, 0);
        layout.addView(view);
        builder.setView(layout);

        builder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
//                getActivity().getSupportFragmentManager().popBackStack();
            }
        });

        androidx.appcompat.app.AlertDialog alert = builder.create();
        alert.setCancelable(false);
        alert.show();

        final Button positiveButton = alert.getButton(androidx.appcompat.app.AlertDialog.BUTTON_POSITIVE);
        LinearLayout.LayoutParams positiveButtonLL = (LinearLayout.LayoutParams) positiveButton.getLayoutParams();
//        positiveButton.setBackground(null);

        positiveButtonLL.width = ViewGroup.LayoutParams.MATCH_PARENT;
        positiveButton.setLayoutParams(positiveButtonLL);
    }


}