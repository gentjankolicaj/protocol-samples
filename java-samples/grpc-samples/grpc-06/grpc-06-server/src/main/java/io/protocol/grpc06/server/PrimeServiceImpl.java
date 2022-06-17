package io.protocol.grpc06.server;


import io.grpc.stub.StreamObserver;
import io.protocol.grpc06.PrimeRequest;
import io.protocol.grpc06.PrimeResponse;
import io.protocol.grpc06.PrimeServiceGrpc;

public class PrimeServiceImpl extends PrimeServiceGrpc.PrimeServiceImplBase {
    @Override
    public void primes(PrimeRequest request, StreamObserver<PrimeResponse> responseObserver) {
        try{
            int counter=0;
            int max=10000000;
            while(counter<=max){
                responseObserver.onNext(PrimeResponse.newBuilder().setValue(counter).build());
                counter++;
                Thread.sleep(2);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        responseObserver.onCompleted();
    }
}
