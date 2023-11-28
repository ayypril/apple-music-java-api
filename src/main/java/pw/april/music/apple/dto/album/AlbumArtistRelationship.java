package pw.april.music.apple.dto.album;

import pw.april.music.apple.dto.artist.Artist;

import java.util.List;

public class AlbumArtistRelationship {
    String href;
    String next;
    List<Artist> data;
}
