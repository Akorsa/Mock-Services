package com.test.mock;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Test {

    private static final Logger log = Logger.getLogger(Test.class);

    public static void main(String[] args) throws Exception {

        log.info("Initializing Spring context...");

        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("spring-config.xml");

        HttpServer server = HttpServer.create(new InetSocketAddress(9598), 0);
        server.createContext("/test", new MyHandler());
        server.setExecutor(null); // creates a default executor
        server.start();
    }

    static class MyHandler implements HttpHandler {
        public void handle(HttpExchange t) throws IOException {
            System.out.println("Handling incoming HTTP request");
            byte[] response = new byte[0];
            try {
                response = getResponse();
            } catch (Exception e) {
                e.printStackTrace();
            }
            t.sendResponseHeaders(200, response.length);
            OutputStream os = t.getResponseBody();
            os.write(response);
            os.close();
        }

        public static byte[] getResponse() throws Exception {
            Path path = Paths.get(
                    Test.class.getClassLoader().getResource("ResponseBDS.xml").toURI());

            byte[] encoded = Files.readAllBytes(path);
            return encoded;
        }
    }
}
