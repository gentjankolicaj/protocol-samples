package io.protocol.grpc03.client;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

import io.protocol.grpc03.GreetingServiceGrpc;
import io.protocol.grpc03.HelloWorldRequest;
import io.protocol.grpc03.HelloWorldResponse;
import io.protocol.grpc03.Sentiment;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Grpc03Client {
    static final String ADDRESS = "localhost";
    static final int PORT = 8080;

    public static void main(String[] args) {
        ManagedChannel managedChannel = ManagedChannelBuilder.forAddress(ADDRESS, PORT)
                .usePlaintext() //use plaintext not tls
                .build();

        //I give connection to stub
        GreetingServiceGrpc.GreetingServiceBlockingStub blockingStub = GreetingServiceGrpc.newBlockingStub(managedChannel);

        //Build request
        HelloWorldRequest helloWorldRequest = HelloWorldRequest.newBuilder()
                .setFirstname("John")
                .setLastname("Doe")
                .setAge(27)
                .setSentiment(Sentiment.HAPPY)
                .putBagOfTricks("key", "Coding gRPC")
                .build();

        //Call rpc
        log.info("Sub calling rpc with request {}",helloWorldRequest);
        HelloWorldResponse helloWorldResponse=blockingStub.greeting(helloWorldRequest);
        log.info("Sub received rpc response {}",helloWorldResponse);
    }
}
