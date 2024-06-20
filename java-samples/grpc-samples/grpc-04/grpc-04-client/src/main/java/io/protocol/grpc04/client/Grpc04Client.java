package io.protocol.grpc04.client;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;


import io.grpc.stub.StreamObserver;
import io.protocol.grpc04.ChatMessage;
import io.protocol.grpc04.ChatMessageFromServer;
import io.protocol.grpc04.ChatServiceGrpc;
import lombok.extern.slf4j.Slf4j;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.UUID;

@Slf4j
public class Grpc04Client {
    static final String ADDRESS = "localhost";
    static final int PORT = 8080;

    private static JFrame jFrame;
    private static JPanel parentPanel;
    private static JButton sendBtn;
    private static JTextField inputFld;
    private static JTextField toFld;
    private static JTextArea messageArea;

    public static void main(String[] args) {
        UUID uuid=UUID.randomUUID();
        String uuidStr=uuid.toString();

        initGui();
        log.info("Gui created...");

        ManagedChannel managedChannel = ManagedChannelBuilder.forAddress(ADDRESS, PORT)
                .usePlaintext() //use plaintext not tls
                .build();

        //Create chat service stub
        ChatServiceGrpc.ChatServiceStub chatServiceStub = ChatServiceGrpc.newStub(managedChannel);

        //Listener for messages from server to client , implementing a callback interface
        StreamObserver<ChatMessageFromServer> fromServerStreamObserver = new StreamObserver<ChatMessageFromServer>() {
            @Override
            public void onNext(ChatMessageFromServer chatMessageFromServer) {
                //Read from server
                log.info("Message received : {}", chatMessageFromServer);
                ChatMessage chatMessage=chatMessageFromServer.getMessage();
                messageArea.append("from : "+chatMessage.getFrom()+" | to : "+chatMessage.getTo()+" ~ ");
            }

            @Override
            public void onError(Throwable throwable) {
                //If something happens while listening from server
                throwable.printStackTrace();
            }

            @Override
            public void onCompleted() {

            }
        };


        //Returns a stream observer,we use this stream to send message from client to server
        StreamObserver<ChatMessage> toServerStreamObserver = chatServiceStub.chat(fromServerStreamObserver);


        //Anonymous class implementation for action listener
        sendBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //Read input
                String input=inputFld.getText();
                String to=toFld.getText();

                inputFld.setText("");
                toFld.setText("");

                //Build message
                ChatMessage chatMessage = ChatMessage.newBuilder()
                        .setFrom(uuidStr)
                        .setTo(to)
                        .setMessage(input)
                        .build();


                //We use streamObserver to send messages from client to server
                toServerStreamObserver.onNext(chatMessage);
            }
        });


    }

    static void initGui(){
        parentPanel=new JPanel();
        parentPanel.setLayout(new BorderLayout());

        JPanel textPanel=new JPanel();
        messageArea=new JTextArea();
        messageArea.setColumns(20);
        messageArea.setRows(10);
        textPanel.add(messageArea, BorderLayout.CENTER);

        JPanel inputPanel=new JPanel();
         inputFld=new JTextField();
         inputFld.setSize(new Dimension(400,30));
         toFld=new JTextField();
         toFld.setSize(new Dimension(400,30));
         sendBtn=new JButton("Send");
         sendBtn.setSize(new Dimension(400,30));
         inputPanel.setLayout(new FlowLayout());
         inputPanel.add(toFld);
         inputPanel.add(inputFld);
         inputPanel.add(sendBtn);

         parentPanel.add(textPanel,BorderLayout.CENTER);
         parentPanel.add(inputPanel,BorderLayout.SOUTH);

        jFrame=new JFrame();
        jFrame.setContentPane(parentPanel);
        jFrame.setTitle("gRPC-client");
        jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jFrame.setSize(new Dimension(800,600));
        jFrame.setVisible(true);
        jFrame.pack();
    }
}
