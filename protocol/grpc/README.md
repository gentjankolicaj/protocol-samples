# About gRPC

Source of info below is https://grpc.io

In gRPC a client application can directly call a method on server on a different machines as if ti
were local object , making it easier for you to create distributed application and services.
As in many RPC systems , gRPC is based around the idea of defining a service , specifying methods
that can be called remotely by their parameters and returns types.
On the server side the server implements this interface and run gRPC server to handle client
calls.On client side client has a stub (referred to as just a client in some languages) that
provides the same
methods as the server.

![](../../img/grpc_0.png)

Steps to working with protocol buffers :

1.Define structure for data you want to serialize in proto file (Proto file is an ordinary text file
with .proto extension)

2.Protocol buffered data is structured as message, where each message is a small logical record of
information containg a series of name-value paries called fields.

```
message Person{
int64 id=1
string name=2
string surname=3
string username=4
string password=5
bool is_active=6
}
```

3.Once specified data structures , you use protocol buffer compiler 'protoc' to generate data access
classes in preferred language from proto definition.

4.Data access classes provide :

- Simple accessor to fields
- Methods to serialize/parse the whole structure to/from raw bytes

5.You define gRPC service in proto files, with GRP method parameters & return types of specified as
protocol buffer message

```
// The greeter service definition.
service Greeter {
// Sends a greeting
rpc SayHello (HelloRequest) returns (HelloReply) {}
}

// The request message containing the user's name.
message HelloRequest {
string name = 1;
}

// The response message containing the greetings
message HelloReply {
string message = 1;
}
```

6.gRPC uses protoc with special gRPC plugin to generate code from .proto file.

## Advantages of working with protocol buffers :

- Efficient serialization
- A simple IDL
- Easy interface updating.

## Types of gRPC methods :

- A simple RPC where client sends a request to server using stub and waits for a response to come
  back, just like normal function call.

```
rpc PrintName(PrintRequest) returns (PrintResponse){}

message PrintRequest{
string content=1;
}

message PrintResponse{
string content=1;
int32 status=2;
}
```

<br>

- A server-side streaming RPC where client sends a request to the server and gets a stream to read a
  sequence of messages back.The client reads from returned stream
  until there are no more messages.As you can see below you specify a server-side streaming method
  by placing stream keyword before 'response' of method.

```
rpc PrintName(PrintRequest) returns (stream PrintResponse){}

message PrintRequest{
string content=1;
}

message PrintResponse{
string content=1;
int32 status=2;
}
```

- A client-side streaming RPC where the client writes a sequence of messages and sends them to
  server, again using a provided stream.Once the client has finished writing the message,
  it waits for the server to read them all and returns its response.You specify a client-side
  streaming method by placing stream
  keyword before 'request' type of method.

```
rpc PrintName(stream PrintRequest) returns (PrintResponse){}

message PrintRequest{
string content=1;
}

message PrintResponse{
string content=1;
int32 status=2;
}
```

- A bidirectional streaming RPC where both sides send a sequences of messages using read-write
  stream.
  The two streams operate independently, so clients & servers can read and write int whatever order
  they like.For example the server could wait
  to receive all the client messages before writing its response or it could alternatively read a
  message then write a message, or some other combination or reads and writes.
  The order or messages in each stream is preserved.You specify this type of method by placing
  stream keyword before both request & response types.

```
rpc PrintName(stream PrintRequest) returns (PrintResponse){}

message PrintRequest{
string content=1;
}

message PrintResponse{
string content=1;
int32 status=2;
}
```

## Note:

- Everything on server-side is implemented as async by default.
- It is decision of a client whether to block or not.



