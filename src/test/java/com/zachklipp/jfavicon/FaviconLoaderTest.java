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

import com.zachklipp.jfavicon.FaviconLoader.FaviconCallback;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import java.net.URL;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

public class FaviconLoaderTest {
  @Test public void example() {
    new FaviconLoader().getFavicons("http://www.wikipedia.com", new FaviconCallback() {
      @Override public void onFaviconsLoaded(Set<URL> favicons) {
        for (URL fav : favicons) {
          System.out.println(fav);
        }
      }
    });
  }

  @Test public void withHtmlWithoutLinksReturnsDomainFavicons() throws Exception {
    HttpClient client = mock(HttpClient.class);
    FaviconLoader loader = new FaviconLoader().setClient(client);

    when(client.doesResourceExist(any(URL.class))).thenReturn(true);

    Source source = Source.fromUrl("http://example.com")
        .withBody("")
        .build();

    Set<URL> favicons = captureFavicons(loader, source);
    assertThat(favicons).contains(new URL("http://example.com/favicon.ico"));
  }

  @Test public void withHtmlWithLinkReturnsOnlyLinksWithoutDomainFavicons() throws Exception {
    Set<URL> favicons = captureFaviconsFromLinks("<link rel=\"shortcut icon\" href=\"/bla.png\"/>");
    assertThat(favicons).containsOnly(new URL("http://example.com/bla.png"));
  }

  @Test public void withHtmlWithLinksReturnsAllLinks() throws Exception {
    Set<URL> favicons = captureFaviconsFromLinks(
        "<link rel=\"shortcut icon\" href=\"/bla.png\"/>",
        "<link rel=\"icon\" href=\"//foobar.com/asdf.ico\" />");

    assertThat(favicons).containsOnly(
        new URL("http://example.com/bla.png"),
        new URL("http://foobar.com/asdf.ico"));
  }

  @Test public void regexNegativeMatch() {
    HttpClient client = mock(HttpClient.class);
    FaviconLoader loader = new FaviconLoader().setClient(client);

    when(client.doesResourceExist(any(URL.class))).thenReturn(false);

    Source source = Source.fromUrl("http://example.com")
        .withBody("<html><head><link rel=\"-icon\" href=\"fail.ico\"/></head></html>")
        .build();

    Set<URL> favicons = captureFavicons(loader, source);
    assertThat(favicons).isEmpty();
  }

  @Test public void withHtmlWithMalformedLinkReturnsDomainFavicons() throws Exception {
    Set<URL> favicons = captureFaviconsFromLinks("<link rel=\"shortcut icon\">/bla.png</link>");
    assertThat(favicons).contains(new URL("http://example.com/favicon.ico")).hasSize(3);
  }

  private static Set<URL> captureFaviconsFromLinks(String... links) {
    HttpClient client = mock(HttpClient.class);
    FaviconLoader loader = new FaviconLoader().setClient(client);

    when(client.doesResourceExist(any(URL.class))).thenReturn(true);

    StringBuilder linkBuilder = new StringBuilder();
    for (String link : links) {
      linkBuilder.append(link);
    }

    Source source = Source.fromUrl("http://example.com")
        .withBody("<html><head>" +
            linkBuilder.toString() +
            "</head></html>")
        .build();

    return captureFavicons(loader, source);
  }

  private static Set<URL> captureFavicons(FaviconLoader loader, Source source) {
    ArgumentCaptor<Set> faviconsCaptor = ArgumentCaptor.forClass(Set.class);
    FaviconCallback callback = mock(FaviconCallback.class);

    loader.getFavicons(source, callback);

    verify(callback).onFaviconsLoaded(faviconsCaptor.capture());
    return faviconsCaptor.getValue();
  }
}
