package com.twitter.common.codelab.echo;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FileEchoer implements Echoer {

  @Override
  public String getEchoString() {
    try {
      // TODO(travis): Pass yaml to configure?
      Path p = Paths.get("/Users/travis/src/commons/codelab/echo.txt");
      return new String(Files.readAllBytes(p));
    } catch (FileNotFoundException e) {
      throw new RuntimeException();
    } catch (IOException e) {
      throw new RuntimeException();
    }
  }
}
