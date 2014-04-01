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


jvm_binary(
  name = 'trip-protobuf-example',
  source = './src/main/java/com/squareup/zundel/hello/TripProtobuf.java' ,
  dependencies = [
    'zundel-hello/src/main/protobuf/squareup/zundel/hello',
  ],
  main='com.squareup.zundel.hello.TripProtobuf',
)
