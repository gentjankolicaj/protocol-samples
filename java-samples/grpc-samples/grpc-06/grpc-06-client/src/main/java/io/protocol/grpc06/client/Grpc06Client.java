package io.protocol.grpc06.client;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.protocol.grpc06.PrimeRequest;
import io.protocol.grpc06.PrimeResponse;
import io.protocol.grpc06.PrimeServiceGrpc;
import lombok.extern.slf4j.Slf4j;

import java.util.Iterator;
import java.util.Random;

@Slf4j
public class Grpc06Client {

    static final String HOSTNAME="localhost";
    static final int PORT=8080;

    public static void main(String[] args){
        ManagedChannel managedChannel= ManagedChannelBuilder.forAddress(HOSTNAME,PORT)
                .usePlaintext()
                .build();


        PrimeServiceGrpc.PrimeServiceBlockingStub stub=PrimeServiceGrpc.newBlockingStub(managedChannel);
        Iterator<PrimeResponse> responseIterator=stub.primes(PrimeRequest.newBuilder().build());
        responseIterator.forEachRemaining(c->System.out.println(c));


        //Shutdown channel
        managedChannel.shutdown();
    }

}
