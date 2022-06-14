package io.protocol.grpc05.client;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.protocol.grpc05.*;
import lombok.extern.slf4j.Slf4j;

import java.util.Random;

@Slf4j
public class Grpc05Client {

    static final String HOSTNAME="localhost";
    static final int PORT=8080;

    public static void main(String[] args){

        ManagedChannel managedChannel= ManagedChannelBuilder.forAddress(HOSTNAME,PORT)
                .usePlaintext()
                .build();


        if(args==null || args.length==0){
            managedChannel.shutdown();
            return;
        }

        log.info("Passed args validation.");
        switch(args[0]){
            case "greet": performGreet(managedChannel);
            case "calc": performCalc(managedChannel);
            default: performCalc(managedChannel);
        }


        //Shutdown channel
        managedChannel.shutdown();
    }

    static void performGreet(ManagedChannel managedChannel){
        GreetingServiceGrpc.GreetingServiceBlockingStub blockingStub=GreetingServiceGrpc.newBlockingStub(managedChannel);
        GreetingRequest request=GreetingRequest.newBuilder()
                .setContent("Hello world.")
                .build();

        log.info("Greet-request : {}",request);
        GreetingResponse response=blockingStub.greet(request);
        log.info("Greet-response : {}",response);

    }

    static void performCalc(ManagedChannel managedChannel){
        CalculatorServiceGrpc.CalculatorServiceBlockingStub serviceStub=CalculatorServiceGrpc.newBlockingStub(managedChannel);
        long a=10;
        long b=10;

        Random random=new Random();
        int randomValue=random.nextInt(4);
        int counter=0;
        while(counter<randomValue) {
            switch (randomValue) {
                case 0: {
                    CalcRequest calcRequest = CalcRequest.newBuilder()
                            .setA(a)
                            .setB(b).build();
                    CalcResponse calcResponse = serviceStub.add(calcRequest);
                    log.info("Request {} | Response {} ", calcResponse);
                }
                case 1: {
                    CalcRequest calcRequest = CalcRequest.newBuilder()
                            .setA(a)
                            .setB(b).build();
                    CalcResponse calcResponse = serviceStub.sub(calcRequest);
                    log.info("Request {} | Response {} ", calcResponse);
                }
                case 2: {
                    CalcRequest calcRequest = CalcRequest.newBuilder()
                            .setA(a)
                            .setB(b).build();
                    CalcResponse calcResponse = serviceStub.mul(calcRequest);
                    log.info("Request {} | Response {} ", calcResponse);
                }
                case 3: {
                    CalcRequest calcRequest = CalcRequest.newBuilder()
                            .setA(a)
                            .setB(b).build();
                    CalcResponse calcResponse = serviceStub.div(calcRequest);
                    log.info("Request {} | Response {} ", calcResponse);
                }
            }
            counter++;
        }
    }
}
