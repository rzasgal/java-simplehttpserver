package org.example;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

public class ServerHandler implements HttpHandler {

  @Override
  public void handle(HttpExchange exchange) throws IOException {
    String response;
    int statusCode;

    if (!"GET".equalsIgnoreCase(exchange.getRequestMethod())) {
      response = "Only GET requests are allowed.";
      statusCode = 405;
    } else {
      Map<String, String> params = queryToMap(exchange.getRequestURI().getQuery());
      String wait = params.get("wait");
      String id = params.get("id");

      if (wait != null) {
        response = "Request blocked. for while :" +id;
        System.out.println(response);
        statusCode = 200;
        try {
          Thread.sleep(Integer.parseInt(wait));
          System.out.println("Released :" + id);
        } catch (InterruptedException e) {
          throw new RuntimeException(e);
        }
      } else {
        response = "Instant response :"+id;
        System.out.println(response);
        statusCode = 200;
      }
    }

    exchange.sendResponseHeaders(statusCode, response.getBytes().length);
    OutputStream os = exchange.getResponseBody();
    os.write(response.getBytes());
    os.close();
  }

  private Map<String, String> queryToMap(String query) {
    Map<String, String> result = new HashMap<>();
    if (query == null) {
      return result;
    }

    for (String param : query.split("&")) {
      String[] entry = param.split("=");
      if (entry.length > 1) {
        result.put(entry[0], entry[1]);
      } else {
        result.put(entry[0], "");
      }
    }
    return result;
  }
}
