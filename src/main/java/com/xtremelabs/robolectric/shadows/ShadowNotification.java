package com.xtremelabs.robolectric.shadows;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.widget.RemoteViews;

import com.xtremelabs.robolectric.internal.Implementation;
import com.xtremelabs.robolectric.internal.Implements;
import com.xtremelabs.robolectric.internal.RealObject;

@SuppressWarnings({"UnusedDeclaration"})
@Implements(Notification.class)
public class ShadowNotification {

    public Notification getRealNotification() {
        return realNotification;
    }

    @RealObject
    Notification realNotification;

    private LatestEventInfo latestEventInfo;

    public void __constructor__(int icon, CharSequence tickerText, long when) {
        realNotification.icon = icon;
        realNotification.tickerText = tickerText;
        realNotification.when = when;
    }

    @Implementation
    public void setLatestEventInfo(Context context, CharSequence contentTitle,
                                   CharSequence contentText, PendingIntent contentIntent) {
        latestEventInfo = new LatestEventInfo(contentTitle, contentText, contentIntent);
        realNotification.contentIntent = contentIntent;
    }

    public LatestEventInfo getLatestEventInfo() {
        return latestEventInfo;
    }

    public static class LatestEventInfo {
        private final CharSequence contentTitle;
        private final CharSequence contentText;
        private final PendingIntent contentIntent;

        private LatestEventInfo(CharSequence contentTitle, CharSequence contentText, PendingIntent contentIntent) {
            this.contentTitle = contentTitle;
            this.contentText = contentText;
            this.contentIntent = contentIntent;
        }

        public CharSequence getContentTitle() {
            return contentTitle;
        }

        public CharSequence getContentText() {
            return contentText;
        }

        public PendingIntent getContentIntent() {
            return contentIntent;
        }
    }

    /**
     * Shadows the {@code android.app.Notification.Builder} class.
     */
    @Implements(Notification.Builder.class)
    public static class ShadowBuilder {
        @RealObject
        private Notification.Builder realBuilder;

        private Context mContext;

        private long mWhen;
        private int mSmallIcon;
        private int mSmallIconLevel;
        private int mNumber;
        private CharSequence mContentTitle;
        private CharSequence mContentText;
        private CharSequence mContentInfo;
        private PendingIntent mContentIntent;
        private RemoteViews mContentView;
        private PendingIntent mDeleteIntent;
        private PendingIntent mFullScreenIntent;
        private CharSequence mTickerText;
        private RemoteViews mTickerView;
        private Bitmap mLargeIcon;
        private Uri mSound;
        private int mAudioStreamType;
        private long[] mVibrate;
        private int mLedArgb;
        private int mLedOnMs;
        private int mLedOffMs;
        private int mDefaults;
        private int mFlags;
        private int mProgressMax;
        private int mProgress;
        private boolean mProgressIndeterminate;

        public void __constructor__(Context context) {
            mContext = context;

            // Set defaults to match the defaults of a Notification
            mWhen = System.currentTimeMillis();
            mAudioStreamType = Notification.STREAM_DEFAULT;
        }

        @Implementation
        public Notification.Builder setWhen(long when) {
            mWhen = when;
            return realBuilder;
        }

        @Implementation
        public Notification.Builder setSmallIcon(int icon) {
            mSmallIcon = icon;
            return realBuilder;
        }

        @Implementation
        public Notification.Builder setSmallIcon(int icon, int level) {
            mSmallIcon = icon;
            mSmallIconLevel = level;
            return realBuilder;
        }

        @Implementation
        public Notification.Builder setContentTitle(CharSequence title) {
            mContentTitle = title;
            return realBuilder;
        }

        @Implementation
        public Notification.Builder setContentText(CharSequence text) {
            mContentText = text;
            return realBuilder;
        }

        @Implementation
        public Notification.Builder setNumber(int number) {
            mNumber = number;
            return realBuilder;
        }

