package me.tuskdev.points.util;

import org.jetbrains.annotations.NotNull;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;

public class NumberUtil {

    private static final NumberFormat FORMATTER = NumberFormat.getNumberInstance(Locale.forLanguageTag("pt-BR"));

    public static String format(double value) {
        return FORMATTER.format(value > 0 ? value : 0);
    }

    public static int parseInt(@NotNull String value) {
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException exception) {
            return -1;
        }
    }

}
