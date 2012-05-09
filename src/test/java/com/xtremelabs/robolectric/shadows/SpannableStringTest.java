package com.xtremelabs.robolectric.shadows;

import android.text.SpannableString;

import com.xtremelabs.robolectric.WithTestDefaultsRunner;

import org.junit.Test;
import org.junit.runner.RunWith;

import static junit.framework.Assert.assertSame;
import static org.junit.Assert.assertEquals;

@RunWith(WithTestDefaultsRunner.class)
public class SpannableStringTest {
    @Test
    public void toString_shouldDelegateToUnderlyingCharSequence() {
        SpannableString spannedString = new SpannableString("foo");
        assertEquals("foo", spannedString.toString());
    }

    @Test
    public void valueOfSpannedString_shouldReturnItself() {
        SpannableString spannedString = new SpannableString("foo");
        assertSame(spannedString, SpannableString.valueOf(spannedString));
    }

    @Test
    public void valueOfCharSequence_shouldReturnNewSpannedString() {
        assertEquals("foo", SpannableString.valueOf("foo").toString());
    }
}
