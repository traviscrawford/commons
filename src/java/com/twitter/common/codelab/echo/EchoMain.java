package com.twitter.common.codelab.echo;

import java.nio.file.Files;
import java.nio.file.Paths;

public final class EchoMain {

  private EchoMain() {
  }

  /**
   * First arg is path of the configuration file.
   */
  public static void main(String[] args) throws Exception {
      String classStr =
          new String(Files.readAllBytes(Paths.get(args[0])));
      System.out.println("Using echoer: " + classStr);
      Class clazz = Class.forName(classStr.trim());
      Echoer echoer = (Echoer) clazz.newInstance();
      System.out.println(echoer.getEchoString());
  }
}
