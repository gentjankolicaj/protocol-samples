package io.protocol.grpc07.server;

import io.grpc.Server;
import io.grpc.ServerBuilder;
import lombok.extern.slf4j.Slf4j;


@Slf4j
public class Grpc07Server {

    static final int PORT=8080;
    public static void main(String[] args){

        try {

            //Create server object
            //And register service , in our case is greetingServiceImpl
            final Server server = ServerBuilder.forPort(PORT)
                    .addService(new AverageServiceImpl())
                    .build();

            //Start server
            server.start();

            //When jvm receives termination signal, this shutdown hook is invoke for execution
            Runtime.getRuntime().addShutdownHook(new Thread(()->{
                log.info("Received shutdown request ...");
                server.shutdown();
                log.info("Server shutdown.");
            }));


            //Call method awaitTermination to stop main Thread from finishing && JVM shutdown
            //Waiting for server termination
            server.awaitTermination();

            log.info("Exiting main[]");

        }catch (Exception e){
            e.printStackTrace();
        }

    }
}
