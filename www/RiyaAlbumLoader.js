var exec = require('cordova/exec');

exports.loadAlbums = function(success, error) {
    exec(success, error, 'RiyaAlbumLoader', 'loadAlbums', []);
};

exports.loadPicturesInAlbum = function(albumName, success, error) {
    exec(success, error, 'RiyaAlbumLoader', 'loadPicturesInAlbum', [albumName]);
};
