package io.protocol.grpc04.server;

import com.google.protobuf.Timestamp;
import io.grpc.stub.StreamObserver;
import io.protocol.grpc04.ChatMessage;
import io.protocol.grpc04.ChatMessageFromServer;
import io.protocol.grpc04.ChatServiceGrpc;
import lombok.extern.slf4j.Slf4j;

import java.util.LinkedHashSet;

@Slf4j
public class ChatServiceImpl extends ChatServiceGrpc.ChatServiceImplBase {
    private final LinkedHashSet<StreamObserver<ChatMessageFromServer>> observers;

    public ChatServiceImpl(){
        observers=new LinkedHashSet<>();
    }

    @Override
    public StreamObserver<ChatMessage> chat(StreamObserver<ChatMessageFromServer> responseObserver) {
        //Add response observers , to be used later for sending form server to clients
        observers.add(responseObserver);


        //Implement server listener & incoming messages from client are dealt here
        return new StreamObserver<ChatMessage>() {
            @Override
            public void onNext(ChatMessage chatMessage) {
                //On receiving messages from client

                //Get message from client & build server message object
                ChatMessageFromServer chatMessageFromServer=ChatMessageFromServer.newBuilder()
                                .setMessage(chatMessage)
                                        .build();

                //Sent message received to all existing connections opened
                observers.stream().forEach(o->{o.onNext(chatMessageFromServer);});

            }

            @Override
            public void onError(Throwable throwable) {
               //On error while receiving from client
                //Remove connection we don't sent messages to it
                observers.remove(responseObserver);
            }

            @Override
            public void onCompleted() {
              //On completed from receiving from client
                observers.remove(responseObserver);
            }
        };
    }
}
