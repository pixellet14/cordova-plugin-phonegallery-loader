package com.example;

import org.apache.cordova.*;
import org.json.JSONArray;
import org.json.JSONException;
import android.os.Environment;
import java.io.File;
import java.util.ArrayList;
import org.json.JSONObject;

public class RiyaAlbumLoader extends CordovaPlugin {

    private static final String[] TARGET_FOLDERS = {"camera", "whatsapp", "instagram", "facebook", "DCIM"};

    @Override
    public boolean execute(String action, JSONArray data, CallbackContext callbackContext) throws JSONException {
        if (action.equals("loadAlbums")) {
            this.loadAlbums(callbackContext);
            return true;
        } else if (action.equals("loadPicturesInAlbum")) {
            if(data != null && data.length() > 0){
                String albumName = data.getString(0);
                this.loadPicturesInAlbum(albumName, callbackContext);
                return true;
            }
        }

        return false;
    }

    private void loadAlbums(CallbackContext callbackContext) {
        // Logic to find folders containing images.
    }

    private void loadPicturesInAlbum(String albumName, CallbackContext callbackContext) {
        try {
            File picDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), albumName);

            if (picDir.exists() && picDir.isDirectory()) {
                ArrayList<String> pictures = new ArrayList<String>();
                File[] allContents = picDir.listFiles();
                for (File file : allContents) {
                    if (file.isFile() && (file.getName().endsWith(".jpg") || file.getName().endsWith(".png"))) {
                        pictures.add(file.getAbsolutePath());
                    }
                }

                JSONArray result = new JSONArray(pictures);
                callbackContext.success(result);
            } else {
                callbackContext.error("Album does not exist or is not a directory.");
            }
        } catch (Exception e) {
            callbackContext.error("Error loading pictures: " + e.getMessage());
        }
    }
}
