package io.protocol.grpc08.client;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;
import io.protocol.grpc08.MaxRequest;
import io.protocol.grpc08.MaxResponse;
import io.protocol.grpc08.MaxServiceGrpc;
import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;


@Slf4j
public class Grpc08Client {

    static final String HOSTNAME="localhost";
    static final int PORT=8080;

    public static void main(String[] args) throws InterruptedException {
        ManagedChannel managedChannel= ManagedChannelBuilder.forAddress(HOSTNAME,PORT)
                .usePlaintext()
                .build();

        //Note :
        //We use Stub not BlockingStub
        //Because BlockingStub doesn't implement rpc method when we have bidirectional streaming
        MaxServiceGrpc.MaxServiceStub stub=MaxServiceGrpc.newStub(managedChannel);

        //Create countdown latch to keep main thread from shutting down
        //Countdown latch keeps main thread from exit till 'fromServerStream.onCompleted())' is called
        CountDownLatch countDownLatch=new CountDownLatch(1);

        StreamObserver<MaxResponse> fromServerStream=new StreamObserver<MaxResponse>() {
            @Override
            public void onNext(MaxResponse maxResponse) {
                log.info(" {}",maxResponse.getMax());
            }

            @Override
            public void onError(Throwable throwable) {
                log.error("{}",throwable);

                //count down to allow shutting down
                countDownLatch.countDown();

            }

            @Override
            public void onCompleted() {
                //count down to allow shutting down
                countDownLatch.countDown();
            }
        };


        //Call RPC max()
        StreamObserver<MaxRequest> toServerStream=stub.max(fromServerStream);

        //Create a list of numbers
        List<Float> numbers= Arrays.asList(1.0f,2.2f,3.2f,4.3f,20f,10f,222f,2f,23f,322f,2f,20f,220f,0f);

        //Sent a stream of floats to server
        numbers.stream().forEach(e->toServerStream.onNext(MaxRequest.newBuilder().setNumber(e).build()));

        //Call toServerStream.onCompleted() to finish
        toServerStream.onCompleted();

        //Wait with countdown latch
        countDownLatch.await(10,TimeUnit.SECONDS);

        //Shutdown channel
        managedChannel.shutdown();
    }



}
