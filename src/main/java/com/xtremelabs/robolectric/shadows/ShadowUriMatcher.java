package com.xtremelabs.robolectric.shadows;

import android.content.UriMatcher;
import android.net.Uri;

import com.xtremelabs.robolectric.internal.Implementation;
import com.xtremelabs.robolectric.internal.Implements;
import com.xtremelabs.robolectric.internal.RealObject;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Shadow class to mock functionality of {@link UriMatcher} for Robolectric unit tests.
 *
 * @author Chuck Greb <charles.greb@gmail.com>
 */
@Implements(UriMatcher.class)
public class ShadowUriMatcher {

    @RealObject
    private UriMatcher realUriMatcher;

    public static final int NO_MATCH = -1;

    private static final int EXACT = 0;
    private static final int NUMBER = 1;
    private static final int TEXT = 2;
    private int mCode;

    private int mWhich;
    private String mText;
    private ArrayList<ShadowUriMatcher> mChildren = new ArrayList<ShadowUriMatcher>();

    static final Pattern PATH_SPLIT_PATTERN = Pattern.compile("/");

    public void __constructor__(int code) {
        mCode = code;
        mWhich = -1;
        mText = null;
    }

    public void __constructor__() {
        mCode = NO_MATCH;
        mWhich = -1;
        mText = null;
    }

    @Implementation
    public int match(Uri uri) {
        final List<String> pathSegments = uri.getPathSegments();
        final int li = pathSegments.size();

        ShadowUriMatcher node = this;

        if (li == 0 && uri.getAuthority() == null) {
            return this.mCode;
        }

        for (int i = -1; i < li; i++) {
            String u = i < 0 ? uri.getAuthority() : pathSegments.get(i);
            ArrayList<ShadowUriMatcher> list = node.mChildren;
            if (list == null) {
                break;
            }
            node = null;
            int lj = list.size();
            for (int j = 0; j < lj; j++) {
                ShadowUriMatcher n = list.get(j);
                which_switch:
                switch (n.mWhich) {
                    case EXACT:
                        if (n.mText.equals(u)) {
                            node = n;
                        }
                        break;
                    case NUMBER:
                        int lk = u.length();
                        for (int k = 0; k < lk; k++) {
                            char c = u.charAt(k);
                            if (c < '0' || c > '9') {
                                break which_switch;
                            }
                        }
                        node = n;
                        break;
                    case TEXT:
                        node = n;
                        break;
                }
                if (node != null) {
                    break;
                }
            }
            if (node == null) {
                return NO_MATCH;
            }
        }

        return node.mCode;
    }

    @Implementation
    public void addURI(String authority, String path, int code) {
        if (code < 0) {
            throw new IllegalArgumentException("code " + code + " is invalid: it must be positive");
        }

        String[] tokens = path != null ? PATH_SPLIT_PATTERN.split(path) : null;
        int numTokens = tokens != null ? tokens.length : 0;
        ShadowUriMatcher node = this;

        for (int i = -1; i < numTokens; i++) {
            String token = i < 0 ? authority : tokens[i];
            ArrayList<ShadowUriMatcher> children = node.mChildren;
            int numChildren = children.size();
            ShadowUriMatcher child;
            int j;
            for (j = 0; j < numChildren; j++) {
                child = children.get(j);
                if (token.equals(child.mText)) {
                    node = child;
                    break;
                }
            }

            if (j == numChildren) {
                // Child not found, create it
                child = new ShadowUriMatcher();
                if (token.equals("#")) {
                    child.mWhich = NUMBER;
                } else if (token.equals("*")) {
                    child.mWhich = TEXT;
                } else {
                    child.mWhich = EXACT;
                }
                child.mText = token;
                node.mChildren.add(child);
                node = child;
            }
        }
        node.mCode = code;
    }

}
