package org.ternence.compressionfile.utils;

import android.content.Context;
import android.util.Log;

/*File added by OnePlus Willis for OOS-1986*/
public final class ReflectUtils {

    public static <T> T readValue(Class<?> clazz, String field, T defaultValue)
    {
        try
        {
            return (T)clazz.getDeclaredField(field).get(null);
        }
        catch(Throwable ex)
        {
            Log.e("ReflectUtils", "readValue() - Fail to read '" +
                    field + "' from '"+ clazz.getSimpleName() + "'" + ex.getMessage());
            return defaultValue;
        }
    }

    public static int getResIdentifier(Context context, String name, String defType, String defPackage){
        if(context == null) {
            Log.e("ReflectUtils", "context == null, getResIdentifier failed ");
            return 0;
        }
        Log.e("ReflectUtils", "name:" + name + ", defType:" + defType + ", defPackage:" + defPackage);
        int resId = context.getResources().getIdentifier(name, defType, defPackage);

        return resId;
    }
}