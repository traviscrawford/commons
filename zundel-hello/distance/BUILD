java_protobuf_library( 
  name = 'proto',
  sources = [
    './src/main/proto/squareup/zundel/hello/distance/distances.proto'
  ]
)

jvm_binary(
  name = 'distance-protobuf-example',
  source =  './src/main/java/com/squareup/zundel/hello/DistanceProtobuf.java',
  dependencies = [
    pants(':proto'),
  ],
  main = 'com.squareup.zundel.hello.DistanceProtobuf',
)
