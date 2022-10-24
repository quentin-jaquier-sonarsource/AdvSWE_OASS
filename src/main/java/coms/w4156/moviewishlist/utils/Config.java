package coms.w4156.moviewishlist.utils;

import lombok.Getter;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Config {
    /**
     * The file path to where the config file is.
     * This file should just have the API key in it.
     * The file path is relative to the root of the project.
     */
    private final String configFilepath = "config.txt";

    /**
     * Error message in case there is an issues reading the config file.
     */
    private final String errorMsg = "There was an issue with the config file. "
            + "Make sure that there is a config.txt at the root of the "
            + "project and its contents are the API key";
    /**
     * The WatchMode API key needed to query the WatchMode API.
     * This is pulled from the config file.
     */
    @Getter
    private String apikey;

    /**
     * Reads in the API key from the config.txt file and stores it in the
     * instance variable.
     */
    public Config() {

        apikey = System.getenv("apikey");

    }
}
