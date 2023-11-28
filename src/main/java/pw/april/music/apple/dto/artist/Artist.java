package pw.april.music.apple.dto.artist;

import pw.april.music.apple.dto.artist.relationship.ArtistRelationship;

/**
 * A resource object that represents an artist of an album where an artist can be one or more persons.
 */
public record Artist(String id, String type, String href,
                     ArtistAttributes attributes, ArtistRelationship relationships){}
