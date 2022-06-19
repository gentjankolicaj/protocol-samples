package io.protocol.blog.client;

import io.grpc.*;
import io.protocol.grpc13.*;
import lombok.extern.slf4j.Slf4j;


import java.util.concurrent.TimeUnit;


@Slf4j
public class BlogGrpcClient {

    static final String HOSTNAME = "localhost";
    static final int PORT = 8080;

    public static void main(String[] args) throws InterruptedException {
        ManagedChannel managedChannel = ManagedChannelBuilder.forAddress(HOSTNAME, PORT)
                .usePlaintext()
                .build();

        //Note :
        //We use Stub for streaming
        //We use BlockingStub for Unary


        //Shutdown channel
        managedChannel.shutdown();
    }




}
