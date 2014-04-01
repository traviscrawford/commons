package com.squareup.zundel.hello;

import com.squareup.zundel.hello.distance.Distances;

class DistanceProtobuf {

  private DistanceProtobuf() {
  }

  public static void main(String[] args) {
      System.out.println(Distances.Distance.newBuilder().setNumber(12).setUnit("parsecs").build());
  }
}
