var exec = require('cordova/exec');

exports.loadAlbums = function(successCallback, errorCallback) {
    exec(successCallback, errorCallback, 'RiyaAlbumLoader', 'loadAlbums', []);
};

exports.loadPicturesInAlbum = function(albumName, successCallback, errorCallback) {
    exec(successCallback, errorCallback, 'RiyaAlbumLoader', 'loadPicturesInAlbum', [albumName]);
};
