package com.codingbucket.linkshare;

import android.content.Intent;

public interface BaseLinkHandler {
    String loadLink();
    boolean sendLink(String text);

}
