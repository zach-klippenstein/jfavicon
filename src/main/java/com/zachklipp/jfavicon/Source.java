/*
Copyright 2014 Zach Klippenstein

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
*/

package com.zachklipp.jfavicon;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.HashSet;
import java.util.Set;

import static com.zachklipp.jfavicon.Utils.checkNotNull;
import static com.zachklipp.jfavicon.Utils.parseUrl;

/**
 * A thin wrapper around a {@link java.net.URL} for a webpage.
 */
class Source {
  private static final String[] DOMAIN_FAVICON_PATHS = new String[]{
      "/favicon.ico",
      "/favicon.png",
      "/favicon.gif"
  };

  public static Builder fromUrl(String url) {
    return new Builder(parseUrl(url));
  }

  public static Builder fromUrl(URL url) {
    return new Builder(url);
  }

  public static class Builder {
    private final URL url;
    private String body;

    private Builder(URL url) {
      this.url = checkNotNull(url, "url");
    }

    public Builder withBody(String html) {
      this.body = html;
      return this;
    }

    public Source build() {
      return new Source(url, body);
    }
  }

  private final URL url;
  private final String body;

  private Source(URL url, String body) {
    this.url = url;
    this.body = body;
  }

  public URL getUrl() {
    return url;
  }

  public String getBody() {
    return body;
  }

  /**
   * Returns a list of URLs created by appending common favicon filenames to the root of the page URL.
   * E.g. {@code http://example.com/index.html} will give {@code http://example.com/favicon.ext} where {@code ext} is
   * one of:
   * <ul>
   *   <li>ico</li>
   *   <li>png</li>
   *   <li>gif</li>
   * </ul>
   */
  public Set<URL> getDomainFavicons() {
    final Set<URL> favicons = new HashSet<>();
    URI pageUri;

    try {
      pageUri = url.toURI();
    } catch (URISyntaxException e) {
      throw new IllegalArgumentException(e);
    }

    try {
      for (String path : DOMAIN_FAVICON_PATHS) {
        favicons.add(pageUri.resolve(path).toURL());
      }
    } catch (MalformedURLException e) {
      throw new IllegalArgumentException(e);
    }

    return favicons;
  }
}
