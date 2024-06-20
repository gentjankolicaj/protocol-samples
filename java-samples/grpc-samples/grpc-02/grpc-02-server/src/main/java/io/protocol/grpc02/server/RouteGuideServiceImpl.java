package io.protocol.grpc02.server;

import io.grpc.stub.StreamObserver;
import io.protocol.grpc02.*;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class RouteGuideServiceImpl extends RouteGuideGrpc.RouteGuideImplBase{

    @Override
    public void getFeature(Point request, StreamObserver<Feature> responseObserver) {
        log.info("Request received {}"+request);

        //Build response
        Feature feature= Feature.newBuilder()
                .setName("Random-location")
                .setLocation(Point.newBuilder()
                        .setLatitude(23)
                        .setLongitude(34)
                        .build()
                ).build();

        //Print response
        log.info("Sending request {}",feature);

        //Since everything on server-side is async is used streamObserver
        responseObserver.onNext(feature);

        //When response is sent , tell client to close connection
        responseObserver.onCompleted();

    }

    @Override
    public void listFeature(Rectangle request, StreamObserver<Feature> responseObserver) {
        super.listFeature(request, responseObserver);
    }

    @Override
    public StreamObserver<Point> recordRoute(StreamObserver<RouteSummary> responseObserver) {
        return super.recordRoute(responseObserver);
    }

    @Override
    public StreamObserver<RouteNote> routeChat(StreamObserver<RouteNote> responseObserver) {
        return super.routeChat(responseObserver);
    }
}
