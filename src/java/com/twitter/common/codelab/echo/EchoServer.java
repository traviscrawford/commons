package com.twitter.common.codelab.echo;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import com.twitter.common.application.AbstractApplication;
import com.twitter.common.application.AppLauncher;
import com.twitter.common.args.Arg;
import com.twitter.common.args.CmdLine;
import com.twitter.common.args.constraints.NotNull;

public class EchoServer extends AbstractApplication {

  @NotNull
  @CmdLine(name = "config", help = "Path of the configuration file.")
  private static final Arg<File> CONFIG_FILE = Arg.create(null);

  @Override
  public void run() {
    try {
      String classStr =
          new String(Files.readAllBytes(Paths.get(CONFIG_FILE.get().getPath())));
      System.out.println("Using echoer: " + classStr);
      Class clazz = Class.forName(classStr.trim());
      Echoer echoer = (Echoer) clazz.newInstance();
      System.out.println(echoer.getEchoString());
    } catch (ClassNotFoundException e) {
      throw new RuntimeException();
    } catch (InstantiationException e) {
      throw new RuntimeException();
    } catch (IllegalAccessException e) {
      throw new RuntimeException();
    } catch (IOException e) {
      throw new RuntimeException();
    }
  }

  public static void main(String[] args) {
    AppLauncher.launch(EchoServer.class, args);
  }
}
