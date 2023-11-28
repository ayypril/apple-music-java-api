package pw.april.music.apple.dto.album;


import pw.april.music.apple.dto.Artwork;
import pw.april.music.apple.dto.EditorialNotes;
import pw.april.music.apple.dto.PlayParameters;

import java.util.List;

/**
 * The attributes for an album resource.<br>
 * <a href="https://developer.apple.com/documentation/applemusicapi/albums/attributes">Documentation</a>
 */
public class AlbumAttributes {
    String artistName;
    String artistUrl;
    Artwork artwork;
    // RIAA rating, "clean", "explicit", or null for no rating.
    String contentRating;
    String copyright;
    EditorialNotes editorialNotes;
    List<String> genreNames;
    boolean isCompilation;
    boolean isComplete;
    boolean isMasteredForItunes;
    boolean isSingle;
    String name;
    PlayParameters playParams;
    String recordLabel;
    String releaseDate;
    int trackCount;
    String upc;
    String url;
}
