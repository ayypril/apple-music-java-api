package pw.april.music.apple;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.apache.http.HttpHost;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pw.april.music.apple.exceptions.TooManyRequestsException;
import pw.april.music.apple.requests.MusicRequest;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

public final class HttpHandler {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    private final ObjectMapper mapper;
    private final CloseableHttpClient client;
    private static final HttpHost host = new HttpHost("api.music.apple.com", 443, "https");


    /**
     * Initialize the HttpHandler with a custom ObjectMapper and Client.
     * @param objectMapper custom object mapper
     * @param httpClient custom http client
     */
    public HttpHandler(ObjectMapper objectMapper, CloseableHttpClient httpClient) {
        this.mapper = objectMapper;
        this.client = httpClient;
    }

    /**
     * Initialize the HttpHandler with the default settings.
     */
    public HttpHandler() {
        RequestConfig config = RequestConfig.custom()
                .setConnectTimeout(30 * 1000)
                .setConnectionRequestTimeout(12 * 1000)
                .setSocketTimeout(120 * 1000).build();

        PoolingHttpClientConnectionManager manager
                = new PoolingHttpClientConnectionManager();
        manager.setDefaultMaxPerRoute(250);
        manager.setMaxTotal(500);

        this.mapper = new ObjectMapper()
                .registerModule(new JavaTimeModule())

                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        this.client = HttpClientBuilder.create().disableDefaultUserAgent()
                .setDefaultRequestConfig(config)
                .setConnectionManager(manager)
                .build();
    }

    <T> T doExecute(MusicRequest<T> request, String authToken){
        request.getRequest().setHeader("Authorization", authToken);
        try {
            logger.debug("Executing http request " + request);
            CloseableHttpResponse response = client.execute(host, request.getRequest());

            logger.debug("Got http response: " + response.getStatusLine());
            logger.debug("Response headers: " + Arrays.toString(response.getAllHeaders()));

            int statusCode = response.getStatusLine().getStatusCode();

            if (statusCode == 429)
                throw new TooManyRequestsException();

            InputStream stream = response.getEntity().getContent();

            if (logger.isTraceEnabled()){
                byte[] bytes = stream.readAllBytes();
                var res = StandardCharsets.UTF_8.decode(ByteBuffer.wrap(bytes)).toString();
                logger.trace("Current response: " + res);
                stream = new ByteArrayInputStream(bytes);
            }
            T obj = mapper.reader().readValue(stream, request.getType());
            stream.close();
            response.close();
            return obj;
        } catch (IOException e) {
            logger.error("", e);
            throw new RuntimeException(e);
        }
    }
}
