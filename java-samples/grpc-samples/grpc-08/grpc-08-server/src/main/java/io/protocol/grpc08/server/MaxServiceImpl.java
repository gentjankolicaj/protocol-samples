package io.protocol.grpc08.server;


import io.grpc.stub.StreamObserver;
import io.protocol.grpc08.MaxRequest;
import io.protocol.grpc08.MaxResponse;
import io.protocol.grpc08.MaxServiceGrpc;
import java.util.ArrayList;
import java.util.List;

public class MaxServiceImpl extends MaxServiceGrpc.MaxServiceImplBase {

  @Override
  public StreamObserver<MaxRequest> max(StreamObserver<MaxResponse> responseObserver) {
    //Do something before return request stream

    return new StreamObserver<MaxRequest>() {
      final List<Float> numbers = new ArrayList<>();

      @Override
      public void onNext(MaxRequest maxRequest) {
        //Each time e request comes

        //Add number to list & find max
        numbers.add(maxRequest.getNumber());
        Float maxNumber = numbers.stream().max((a, b) -> a.compareTo(b)).get();

        //Send response to client
        responseObserver.onNext(MaxResponse.newBuilder().setMax(maxNumber).build());

      }

      @Override
      public void onError(Throwable throwable) {
        //Clear list
        numbers.clear();
        responseObserver.onError(throwable);
      }

      @Override
      public void onCompleted() {
        responseObserver.onCompleted();
      }
    };

  }
}
