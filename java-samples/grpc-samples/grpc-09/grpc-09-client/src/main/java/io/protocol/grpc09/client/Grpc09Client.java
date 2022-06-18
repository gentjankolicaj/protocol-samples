package io.protocol.grpc09.client;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;


@Slf4j
public class Grpc09Client {

    static final String HOSTNAME="localhost";
    static final int PORT=8080;

    public static void main(String[] args) throws InterruptedException {
        ManagedChannel managedChannel= ManagedChannelBuilder.forAddress(HOSTNAME,PORT)
                .usePlaintext()
                .build();

        //Note :
        //We use Stub not BlockingStub
        //Because BlockingStub doesn't implement rpc method when we have bidirectional streaming





        //Shutdown channel
        managedChannel.shutdown();
    }



}
