package com.twitter.common.examples.distance_protobuf;

import com.twitter.common.examples.distance.Distances;

class DistanceProtobuf {

  private DistanceProtobuf() {
  }

  public static void main(String[] args) {
      System.out.println(Distances.Distance.newBuilder().setNumber(12).setUnit("parsecs").build());
  }
}