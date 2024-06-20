package io.protocol.blog.server.db;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import java.util.List;
import java.util.Optional;

public abstract class GenericDao<T, I> {


  public static MongoClient mongoClient;

  static {
    //Init mongo client object when class is loaded into memory
    mongoClient = init();

    //Add shutdown hook to close mongo connection when jvm gets termination signal
    Runtime.getRuntime().addShutdownHook(new Thread(() -> {
      destroy();
    }));
  }

  static MongoClient init() {
    String connectionString = "mongodb://johndoe:johndoe@localhost:27017/";
    MongoClient mongoClient = MongoClients.create(connectionString);
    return mongoClient;
  }

  static void destroy() {
    mongoClient.close();
  }

  abstract List<T> findAll();

  abstract Optional<T> find(I id);

  abstract T save(T t);

  abstract void update(T t);

  abstract void delete(T t);


}
