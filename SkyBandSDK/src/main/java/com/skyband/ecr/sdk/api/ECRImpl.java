package com.skyband.ecr.sdk.api;

import android.bluetooth.BluetoothDevice;

import com.skyband.ecr.sdk.BluetoothConnectionManager;
import com.skyband.ecr.sdk.CLibraryLoad;
import com.skyband.ecr.sdk.ConnectionManager;
import com.skyband.ecr.sdk.api.listener.ECRCore;
import com.skyband.ecr.sdk.logger.Logger;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Objects;

public class ECRImpl implements ECRCore {

    private static ECRCore ecrCore;
    private Logger logger = Logger.getNewLogger(ECRImpl.class.getName());
    private static boolean isLastTxnSummary;

    public static ECRCore getConnectInstance() {
        if (ecrCore == null) {
            ecrCore = new ECRImpl();
            return ecrCore;
        }
        return ecrCore;
    }

    @Override
    public String doTCPIPTransaction(String ipAddress, int portNumber, String requestData, int transactionType, String signature) throws Exception {

        String terminalResponse = "";

        try {
            ECRImpl.getConnectInstance().doTCPIPConnection(ipAddress, portNumber);
        } catch (Exception e) {
            throw new Exception("3");
        }
        byte[] packData = CLibraryLoad.getInstance().getPackData(requestData, transactionType, signature);

        logger.info("Socket connected");

        if (ConnectionManager.instance() != null && Objects.requireNonNull(ConnectionManager.instance()).isConnected()) {

            if (transactionType == 23 && isLastTxnSummary) {
                transactionType = 22;
            }

            try {
                if (transactionType != 22) {
                    terminalResponse = Objects.requireNonNull(ConnectionManager.instance()).sendAndRecv(packData);
                    terminalResponse = terminalResponse.replace("�", ";");
                    logger.debug("After Replace  with ;>>" + terminalResponse);
                    terminalResponse = changeToTransactionType(terminalResponse);
                    logger.debug("After Replace with Transactiontype>>" + terminalResponse);
                    isLastTxnSummary = false;
                    return terminalResponse;
                } else {
                    terminalResponse = Objects.requireNonNull(ConnectionManager.instance()).sendAndRecvSummary(packData);
                    terminalResponse = terminalResponse.replace("�", ";");
                    isLastTxnSummary = true;
                    return terminalResponse;
                }

            } catch (IOException e) {
                try {
                    ECRImpl.getConnectInstance().doDisconnection();
                    logger.info("Socket Disconnected");
                    throw new Exception("0");
                } catch (IOException ex) {
                    logger.severe("Exception in Disconnect >>", ex);
                    throw new Exception("1");
                }
            }

        } else {
            throw new Exception("2");
        }
        //Library parsing is not giving correct response
      /*  String parseData = CLibraryLoad.getInstance().getParseData(terminalResponse);
        parseData = parseData.replace("�", ";");
        logger.debug("After Replace  with ;>>"+parseData);
        return parseData;*/

    }

    private String changeToTransactionType(String terminalResponse) throws Exception {
        String[] response = terminalResponse.split(";");
        if (response.length < 2) {
            throw new Exception("3");
        }
        String index1 = response[1];
        switch (response[1]) {
            case "A0":
                terminalResponse = terminalResponse.replaceFirst("A0", String.valueOf(17));
                break;
            case "B6":
                terminalResponse = terminalResponse.replaceFirst("B6", String.valueOf(18));
                break;
            case "B7":
                terminalResponse = terminalResponse.replaceFirst("B7", String.valueOf(19));
                break;
            case "A1":
                terminalResponse = terminalResponse.replaceFirst("A1", String.valueOf(0));
                break;
            case "A2":
                terminalResponse = terminalResponse.replaceFirst("A2", String.valueOf(1));
                break;
            case "A3":
                terminalResponse = terminalResponse.replaceFirst("A3", String.valueOf(8));
                break;
            case "A4":
                terminalResponse = terminalResponse.replaceFirst("A4", String.valueOf(3));
                break;
            case "A5":
                terminalResponse = terminalResponse.replaceFirst("A5", String.valueOf(9));
                break;
            case "A6":
                terminalResponse = terminalResponse.replaceFirst("A6", String.valueOf(2));
                break;
            case "A7":
                terminalResponse = terminalResponse.replaceFirst("A7", String.valueOf(4));
                break;
            case "A8":
                terminalResponse = terminalResponse.replaceFirst("A8", String.valueOf(5));
                break;
            case "A9":
                terminalResponse = terminalResponse.replaceFirst("A9", String.valueOf(6));
                break;
            case "B1":
                terminalResponse = terminalResponse.replaceFirst("B1", String.valueOf(10));
                break;
            case "B2":
                terminalResponse = terminalResponse.replaceFirst("B2", String.valueOf(11));
                break;
            case "B3":
                terminalResponse = terminalResponse.replaceFirst("B3", String.valueOf(12));
                break;
            case "B4":
                terminalResponse = terminalResponse.replaceFirst("B4", String.valueOf(13));
                break;
            case "B5":
                terminalResponse = terminalResponse.replaceFirst("B5", String.valueOf(14));
                break;
            case "B8":
                terminalResponse = terminalResponse.replaceFirst("B8", String.valueOf(20));
                break;
            case "B9":
                terminalResponse = terminalResponse.replaceFirst("B9", String.valueOf(21));
                break;
            case "C1":
                terminalResponse = terminalResponse.replaceFirst("C1", String.valueOf(22));
                break;
            case "C2":
                terminalResponse = terminalResponse.replaceFirst("C2", String.valueOf(23));
                break;
            case "C3":
                terminalResponse = terminalResponse.replaceFirst("C3", String.valueOf(24));
                break;
            case "C4":
                terminalResponse = terminalResponse.replaceFirst("C4", String.valueOf(25));
                break;
            case "C5":
                terminalResponse = terminalResponse.replaceFirst("C5", String.valueOf(26));
                break;
            case "D1":
                terminalResponse = terminalResponse.replaceFirst("D1", String.valueOf(30));
                break;
            default:
                terminalResponse = terminalResponse.replaceFirst(index1, String.valueOf(40));
                break;
        }
        return terminalResponse;
    }

