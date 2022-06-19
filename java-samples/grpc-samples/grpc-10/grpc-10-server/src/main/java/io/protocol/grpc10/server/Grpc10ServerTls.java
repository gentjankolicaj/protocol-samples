package io.protocol.grpc10.server;

import io.grpc.*;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.IOException;


@Slf4j
public class Grpc10ServerTls {

    static final int PORT=50051;
    public static void main(String[] args) throws IOException {

        try {


            // server.crt: Server certificate signed by the CA (this would be sent back by the CA owner) - keep on server
            // server.pem: Conversion of server.key into a format gRPC likes (this shouldn't be shared)
            ServerCredentials serverCredentials= TlsServerCredentials.create( Grpc10ServerTls.class.getResourceAsStream("/server.crt"), Grpc10ServerTls.class.getResourceAsStream("/server.pem"));

            //Create server object
            //Server uses TLS,
            final Server server = Grpc.newServerBuilderForPort(PORT,serverCredentials)
                    .addService(new CalculatorServiceImpl())
                    .build();

            //Start server
            server.start();

            log.info("Server started.");
            log.info("Server listening on port {}",PORT);


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
