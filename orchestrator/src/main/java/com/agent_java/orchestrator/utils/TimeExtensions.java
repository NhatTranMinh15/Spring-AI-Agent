package com.agent_java.orchestrator.utils;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.stream.Collectors;

public class TimeExtensions {

    public static String toRelativeString(Instant instant) {
        return toRelativeString(instant, RelativeFormat.FRIENDLY);
    }

    public static String toRelativeString(Instant instant, RelativeFormat format) {
        if (instant == null) {
            return null;
        }
        var now = Instant.now();

        var duration = Duration.between(instant, now);
        String result;
        if (duration.isNegative()) {
            result = "in the future";
        } else {
            result = switch (format) {
                case FRIENDLY ->
                    formatFriendly(duration);

                case COMPACT ->
                    formatCompact(duration);
            };
        }
        return result;
    }

    // -----------------------------------------------------------
    // Constants
    // -----------------------------------------------------------
    private static final int MINUTES_PER_HOUR = 60;
    private static final int HOURS_PER_DAY = 24;
    private static final int DAYS_PER_MONTH = 30; // Approximation
    private static final int MONTHS_PER_YEAR = 12;

    private static final int MINUTES_PER_DAY = MINUTES_PER_HOUR * HOURS_PER_DAY;
    private static final int MINUTES_PER_MONTH = MINUTES_PER_DAY * DAYS_PER_MONTH;
    private static final int MINUTES_PER_YEAR = MINUTES_PER_MONTH * MONTHS_PER_YEAR;

    // -----------------------------------------------------------
    // FRIENDLY FORMAT
    // -----------------------------------------------------------
    private static String formatFriendly(Duration duration) {
        var minutes = duration.toMinutes();

        var years = minutes / MINUTES_PER_YEAR;
        var months = (minutes % MINUTES_PER_YEAR) / MINUTES_PER_MONTH;
        var days = (minutes % MINUTES_PER_MONTH) / MINUTES_PER_DAY;
        var hours = (minutes % MINUTES_PER_DAY) / MINUTES_PER_HOUR;
        var mins = minutes % MINUTES_PER_HOUR;

        var parts = new ArrayList<String>();

        if (years > 0) {
            parts.add(plural(years, "year"));
        }
        if (months > 0) {
            parts.add(plural(years, "month"));
        }
        if (days > 0) {
            parts.add(plural(years, "day"));
        }
        if (hours > 0) {
            parts.add(plural(years, "hour"));
        }
        if (mins > 0) {
            parts.add(plural(years, "minute"));
        }

        if (parts.isEmpty()) {
            return "just now";
        } else {
            return parts.stream().limit(2).collect(Collectors.joining(" ")) + " ago";
        }
    }

    private static String formatCompact(Duration duration) {
        var minutes = duration.toMinutes();

        var years = minutes / MINUTES_PER_YEAR;
        var months = (minutes % MINUTES_PER_YEAR) / MINUTES_PER_MONTH;
        var days = (minutes % MINUTES_PER_MONTH) / MINUTES_PER_DAY;
        var hours = (minutes % MINUTES_PER_DAY) / MINUTES_PER_HOUR;
        var mins = minutes % MINUTES_PER_HOUR;

        if (years > 0) {
            return years + "y" + (months > 0 ? months + "mo" : "") + " ago";
        }
        if (months > 0) {
            return months + "mo" + (days > 0 ? days + "d" : "") + " ago";
        }
        if (days > 0) {
            return days + "d" + (hours > 0 ? hours + "h" : "") + " ago";
        }
        if (hours > 0) {
            return hours + "h" + (mins > 0 ? mins + "m" : "") + " ago";
        }
        if (mins > 0) {
            return mins + "m ago";
        }
        return "just now";
    }

    // -----------------------------------------------------------
    // Helper
    // -----------------------------------------------------------
    private static String plural(Long varue, String label) {
        return varue + label + (varue > 1 ? "s" : "");
    }
}
