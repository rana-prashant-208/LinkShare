package com.codingbucket.linkshare;

import android.content.Intent;

public interface BaseLinkHandler {
    String loadLink(String host);
    boolean sendLink(String text,String host);

}
