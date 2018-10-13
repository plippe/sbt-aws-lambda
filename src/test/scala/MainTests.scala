import utest._

import com.amazonaws.services.lambda.model._
import java.io.File
import java.nio.ByteBuffer
import scala.io.Source

import com.github.plippe._

object MainTests extends TestSuite {
  val tests = Tests {
    "fileToBuffer" - {
      "fail file missing" - {
        val file = new File("missing")
        val result = Main.fileToBuffer(file)
        assert(result.isLeft)
      }
      "file content equal buffer content" - {
        val file = new File(getClass.getResource("/lipsum.com.txt").getFile)
        val result = Main.fileToBuffer(file)

        val resultContent = new String(result.right.get.array())
        val fileContent = Source.fromFile(file).getLines.mkString("\n")

        assert(fileContent == resultContent.trim)
      }
    }
    "updateFunctionCode" - {
      "fail aws error" - {
        val awsLambda = new AwsLambdaWrapper {
          def publishVersion(request: PublishVersionRequest) = ???
          def updateFunctionCode(request: UpdateFunctionCodeRequest) =
            Left(new Throwable(""))
        }

        val result =
          Main.updateFunctionCode(awsLambda, "", ByteBuffer.allocate(0))
        assert(result.isLeft)
      }
      "call aws with name" - {
        val functionName = "function-name"
        val awsLambda = new AwsLambdaWrapper {
          def publishVersion(request: PublishVersionRequest) = ???
          def updateFunctionCode(request: UpdateFunctionCodeRequest) = {
            assert(request.getFunctionName == functionName)
            Right(new UpdateFunctionCodeResult())
          }
        }

        val result =
          Main.updateFunctionCode(awsLambda,
                                  functionName,
                                  ByteBuffer.allocate(0))
        assert(result.isRight)
      }
      "call aws with buffer" - {
        val zipFile = ByteBuffer.allocate(0)
        val awsLambda = new AwsLambdaWrapper {
          def publishVersion(request: PublishVersionRequest) = ???
          def updateFunctionCode(request: UpdateFunctionCodeRequest) = {
            assert(request.getZipFile == zipFile)
            Right(new UpdateFunctionCodeResult())
          }
        }

        val result = Main.updateFunctionCode(awsLambda, "", zipFile)
        assert(result.isRight)
      }
    }
    "publishVersion" - {
      "fail aws error" - {
        val awsLambda = new AwsLambdaWrapper {
          def updateFunctionCode(request: UpdateFunctionCodeRequest) = ???
          def publishVersion(request: PublishVersionRequest) =
            Left(new Throwable(""))
        }

        val result = Main.publishVersion(awsLambda, "", "", "")
        assert(result.isLeft)
      }
      "call aws with name" - {
        val functionName = "function-name"
        val awsLambda = new AwsLambdaWrapper {
          def updateFunctionCode(request: UpdateFunctionCodeRequest) = ???
          def publishVersion(request: PublishVersionRequest) = {
            assert(request.getFunctionName == functionName)
            Right(new PublishVersionResult())
          }
        }

        val result = Main.publishVersion(awsLambda, functionName, "", "")
        assert(result.isRight)
      }
      "call aws with revisionId" - {
        val revisionId = "revision-id"
        val awsLambda = new AwsLambdaWrapper {
          def updateFunctionCode(request: UpdateFunctionCodeRequest) = ???
          def publishVersion(request: PublishVersionRequest) = {
            assert(request.getRevisionId == revisionId)
            Right(new PublishVersionResult())
          }
        }

        val result = Main.publishVersion(awsLambda, "", revisionId, "")
        assert(result.isRight)
      }
      "call aws with version" - {
        val description = "description"
        val awsLambda = new AwsLambdaWrapper {
          def updateFunctionCode(request: UpdateFunctionCodeRequest) = ???
          def publishVersion(request: PublishVersionRequest) = {
            assert(request.getDescription == description)
            Right(new PublishVersionResult())
          }
        }

        val result = Main.publishVersion(awsLambda, "", "", description)
        assert(result.isRight)
      }
    }
  }
}
