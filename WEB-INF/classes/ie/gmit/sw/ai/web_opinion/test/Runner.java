package ie.gmit.sw.ai.web_opinion.test;

import ie.gmit.sw.ai.web_opinion.utils.Config;

public class Runner {

    public static void main(String[] args) {
//        Config.FCL_FILE_LOCATION = "./res/my-fcl.fcl";
        Config.FCL_FILE_LOCATION = "./res/wcloud.fcl";
        Config.IGNORE_WORDS_FILE_LOCATION = "./res/ignorewords.txt";

        Thread t = new Thread(new MockServiceHandler());
        t.start();
    }
}
