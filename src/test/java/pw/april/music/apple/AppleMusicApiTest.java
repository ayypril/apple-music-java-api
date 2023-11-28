package pw.april.music.apple;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pw.april.music.apple.dto.artist.ArtistsResponse;
import pw.april.music.apple.dto.search.SearchResponse;
import pw.april.music.apple.requests.ArtistRequest;
import pw.april.music.apple.requests.SearchRequest;
import pw.april.music.apple.search.SearchType;

import java.io.IOException;
import java.time.Duration;
import java.util.Properties;

import static org.junit.jupiter.api.Assertions.*;

class AppleMusicApiTest {
    private static String token;
    private static String teamId;
    private static String keyId;
    private AppleMusicApi music;

    @BeforeAll
    static void staticSetup(){

        Properties p = new Properties();
        try {
            p.load(AppleMusicApiTest.class.getClassLoader().getResourceAsStream("config.properties"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        token = p.getProperty("apple-music-token");
        teamId = p.getProperty("apple-developer-team-id");
        keyId = p.getProperty("apple-music-key-id");
    }

    @BeforeEach
    void setUp() {
        music = new AppleMusicApi.Builder().keyId(keyId).token(token).
                developerTeamId(teamId).tokenTTL(Duration.ofDays(180)).build();
    }

    @Test
    void testMusicSearch1() {
        var req = new SearchRequest().addSearchType(SearchType.ARTISTS)
                .setLimit(3)
                .setTerm("Drake").build();

        SearchResponse results = music.execute(req);
        results.getArtists().forEach(System.out::println);
    }

    @Test
    void getArtistById(){
        var req = new ArtistRequest("580391756").build();
        ArtistsResponse res = music.execute(req);
        System.out.println(res.getArtist());

        req = new ArtistRequest("1349818950").build();
        res = music.execute(req);
        System.out.println(res.getArtist());
    }

    @Test
    void whenBuilderUnspecifiedKey_expectException(){
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            new AppleMusicApi.Builder()
                    .token(token)
                    .developerTeamId(teamId)
                    .tokenTTL(Duration.ofDays(30)).build();
        });
        String expected = "Key ID must not be blank";
        assertTrue(exception.getMessage().contains(expected));
    }

    @Test
    void whenBuilderUnspecifiedToken_expectException(){
        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                new AppleMusicApi.Builder().keyId(keyId).developerTeamId(teamId)
                .tokenTTL(Duration.ofHours(4)).build());

        String expected = "Token must not be blank";
        assertTrue(exception.getMessage().contains(expected));
    }

    @Test
    void whenBuilderUnspecifiedTeamId_expectException(){
        Exception exception = assertThrows(IllegalArgumentException.class, () ->
            new AppleMusicApi.Builder().keyId(keyId).token(token)
                    .tokenTTL(Duration.ofHours(4)).build());
        String expected = "Team ID must not be blank";
        assertTrue(exception.getMessage().contains(expected));
    }

    /**
     * This test should pass:
     *  when the duration is unspecified, we default to 4 hours.
     */
    @Test
    void whenBuilderUnspecifiedDuration_expectNoException(){
        assertDoesNotThrow(() ->
                new AppleMusicApi.Builder()
                        .token(token)
                        .keyId(keyId).developerTeamId(teamId)
                        .build());
    }

    @Test
    void whenBuilderInvalidDuration_expectException(){
        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                new AppleMusicApi.Builder()
                        .token(token)
                        .keyId(keyId).developerTeamId(teamId)
                        .tokenTTL(Duration.ZERO).build());

        String expected = "Invalid duration";
        assertTrue(exception.getMessage().contains(expected));
    }


    @AfterEach
    void tearDown() {
    }
}