package io.protocol.grpc11.server;

import io.grpc.Context;
import io.grpc.Status;
import io.grpc.stub.StreamObserver;
import io.protocol.grpc11.*;

import java.util.ArrayList;
import java.util.DoubleSummaryStatistics;
import java.util.List;

public class CalculatorServiceImpl extends CalculatorServiceGrpc.CalculatorServiceImplBase{

    @Override
    public void primes(PrimeRequest request, StreamObserver<PrimeResponse> responseObserver) {
        super.primes(request, responseObserver);
    }

    @Override
    public StreamObserver<AverageRequest> average(StreamObserver<AverageResponse> responseObserver) {

        return new StreamObserver<AverageRequest>() {
            List<Double> numbers=new ArrayList<>();
            @Override
            public void onNext(AverageRequest averageRequest) {
                numbers.add(averageRequest.getNumber());
            }

            @Override
            public void onError(Throwable throwable) {
              responseObserver.onError(throwable);
            }

            @Override
            public void onCompleted() {
                int size=numbers.size();
                DoubleSummaryStatistics summaryStatistics= numbers.stream().mapToDouble(e->e.doubleValue()).summaryStatistics();

                //Sent response & close connection
                responseObserver.onNext(AverageResponse.newBuilder().setAverage(summaryStatistics.getAverage()).build());
                responseObserver.onCompleted();
            }
        };
    }

    @Override
    public StreamObserver<MaxRequest> max(StreamObserver<MaxResponse> responseObserver) {

        return new StreamObserver<MaxRequest>() {
            List<Double> numbers=new ArrayList<>();

            @Override
            public void onNext(MaxRequest maxRequest) {
                numbers.add(maxRequest.getNumber());

                //Find max
                double actualMax=numbers.stream().max((a,b)->a.compareTo(b)).get();

                //Send max
                responseObserver.onNext(MaxResponse.newBuilder().setMax(actualMax).build());
            }

            @Override
            public void onError(Throwable throwable) {
                //sent error to client
                responseObserver.onError(throwable);
            }

            @Override
            public void onCompleted() {
                responseObserver.onCompleted();;
            }
        };
    }

    @Override
    public void sqrt(SqrtRequest request, StreamObserver<SqrtResponse> responseObserver) {
        double number=request.getNumber();
        if(number<0){
            responseObserver.onError(Status.INVALID_ARGUMENT.asRuntimeException());
            //return method after sending error, no need to proceed down further
            return;
        }

        //Sent response and close connection
        responseObserver.onNext(SqrtResponse.newBuilder().setValue(Math.sqrt(number)).build());
        responseObserver.onCompleted();
    }

    @Override
    public void sqrtWithDeadline(SqrtRequest request, StreamObserver<SqrtResponse> responseObserver) {
        //Get current rpc context
        Context context=Context.current();
        try {
            for(int i=0;i<10;++i){
                //Check if request is cancelled by client
                if(context.isCancelled()){
                    //return sqrtWithDeadline
                    return;
                }
                Thread.sleep(100);
            }


            double number = request.getNumber();
            if (number < 0) {
                responseObserver.onError(Status.INVALID_ARGUMENT.asRuntimeException());
                //return method after sending error, no need to proceed down further
                return;
            }

            //Sent response and close connection
            responseObserver.onNext(SqrtResponse.newBuilder().setValue(Math.sqrt(number)).build());
            responseObserver.onCompleted();
        }catch (InterruptedException ie){
            responseObserver.onError(ie);
        }
    }
}
