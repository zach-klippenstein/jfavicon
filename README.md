# JFavicon [![Build Status](https://travis-ci.org/zach-klippenstein/jfavicon.svg)](https://travis-ci.org/zach-klippenstein/jfavicon)

JFavicon is a simple library for finding the URLs of [favicons](http://en.wikipedia.org/wiki/Favicon).

The correct way for a page to define its favicon is with a link element in the head. However, not all pages do this,
so if there are no link elements, the root is checked for files like `favicon.ico` (see
`Source` for which extensions are checked).

    new FaviconLoader().getFavicons("http://www.wikipedia.com", new FaviconCallback() {
      @Override public void onFaviconsLoaded(Set<URL> favicons) {
        for (URL fav : favicons) {
          System.out.println(fav);
        }
      }
    });