    @Override
    public int doTCPIPConnection(String ipAddress, int portNumber) throws IOException {
        if (ConnectionManager.instance(ipAddress, portNumber).isConnected()) {
            return 0;
        } else {
            return 1;
        }
    }

    @Override
    public int doDisconnection() throws IOException {
        if (ConnectionManager.instance() != null) {
            ConnectionManager.instance().disconnect();
            return 0;
        } else {
            return 1;
        }
    }

    @Override
    public String computeSha256Hash(String combinedValue) throws NoSuchAlgorithmException {

        MessageDigest md = MessageDigest.getInstance("SHA-256");
        byte[] hashInBytes = md.digest(combinedValue.getBytes());

        StringBuilder sb = new StringBuilder();

        for (byte b : hashInBytes) {
            sb.append(String.format("%02x", b));
        }

        String resultSHA = sb.toString();

        return resultSHA;
    }

    @Override
    public String doBluetoothTransaction(BluetoothDevice device, String requestData, int transactionType, String signature) throws Exception {
        String terminalResponse = "";

        try {
            BluetoothConnectionManager.instances(device);
        } catch (IOException e) {
            throw new Exception("3");
        }

        byte[] packData = CLibraryLoad.getInstance().getPackData(requestData, transactionType, signature);

        if (BluetoothConnectionManager.instance() != null && Objects.requireNonNull(BluetoothConnectionManager.instance()).isConnected()) {

            try {
                if (transactionType != 22) {
                    terminalResponse = Objects.requireNonNull(BluetoothConnectionManager.instance()).sendAndRecv(packData);
                    terminalResponse = terminalResponse.replace("�", ";");
                    logger.debug("After Replace  with ;>>" + terminalResponse);
                    terminalResponse = changeToTransactionType(terminalResponse);
                    logger.debug("After Replace with Transactiontype>>" + terminalResponse);

                    return terminalResponse;
                } else {
                    terminalResponse = Objects.requireNonNull(BluetoothConnectionManager.instance()).sendAndRecvSummary(packData);
                    terminalResponse = terminalResponse.replace("�", ";");

                    return terminalResponse;
                }

            } catch (IOException e) {
                try {
                    ECRImpl.getConnectInstance().doDisconnection();
                    logger.info("Socket Disconnected");
                    throw new Exception("0");
                } catch (IOException ex) {
                    logger.severe("Exception in Disconnect >>", ex);
                    throw new Exception("1");
                }
            }

        } else {
            throw new Exception("2");
        }
        //Library parsing is not giving correct response
      /*  String parseData = CLibraryLoad.getInstance().getParseData(terminalResponse);
        parseData = parseData.replace("�", ";");
        logger.debug("After Replace  with ;>>"+parseData);
        return parseData;*/
    }

    @Override
    public int doBluetoothConnection(BluetoothDevice device) throws IOException {
        if (BluetoothConnectionManager.instances(device).isConnected()) {
            return 0;
        } else {
            return 1;
        }
    }

    @Override
    public int doBluetoothDisconnection() throws IOException {
        if (BluetoothConnectionManager.instance() != null) {
            BluetoothConnectionManager.instance().disconnect();
            return 0;
        } else {
            return 1;
        }
    }

}