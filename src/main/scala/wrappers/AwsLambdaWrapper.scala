package com.github.plippe

import com.amazonaws.services.lambda.AWSLambda
import com.amazonaws.services.lambda.model._
import scala.util.Try

trait AwsLambdaWrapper {
  def updateFunctionCode(request: UpdateFunctionCodeRequest)
    : Either[Throwable, UpdateFunctionCodeResult]

  def publishVersion(
      request: PublishVersionRequest): Either[Throwable, PublishVersionResult]
}

class AwsLambdaWrapperImpl(client: AWSLambda) extends AwsLambdaWrapper {
  def updateFunctionCode(request: UpdateFunctionCodeRequest) =
    Try(client.updateFunctionCode(request)).toEither

  def publishVersion(request: PublishVersionRequest) =
    Try(client.publishVersion(request)).toEither
}
