// build root project
lazy val root = Project("plugins", file(".")) dependsOn(awsLambda)

// depends on the publishDoc project
lazy val awsLambda = RootProject(file("../../.."))
