/*
 Licensed to the Apache Software Foundation (ASF) under one
 or more contributor license agreements. See the NOTICE file
 distributed with this work for additional information
 regarding copyright ownership. The ASF licenses this file
 to you under the Apache License, Version 2.0 (the
 "License"); you may not use this file except in compliance
 with the License. You may obtain a copy of the License at

 http://www.apache.org/licenses/LICENSE-2.0

 Unless required by applicable law or agreed to in writing,
 software distributed under the License is distributed on an
 "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 KIND, either express or implied. See the License for the
 specific language governing permissions and limitations
 under the License.
 */

package com.polyvi.plugins.weibo;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaPlugin;
import org.json.JSONArray;
import org.json.JSONException;

import android.app.Activity;

public class Weibo extends CordovaPlugin {

    private static String HEAD_URL = "https://open.weibo.cn/oauth2/authorize?display=mobile&forcelogin=true";
    private static String CLIENT_ID = "&client_id=";
    private static String RESPONSE_TYPE = "&response_type=token";
    private static String REDIRECT_URI = "&redirect_uri=";
    private static String ERROR_MSG = "clientID or redirect_uri is empty";
    private static final String TAG_WEIBO_LOGIN = "login";
    private CallbackContext mCallbackContext;

    /**
     * Executes the request and returns PluginResult.
     *
     * @param action
     *            The action to execute.
     * @param args
     *            JSONArry of arguments for the plugin.
     * @param callbackContext
     *            The callback id used when calling back into JavaScript.
     * @return True if the action was valid, false if not.
     */
    @Override
    public boolean execute(String action, JSONArray args,
            CallbackContext callbackContext) throws JSONException {
        mCallbackContext = callbackContext;
        if (action.equals(TAG_WEIBO_LOGIN)) {
            try {
                weiboLogin(args.getString(0), args.getString(1));
            } catch (JSONException e) {
                mCallbackContext.error(e.getMessage());
                return false;
            }
        } else {
            return false;
        }
        return true;
    }

    private void weiboLogin(String clientID, String redirector) {
        if (isEmptyString(clientID) || isEmptyString(redirector)) {
            mCallbackContext.error(ERROR_MSG);
        }
        String url = HEAD_URL + CLIENT_ID + clientID + RESPONSE_TYPE
                + REDIRECT_URI + redirector;
        String redirectorUrl = redirector;
        final Activity activity = this.cordova.getActivity();
        final WeiboContent weiboContent = new WeiboContent(url, redirectorUrl);
        this.cordova.getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                new WebViewHandler(activity, mCallbackContext, weiboContent);
            }
        });

    }

    /**
     * ?斗摮泵銝脫?虫蛹蝛?
     */
    public static boolean isEmptyString(String str) {
        return null == str || "".equals(str);
    }
}
