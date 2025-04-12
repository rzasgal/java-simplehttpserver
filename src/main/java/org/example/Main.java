package org.example;

import com.sun.net.httpserver.HttpServer;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.concurrent.Executors;

public class Main {

  public static void main(String[] args) throws IOException {
    int defaultPort = 8080;
    int threadPoolThreadCount = 10;
    for (String arg : args) {
      if(arg.contains("port")){
        String[] port = arg.split("=");
        defaultPort = Integer.parseInt(port[1]);
      } else if(arg.contains("threadPoolThreadCount")) {
        String[] threadPoolThreadCountPair = arg.split("=");
        threadPoolThreadCount =  Integer.parseInt(threadPoolThreadCountPair[1]);
      }
    }

    HttpServer server = HttpServer.create(new InetSocketAddress(defaultPort), 0);

    server.createContext("/", new ServerHandler());

    server.setExecutor(Executors.newFixedThreadPool(threadPoolThreadCount));
    System.out.println("Server started on port:" + defaultPort +" with threadPoolThreadCount:" + threadPoolThreadCount);
    server.start();
  }
}