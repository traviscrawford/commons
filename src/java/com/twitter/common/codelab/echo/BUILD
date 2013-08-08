java_library(name='echo',
  dependencies=[
    pants('3rdparty:snakeyaml'),
    pants('src/java/com/twitter/common/application'),
  ],
  sources=globs('*.java'),
)

jvm_binary(name='echo-bin',
  main = 'com.twitter.common.codelab.echo.EchoServer',
  dependencies = [
    ':echo',
  ],
)
