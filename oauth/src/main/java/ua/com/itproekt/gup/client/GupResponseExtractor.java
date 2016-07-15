package ua.com.itproekt.gup.client;

import ua.com.itproekt.gup.exception.RestError;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.util.Assert;
import org.springframework.web.client.ResponseExtractor;
import org.springframework.web.client.RestClientException;

import java.io.IOException;
import java.util.List;

@SuppressWarnings("rawtypes")
public class GupResponseExtractor implements ResponseExtractor<Response> {
    private final Class<?>                           responseType;
    private final List<HttpMessageConverter<?>> messageConverters;
    private HttpMessageConverter                  stringConverter;
    private boolean                                      passThru = false;

    public GupResponseExtractor(Class<?> responseType, List<HttpMessageConverter<?>> messageConverters) {
        Assert.notNull(responseType, "'responseType' must not be null");
        Assert.notEmpty(messageConverters, "'messageConverters' must not be empty");
        this.responseType      = responseType;
        this.messageConverters = messageConverters;
    }

    public GupResponseExtractor(Class<?> responseType, List<HttpMessageConverter<?>> messageConverters, boolean passThru) {
        Assert.notNull(responseType, "'responseType' must not be null");
        Assert.notEmpty(messageConverters, "'messageConverters' must not be empty");
        this.responseType      = responseType;
        this.messageConverters = messageConverters;
        this.passThru          = passThru;
        for (HttpMessageConverter converter : messageConverters) {
            if (converter instanceof org.springframework.http.converter.StringHttpMessageConverter) {
                this.stringConverter = converter;
                break;
            }
        }
    }

    @Override
    public Response extractData(ClientHttpResponse response) throws IOException {
        Response pngResponse = new Response();
        if (response.getStatusCode() == HttpStatus.OK) {
            pngResponse.setResponse(extractResponseData(response));
            pngResponse.setFoundError(false);
        } else {
            pngResponse.setErrResponse(extractErrorData(response));
            pngResponse.setFoundError(true);
        }
        return pngResponse;
    }

    @SuppressWarnings("unchecked")
    private Object extractResponseData(ClientHttpResponse response) throws IOException {
        MediaType contentType = response.getHeaders().getContentType();

        if (contentType == null) {
            throw new RestClientException("Cannot extract response: no Content-Type found");
        }

        if (passThru && stringConverter != null) {
            return stringConverter.read(this.responseType, response);
        } else {
            for (HttpMessageConverter messageConverter : messageConverters) {
                if (messageConverter.canRead(responseType, contentType)) {
                    return messageConverter.read(this.responseType, response);
                }
            }
        }

        throw new RestClientException(
                "Could not extract response: no suitable HttpMessageConverter found for response type [" +
                        this.responseType.getName() + "] and content type [" + contentType + "]");
    }

    @SuppressWarnings("unchecked")
    private RestError extractErrorData(ClientHttpResponse response) throws IOException {
        MediaType contentType = response.getHeaders().getContentType();
        for (HttpMessageConverter messageConverter : messageConverters) {
            if (messageConverter.canRead(RestError.class, contentType)) {
                return (RestError) messageConverter.read(RestError.class, response);
            }
        }
        RestError restErr = new RestError();
        return restErr;
    }
}
