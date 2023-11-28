package pw.april.music.apple.dto.artist;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;
import java.util.Optional;

public record ArtistsResponse(@JsonProperty("data") List<Artist> artists){
    public Optional<Artist> getArtist(){
        if (artists.isEmpty())
            return Optional.empty();
        return Optional.of(artists.get(0));
    }
}