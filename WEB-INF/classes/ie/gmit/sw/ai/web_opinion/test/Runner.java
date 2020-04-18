package ie.gmit.sw.ai.web_opinion.test;

import ie.gmit.sw.ai.web_opinion.utils.Config;

import java.io.IOException;

public class Runner {

    public static void main(String[] args) throws IOException {
        Config.FCL_FILE_LOCATION = "./res/my-fcl.fcl";
        Config.IGNORE_WORDS_FILE_LOCATION = "./res/ignorewords.txt";

        Thread t = new Thread(new MockServiceHandler());
        t.start();

//        QueryRunner queryRunner = new QueryRunner();
//        queryRunner.run(qUrl, query);
//
//        WordFrequency[] results = queryRunner.getResults();
//
//        for (WordFrequency wf : results) {
//            System.out.println(String.format("%s : %d", wf.getWord(), wf.getFrequency()));
//        }

    }
}
