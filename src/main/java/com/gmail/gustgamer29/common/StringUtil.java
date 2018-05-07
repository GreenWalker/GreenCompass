package com.gmail.gustgamer29.common;

import org.bukkit.ChatColor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

public class StringUtil {

    public static List<String> join(Collection<String> strings, String separator) {
        StringBuilder sb = null;
        List<String> lines = new ArrayList<>();
        Iterator<String> stringIterator = strings.iterator();

        while (stringIterator.hasNext()) {
            if (sb == null) sb = new StringBuilder();

            String next = stringIterator.next();

            if (sb.length() >= 53 || (sb.append(next).length() >= 52)) {
                lines.add(sb.toString().replaceAll(next, ""));
                sb = new StringBuilder();
                sb.append(next);
            }

            if (stringIterator.hasNext()) { // don't add separator in last string.
                sb.append(separator);
            }

            if (sb.length() < 53 && !stringIterator.hasNext()) {
                lines.add(sb.toString());
            }
        }
        return lines;
    }

    public static <T extends String> String colorize(T msg) {
        return ChatColor.translateAlternateColorCodes('@', msg);
    }

    public static Collection<String> colorize(Collection<String> msgs) {
        return msgs.stream().map(StringUtil::colorize).collect(Collectors.toList());
    }
}
