package com.twitter.common.myapp.echo;

import com.twitter.common.codelab.echo.Echoer;

public class StaticEchoer implements Echoer {
  @Override
  public String getEchoString() {
    return "tall cat is tall";
  }
}
