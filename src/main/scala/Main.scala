import com.amazonaws.services.lambda.model._
import java.io.{File, FileInputStream}
import java.nio.ByteBuffer
import scala.util.Try

import com.github.plippe._

object Main {

  def fileToBuffer(file: File) =
    Try {
      val buffer = ByteBuffer.allocate(file.length().toInt)
      val inputStream = new FileInputStream(file)
      inputStream.getChannel().read(buffer)

      buffer.rewind()
      buffer
    }.toEither

  def updateFunctionCode(
      awsLambda: AwsLambdaWrapper,
      name: String,
      buffer: ByteBuffer): Either[Throwable, UpdateFunctionCodeResult] = {

    val request = new UpdateFunctionCodeRequest()
      .withFunctionName(name)
      .withZipFile(buffer)

    awsLambda.updateFunctionCode(request)
  }

  def publishVersion(
      awsLambda: AwsLambdaWrapper,
      name: String,
      revisionId: String,
      version: String): Either[Throwable, PublishVersionResult] = {

    val request = new PublishVersionRequest()
      .withFunctionName(name)
      .withRevisionId(revisionId)
      .withDescription(version)

    awsLambda.publishVersion(request)
  }

  def run(assemblyPlugin: AssemblyPluginWrapper,
          awsLambda: AwsLambdaWrapper,
          name: String,
          version: String): Either[Throwable, Unit] = {

    for {
      file <- assemblyPlugin.assembly
      buffer <- fileToBuffer(file)
      result <- updateFunctionCode(awsLambda, name, buffer)
      _ <- publishVersion(awsLambda, name, result.getRevisionId(), version)
    } yield ()

  }

}
