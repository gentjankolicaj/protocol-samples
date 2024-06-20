package io.protocol.blog.server;

import io.grpc.Server;
import io.grpc.ServerBuilder;
import io.grpc.protobuf.services.ProtoReflectionService;
import lombok.extern.slf4j.Slf4j;


@Slf4j
public class BlogGrpcServer {

  static final int PORT = 8080;

  public static void main(String[] args) {

    try {
      //Create server object
      //And register service
      final Server server = ServerBuilder.forPort(PORT)
          .addService(ProtoReflectionService.newInstance())
          //  .addService(new BlogServiceImpl())
          .build();

      //Start server
      server.start();

      //When jvm receives termination signal, this shutdown hook is invoke for execution
      Runtime.getRuntime().addShutdownHook(new Thread(() -> {
        log.info("Received shutdown request ...");
        server.shutdown();
        log.info("Server shutdown.");
      }));

      //Call method awaitTermination to stop main Thread from finishing && JVM shutdown
      //Waiting for server termination
      server.awaitTermination();

      log.info("Exiting main[]");
    } catch (Exception e) {
      e.printStackTrace();
    }
  }


}
