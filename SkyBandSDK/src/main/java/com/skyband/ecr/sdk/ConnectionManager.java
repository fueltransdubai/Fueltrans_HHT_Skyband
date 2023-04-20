package com.skyband.ecr.sdk;

import com.skyband.ecr.sdk.logger.Logger;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.ByteBuffer;

public class ConnectionManager {

    private static Logger logger = Logger.getNewLogger(ConnectionManager.class.getName());

    private Socket socket;
    private OutputStream output;
    private InputStream input;
    private String serverIp;
    private int serverPort;
    private static ConnectionManager socketHostConnector;
    private final int SOCKET_TIMEOUT = 120000;

    public static ConnectionManager instance(String ip, int port) throws IOException {

        logger.info("ConnectionManager | Instance | Entering");

        // First time
        if (socketHostConnector == null) {
            socketHostConnector = new ConnectionManager(ip, port);
            socketHostConnector.createConnection();
        } else {
            socketHostConnector.cleanup();
            socketHostConnector = new ConnectionManager(ip, port);
            socketHostConnector.createConnection();
        }

        logger.info("ConnectionManager | Instance | Exiting");

        return socketHostConnector;
    }

    /**
     * This use only for exiting connection access
     *
     * @return
     * @throws IOException
     */

    public static ConnectionManager instance() {

        logger.info("ConnectionManager | Instance | Entering");

        if (socketHostConnector != null) {
            return socketHostConnector;
        }
        logger.info("ConnectionManager | Instance | Exiting");

        return null;
    }

    private ConnectionManager(String ip, int port) {

        serverIp = ip;
        serverPort = port;
    }

    public void createConnection() throws IOException {

        logger.info("ConnectionManager | CreateConnection | Entering");
        socket = new Socket();
        socket.connect(new InetSocketAddress(serverIp, serverPort), SOCKET_TIMEOUT);
        socket.setSoTimeout(SOCKET_TIMEOUT);

        logger.info("ConnectionManager | CreateConnection | Exiting");

    }

    public void disconnect() throws IOException {

        logger.info("ConnectionManager | Disconnect | Entering");

        if (socket != null && socket.isConnected()) {
            cleanup();
        }

        logger.info("ConnectionManager | Disconnect | Exiting");

    }

    public void cleanup() throws IOException {

        logger.info("ConnectionManager | Cleanup | Entering");

        if (input != null) {

            try {
                input.close();
            } catch (IOException e) {
                logger.severe("Exception while closing",e);
            }
        }

        if (output != null) {

            output.close();
        }

        if (socket != null) {

            try {
                socket.close();
            } catch (IOException e) {
                logger.severe("Exception while closing",e);
            }
        }

        input = null;
        output = null;
        socket = null;

        logger.info("ConnectionManager | Cleanup | Exiting");

    }

    public String sendAndRecv(byte[] in) throws IOException, InterruptedException {

        logger.info("ConnectionManager | SedAndRecv | Entering");
        output = socket.getOutputStream();
        input = socket.getInputStream();

        output.write(in);

        output.flush();

        byte[] responseBytes = new byte[50000];

        int noOfBytesRead = input.read(responseBytes, 0, responseBytes.length);

        System.out.println(noOfBytesRead);

        if (noOfBytesRead <= 0) {
            throw new IOException();
        }

        byte[] finalResponse = new byte[noOfBytesRead];

        System.arraycopy(responseBytes, 0, finalResponse, 0, noOfBytesRead);
        String terminalResponse = new String(finalResponse);

        logger.info("ConnectionManager | SedAndRecv | Exiting");

        return terminalResponse;
    }


    public String sendAndRecvSummary(byte[] in) throws IOException, InterruptedException {
        logger.info("ConnectionManager | SedAndRecv | Entering");
        output = socket.getOutputStream();
        input = socket.getInputStream();

        output.write(in);

        output.flush();

        byte[] responseBytes = new byte[50000];
        byte[] messageByte = new byte[25000];

        DataInputStream in1 = new DataInputStream(socket.getInputStream());
        int bytesRead = 0;

        messageByte[0] = in1.readByte();
        messageByte[1] = in1.readByte();
        ByteBuffer byteBuffer = ByteBuffer.wrap(messageByte, 0, 2);

        int bytesToRead = byteBuffer.getShort();

        Thread.sleep(1000);

        int noOfBytesRead = input.read(responseBytes, 0, responseBytes.length);

        System.out.println(noOfBytesRead);

        if (noOfBytesRead <= 0) {
            throw new IOException();
        }

        byte[] finalResponse = new byte[noOfBytesRead];

        System.arraycopy(responseBytes, 0, finalResponse, 0, noOfBytesRead);
        String terminalResponse = new String(finalResponse);

        logger.info("ConnectionManager | SedAndRecv | Exiting");

        logger.info("SocketResponse"+terminalResponse);

        return terminalResponse;

    }

    public boolean isConnected() {

        logger.info("ConnectionManager | Isconnected | Entering");

        if (socket != null) {
            return socket.isConnected();
        }
        logger.info("ConnectionManager | Isconnected | Exiting");

        return false;
    }

}
