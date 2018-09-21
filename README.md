# sbt-aws-lambda

[![Download](https://api.bintray.com/packages/plippe/sbt/sbt-aws-lambda/images/download.svg)](https://bintray.com/plippe/sbt/sbt-aws-lambda/_latestVersion)

Sbt plugin to publish code to [AWS Lambda][aws-lambda].


### Installing sbt-aws-lambda

Add the plugin to `project/plugins.sbt` file.

```sbt
// in project/plugins.sbt
resolvers += Resolver.url("plippe-sbt", url("http://dl.bintray.com/plippe/sbt"))(Resolver.ivyStylePatterns)
addSbtPlugin("com.github.plippe" % "sbt-aws-lambda" % "XXX")
```

Enable the plugin in your `build.sbt` file.

```sbt
// in build.sbt
enablePlugins(AwsLambdaPlugin)
```


### Using sbt-aws-lambda

The only requirement is setting the `awsLambdaFunctionName` setting in your `build.sbt`. This `String` represents the
name of the AWS Lambda function name.

```sbt
// in build.sbt
awsLambdaFunctionName := "my-function"
```

The `awsLambdaClient` setting defaults to [`AWSLambdaClientBuilder.defaultClient()`][aws-lambda-client-default]. It can
be overridden with your own Amazon client.

```sbt
// in build.sbt
awsLambdaClient := com.amazonaws.services.lambda.AWSLambdaClientBuilder
    .standard()
    .withRegion(com.amazonaws.regions.Regions.US_EAST_1)
    .withCredentials(new com.amazonaws.auth.profile.ProfileCredentialsProvider("my-profile"))
    ...
    .build()
```

Once configured, run `sbt awsLambdaUpdateFunctionCode` to build, and push your function to Lambda.


[aws-lambda]: https://aws.amazon.com/lambda/
[aws-lambda-client-default]: https://docs.aws.amazon.com/AWSJavaSDK/latest/javadoc/com/amazonaws/services/lambda/AWSLambdaClientBuilder.html#defaultClient--
