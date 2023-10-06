var exec = require('cordova/exec');

exports.loadAlbums = function(successCallback, errorCallback) {
    exec(successCallback, errorCallback, 'RiyaAlbumLoader', 'loadAlbums', []);
};

exports.loadPicturesInAlbum = function(albumName, startIndex, count, successCallback, errorCallback) {
    exec(successCallback, errorCallback, 'RiyaAlbumLoader', 'loadPicturesInAlbum', [albumName, startIndex, count]);
};
