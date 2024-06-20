package io.protocol.grpc03.server;

import io.grpc.Server;
import io.grpc.ServerBuilder;

import java.io.IOException;

public class Grpc03Server {
     static final int GRPC_SERVER_PORT=8080;

    public static void main(String[] args) throws IOException,InterruptedException{
        //Create server
        //Add service
       Server server= ServerBuilder.forPort(GRPC_SERVER_PORT)
               .addService(new GreetingServiceImpl())
                .build();

       //Start server listening
        server.start();

        //Wait for server because is running on background thread , not on main[] thread
        server.awaitTermination();;
    }
}
