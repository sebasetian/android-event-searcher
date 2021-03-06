package csci571.hw9.adapters;

import android.databinding.InverseMethod;

public class DistanceConverter {
    @InverseMethod("StringToInt")
    public static String IntToString(Integer value) {
        if (value == null) return "";
        return String.valueOf(value);
    }

    public static Integer StringToInt(String value) {
        if (value == null || value.length() == 0) return null;
        return Integer.valueOf(value);

    }

}
