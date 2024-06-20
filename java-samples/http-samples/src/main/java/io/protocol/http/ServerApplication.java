package io.protocol.http;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ServerApplication {

  public static void main(String[] args) throws IOException {
    Executor executor = Executors.newFixedThreadPool(2);

    //create http server
    HttpServer httpServer = HttpServer.create(new InetSocketAddress("127.0.0.1", 8080), 10);
    httpServer.setExecutor(executor);
    httpServer.createContext("/", new RootContextHandler());
    httpServer.start();
  }

  static class RootContextHandler implements HttpHandler {

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
      log.info("Request {}", httpExchange.getRequestMethod());
      String response = "Successful at : /";
      httpExchange.sendResponseHeaders(200, response.length());
      OutputStream os = httpExchange.getResponseBody();
      os.write(response.getBytes());
      os.close();
    }
  }


}
