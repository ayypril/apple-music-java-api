package pw.april.music.apple;

import pw.april.music.apple.requests.MusicRequest;

import java.time.Duration;
import java.time.temporal.ChronoUnit;

public class AppleMusicApi {
    private final AuthToken authToken;
    private final HttpHandler handler;

    public AuthToken getAuthTokenHolder() {
        return authToken;
    }

    public String getAuthTokenJwt(){
        return authToken.getToken();
    }

    private AppleMusicApi(AuthToken token, HttpHandler handler){
        this.authToken = token;
        this.handler = handler;
    }

    public <T> T execute(MusicRequest<T> musicRequest){
        return handler.doExecute(musicRequest, authToken.getBearerToken());
    }


     public static class Builder {
        private String appleMusicToken;
        private String appleDeveloperTeamId;
        private String appleMusicKeyId;
        private Duration tokenTTL = Duration.of(4, ChronoUnit.HOURS);
        private HttpHandler handler;

        public Builder token(String token){
            this.appleMusicToken = token;
            return this;
        }

        public Builder developerTeamId(String developerTeamId){
            this.appleDeveloperTeamId = developerTeamId;
            return this;
        }

        public Builder keyId(String keyId){
            this.appleMusicKeyId = keyId;
            return this;
        }

        public Builder tokenTTL(Duration tokenTTL){
            this.tokenTTL = tokenTTL;
            return this;
        }

         public Builder handler(HttpHandler handler) {
             this.handler = handler;
             return this;
         }

         public AppleMusicApi build(){
            if (handler == null)
                this.handler = new HttpHandler();

            if(appleMusicToken == null || appleMusicToken.isBlank())
                throw new IllegalArgumentException("Token must not be blank");
            if(appleDeveloperTeamId == null || appleDeveloperTeamId.isBlank())
                throw new IllegalArgumentException("Team ID must not be blank");
            if(appleMusicKeyId == null || appleMusicKeyId.isBlank())
                throw new IllegalArgumentException("Key ID must not be blank");
            if(tokenTTL == null || tokenTTL.isNegative() || tokenTTL.isZero() || tokenTTL.compareTo(Duration.ofDays(180)) > 0)
                throw new IllegalArgumentException("Invalid duration: Must be greater than 0 seconds, but less than 180 days.");

            return new AppleMusicApi(new AuthToken(appleMusicToken, appleDeveloperTeamId, appleMusicKeyId, tokenTTL), handler);
        }
        
    }

}