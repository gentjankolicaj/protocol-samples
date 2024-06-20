package io.protocol.grpc02.client;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.protocol.grpc02.Feature;
import io.protocol.grpc02.Point;
import io.protocol.grpc02.RouteGuideGrpc;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Grpc02Client {

  static final String ADDRESS = "localhost";
  static final int PORT = 7070;

  public static void main(String[] args) {
    log.info("Building managed channel...");
    ManagedChannel managedChannel = ManagedChannelBuilder.forAddress(ADDRESS, PORT)
        .usePlaintext()
        .build();

    //Create stub
    log.info("Creating blocking stub ...");
    RouteGuideGrpc.RouteGuideBlockingStub blockingStub = RouteGuideGrpc.newBlockingStub(
        managedChannel);

    //Build request
    Point request = Point.newBuilder()
        .setLatitude(101)
        .setLongitude(202)
        .build();

    //Send request
    Feature feature = blockingStub.getFeature(request);

    log.info("Response receive {}", feature);

  }
}
