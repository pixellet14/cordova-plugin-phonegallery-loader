package com.example;

import org.apache.cordova.*;
import org.json.JSONArray;
import org.json.JSONException;
import java.io.File;
import android.os.Environment;

public class RiyaAlbumLoader extends CordovaPlugin {

    @Override
    public boolean execute(String action, JSONArray data, CallbackContext callbackContext) throws JSONException {

        if (action.equals("getAlbums")) {
            String[] folders = {"Camera", "WhatsApp", "Instagram", "Facebook", "DCIM"};

            JSONArray albums = new JSONArray();
            
            String state = Environment.getExternalStorageState();
            if (Environment.MEDIA_MOUNTED.equals(state) || Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
                // We can read the media
                File root = Environment.getExternalStorageDirectory();

                for (String folderName : folders) {
                    File folder = new File(root, folderName);
                    if (folder.isDirectory() && folder.listFiles().length > 0) {
                        albums.put(folderName);
                    }
                }
            }

            // Send a positive result to the callbackContext
            PluginResult pluginResult = new PluginResult(PluginResult.Status.OK, albums);
            callbackContext.sendPluginResult(pluginResult);
            return true;
        }
        return false;  // Returning false results in a "MethodNotFound" error.
    }
}
