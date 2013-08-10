package com.twitter.common.codelab.echo;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class FileEchoer implements Echoer {

  @Override
  public String getEchoString() {
    try {
      FileReader fileReader = new FileReader("/Users/travis/src/commons/codelab/echo.txt");
      BufferedReader bufferedReader = new BufferedReader(fileReader);
      return bufferedReader.readLine();
    } catch (FileNotFoundException e) {
      throw new RuntimeException();
    } catch (IOException e) {
      throw new RuntimeException();
    }
  }
}
