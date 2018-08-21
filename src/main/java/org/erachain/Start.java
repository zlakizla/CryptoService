package start;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import webserver.SetSettingFile;
import webserver.WebServer;

import java.io.File;
import java.io.InputStream;

public class Start {
    static Logger LOGGER = Logger.getLogger(Start.class.getName());

    public static void main(String args[]) throws Exception {
        System.out.println("Build version: " + getBuildVersion());
        System.exit(-1);
        //System.out.println("Build info: " + getManifestInfo());


        File log4j = new File("log4j.properties");
        if (log4j.exists()) {
            PropertyConfigurator.configure(log4j.getAbsolutePath());
        } else {
            try (InputStream inputStream = ClassLoader.class.getResourceAsStream("/log4j/log4j.default")) {
                PropertyConfigurator.configure(inputStream);
                LOGGER.error("log4j.properties not found: " + log4j.getAbsolutePath() + ", using default.");
            } catch (Exception e) {
                System.out.println("Error: missing configuration log4j file.");
                System.exit(-1);
            }
        }

        new SetSettingFile().SettingFile();
        new WebServer().start();
    }

    public static String getBuildVersion() {
        return Start.class.getPackage().getImplementationVersion();
    }
}
