package com.squareup.zundel.hello;

import java.util.List;

import com.google.common.base.Joiner;
import com.google.common.collect.Lists;

/**
 * Sample library for Pants evaluation
 */
public final class Hello {
  public Hello() {
  }

  /**
   * prints "Hello World!" to stdout.
   */
  public void run() {
    // Add in a guava dependency
    List<String> stuff = Lists.newArrayList();
    stuff.add("Hello");
    stuff.add("World!");
    stuff.add("using 3rdparty library ");

    System.out.println(Joiner.on(" ").join(stuff));


  }
}
