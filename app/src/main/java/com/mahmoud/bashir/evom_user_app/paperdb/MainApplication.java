package com.mahmoud.bashir.evom_user_app.paperdb;

import android.app.Application;
import android.content.Context;

import com.mahmoud.bashir.evomdriverapp.paperdb.helper.LocaleHelper;

public class MainApplication extends Application {

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(LocaleHelper.onAttach(base,"ar"));
    }
}
