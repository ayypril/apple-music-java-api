package pw.april.music.apple.requests;

import org.apache.http.HttpRequest;

public interface MusicRequest<T> {

    Class<T> getType();

    HttpRequest getRequest();
}
