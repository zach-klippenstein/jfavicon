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
import java.net.URL;

final class Utils {
  public static <T> T checkNotNull(T obj, String description) {
    if (obj == null) {
      throw new IllegalArgumentException(description + " cannot be null");
    }
    return obj;
  }

  public static URL parseUrl(String url) {
    try {
      return new URL(url);
    } catch (MalformedURLException e) {
      throw new IllegalArgumentException(e);
    }
  }

  private Utils() {
  }
}
