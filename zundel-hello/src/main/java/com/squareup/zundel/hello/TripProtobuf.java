package com.squareup.zundel.hello;

import com.squareup.zundel.hello.distance.Distances;
import com.squareup.zundel.hello.trip.Trips;

class TripProtobuf {

  private TripProtobuf() {
  }

  public static void main(String[] args) {
      Distances.Distance distance = Distances.Distance.newBuilder()
          .setNumber(12 * (long)Math.pow(10,8)).setUnit("parsecs").build();
      //System.out.println(distance);
      Trips.Trip trip = Trips.Trip.newBuilder().setDestination("delta quadrant").setDistance(distance).build();
      System.out.println(trip);
  }
}