        @Implementation
        public Notification.Builder setContentInfo(CharSequence info) {
            mContentInfo = info;
            return realBuilder;
        }

        @Implementation
        public Notification.Builder setProgress(int max, int progress, boolean indeterminate) {
            mProgressMax = max;
            mProgress = progress;
            mProgressIndeterminate = indeterminate;
            return realBuilder;
        }

        @Implementation
        public Notification.Builder setContent(RemoteViews views) {
            mContentView = views;
            return realBuilder;
        }

        @Implementation
        public Notification.Builder setContentIntent(PendingIntent intent) {
            mContentIntent = intent;
            return realBuilder;
        }

        @Implementation
        public Notification.Builder setDeleteIntent(PendingIntent intent) {
            mDeleteIntent = intent;
            return realBuilder;
        }

        @Implementation
        public Notification.Builder setFullScreenIntent(PendingIntent intent, boolean highPriority) {
            mFullScreenIntent = intent;
            setFlag(Notification.FLAG_HIGH_PRIORITY, highPriority);
            return realBuilder;
        }

        @Implementation
        public Notification.Builder setTicker(CharSequence tickerText) {
            mTickerText = tickerText;
            return realBuilder;
        }

        @Implementation
        public Notification.Builder setTicker(CharSequence tickerText, RemoteViews views) {
            mTickerText = tickerText;
            mTickerView = views;
            return realBuilder;
        }

        @Implementation
        public Notification.Builder setLargeIcon(Bitmap icon) {
            mLargeIcon = icon;
            return realBuilder;
        }

        @Implementation
        public Notification.Builder setSound(Uri sound) {
            mSound = sound;
            mAudioStreamType = Notification.STREAM_DEFAULT;
            return realBuilder;
        }

        @Implementation
        public Notification.Builder setSound(Uri sound, int streamType) {
            mSound = sound;
            mAudioStreamType = streamType;
            return realBuilder;
        }

        @Implementation
        public Notification.Builder setVibrate(long[] pattern) {
            mVibrate = pattern;
            return realBuilder;
        }

        @Implementation
        public Notification.Builder setLights(int argb, int onMs, int offMs) {
            mLedArgb = argb;
            mLedOnMs = onMs;
            mLedOffMs = offMs;
            return realBuilder;
        }

        @Implementation
        public Notification.Builder setOngoing(boolean ongoing) {
            setFlag(Notification.FLAG_ONGOING_EVENT, ongoing);
            return realBuilder;
        }

        @Implementation
        public Notification.Builder setOnlyAlertOnce(boolean onlyAlertOnce) {
            setFlag(Notification.FLAG_ONLY_ALERT_ONCE, onlyAlertOnce);
            return realBuilder;
        }

        @Implementation
        public Notification.Builder setAutoCancel(boolean autoCancel) {
            setFlag(Notification.FLAG_AUTO_CANCEL, autoCancel);
            return realBuilder;
        }

        @Implementation
        public Notification.Builder setDefaults(int defaults) {
            mDefaults = defaults;
            return realBuilder;
        }

        private void setFlag(int mask, boolean value) {
            if (value) {
                mFlags |= mask;
            } else {
                mFlags &= ~mask;
            }
        }

        @Implementation
        public Notification getNotification() {
            Notification n = new Notification();
            n.when = mWhen;
            n.icon = mSmallIcon;
            n.iconLevel = mSmallIconLevel;
            n.number = mNumber;
            n.contentIntent = mContentIntent;
            n.deleteIntent = mDeleteIntent;
            n.fullScreenIntent = mFullScreenIntent;
            n.tickerText = mTickerText;
            n.largeIcon = mLargeIcon;
            n.sound = mSound;
            n.audioStreamType = mAudioStreamType;
            n.vibrate = mVibrate;
            n.ledARGB = mLedArgb;
            n.ledOnMS = mLedOnMs;
            n.ledOffMS = mLedOffMs;
            n.defaults = mDefaults;
            n.flags = mFlags;
           return n;
        }
    }
}
