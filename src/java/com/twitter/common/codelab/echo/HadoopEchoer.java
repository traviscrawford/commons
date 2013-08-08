package com.twitter.common.codelab.echo;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IOUtils;

public class HadoopEchoer implements Echoer {
  @Override
  public String getEchoString() {
    try {
      Configuration conf = new Configuration();
      Path p = new Path("file:///Users/travis/src/commons/codelab/echo.txt");
      FSDataInputStream in = p.getFileSystem(conf).open(p);
      ByteArrayOutputStream out = new ByteArrayOutputStream();
      IOUtils.copyBytes(in, out, conf);
      return out.toString();
    } catch (IOException e) {
      throw new RuntimeException();
    }
  }
}
