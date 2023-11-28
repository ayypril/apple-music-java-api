package pw.april.music.apple.dto.search;

import pw.april.music.apple.dto.artist.Artist;

import java.util.List;

public class SearchResponse {
    private SearchResults results;

    public List<Artist> getArtists(){
        if (results.artists() != null)
            return results.artists().artists();
        return null;
    }

    private void setResults(SearchResults results) {
        this.results = results;
    }

    public SearchResults getData() {
        return results;
    }
}
