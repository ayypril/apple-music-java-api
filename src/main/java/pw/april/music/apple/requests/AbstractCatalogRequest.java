package pw.april.music.apple.requests;

import com.neovisionaries.i18n.CountryCode;

public abstract class AbstractCatalogRequest {
    private static final String BASE_URI = "/v1/catalog/";
    private final CountryCode storefront;

    /**
     * The base URI with the storefront and a trailing slash, e.g. /v1/catalog/us/
     */
    protected final String baseUri;


    protected AbstractCatalogRequest(CountryCode storefront) {
        this.storefront = storefront;
        this.baseUri = BASE_URI + storefront.getAlpha2().toLowerCase() + "/";
    }

    protected AbstractCatalogRequest() {
        this(CountryCode.US);
    }

    abstract MusicRequest<?> build();

}
