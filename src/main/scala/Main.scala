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
      client: AwsLambdaWrapper,
      name: String,
      file: File): Either[Throwable, UpdateFunctionCodeResult] = {
    fileToBuffer(file)
      .map { buffer =>
        new UpdateFunctionCodeRequest()
          .withFunctionName(name)
          .withZipFile(buffer)
      }
      .flatMap(client.updateFunctionCode)
  }

  def publishVersion(
      client: AwsLambdaWrapper,
      name: String,
      revisionId: String,
      version: String): Either[Throwable, PublishVersionResult] = {
    val request = new PublishVersionRequest()
      .withFunctionName(name)
      .withRevisionId(revisionId)
      .withDescription(version)

    client.publishVersion(request)
  }

  def run(plugin: AssemblyPluginWrapper,
          client: AwsLambdaWrapper,
          name: String,
          version: String): Either[Throwable, Unit] = {

    for {
      file <- plugin.assembly
      result <- updateFunctionCode(client, name, file)
      _ <- publishVersion(client, name, result.getRevisionId(), version)
    } yield ()

  }

}
