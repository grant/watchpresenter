/*
 * Copyright 2014 Google Inc. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.zuluindia.watchpresenter.messaging;

/**
 * Created by pablogil on 1/16/15.
 */

import android.os.AsyncTask;
import android.util.Log;

import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.zuluindia.watchpresenter.Constants;
import com.zuluindia.watchpresenter.MainActivity;
import com.zuluindia.watchpresenter.backend.messaging.Messaging;
import com.zuluindia.watchpresenter.backend.messaging.model.RegisteredResponse;
import com.zuluindia.watchpresenter.backend.messaging.model.VersionMessage;

import java.io.IOException;

public class GcmCheckRegistrationAsyncTask extends AsyncTask<Integer, Void, Boolean> {
    private Messaging messagingService = null;
    private GoogleCloudMessaging gcm;
    private GoogleAccountCredential credential;
    private static final String LOG_TAG = "GetVersionMessage";
    private MainActivity mainActivity;


    public GcmCheckRegistrationAsyncTask(Messaging messagingService, MainActivity mainActivity) {
        this.messagingService = messagingService;
        this.mainActivity = mainActivity;
    }

    @Override
    protected Boolean doInBackground(Integer... versionNumbers) {

        RegisteredResponse response = null;
        boolean result = false;
        if(messagingService != null) {

            try {
                response = messagingService.checkRegistration().execute();

            } catch (IOException ex) {
                Log.e(LOG_TAG, "Could not check registration", ex);
            }
        }
        else{
            Log.e(LOG_TAG, "Could not check registration, no MessagingService available");
        }

        if(response != null && response.getRegistered()){
            result = true;
        }

        return result;
    }


    @Override
    protected void onPostExecute(Boolean registered) {
        mainActivity.registrationUpdate(registered);
    }
}