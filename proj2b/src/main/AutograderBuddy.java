package main;

import browser.NgordnetQueryHandler;


public class AutograderBuddy {
    /** Returns a HyponymHandler */
    public static NgordnetQueryHandler getHyponymsHandler(
            String wordFile, String countFile,
            String synsetFile, String hyponymFile) {
        WorldNet worldNet = new WorldNet(synsetFile,hyponymFile);
        return new HyponymsHandler(worldNet);
    }
}
