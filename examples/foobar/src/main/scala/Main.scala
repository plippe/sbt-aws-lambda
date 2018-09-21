package example

object Hello {
  def handleRequest(): Unit = {
    println("Lambda Code")
  }
}

object Main extends App {
  println("foo bar")
  Hello.handleRequest
}
