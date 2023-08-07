package com.example;

import org.apache.cordova.*;
import org.json.JSONArray;
import org.json.JSONException;

public class RiyaAlbumLoader extends CordovaPlugin {

    @Override
    public boolean execute(String action, JSONArray data, CallbackContext callbackContext) throws JSONException {

        if (action.equals("getAlbums")) {
            // TODO: implement your logic to get album list here
            
            // Send a positive result to the callbackContext
            PluginResult pluginResult = new PluginResult(PluginResult.Status.OK, "Success");
            callbackContext.sendPluginResult(pluginResult);
            return true;
        }
        return false;  // Returning false results in a "MethodNotFound" error.
    }
}
