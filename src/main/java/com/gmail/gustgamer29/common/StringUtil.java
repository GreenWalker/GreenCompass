package com.gmail.gustgamer29.common;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

public class StringUtil extends InstanceOfClasses {

    public static List<String> join(Collection<String> strings, String separator) {
        StringBuilder sb = null;
        List<String> lines = new ArrayList<>();
        Iterator<String> stringIterator = strings.iterator();

        while (stringIterator.hasNext()) {
            if (sb == null) sb = newStringBuilder();

            String next = stringIterator.next();

            if (sb.length() >= 53 || (sb.append(next).length() >= 52)) {
                lines.add(sb.toString().replaceAll(next, ""));
                sb = newStringBuilder();
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

}
