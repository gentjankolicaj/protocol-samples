package io.protocol.grpc05.server;

import io.grpc.stub.StreamObserver;
import io.protocol.grpc05.CalcRequest;
import io.protocol.grpc05.CalcResponse;
import io.protocol.grpc05.CalculatorServiceGrpc;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CalculatorServiceImpl extends CalculatorServiceGrpc.CalculatorServiceImplBase {
    @Override
    public void add(CalcRequest request, StreamObserver<CalcResponse> responseObserver) {
       log.info("Add() : {}",request);
       long result=request.getA()+request.getB();

       CalcResponse response=CalcResponse.newBuilder()
               .setResult(result)
               .setRequest(request)
               .build();

       responseObserver.onNext(response);
    }

    @Override
    public void sub(CalcRequest request, StreamObserver<CalcResponse> responseObserver) {
        log.info("Sub() : {}",request);
        long result=request.getA()-request.getB();

        CalcResponse response=CalcResponse.newBuilder()
                .setResult(result)
                .setRequest(request)
                .build();

        responseObserver.onNext(response);
    }

    @Override
    public void mul(CalcRequest request, StreamObserver<CalcResponse> responseObserver) {
        log.info("Mul() : {}",request);
        long result=request.getA()*request.getB();

        CalcResponse response=CalcResponse.newBuilder()
                .setResult(result)
                .setRequest(request)
                .build();

        responseObserver.onNext(response);
    }

    @Override
    public void div(CalcRequest request, StreamObserver<CalcResponse> responseObserver) {
        log.info("Div() : {}",request);
        long result=request.getA()/request.getB();

        CalcResponse response=CalcResponse.newBuilder()
                .setResult(result)
                .setRequest(request)
                .build();

        responseObserver.onNext(response);
    }
}
