package io.protocol.http;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;

/**
 * Hello world!
 */
public class ClientApplication {

  public static void main(String[] args) throws IOException, InterruptedException {
    HttpClient httpClient = HttpClient.newHttpClient();
    HttpRequest request = HttpRequest.newBuilder()
        .uri(URI.create("http://localhost:8080/"))
        .build();

    HttpResponse<String> response = httpClient.send(request, BodyHandlers.ofString());
    response.statusCode();
    System.out.println(response.body());
  }
}
