package io.protocol.grpc07.client;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;
import io.protocol.grpc07.AverageRequest;
import io.protocol.grpc07.AverageResponse;
import io.protocol.grpc07.AverageServiceGrpc;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import lombok.extern.slf4j.Slf4j;


@Slf4j
public class Grpc07Client {

  static final String HOSTNAME = "localhost";
  static final int PORT = 8080;

  public static void main(String[] args) throws InterruptedException {
    ManagedChannel managedChannel = ManagedChannelBuilder.forAddress(HOSTNAME, PORT)
        .usePlaintext()
        .build();

    //Note :
    //We use Stub not BlockingStub
    //Because BlockingStub doesn't implement rpc method when client-side is streaming
    AverageServiceGrpc.AverageServiceStub stub = AverageServiceGrpc.newStub(managedChannel);

    //Implement callback interface for response stream
    //When response stream is finished , we decrease countdown
    CountDownLatch countDownLatch = new CountDownLatch(1);
    StreamObserver<AverageResponse> fromServerStream = new StreamObserver<AverageResponse>() {
      @Override
      public void onNext(AverageResponse averageResponse) {
        log.info("Server : average {} ", averageResponse.getAverage());
      }

      @Override
      public void onError(Throwable throwable) {
        log.info("Error {} ", throwable);
        log.info("Shutting down channel");

      }

      @Override
      public void onCompleted() {
        countDownLatch.countDown();
      }
    };

    StreamObserver<AverageRequest> toServerStream = stub.average(fromServerStream);

    int counter = 0;
    int max = 10;
    while (max > counter) {
      //Send request to request stream
      toServerStream.onNext(AverageRequest.newBuilder().setNumber(counter).build());

      //increment counter
      counter++;
    }
    //Complete sending stream
    //This allows server to call requestStreamObserver.onCompleted() implementation
    toServerStream.onCompleted();

    //Await for the fromServerStream.onCompleted() to be called
    countDownLatch.await(5, TimeUnit.SECONDS);

    //Shutdown channel
    managedChannel.shutdown();
  }


}
