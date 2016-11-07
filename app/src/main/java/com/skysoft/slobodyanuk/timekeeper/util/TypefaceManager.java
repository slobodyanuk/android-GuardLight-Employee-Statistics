package com.skysoft.slobodyanuk.timekeeper.util;

import android.content.Context;
import android.graphics.Typeface;
import android.util.SparseArray;
import static com.skysoft.slobodyanuk.timekeeper.util.Globals.*;

/**
 * Created by Serhii Slobodyanuk on 28.10.2016.
 */

public class TypefaceManager {

    private final static SparseArray<Typeface> mTypefaces = new SparseArray<>(20);

    public static Typeface obtainTypeface(Context context, int typefaceValue) throws IllegalArgumentException {
        Typeface typeface = mTypefaces.get(typefaceValue);
        if (typeface == null) {
            typeface = createTypeface(context, typefaceValue);
            mTypefaces.put(typefaceValue, typeface);
        }
        return typeface;
    }

    private static Typeface createTypeface(Context context, int typefaceValue) throws IllegalArgumentException {
        Typeface typeface;
        switch (typefaceValue) {
            case OPEN_SANS_REGULAR:
                typeface = Typeface.createFromAsset(context.getAssets(), "fonts/OpenSans-Regular.ttf");
                break;

            case OPEN_SANS_LIGHT:
                typeface = Typeface.createFromAsset(context.getAssets(), "fonts/OpenSans-Light.ttf");
                break;

            case OPEN_SANS_BOLD:
                typeface = Typeface.createFromAsset(context.getAssets(), "fonts/OpenSans-Bold.ttf");
                break;

            case OPEN_SANS_SEMI_BOLD:
                typeface = Typeface.createFromAsset(context.getAssets(), "fonts/OpenSans-Semibold.ttf");
                break;

            default:
                throw new IllegalArgumentException("Unknown `typeface` attribute value " + typefaceValue);
        }
        return typeface;
    }

}
