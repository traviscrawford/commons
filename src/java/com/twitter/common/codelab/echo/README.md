The goal of this codelab is to help you learn how to best structure your code and configure pants to effectively manage dependencies within a single code repository.

Let's begin by surveying they echo codelab, located at `src/java/com/twitter/common/codelab/echo`. The example echoer implementations require an additional file containing the string to print, located at `codelab/echo.txt`.

    [tw-mbp13-travis commons]$ ls -1 src/java/com/twitter/common/codelab/echo
    BUILD
    EchoMain.java
    Echoer.java
    FileEchoer.java
    HadoopEchoer.java
    README.md

Here we see an appliction called `EchoMain`, that simply prints a string provided by an implementation of `Echoer`. Two implementations exist:

* `FileEchoer`, a simple local file-based implementation that only depends on the interface and standard library
* `HadoopEchoer`, a Hadoop-based implementation that depends on the interface and Hadoop

Let's see an example run:

    $ ./pants goal run src/java/com/twitter/common/codelab/echo:echo-bin \
      --jvm-run-args='com.twitter.common.codelab.echo.HadoopEchoer'
    Using Echoer: com.twitter.common.codelab.echo.HadoopEchoer
    Hello there!

Now let's use the local file-based implementaion.

    ./pants goal clean-all run src/java/com/twitter/common/codelab/echo:echo-bin \
      --jvm-run-args='com.twitter.common.codelab.echo.FileEchoer'
    Using Echoer: com.twitter.common.codelab.echo.FileEchoer
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
    $ cp codelab/StaticEchoer.java src/java/com/twitter/myapp/echo/StaticEchoer.java

This simple implementation always returns the same string. Now, let's write the BUILD file for this library.

    src/java/com/twitter/myapp/echo/BUILD:
    
    java_library(name='echo',
      dependencies=[
        # Necessary for Echoer interface - but with many fellow travelers!
        pants('src/java/com/twitter/common/codelab/echo'),
      ],
      sources=globs('*.java'),
    )
    
    jvm_binary(name='echo-bin',
      main='com.twitter.common.codelab.echo.EchoMain',
      dependencies=[pants(':echo')],
    )

    jvm_app(name='echo-app',
      binary=pants(':echo-bin'),
      bundles=[bundle()],
    )

Let's run our new echoer implementation and view the dependencies in our bundle. Notice how Hadoop is included even though we use nothing beyond the standard library.

    $ ./pants goal clean-all run src/java/com/twitter/myapp/echo:echo-bin \
      --jvm-run-args='com.twitter.common.myapp.echo.StaticEchoer'
    Using Echoer: com.twitter.common.myapp.echo.StaticEchoer
    tall cat is tall
    $ ./pants goal bundle src/java/com/twitter/myapp/echo:echo-app --bundle-archive=zip
    $ du -sh dist/echo-app.zip 
     14M	dist/echo-app.zip
    $ ls dist/echo-app-bundle/libs/ | wc -l
          32


# Rule of thumb: Use a subpackage when adding large dependencies

Let's remind ourselves of the echoer source files:

    src/java/com/twitter/common/codelab/echo/Echoer.java
    src/java/com/twitter/common/codelab/echo/EchoMain.java
    src/java/com/twitter/common/codelab/echo/FileEchoer.java
    src/java/com/twitter/common/codelab/echo/HadoopEchoer.java

Hadoop and its transitive dependencies are quite large, and not critical to the echoer, making `HadoopEchoer` a great candidate to refactor into a subpackage. Let's move `HadoopEchoer` into a subpackage and expose it as a stand-alone library.

    $ mkdir src/java/com/twitter/common/codelab/echo/hadoop
    $ vi src/java/com/twitter/common/codelab/echo/hadoop/BUILD

    java_library(name='hadoop',
      dependencies=[
        pants('3rdparty:hadoop-core'),
        pants('src/java/com/twitter/myapp/echo'),
      ]
      sources=globs('*.java'),
    )

Now we can remove the `hadoop-core` dependency fom `src/java/com/twitter/common/codelab/echo`. Now `src/java/com/twitter/myapp/echo` no longer has a transitive dependency on Hadoop!

Let's create a bundle with `StaticEchoer` and see how much fat we've cut.

    $ ./pants goal clean-all run src/java/com/twitter/myapp/echo:echo-bin \
      --jvm-run-args='com.twitter.common.myapp.echo.StaticEchoer'
    Using Echoer: com.twitter.common.myapp.echo.StaticEchoer
    tall cat is tall
    $ ./pants goal bundle src/java/com/twitter/myapp/echo:echo-app --bundle-archive=zip
    $ du -sh dist/echo-app.zip 
    4.0K	dist/echo-app.zip
    $ ls dist/echo-app-bundle/libs/ | wc -l
          0

Prior to this refactor our bundle was 14 MB with 32 dependencies! By simply refactoring the Hadoop-based functionality into an optional library we've significantly shrunk the application bundle.

Before moving on, let's remember `FileEchoer` is still bundled with the interface. As `FileEchoer` requires no additional dependencies (just the standard library) there's no harm in combining them in a single target.
