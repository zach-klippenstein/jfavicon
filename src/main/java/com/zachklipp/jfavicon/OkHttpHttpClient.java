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

import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.net.URL;

/** An {@link HttpClient} backed by OkHttp. */
class OkHttpHttpClient implements HttpClient {
  private final OkHttpClient client;

  public OkHttpHttpClient(OkHttpClient client) {
    this.client = client;
  }

  @Override
  public String tryLoadBody(URL url) {
    String body = null;

    try {
      final Request request = new Request.Builder()
          .url(url)
          .get()
          .build();

      final Response response = client.newCall(request).execute();

      if (response.isSuccessful()) {
        body = response.body().string();
      }
    } catch (IOException e) {
    }

    return body;
  }

  @Override
  public boolean doesResourceExist(URL url) {
    final Request request = new Request.Builder()
        .url(url)
        .head()
        .build();

    try {
      return client.newCall(request).execute().isSuccessful();
    } catch (IOException e) {
      throw new RuntimeException("Error checking for resource at " + url, e);
    }
  }
}
