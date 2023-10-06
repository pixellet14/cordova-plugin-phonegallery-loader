package com.example;

import org.apache.cordova.*;
import org.json.JSONArray;
import org.json.JSONException;
import android.provider.MediaStore;
import android.database.Cursor;
import android.net.Uri;
import org.json.JSONObject;
import java.util.HashSet;

public class RiyaAlbumLoader extends CordovaPlugin {

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
        Uri uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        String[] projection = { MediaStore.Images.Media.BUCKET_DISPLAY_NAME, MediaStore.Images.Media.DATA };
        
        Cursor cursor = this.cordova.getActivity().getContentResolver().query(uri, projection, null, null, MediaStore.Images.Media.DATE_MODIFIED + " DESC");
        
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
    String sortOrder = MediaStore.Images.Media.DATE_MODIFIED + " DESC LIMIT " + count + " OFFSET " + startIndex;

    Cursor cursor = this.cordova.getActivity().getContentResolver().query(uri, projection, selection, selectionArgs, sortOrder);
    JSONArray result = new JSONArray();

    int imagePathColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
    int imageSizeColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.SIZE);

    while (cursor.moveToNext()) {
        // Check if image size is greater than 0
        if (cursor.getInt(imageSizeColumn) > 0) {
            result.put(cursor.getString(imagePathColumn));
        }
    }

    cursor.close();
      Log.d("RiyaAlbumLoader", "Returning " + result.length() + " pictures.");
    callbackContext.success(result);
}


}
