package coms.w4156.moviewishlist.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Config {
    private final String config_filepath = "config.txt";
    private String APIKey;

    public Config() {

        try {
            File configFile = new File(config_filepath);
            Scanner myReader = new Scanner(configFile);
            APIKey = myReader.nextLine();
            myReader.close();
        } catch (FileNotFoundException e) {
            System.out.println("There was an issue with the config file. Make sure that there is a config.txt at" +
                    " the root of the project and its contents are the API key");
            e.printStackTrace();
        }
    }

    public String getAPIKey() {
        return APIKey;
    }

    public void setAPIKey(String APIKey) {
        this.APIKey = APIKey;
    }
}
