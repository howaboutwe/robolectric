package com.xtremelabs.robolectric.shadows;

import android.content.Context;
import android.text.format.DateFormat;

import com.xtremelabs.robolectric.internal.Implementation;
import com.xtremelabs.robolectric.internal.Implements;

/**
 * Shadow of {@link DateFormat} class.
 *
 * @author Chuck Greb <charles.greb@gmail.com>
 */
@Implements(DateFormat.class)
public class ShadowDateFormat {

    public static final char DATE = 'd';
    public static final char MONTH = 'M';
    public static final char YEAR = 'y';

	@Implementation
	public final static java.text.DateFormat getDateFormat(Context context) {
		return java.text.DateFormat.getDateInstance();
	}

    @Implementation
    public static char[] getDateFormatOrder(Context context) {
        char[] order = new char[] { DATE, MONTH, YEAR };
        String value = getDateFormatString(context);
        int index = 0;
        boolean foundDate = false;
        boolean foundMonth = false;
        boolean foundYear = false;

        for (char c : value.toCharArray()) {
            if (!foundDate && (c == DATE)) {
                foundDate = true;
                order[index] = DATE;
                index++;
            }

            if (!foundMonth && (c == MONTH)) {
                foundMonth = true;
                order[index] = MONTH;
                index++;
            }

            if (!foundYear && (c == YEAR)) {
                foundYear = true;
                order[index] = YEAR;
                index++;
            }
        }
        return order;
    }

    @Implementation
    private static String getDateFormatString(Context context) {
        return "%s/%s/%s";
    }

}
