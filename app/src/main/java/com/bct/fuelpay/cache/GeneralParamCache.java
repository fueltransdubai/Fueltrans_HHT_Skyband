package com.bct.fuelpay.cache;

import com.bct.fuelpay.constant.AppConstant;

public class GeneralParamCache extends Cache  {

    private static class LazyHolder {
        private static final GeneralParamCache INSTANCE = new GeneralParamCache();
    }

    private GeneralParamCache() {
        super(SP_NAME_GENERAL_PARAM);

        load();
    }

    private void load() {
        if ( getString(AppConstant.APPLICATION_NAME) == null ) {
            putString(AppConstant.APPLICATION_NAME, "Test ECR Application");
            putString(AppConstant.SOFTWARE_VERSION, "1.0.0");
            putString(AppConstant.PROVIDER_ID, "Girmiti Software Pvt. Ltd");
        }
    }

    public static GeneralParamCache getInstance() {
        return LazyHolder.INSTANCE;
    }
}
