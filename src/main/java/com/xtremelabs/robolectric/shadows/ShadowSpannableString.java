package com.xtremelabs.robolectric.shadows;

import android.text.SpannableString;

import com.xtremelabs.robolectric.internal.Implementation;
import com.xtremelabs.robolectric.internal.Implements;

@Implements(SpannableString.class)
public class ShadowSpannableString {

    private CharSequence charSequence;

    public void __constructor__(CharSequence charSequence) {
        this.charSequence = charSequence;
    }

    @Override @Implementation
    public String toString() {
        return charSequence.toString();
    }

    @Implementation
    public static SpannableString valueOf(CharSequence charSequence) {
        if (charSequence instanceof SpannableString) {
            return (SpannableString) charSequence;
        }
        return new SpannableString(charSequence);
    }
}
