sbtPlugin := true

libraryDependencies ++= Seq(
  "com.amazonaws" % "aws-java-sdk-lambda" % "1.11.410",
  "com.lihaoyi" %% "utest" % "0.6.5" % Test,
  Defaults.sbtPluginExtra(
       "com.eed3si9n" % "sbt-assembly" % "0.14.7",
       (sbtBinaryVersion in pluginCrossBuild).value,
       (scalaBinaryVersion in pluginCrossBuild).value
  )
)

testFrameworks += new TestFramework("utest.runner.Framework")

scalafmtOnCompile := true

organization := "com.github.plippe"
name := "sbt-aws-lambda"

publishMavenStyle := false
bintrayRepository := "sbt"
bintrayOrganization in bintray := None

enablePlugins(GitVersioning)
git.useGitDescribe := true

scalaVersion := "2.12.6"
crossSbtVersions := Vector("0.13.17", "1.2.3")
