java_library(name='echo',
  dependencies=[
    pants('3rdparty:hadoop-core'),
    pants('src/java/com/twitter/common/application'),
  ],
  sources=globs('*.java'),
)

jvm_binary(name='echo-bin',
  main = 'com.twitter.common.codelab.echo.EchoMain',
  dependencies = [
    ':echo',
  ],
)
