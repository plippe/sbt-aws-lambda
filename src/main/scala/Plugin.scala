import sbt._, Keys._

import com.amazonaws.services.lambda.{AWSLambda, AWSLambdaClientBuilder}
import sbtassembly.AssemblyPlugin
import scala.util.Try

import com.github.plippe._

trait AwsLambdaKeys {

  lazy val awsLambdaClient =
    settingKey[AWSLambda]("Client for accessing AWS Lambda")

  lazy val awsLambdaFunctionName = settingKey[String](
    "The existing Lambda function name whose code you want to replace")

  lazy val awsLambdaUpdateFunctionCode =
    taskKey[Unit]("Updates the code for the specified Lambda function")

}

object AwsLambdaPlugin extends AutoPlugin {

  override val requires: Plugins = AssemblyPlugin
  override lazy val projectSettings: Seq[Setting[_]] = awsLambdaProjectSettings

  object autoImport extends AwsLambdaKeys
  import autoImport._

  def awsLambdaProjectSettings: Seq[Setting[_]] = Seq(
    awsLambdaClient := AWSLambdaClientBuilder.defaultClient(),
    awsLambdaUpdateFunctionCode := updateFunctionCode.value,
  )

  def updateFunctionCode = Def.task {
    val plugin = new AssemblyPluginWrapper {
      def assembly(): Either[Throwable, sbt.File] = {
        Try(AssemblyPlugin.autoImport.assembly.value).toEither
      }
    }

    val client = new AwsLambdaWrapperImpl(awsLambdaClient.value)

    Main.run(plugin, client, awsLambdaFunctionName.value, version.value) match {
      case Right(()) => streams.value.log.info("AWS Lambda function updated")
      case Left(err) =>
        streams.value.log.error("Failed AWS Lambda function update")
        throw err
    }

  }

}
