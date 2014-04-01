
java_library(
  name = 'lib',
  sources = [ 
    './src/main/java/com/squareup/zundel/hello/Hello.java' 
  ],
  dependencies = [
    pants('3rdparty:guava'),
  ]
)

jvm_binary(
  name = 'main',
  source = './src/main/java/com/squareup/zundel/hello/Main.java',
  dependencies = [
    pants(':lib'),
  ],
  main = 'com.squareup.zundel.hello.Main',
) 

jvm_binary(
  name = 'simple-main',
  source = './src/main/java/com/squareup/zundel/hello/SimpleMain.java',
  main='com.squareup.zundel.hello.SimpleMain',
)

java_protobuf_library(
  name='proto',
  sources = [
    'src/main/proto/squareup/zundel/hello/trips.proto',
  ],
  dependencies = [
    pants('zundel-hello/distance:proto'),
  ]
)

jvm_binary(
  name = 'trip-protobuf-example',
  source = './src/main/java/com/squareup/zundel/hello/TripProtobuf.java' ,
  dependencies = [
     pants(':proto'),
  ],
  main='com.squareup.zundel.hello.TripProtobuf',
)
