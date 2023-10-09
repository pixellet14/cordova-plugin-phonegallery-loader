<h2>Cordova Phone Gallery Display Plugin</h2>
<p>A simple plugin to display saved albums from your device, along with the images within them.</p>

<h5>Installation</h5>
<p></p>cordova plugin add https://github.com/pixellet14/cordova-plugin-phonegallery-display.git

OR 

Npmjs (NPM Registry)

cordova plugin add cordova-plugin-phonegallery-display</p>


<h4>Sample Usage</h4>
This sample demonstrates how the plugin can be called and operated:


        function gallerySearch() {
            // Load the albums
          cordova.plugins.RiyaAlbumLoader.loadAlbums(function(albums) {
                    // Process the returned albums
                displayAlbums(albums);
            }, function(error) {
                // Handle any errors
                console.error("Error loading albums:", error);
            });
        }

    function displayAlbums(albums) {    
      albums.forEach(function(album) {
            let div = $("<div/>").addClass("gifcubes").text(album.name);
            let img = $("<img/>").attr("src", album.thumbnailPath).addClass("album-thumbnail");
            div.prepend(img); // Display album thumbnail
            div.click(function() {
                // Load pictures of the clicked album
                loadPicturesFromAlbum(album.name);
            });
           document.getElementById("gifcontent1").append(div);
        });
    }

    function loadPicturesFromAlbum(albumName) {
        cordova.plugins.RiyaAlbumLoader.loadPicturesInAlbum(albumName, function(pictures) {
            displayPictures(pictures);
        }, function(error) {
            // Handle any errors
            console.error("Error loading pictures:", error);
        });
    }

    function displayPictures(pictures) {
             document.getElementById("gifcontent1").empty();
            let selectedPictures = []; // To keep track of selected pictures
        
            pictures.forEach(function(picturePath) {
                // Convert the native path into a usable URL for the WebView
                window.resolveLocalFileSystemURL(picturePath, function(fileEntry) {
                    fileEntry.file(function(file) {
                        let reader = new FileReader();
                        reader.onloadend = function() {
                            // This blobURL can be used directly as the src in the img tag
                            let blobURL = this.result;
        
                            let div = $("<div/>").addClass("gifcubes");
                            let img = $("<img/>").attr("src", blobURL).addClass("gifCubeImg").attr("data-long-press-delay", "1000");
                            let selectionIndicator = $("<div/>").addClass("selection-indicator").css("display", "none");
        
                            div.append(img);
                            div.append(selectionIndicator);
                            div.click(function() {
                                if (selectedPictures.length < 5) { // Allow selection of up to 5 pictures
                                    selectionIndicator.show();
                                    selectedPictures.push(picturePath);
                                } else {
                                    showToastCustom('You can select only up to 5 pictures at a time.');
                                }
                            });
                            $("#gifcontent1").append(div);
                        };
                        // Convert the file into a blob URL
                        reader.readAsDataURL(file);
                    });
                }, function(error) {
                    console.error("Error resolving file path:", error);
                });
            });
        }


<h5>It is always advisable to use cordova's sqlite plugin in combination to first fetch and store the pictures in the local DB before displaying them inside any element. this plugin has been tested across all the Android devices from version 9 to 13 and it has been found that android 9,10 and 11 apps crash while displaying pictures from the albums having a large volume of pictures. By using DB, you will be able to even display them using pagination / lazy load logics.<h5>
