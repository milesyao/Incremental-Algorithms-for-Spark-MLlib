name := "incremental-Algorithms-for-Spark-MLlib"

version := "0.0.1"

scalaVersion := "2.10.4"

libraryDependencies ++= Seq("org.apache.spark" % "spark-core_2.10" % "1.3.0",
                        "org.apache.spark" % "spark-mllib_2.10" % "1.3.0",
			"org.apache.spark" % "spark-streaming_2.10" % "1.3.0",
                        "org.apache.commons" % "commons-math3" % "3.3",
			"org.scalatest" % "scalatest_2.10" % "2.0" % "test"
                      )

resolvers  ++= Seq("Apache Repository" at "https://repository.apache.org/content/repositories/releases",
               "Akka Repository" at "http://repo.akka.io/releases/",
               "Spray Repository" at "http://repo.spray.cc/")
