The goal of this codelab is to help you learn how to best structure your code and configure pants to effectively manage dependencies within a single code repository.

Let's begin by surveying they echo codelab, located at src/java/com/twitter/common/codelab/echo.

    $ find src/java/com/twitter/common/codelab/echo
    src/java/com/twitter/common/codelab/echo
    src/java/com/twitter/common/codelab/echo/BUILD
    src/java/com/twitter/common/codelab/echo/Echoer.java
    src/java/com/twitter/common/codelab/echo/EchoMain.java
    src/java/com/twitter/common/codelab/echo/FileEchoer.java
    src/java/com/twitter/common/codelab/echo/HadoopEchoer.java
    src/java/com/twitter/common/codelab/echo/README.md

Here we see an appliction called `EchoMain`, that simply prints a string provided by an implementation of `Echoer`. Two implementations exist:

* `FileEchoer`, a simple local file-based implementation that only depends on the interface and standard library
* `HadoopEchoer`, a Hadoop-based implementation that depends on the interface and Hadoop

The program requires two additional files, a configuration file that simply contains the class name of an `Echoer` implementation to use, and a file with the string to echo.

    $ find codelab
    codelab
    codelab/BUILD
    codelab/echo.txt
    codelab/echo.yaml

Let's see an example run:

    ./pants goal run src/java/com/twitter/common/codelab/echo:echo-bin \
      --compile-java-args='-target 7 -source 7' \
      --jvm-run-args='-config=codelab/echo.yaml'
    Using echoer: com.twitter.common.codelab.echo.hadoop.HadoopEchoer
    Hello there!

Now let's use the local file-based implementaion.

    $ echo com.twitter.common.codelab.echo.FileEchoer > codelab/echo.yaml
    ./pants goal run src/java/com/twitter/common/codelab/echo:echo-bin \
      --compile-java-args='-target 7 -source 7' --jvm-run-args='-config=codelab/echo.yaml'
    Using echoer: com.twitter.common.codelab.echo.FileEchoer
    Hello there!

And let's create a bundle. Notice how the archive is quite large because it includes all Hadoop dependencies.

    $ ./pants goal bundle codelab:echo --bundle-archive=zip
    $ du -sh dist/echo.zip 
     14M	dist/echo.zip
    $ ls dist/echo-bundle/libs/ | wc -l
          32

While the application may work well for its current needs there are a number of issues with this current approach.

* Users must currently bundle the entire echoer, even if they do not need all available echo providers. This causes bloat in deploy artifacts and increases the risk of dependency conflicts.

* Developers may wish to implement a custom `Echoer`, as we'll do shortly. As a single `java_library` exposes the entire echoer, to get the interface users get all dependencies of that target, including Hadoop for example.

Pants provides solutions to these issues by allowing developers to structure their code and build targets such that dependencies are straightforward to manage and users have great flexability in the construction of their bundles.

# Adding an Echoer

Let's put our developer hat on and extend this application with a new `Echoer`. Conveniently, one already exists that you can move into the source tree.

    $ mkdir -p src/java/com/twitter/myapp/echo
    $ mv codelab/StaticEchoer.java src/java/com/twitter/myapp/echo/StaticEchoer.java

This simple implementation always returns the same string. Now, let's write the BUILD file for this library.

    src/java/com/twitter/myapp/echo/BUILD:
    
    java_library(name='echo',
      dependencies=[
        # Necessary for Echoer interface - but with many fellow travelers!
        pants('src/java/com/twitter/common/codelab/echo'),
      ],
      sources=('*.globs'),
    )

To build our very simple implementation of this interface we pull in the Appication stack, as well as Hadoop. There's got to be a better way.

# Rule of thumb: Use a subpackage when adding large dependencies

Let's remind ourselves of the echoer source files:

    src/java/com/twitter/common/codelab/echo/Echoer.java
    src/java/com/twitter/common/codelab/echo/EchoMain.java
    src/java/com/twitter/common/codelab/echo/FileEchoer.java
    src/java/com/twitter/common/codelab/echo/HadoopEchoer.java

Hadoop and its transitive dependencies are quite large, and not critical to the echoer, making `HadoopEchoer` a great candidate to refactor into a subpackage. Let's move `HadoopEchoer` into a subpackage and expose it as a stand-alone library.

    src/java/com/twitter/common/codelab/echo/hadoop/BUILD:
    java_library(name='hadoop',
      dependencies=[
        pants('3rdparty:hadoop-core'),
        pants('src/java/com/twitter/myapp/echo'),
      ]
      sources=globs('*.java'),
    )

Now we can remove the `hadoop-core` dependency fom `src/java/com/twitter/common/codelab/echo`. Now `src/java/com/twitter/myapp/echo` no longer has a transitive dependency on Hadoop!

Let's create a bundle with `StaticEchoer` and see how much fat we've cut.

    $ ./pants goal bundle codelab:echo --bundle-archive=zip
    $ du -sh dist/echo.zip 
    $ du -sh dist/echo.zip 
    4.0K	dist/echo.zip
    $ ls dist/echo-bundle/libs/ | wc -l
           0

Prior to this refactor our bundle was 14 MB with 32 dependencies! By simply refactoring the Hadoop-based functionality into an optional library we've significantly shrunk the application bundle.

Before moving on, let's remember `FileEchoer` is still bundled with the interface. As `FileEchoer` requires no additional dependencies (just the standard library) there's no harm in combining them in a single target.

# TODO

Its pretty tedious to have users create a `jvm_binary` and `jvm_app`. What I'd like to do is have a single target where users combine all the dependencies together and create the deploy bundle. Currently we need to have people first create a `jvm_binary` where all the dependencies are pulled together, and a separate `jvm_app` where we create the package.