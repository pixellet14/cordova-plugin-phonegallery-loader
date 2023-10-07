var exec = require('cordova/exec');

var RiyaAlbumLoader = {
    loadAlbums: function(successCallback, errorCallback) {
        exec(successCallback, errorCallback, "RiyaAlbumLoader", "loadAlbums", []);
    },
    loadPicturesInAlbum: function(albumName, startIndex, count, successCallback, errorCallback) {
        exec(successCallback, errorCallback, "RiyaAlbumLoader", "loadPicturesInAlbum", [albumName, startIndex, count]);
    }
};

module.exports = RiyaAlbumLoader;
