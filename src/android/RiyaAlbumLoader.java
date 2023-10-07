package com.example;

import org.apache.cordova.*;
import org.json.JSONArray;
import org.json.JSONException;
import android.provider.MediaStore;
import android.database.Cursor;
import android.net.Uri;
import org.json.JSONObject;
import java.util.HashSet;
import android.util.Log;

public class RiyaAlbumLoader extends CordovaPlugin {

    private static final int DEFAULT_START_INDEX = 0;
    private static final int DEFAULT_COUNT = 25;  // Default batch size

    @Override
    public boolean execute(String action, JSONArray data, CallbackContext callbackContext) throws JSONException {
        if (action.equals("loadAlbums")) {
            this.loadAlbums(callbackContext);
            return true;
        } else if (action.equals("loadPicturesInAlbum")) {
            if(data != null && data.length() > 2){
                String albumName = data.getString(0);
                int startIndex = data.getInt(1);
                int count = data.getInt(2);
                this.loadPicturesInAlbum(albumName, startIndex, count, callbackContext);
                return true;
            }
        }

        return false;
    }

   private void loadAlbums(CallbackContext callbackContext) {
        Uri uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        String[] projection = { MediaStore.Images.Media.BUCKET_DISPLAY_NAME, MediaStore.Images.Media.DATA };
        
        Cursor cursor = this.cordova.getActivity().getContentResolver().query(uri, projection, null, null, MediaStore.Images.Media.BUCKET_DISPLAY_NAME + " ASC");
       
        JSONArray result = new JSONArray();

        // Use a HashSet to keep track of the albums we've already processed
        HashSet<String> albumNames = new HashSet<>();

        int albumNameColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.BUCKET_DISPLAY_NAME);
        int albumPathColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);

        while (cursor.moveToNext()) {
            String albumName = cursor.getString(albumNameColumn);

            // Check if we've already processed this album
            if (!albumNames.contains(albumName)) {
                albumNames.add(albumName);

                JSONObject album = new JSONObject();
                try {
                    album.put("name", albumName);
                    album.put("thumbnailPath", cursor.getString(albumPathColumn));
                    result.put(album);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }

        cursor.close();
        callbackContext.success(result);
    }

    private void loadPicturesInAlbum(String albumName, int startIndex, int count, CallbackContext callbackContext) {
        Log.d("RiyaAlbumLoader", "Fetching pictures for album: " + albumName + ", starting at index: " + startIndex + ", count: " + count);
    
    Uri uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
    String[] projection = { MediaStore.Images.Media.DATA, MediaStore.Images.Media.SIZE };
    String selection = MediaStore.Images.Media.BUCKET_DISPLAY_NAME + "=?";
    String[] selectionArgs = { albumName };
    String sortOrder = MediaStore.Images.Media._ID + " ASC";
    
    Cursor cursor = this.cordova.getActivity().getContentResolver().query(uri, projection, selection, selectionArgs, sortOrder);
    JSONArray result = new JSONArray();

    int imagePathColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
    int imageSizeColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.SIZE);

    // move the cursor to the start index
    if (cursor.move(startIndex)) {
        int i = 0;  // counter for the number of images fetched
        do {
            // Check if image size is greater than 0
            if (cursor.getInt(imageSizeColumn) > 0) {
                result.put(cursor.getString(imagePathColumn));
                i++;
            }
        } while (cursor.moveToNext() && i < count);
    }

    cursor.close();
    Log.d("RiyaAlbumLoader", "Returning " + result.length() + " pictures.");
    callbackContext.success(result);
}


}
