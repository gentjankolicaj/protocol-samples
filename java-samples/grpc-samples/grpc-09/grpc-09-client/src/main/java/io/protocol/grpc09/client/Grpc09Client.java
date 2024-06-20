package io.protocol.grpc09.client;

import io.grpc.Deadline;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.Status;
import io.grpc.StatusRuntimeException;
import io.protocol.grpc09.CalculatorServiceGrpc;
import io.protocol.grpc09.SqrtRequest;
import io.protocol.grpc09.SqrtResponse;
import java.util.concurrent.TimeUnit;
import lombok.extern.slf4j.Slf4j;


@Slf4j
public class Grpc09Client {

  static final String HOSTNAME = "localhost";
  static final int PORT = 8080;

  public static void main(String[] args) throws InterruptedException {
    ManagedChannel managedChannel = ManagedChannelBuilder.forAddress(HOSTNAME, PORT)
        .usePlaintext()
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
    CalculatorServiceGrpc.CalculatorServiceBlockingStub stub = CalculatorServiceGrpc.newBlockingStub(
        managedChannel);

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
          log.error("Deadline-exceeded: {} | {} ", var, sre);
        } else {
          log.error("Error from stub : {} | {} . {} ", var, sre.getStatus(), sre.getMessage());
        }
      }
    }
  }


  public static void callSqrt(ManagedChannel managedChannel) {
    //Create a blocking stub because is unary rpc
    CalculatorServiceGrpc.CalculatorServiceBlockingStub stub = CalculatorServiceGrpc.newBlockingStub(
        managedChannel);

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
