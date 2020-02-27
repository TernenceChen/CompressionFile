package org.ternence.compressionfile.utils;

import android.app.Activity;
import android.util.Log;

import java.lang.ref.WeakReference;
import java.util.Iterator;
import java.util.Stack;

/**
 * Activity Manager Utils
 * @author oneplus
 */
public class ActivityManagerUtils {

    private static final String TAG = "FinishActivityManagerUtils";

    private ActivityManagerUtils() {

    }

    private static ActivityManagerUtils sManager;
    private Stack<WeakReference<Activity>> mActivityStack;
    public static ActivityManagerUtils getInstance() {
        if (sManager == null) {
            synchronized (ActivityManagerUtils.class) {
                if (sManager == null) {
                    sManager = new ActivityManagerUtils();
                }
            }
        }
        return sManager;
    }

    public void addActivity(Activity activity) {
        if (mActivityStack == null) {
            mActivityStack = new Stack<>();
        }
        Log.d(TAG, "AddActivity: " + getActivityName(activity));
        mActivityStack.add(new WeakReference<>(activity));
    }

    public void checkWeakReference() {
        if (mActivityStack != null) {
            for (Iterator<WeakReference<Activity>> iterator = mActivityStack.iterator(); iterator.hasNext(); ) {
                WeakReference<Activity> activityWeakReference = iterator.next();
                Activity temp = activityWeakReference.get();
                if (temp == null) {
                    iterator.remove();
                }
            }
        }
    }

    public Activity currentActivity() {
        checkWeakReference();
        if (mActivityStack != null && !mActivityStack.isEmpty()) {
            Activity activity = mActivityStack.lastElement().get();
            Log.i(TAG, "Current Activity: " + getActivityName(activity));
            return activity;
        }
        return null;
    }

    public void finishActivity() {
        Activity activity = currentActivity();
        if (activity != null) {
            Log.i(TAG, "Finish Activity: " + getActivityName(activity));
            finishActivity(activity);
        }
    }

    public void finishActivity(Activity activity) {
        if (mActivityStack != null && activity != null) {
            for (Iterator<WeakReference<Activity>> activityIterator = mActivityStack.iterator(); activityIterator.hasNext(); ) {
                WeakReference<Activity> activityWeakReference = activityIterator.next();
                Activity temp = activityWeakReference.get();
                if (temp == null) {
                    activityIterator.remove();
                    continue;
                }
                if (temp == activity) {
                    activityIterator.remove();
                }
            }
            activity.finish();
        }
    }

    public void finishActivity(Class<?> cls) {
        if (mActivityStack != null) {
            for (Iterator<WeakReference<Activity>> activityIterator = mActivityStack.iterator(); activityIterator.hasNext(); ) {
                WeakReference<Activity> activityWeakReference  = activityIterator.next();
                Activity activity = activityWeakReference.get();
                if (activity == null) {
                    activityIterator.remove();
                    continue;
                }
                if (activity.getClass().equals(cls)) {
                    activityIterator.remove();
                    activity.finish();
                }
            }
        }
    }

    public void finishAllActivity() {
        if (mActivityStack != null) {
            for (WeakReference<Activity> activityWeakReference : mActivityStack) {
                Activity activity = activityWeakReference.get();
                if (activity != null) {
                    Log.i(TAG, "Finish All Activity: " + getActivityName(activity));
                    activity.finish();
                }
            }
            mActivityStack.clear();
        }
    }

    public void exitApp() {
        try {
            finishAllActivity();
            Log.d(TAG, "Exit App");
            System.exit(0);
            android.os.Process.killProcess(android.os.Process.myPid());
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, "exitApp have a exception: " + e);
        }
    }

    private static String getActivityName(Activity activity) {
        return activity.getClass().getSimpleName();
    }

}
