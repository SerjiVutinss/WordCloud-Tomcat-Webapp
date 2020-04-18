package ie.gmit.sw.ai.web_opinion.services;

import ie.gmit.sw.ai.web_opinion.utils.Config;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

public class IgnoredWordsService {

        private static Set<String> IGNORED_WORDS;

        public static Set<String> getIgnoredWords() throws IOException {

            if(IGNORED_WORDS == null) {
                IGNORED_WORDS = new HashSet<>();
                File f = new File(Config.IGNORE_WORDS_FILE_LOCATION);
                BufferedReader br = new BufferedReader(new FileReader(f));
                String str;
                while ((str = br.readLine()) != null) IGNORED_WORDS.add(str);
            }

            IGNORED_WORDS.add("et");
            IGNORED_WORDS.add("al");

            return new HashSet<>(IGNORED_WORDS);
        }
}
