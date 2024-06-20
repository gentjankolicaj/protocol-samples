package io.protocol.grpc10.client;

import io.grpc.*;
import io.protocol.grpc10.*;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;


@Slf4j
public class Grpc10ClientTls {

    static final String HOSTNAME = "localhost";
    static final int PORT = 50051;

    public static void main(String[] args) throws InterruptedException, IOException {
        //Credentials for channel
        ChannelCredentials channelCredentials=TlsChannelCredentials.newBuilder()
                .trustManager(
                        new File("ca.crt")
                ).build();


        //Build channel with CA trust certificate
        ManagedChannel managedChannel =Grpc.newChannelBuilderForAddress(HOSTNAME, PORT,channelCredentials)
                .build();

        //Note :
        //We use Stub for streaming
        //We use BlockingStub for Unary
        callSqrt(managedChannel);
        callSqrtWithDeadline(managedChannel);


        //Shutdown channel
        managedChannel.shutdown();
    }

    public static void callSqrtWithDeadline(ManagedChannel managedChannel) {
        log.info("\n----------------------------------------------------------\n");
        log.info("Called callSqrtWithDeadline()");

        //Create a blocking stub because is unary rpc
        CalculatorServiceGrpc.CalculatorServiceBlockingStub stub = CalculatorServiceGrpc.newBlockingStub(managedChannel);

        //Call sqrt RPC inside try-catch because it might throw error Status.ILLEGAL_ARGUMENT
        double[] array = {1, 2, 3, -4, 5, 6, 7, 8, 0, -1, 10, -11};
        for (double var : array) {
            try {

                //Build request
                SqrtRequest sqrtRequest = SqrtRequest.newBuilder().setNumber(var).build();

                //Each RPC call has its own deadline
                //Declare deadline details
                long duration = 4000;
                TimeUnit timeUnit = TimeUnit.MILLISECONDS;
                Deadline deadline = Deadline.after(duration, timeUnit);

                //Call sqrtWithDeadline RPC
                SqrtResponse response = stub.withDeadline(deadline).sqrtWithDeadline(sqrtRequest);
                log.info("Response sqrt : {} ", response.getValue());

            } catch (StatusRuntimeException sre) {
                if (sre.getStatus().getCode() == Status.Code.DEADLINE_EXCEEDED) {
                    log.error("Deadline-exceeded: {} | {} ", var,sre);
                } else {
                    log.error("Error from stub : {} | {} . {} ", var, sre.getStatus(), sre.getMessage());
                }
            }
        }
    }


    public static void callSqrt(ManagedChannel managedChannel) {
        //Create a blocking stub because is unary rpc
        CalculatorServiceGrpc.CalculatorServiceBlockingStub stub = CalculatorServiceGrpc.newBlockingStub(managedChannel);

        //Call sqrt RPC inside try-catch because it might throw error Status.ILLEGAL_ARGUMENT
        double[] array = {1, 2, 3, -4, 5, 6, 7, 8, 0, -1, 10, -11};
        for (double var : array) {
            try {

                SqrtRequest sqrtRequest = SqrtRequest.newBuilder().setNumber(var).build();
                SqrtResponse response = stub.sqrt(sqrtRequest);
                log.info("Response sqrt : {} ", response.getValue());

            } catch (StatusRuntimeException sre) {
                log.error("Error from stub : {} | {} . {} ", var, sre.getStatus(), sre.getMessage());
            }
        }
    }


}
