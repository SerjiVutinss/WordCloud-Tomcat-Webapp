package ie.gmit.sw.ai.web_opinion.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtils {

    public static List<String> splitIntoWords(String s) {

        List<String> words = new ArrayList<>();

        // Match only words - should update to ignore numerics too...
        String regex = "\\w+";

        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(s);

        String str;
        while (matcher.find()) {
            str = matcher.group().toLowerCase();
            // Remove any completely numeric strings
            try {
                Integer.parseInt(str);
            } catch (NumberFormatException e) {
                words.add(str);
            }
        }
        return words;
    }

    public static int getQueryFrequencyInString(String query, String s) {

        int count = 0, startIndex = 0;

        Pattern p = Pattern.compile(query, Pattern.CASE_INSENSITIVE);
        Matcher m = p.matcher(s);

        while (m.find(startIndex)) {
            count++;
            startIndex = m.start() + 1;
        }
        return count;
    }
}
