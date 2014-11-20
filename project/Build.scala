import sbt._
import Keys._

object Build extends Build {

  val buildName         = "scalikejdbc-for-mysql"
  val buildVersion      = "1.0"
  val buildOrganization = "ryoppy"
  val buildScalaVersion = "2.11.4"

  val buildSettings = Defaults.coreDefaultSettings ++ Seq (
    name := buildName,
    organization := buildOrganization,
    version := buildVersion,
    scalaVersion := buildScalaVersion
  )

  lazy val root = Project(
    id = "scalikejdbc-for-mysql",
    base = file("."),
    settings = buildSettings ++ Seq(
      scalacOptions ++= Seq("-encoding", "UTF-8", "-feature", "-deprecation", "-unchecked"),
      javacOptions ++= Seq("-encoding", "UTF-8"),
      resolvers ++= Seq(
        "sonatype snapshots" at "http://oss.sonatype.org/content/repositories/snapshots"
      ),
      libraryDependencies ++= Seq(
        "mysql" % "mysql-connector-java" % "5.1.32",
        "org.scalikejdbc" %% "scalikejdbc"        % "2.2.0",
        "org.scalikejdbc" %% "scalikejdbc-config" % "2.2.0",
        "org.scalikejdbc" %% "scalikejdbc-syntax-support-macro" % "2.2.0",
        "org.scalikejdbc" %% "scalikejdbc-test"   % "2.2.0",
        "org.specs2" %% "specs2" % "2.4",
        "com.h2database"  %  "h2"                 % "1.4.182",
        "ch.qos.logback"  %  "logback-classic"    % "1.1.2"
      )
    )
  )
}
