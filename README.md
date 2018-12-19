```
$ sbt new lagom/lagom-scala.g8
Java HotSpot(TM) 64-Bit Server VM warning: ignoring option MaxPermSize=1536m; support was removed in 8.0
[info] Loading settings for project global-plugins from idea.sbt ...
[info] Loading global plugins from /Users/norfe/.sbt/1.0/plugins
[info] Set current project to junk (in build file:/Users/norfe/junk/)
[info] Set current project to junk (in build file:/Users/norfe/junk/)
name [Hello]: 
organization [com.example]: 
version [1.0-SNAPSHOT]: 
package [com.example.hello]: 

Template applied in /Users/norfe/junk/./hello
```

Changed build.sbt for elastic search bits (with the `import of scala.sys.process.Process`)

```
$ sbt
Java HotSpot(TM) 64-Bit Server VM warning: ignoring option MaxPermSize=1536m; support was removed in 8.0
[info] Loading settings for project global-plugins from idea.sbt ...
[info] Loading global plugins from /Users/norfe/.sbt/1.0/plugins
[info] Loading settings for project hello-build from plugins.sbt ...
[info] Loading project definition from /Users/norfe/junk/hello/project
[info] Loading settings for project hello from build.sbt ...
[info] Set current project to hello (in build file:/Users/norfe/junk/hello/)
[info] sbt server started at local:///Users/norfe/.sbt/1.0/server/89fb9de7927baf5268ba/sock
sbt:hello> runAll
[info] Starting Kafka
[info] Starting Cassandra
.......
[info] Cassandra server running at 127.0.0.1:4000
2018-12-19T21:24:21.382Z [info] akka.event.slf4j.Slf4jLogger [] - Slf4jLogger started
2018-12-19T21:24:23.103Z [info] com.lightbend.lagom.discovery.ServiceLocatorServer [] - Service locator can be reached at http://localhost:9008
2018-12-19T21:24:23.103Z [info] com.lightbend.lagom.discovery.ServiceLocatorServer [] - Service gateway can be reached at http://localhost:9000
[info] Service locator is running at http://localhost:9008
[info] Service gateway is running at http://localhost:9000
[info] Downloading Elastic Search 5.4.0...
[error] java.io.IOException: Cannot run program "/Users/norfe/junk/hello/target/elasticsearch-5.4.0/bin/elasticsearch" (in directory "/Users/norfe/junk/hello/target/elasticsearch-5.4.0"): error=13, Permission denied

<snip>

[error] Total time: 13 s, completed Dec 19, 2018 2:24:27 PM
sbt:hello> [info] shutting down server
```

Make elasticsearch execuatable...

```
$ 
$ chmod +x target/elasticsearch-5.4.0/bin/elasticsearch
```

runAll!

```
$ sbt runAll
Java HotSpot(TM) 64-Bit Server VM warning: ignoring option MaxPermSize=1536m; support was removed in 8.0
[info] Loading settings for project global-plugins from idea.sbt ...
[info] Loading global plugins from /Users/norfe/.sbt/1.0/plugins
[info] Loading settings for project hello-build from plugins.sbt ...
[info] Loading project definition from /Users/norfe/junk/hello/project
[info] Loading settings for project hello from build.sbt ...
[info] Set current project to hello (in build file:/Users/norfe/junk/hello/)
[info] Starting Kafka
[info] Starting Cassandra
.......
[info] Cassandra server running at 127.0.0.1:4000
2018-12-19T21:25:37.615Z [info] akka.event.slf4j.Slf4jLogger [] - Slf4jLogger started
2018-12-19T21:25:39.269Z [info] com.lightbend.lagom.discovery.ServiceLocatorServer [] - Service locator can be reached at http://localhost:9008
2018-12-19T21:25:39.269Z [info] com.lightbend.lagom.discovery.ServiceLocatorServer [] - Service gateway can be reached at http://localhost:9000
[info] Service locator is running at http://localhost:9008
[info] Service gateway is running at http://localhost:9000
[info] Elastic search started on port 9200

<snip>
[info] Service hello-impl listening for HTTP on 0:0:0:0:0:0:0:0:57797
[info] Service hello-stream-impl listening for HTTP on 0:0:0:0:0:0:0:0:58322
[info] (Services started, press enter to stop and go back to the console...)
14:25:47.653 [info] akka.cluster.singleton.ClusterSingletonProxy [sourceThread=hello-impl-application-akka.actor.default-dispatcher-16, akkaSource=akka.tcp://hello-impl-application@127.0.0.1:53484/user/cassandraOffsetStorePrepare-singletonProxy, sourceActorSystem=hello-impl-application, akkaTimestamp=21:25:47.653UTC] - Singleton identified at [akka://hello-impl-application/user/cassandraOffsetStorePrepare-singleton/singleton]
14:25:47.741 [info] com.lightbend.lagom.internal.persistence.cluster.ClusterStartupTaskActor [sourceThread=hello-impl-application-akka.actor.default-dispatcher-21, akkaTimestamp=21:25:47.740UTC, akkaSource=akka.tcp://hello-impl-application@127.0.0.1:53484/user/cassandraOffsetStorePrepare-singleton/singleton/cassandraOffsetStorePrepare, sourceActorSystem=hello-impl-application] - Cluster start task cassandraOffsetStorePrepare done.
```
