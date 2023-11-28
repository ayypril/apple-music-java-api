package pw.april.music.apple;

import io.jsonwebtoken.JwsHeader;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.Hex;
import org.apache.http.message.BasicHeader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

public class AuthToken {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    private final String token;
    private final String developerTeamId;
    private final String keyId;
    private final Duration tokenTTL;
    private final PrivateKey privateKey;

    private String generatedToken;
    private Instant tokenExpiresAt;

    protected AuthToken(String token, String developerTeamId, String keyId, Duration tokenTTL) {
        this.token = token;
        this.developerTeamId = developerTeamId;
        this.keyId = keyId;
        this.tokenTTL = tokenTTL;
        this.privateKey = getPrivateKey();
    }

    public BasicHeader getAuthHeader(){
        return new BasicHeader("Authorization", "Bearer " + getToken());
    }

    public static class Builder {
        private String token;
        private String teamId;
        private String keyId;
        private Duration tokenTTL;

        public Builder token(String token){
            this.token = token;
            return this;
        }
        public Builder teamId(String teamId){
            this.teamId = teamId;
            return this;
        }
        public Builder keyId(String keyId){
            this.keyId = keyId;
            return this;
        }
        public Builder tokenTTL(Duration ttl){
            this.tokenTTL = ttl;
            return this;
        }
        public AuthToken build(){
            return new AuthToken(token, teamId, keyId, tokenTTL);
        }
    }


    /**
     * Gets the bearer token.
     * @return String with 'Bearer ' before it.
     */
    public String getBearerToken(){
        return "Bearer " + getToken();
    }

    /**
     * Gets the signed JWT. Checks to see if we have an already generated one, and if it hasn't expired, return it.
     * @return the JWT (could be newly generated or a previous, non-expired one)
     */
    public String getToken(){
        logger.debug("Getting token. Is generated token null? [" + (generatedToken == null) + "]. Token expiration: " + tokenExpiresAt);

        if(generatedToken != null && tokenExpiresAt.isAfter(Instant.now().plus(1, ChronoUnit.HOURS))){
            return generatedToken;
        }
        this.generatedToken = doGenerateToken();
        logger.debug("generatedToken is " + generatedToken);


        System.out.println(generatedToken);
        this.tokenExpiresAt = Instant.from(Instant.now().plus(tokenTTL));
        return generatedToken;
    }

    /**
     * Generates the JWT.
     * @return signed, valid JWT
     */
    private String doGenerateToken() {
        return Jwts.builder()
                .setHeaderParam(JwsHeader.KEY_ID, keyId)
                .setIssuer(developerTeamId)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + tokenTTL.toMillis()))
                // https://developer.apple.com/documentation/applemusicapi/generating_developer_tokens
                .signWith(privateKey).compact();
    }

    /**
     * Gets the private key from the appleMusicToken.
     * Used for first-time initialization.
     * @return PrivateKey of Apple Music token.
     */
    private PrivateKey getPrivateKey() {
        String key;
        try {
            key = new String(Hex.decodeHex(token))
                    .replace("-----BEGIN PRIVATE KEY-----", "")
                    .replace("-----END PRIVATE KEY-----", "")
                    .replaceAll("\\s+", "");
        } catch (DecoderException e){
            throw new IllegalArgumentException("Failed to decode appleMusicToken");
        }
        PKCS8EncodedKeySpec pkcs8EncodedKeySpec = new PKCS8EncodedKeySpec(new Base64().decode(key));
        PrivateKey privateKey;
        try {
            privateKey = KeyFactory.getInstance("EC").generatePrivate(pkcs8EncodedKeySpec);
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e){
            throw new IllegalArgumentException("Cannot sign key", e);
        }
        return privateKey;
    }
}