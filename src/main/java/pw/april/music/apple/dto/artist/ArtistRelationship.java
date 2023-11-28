package pw.april.music.apple.dto.artist;

public enum ArtistRelationship {
    ALBUMS("albums"),
    GENRES("genres"),
    MUSIC_VIDEOS("music-videos"),
    PLAYLISTS("playlists"),
    STATION("station");

    private final String value;

    ArtistRelationship(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
