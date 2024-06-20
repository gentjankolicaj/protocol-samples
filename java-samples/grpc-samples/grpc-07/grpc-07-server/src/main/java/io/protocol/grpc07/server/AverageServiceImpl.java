package io.protocol.grpc07.server;

import io.grpc.stub.StreamObserver;
import io.protocol.grpc07.AverageRequest;
import io.protocol.grpc07.AverageResponse;
import io.protocol.grpc07.AverageServiceGrpc;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class AverageServiceImpl extends AverageServiceGrpc.AverageServiceImplBase {

  @Override
  public StreamObserver<AverageRequest> average(StreamObserver<AverageResponse> responseObserver) {
    return new StreamObserver<AverageRequest>() {
      int total = 0;
      int counter = 0;

      @Override
      public void onNext(AverageRequest averageRequest) {
        log.info("Received: {} ", averageRequest);
        total += averageRequest.getNumber();
        counter++;
      }

      @Override
      public void onError(Throwable throwable) {
        responseObserver.onError(throwable);

      }

      @Override
      public void onCompleted() {
        int average = total / counter;
        log.info("Sending average : {} ", average);
        //Sent average
        responseObserver.onNext(AverageResponse.newBuilder().setAverage(average).build());

        //Close connection
        responseObserver.onCompleted();
      }
    };

  }
}
