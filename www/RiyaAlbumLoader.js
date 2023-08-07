var exec = require('cordova/exec');

exports.getAlbums = function(success, error) {
    exec(success, error, 'RiyaAlbumLoader', 'getAlbums', []);
};
