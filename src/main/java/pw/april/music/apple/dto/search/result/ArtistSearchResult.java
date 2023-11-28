package pw.april.music.apple.dto.search.result;

import com.fasterxml.jackson.annotation.JsonProperty;
import pw.april.music.apple.dto.artist.Artist;

import java.util.List;

public record ArtistSearchResult(@JsonProperty("data") List<Artist> artists,
                                 String href, String next){}