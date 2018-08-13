import org.apache.log4j.PropertyConfigurator;
import org.bouncycastle.crypto.InvalidCipherTextException;
import webserver.WebServer;

import org.apache.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public class Start {

    static Logger LOGGER = Logger.getLogger(Start.class.getName());
    public static void main(String args[]) throws IOException, InvalidCipherTextException {
        File log4j = new File("log4j.properties");
        if (log4j.exists()) {
            PropertyConfigurator.configure(log4j.getAbsolutePath());
        } else {
            try (InputStream inputStream = ClassLoader.class.getResourceAsStream("/log4j/log4j.default")) {
                PropertyConfigurator.configure(inputStream);
                LOGGER.error("log4j.properties not found: " + log4j.getAbsolutePath() + ", using default.");
            }
        }

       new WebServer().start();

    }
}
