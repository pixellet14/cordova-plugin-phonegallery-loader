This is an experimental plugin. Use it at your own risk. It loads list of albums in your phone and once the album items are clicked it can be used to display pictures in them. Look at a sample code below

document.addEventListener('deviceready', function() {
    // To load the albums
    cordova.plugins.RiyaAlbumLoader.loadAlbums(function(albums) {
        // albums is a JSON array containing album names
        // You can process it to create your list
    }, function(error) {
        // Handle error
    });
    // Attach a click event to each album item. When an album is clicked,

    
    // load the pictures in the album
$(document).on('click', '.album-item', function() {
        var albumName = $(this).data('albumName'); // assuming you stored album name in a data attribute
        cordova.plugins.RiyaAlbumLoader.loadPicturesInAlbum(albumName, function(pictures) {
            // pictures is a JSON array containing file paths of pictures in the album
            // You can process it to create your grid
        }, function(error) {
            // Handle error
        });
    });
});
