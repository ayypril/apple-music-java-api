package pw.april.music.apple.requests;

import com.neovisionaries.i18n.CountryCode;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.client.utils.URIBuilder;
import pw.april.music.apple.dto.search.SearchResponse;
import pw.april.music.apple.search.SearchType;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

/**
 * <h1>Search for Catalog Resources</h1>
 *
 * @see <a href="https://developer.apple.com/documentation/applemusicapi/search_for_catalog_resources">https://developer.apple.com/documentation/applemusicapi/search_for_catalog_resources</a>
 */
public class SearchRequest extends AbstractCatalogRequest {
    /**
     * The entered text for the search with ‘+’ characters between each word,
     * to replace spaces (for example term=james+br).
     */
    private String term;
    private int limit = 15;
    private String offset;
    private List<String> types = new ArrayList<>();

    private static String cleanTerm(String str){
        if (str == null || str.isEmpty())
            throw new IllegalArgumentException("Term must be non-null and not empty.");
        return str.replace(' ', '+');
    }

    public SearchRequest(CountryCode storefront) {
        super(storefront);
    }

    public SearchRequest onlyArtists(){
        types.clear();
        types.add("artists");
        return this;
    }

    public SearchRequest addSearchType(SearchType type){
        types.add(type.getValue());
        return this;
    }

    public SearchRequest() {
        super();
    }

    public String getTerm() {
        return term;
    }

    public SearchRequest setTerm(String term) {
        this.term = cleanTerm(term);
        return this;
    }

    public int getLimit() {
        return limit;
    }

    public SearchRequest setLimit(int limit) {
        this.limit = limit;
        return this;
    }

    public String getOffset() {
        return offset;
    }

    public SearchRequest setOffset(String offset) {
        this.offset = offset;
        return this;
    }

    private URI getUri(){
        try {
            URIBuilder uriBuilder = new URIBuilder(super.baseUri + "search")
                    .addParameter("term", term)
                    .addParameter("types", String.join(",", types))
                    .addParameter("limit", String.valueOf(limit));
            if (offset != null)
                uriBuilder.addParameter("offset", offset);
            return uriBuilder.build();

        } catch (URISyntaxException ex){
            throw new IllegalArgumentException();
        }
    }


    public MusicRequest<SearchResponse> build() {
        return new MusicRequestImpl<>(SearchResponse.class, RequestBuilder.get()
                .setUri(this.getUri()).build());
    }

}
