package io.protocol.grpc03.server;

import io.grpc.stub.StreamObserver;
import io.protocol.grpc03.GreetingServiceGrpc;
import io.protocol.grpc03.HelloWorldRequest;
import io.protocol.grpc03.HelloWorldResponse;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class GreetingServiceImpl extends GreetingServiceGrpc.GreetingServiceImplBase {


    //Its async operation by default
    //gRPC server has async response by default
    @Override
    public void greeting(HelloWorldRequest request, StreamObserver<HelloWorldResponse> responseObserver) {
        HelloWorldResponse helloWorldResponse = HelloWorldResponse.newBuilder()
                .setGreeting("Hello world from " + request.getFirstname() + "-" + request.getLastname()).build();

        log.info("Received request from client {}",request);
        log.info("Sending response to client {} ",request);
        //Sent response to server , we sent one payload
        responseObserver.onNext(helloWorldResponse);

        //We have to call explicitly onCompleted() because the stream is still open
        //If we don't call this , the client will keep connection open even though we are sending 1 response
        responseObserver.onCompleted();
    }
}
