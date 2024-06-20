package io.protocol.grpc06.server;


import io.grpc.stub.StreamObserver;
import io.protocol.grpc06.PrimeRequest;
import io.protocol.grpc06.PrimeResponse;
import io.protocol.grpc06.PrimeServiceGrpc;

public class PrimeServiceImpl extends PrimeServiceGrpc.PrimeServiceImplBase {

  @Override
  public void primes(PrimeRequest request, StreamObserver<PrimeResponse> responseObserver) {
    try {
      int divisor = 2;
      int number = request.getNumber();
      while (number > 1) {
        if (number % divisor == 0) {
          number = number / divisor;
          responseObserver.onNext(PrimeResponse.newBuilder().setPrimeFactor(divisor).build());
        } else {
          divisor = divisor + 1;
        }
      }
    } catch (Exception e) {
      responseObserver.onError(e);
    }
    responseObserver.onCompleted();
  }
}
