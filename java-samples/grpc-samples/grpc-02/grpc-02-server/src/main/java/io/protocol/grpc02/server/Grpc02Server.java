package io.protocol.grpc02.server;

import io.grpc.Server;
import io.grpc.ServerBuilder;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

@Slf4j
public class Grpc02Server {

    public static void main(String[] args){
        try {
            //Build server
            Server server = ServerBuilder.forPort(7070)
                    .addService(new RouteGuideServiceImpl()) //Add reference to service implementations
                    .build();

            //Start server listening
            server.start();


            //main thread waits for server termination
            server.awaitTermination();
        }catch (IOException io){
          log.error(io.getStackTrace().toString());
        }catch (InterruptedException ie){
            log.error(ie.getStackTrace().toString());
        }catch (Exception e){
            log.error(e.getStackTrace().toString());
        }
    }
}
