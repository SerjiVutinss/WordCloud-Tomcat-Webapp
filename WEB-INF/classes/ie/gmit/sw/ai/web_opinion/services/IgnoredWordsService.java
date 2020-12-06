package ie.gmit.sw.ai.web_opinion.services;

import ie.gmit.sw.ai.web_opinion.utils.Config;

import java.io.*;
import java.util.HashSet;
import java.util.Set;

public class IgnoredWordsService {

    private static Set<String> IGNORED_WORDS;

    public static Set<String> getIgnoredWords() {

        if (IGNORED_WORDS == null) {
            IGNORED_WORDS = new HashSet<>();
            File f = new File(Config.IGNORE_WORDS_FILE_LOCATION);
            BufferedReader br = null;
            try {
                br = new BufferedReader(new FileReader(f));
                String str;
                while ((str = br.readLine()) != null) IGNORED_WORDS.add(str);

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        IGNORED_WORDS.add("et");
        IGNORED_WORDS.add("al");

        return new HashSet<>(IGNORED_WORDS);
    }
}
