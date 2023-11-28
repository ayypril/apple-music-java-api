package pw.april.music.apple.dto.artist.relationship;

import com.fasterxml.jackson.annotation.JsonProperty;
import pw.april.music.apple.dto.album.Album;

import java.util.List;

public record ArtistAlbumRelationship(String href, String next,
                                      @JsonProperty("data") List<Album> albums){}