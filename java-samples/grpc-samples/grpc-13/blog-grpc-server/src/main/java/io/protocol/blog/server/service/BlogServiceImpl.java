package io.protocol.blog.server.service;

import io.protocol.blog.server.BlogServiceGrpc;
import io.protocol.blog.server.dao.BlogDaoImpl;

public class BlogServiceImpl extends BlogServiceGrpc.BlogServiceImplBase {

  private BlogDaoImpl blogDao;


}
