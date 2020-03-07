package ie.gmit.sw.ai.web_opinion;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

public class StaticHelperTemp {

    public static Set<String> getIgnoredWords() throws IOException {

        Set<String> results = new HashSet<>();

        File f = new File(Config.IGNORE_WORDS_FILE_LOCATION);
        BufferedReader br = new BufferedReader(new FileReader(f));

        String str;
        while ((str = br.readLine()) != null) {
            results.add(str);
        }
        return results;
    }

}
