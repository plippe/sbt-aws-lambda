package com.github.plippe

trait AssemblyPluginWrapper {
  def assembly(): Either[Throwable, sbt.File]
}
