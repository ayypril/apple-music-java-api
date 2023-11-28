package pw.april.music.apple.requests;

import com.neovisionaries.i18n.CountryCode;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.client.utils.URIBuilder;
import pw.april.music.apple.dto.artist.ArtistsResponse;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

public class ArtistRequest extends AbstractCatalogRequest {
    private String id;
    private final List<String> views = new ArrayList<>();

    public ArtistRequest(String id, CountryCode storefront) {
        super(storefront);
        this.id = id;
    }

    public ArtistRequest(String id) {
        super();
        this.id = id;
    }

    public ArtistRequest addView(String view){
        views.add(view);
        return this;
    }


    private URI getUri(){
        try {
            URIBuilder uriBuilder = new URIBuilder(super.baseUri + "artists/" + id);
            if (!views.isEmpty())
                uriBuilder.addParameter("views", String.join(",", views));

            uriBuilder.addParameter("format[resources]", "map");
            return uriBuilder.build();
        } catch (URISyntaxException ex){
            throw new IllegalArgumentException();
        }
    }

    public MusicRequest<ArtistsResponse> build() {
        return new MusicRequestImpl<>(ArtistsResponse.class, RequestBuilder.get()
                .setUri(this.getUri()).build());
    }
}
