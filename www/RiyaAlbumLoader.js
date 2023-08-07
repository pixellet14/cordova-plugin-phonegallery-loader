var exec = require('cordova/exec');
exports.initialize = function(successCallback, errorCallback) {
    // You can place any initialization code here
    successCallback();
};

exports.loadAlbums = function(success, error) {
    exec(success, error, 'RiyaAlbumLoader', 'loadAlbums', []);
};

exports.loadPicturesInAlbum = function(albumName, success, error) {
    exec(success, error, 'RiyaAlbumLoader', 'loadPicturesInAlbum', [albumName]);
};
