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

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.net.URL;

import static org.assertj.core.api.Assertions.assertThat;

public class SourceTest {
  @Rule public ExpectedException thrown = ExpectedException.none();

  @Test public void domainFaviconsFromDomainOnly() throws Exception {
    Source source = Source.fromUrl("http://example.com").build();

    assertThat(source.getDomainFavicons()).containsOnly(
        new URL("http://example.com/favicon.ico"),
        new URL("http://example.com/favicon.png"),
        new URL("http://example.com/favicon.gif"));
  }

  @Test public void domainFaviconsWithPath() throws Exception {
    Source source = Source.fromUrl("http://example.com/some/thing").build();

    assertThat(source.getDomainFavicons()).containsOnly(
        new URL("http://example.com/favicon.ico"),
        new URL("http://example.com/favicon.png"),
        new URL("http://example.com/favicon.gif"));
  }

  @Test public void malformedUrl() throws Exception {
    thrown.expect(IllegalArgumentException.class);
    Source.fromUrl("hello");
  }
}
