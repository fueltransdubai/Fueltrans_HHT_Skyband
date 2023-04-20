package com.skyband.ecr.sdk.logger;

import android.os.Environment;
import android.util.Log;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class FileLogger extends Logger {

    private static final boolean ISCONSOLELOG = false;
    private static final String LOGFILEPATH = "/skyband/log/";
    private static final String LOGFILENAME = "skybandsdk.log";
    private static File file = null;
    private static BufferedWriter bufWriter = null;

    public FileLogger(String name) {
        super(name);
    }

    protected Logger newLogger(String name) {
        return new FileLogger(name);
    }

    public void init() {
        createFileInSpecifiedPath(LOGFILENAME);
    }

    public void initWithFile(String fileName) {
        createFileInSpecifiedPath(fileName);
    }

    private static void createFileInSpecifiedPath(String fileName) {
        try {
            String rootPath = Environment.getExternalStorageDirectory().getAbsolutePath() + LOGFILEPATH;
            File root = new File(rootPath);
            if (!root.exists()) {
                root.mkdirs();
            }
            file = new File(rootPath, fileName);
            if (!file.exists()) {
                if(!file.createNewFile()) {
                    throw new Exception("File creation error");
                }
            }
        } catch (IOException e) {
            Log.e("ERROR : ", " Create Log File IOException " + e);
        } catch (Exception e) {
            Log.e("ERROR : ", "Unable to open File I/O connection " + e);
        }
    }


    protected void writeLog(int logType, String msg) {

        if (ISCONSOLELOG) {
            switch (logType) {
                case ERROR:
                    Log.e("", msg);
                    break;
                case WARN:
                    Log.w("", msg);
                    break;
                case DEBUG:
                    Log.d("", msg);
                    break;
                case INFO:
                case TRACE:
                    Log.i("", msg);
                    break;
                case DIAGNOSE:
                    Log.v("", msg);
                    break;
                    default:
                        break;
            }
        }

        if (file != null) {
            try(BufferedWriter buffWriter = new BufferedWriter(new FileWriter(file, true))) {
                buffWriter.append(msg);
                buffWriter.newLine();
            } catch (IOException e) {
                Log.e("ERROR : ", "FileLogger" + e);
            } finally {
                close();
            }
        }
    }

    public void close() {
        try {
            if (bufWriter != null) {
                bufWriter.close();
            }
        } catch (IOException e) {
            Log.e("ERROR : ", "FileLogger closing " + e);
        }
    }
}
