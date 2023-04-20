package com.skyband.ecr.sdk;

import com.skyband.ecr.sdk.logger.Logger;

public class CLibraryLoad {

    private Logger logger = Logger.getNewLogger(ConnectionManager.class.getName());

    static {
        System.loadLibrary("ecrcore-lib");
    }

    private static CLibraryLoad instance;

    private CLibraryLoad() {

    }

    public static CLibraryLoad getInstance() {
        if (instance == null) {
            instance = new CLibraryLoad();
        }
        return instance;
    }

    public byte[] getPackData(String reqData, int tranType, String szSignature) throws Exception {

        logger.debug("Calling Pack >>> " + reqData + " szSignature >>> " + szSignature);

        byte[] packedData = pack(reqData, tranType, szSignature, null);
        String packData = new String(packedData);
        if(packData.isEmpty()){
            throw new Exception("4");
        }
        logger.debug("Packed Data:" + packData);
        logger.debug("Sending Packed Data to Terminal>>>");

        return packedData;
    }

    public String getParseData(String respData) {

        logger.debug("Calling Parse >>> " + respData);

        byte[] parseData = parse(respData, "");
        String parsedData = new String(parseData);

        logger.debug("Parse data" + parsedData);

        return parsedData;
    }

    public native byte[] pack(String inputReqData, int transactionType, String szSignature, String szEcrBuffer);

    public native byte[] parse(String respData, String respOutData);
}
