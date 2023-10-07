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
            if(data != null && data.length() > 0){
                String albumName = data.getString(0);
                this.loadPicturesInAlbum(albumName, DEFAULT_START_INDEX, DEFAULT_COUNT, callbackContext);
                return true;
            }
        }

        return false;
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
