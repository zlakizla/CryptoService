package webserver;

import crypto.AEScrypto;
import org.apache.log4j.Logger;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.IPAccessHandler;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.glassfish.jersey.media.multipart.MultiPartFeature;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.servlet.ServletContainer;

import java.net.InetSocketAddress;
import java.util.HashSet;
import java.util.Set;


public class WebServer extends SetSettingFile {
    public Server server;
    static org.apache.log4j.Logger LOGGER = Logger.getLogger(AEScrypto.class.getName());

    public WebServer() {
        Set<Class<?>> s = new HashSet<Class<?>>();
        s.add(ApiCrypto.class);
        ResourceConfig config = new ResourceConfig(s);
        config.register(MultiPartFeature.class);
        //CREATE CONTAINER
        ServletContainer container = new ServletContainer(config);

        //CREATE CONTEXT
        ServletContextHandler context = new ServletContextHandler();
        context.setContextPath("/");
        context.addServlet(new ServletHolder(container), "/*");

        //CREATE WHITELIST
        IPAccessHandler accessHandler = new IPAccessHandler();

        String[] whiteList = new String[WHITE_LIST.size()];
        for (int i = 0; i < WHITE_LIST.size(); i++) {
            whiteList[i] = WHITE_LIST.get(i);
        }
        accessHandler.setWhite(whiteList);
        accessHandler.setHandler(context);

        //CREATE WEB SERVER
        this.server = new Server(new InetSocketAddress("127.0.0.1",SERVER_PORT));
        this.server.setHandler(accessHandler);
    }

    public void start() {
        try {
            //START WEB
            LOGGER.info("Start web server");
            server.start();
        } catch (Exception e) {
            //FAILED TO START WEB
        }
    }

    public void stop() {
        try {
            //STOP RPC
            LOGGER.info("Stop web server");
            server.stop();
        } catch (Exception e) {
            //FAILED TO STOP WEB
        }
    }

}
