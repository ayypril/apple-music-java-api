package pw.april.music.apple.exceptions;

public class TooManyRequestsException extends MusicApiException {

    public TooManyRequestsException() {
        super("Too many requests have seen sent to the API recently. Try again later.");
    }
}
