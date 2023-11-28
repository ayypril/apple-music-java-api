package pw.april.music.apple.requests;

import org.apache.http.HttpRequest;

public class MusicRequestImpl<T> implements MusicRequest<T> {
    private final Class<T> clazz;
    private final HttpRequest request;

    protected MusicRequestImpl(Class<T> clazz, HttpRequest request) {
        // class needs to be set due to type erasure
        this.clazz = clazz;
        this.request = request;
    }

    @Override
    public Class<T> getType() {
        return clazz;
    }

    @Override
    public HttpRequest getRequest() {
        return request;
    }

    @Override
    public String toString() {
        return "MusicRequestImpl{" +
                "clazz=" + clazz +
                ", request=" + request +
                '}';
    }
}
