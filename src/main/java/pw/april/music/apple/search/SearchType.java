package pw.april.music.apple.search;

public enum SearchType {
    ACTIVITIES("activities"),
    ALBUMS("albums"),
    APPLE_CURATORS("apple-curators"),
    ARTISTS("artists"),
    CURATORS("curators"),
    MUSIC_VIDEOS("musicVideos"),
    PLAYLISTS("playlists"),
    RECORD_LABELS("record-labels"),
    SONGS("songs"),
    STATIONS("stations");

    private final String value;

    SearchType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
