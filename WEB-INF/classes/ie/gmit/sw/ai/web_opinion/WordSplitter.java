package ie.gmit.sw.ai.web_opinion;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class WordSplitter {

    private final List<String> strings;
    private final Set<String> ignoredWords;
    private FrequencyMap map;

    public WordSplitter(List<String> strings, FrequencyMap map) throws IOException {
        this.strings = strings;
        this.map = map;

        ignoredWords = StaticHelperTemp.getIgnoredWords();
        indexAll();
    }

    private void indexAll() {

        for (String s : strings) {
            indexOne(s);
        }
    }

    private void indexOne(String s) {

        String[] words = splitIntoWords(s);
        for (String w : words) if (!ignoredWords.contains((w))) map.put(w);
    }

    private String[] splitIntoWords(String s) {

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
        return words.toArray(new String[0]);

    }
}
