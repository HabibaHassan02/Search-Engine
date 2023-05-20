package com.SseApplication;
import java.net.URI;
import java.net.URISyntaxException;
public class UrlNormalizer {
    public static String normalize(String url) {
        String normalizedUrl = url;

            try {
                URI uri = new URI(url);
              //  normalizedUrl = uri.getScheme() + "://" + uri.getHost() + uri.getPath();
                normalizedUrl= uri.normalize().toString();
            } catch (URISyntaxException e) {

        }

//
//        normalizedUrl = normalizedUrl.replaceAll("#.*$", "");
//        normalizedUrl = normalizedUrl.replaceAll("\\?.*$", "");
//        normalizedUrl = normalizedUrl.replaceAll("/$", "");
      // normalizedUrl = normalizedUrl.toLowerCase();
//        normalizedUrl = normalizedUrl.replaceAll("/{2,}", "/");
//        normalizedUrl = normalizedUrl.replaceAll("\\\\", "/");

        return normalizedUrl;
    }
}