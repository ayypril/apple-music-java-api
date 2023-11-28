package pw.april.music.apple.dto.artist;

import pw.april.music.apple.dto.EditorialNotes;

import java.util.List;

/**
 * The attributes for an artist resource.
 */
public record ArtistAttributes(EditorialNotes editorialNotes,
                               List<String> genreNames, String name, String url){}