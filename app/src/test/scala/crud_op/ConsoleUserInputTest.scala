package crud_op

import org.scalatest.{FlatSpec, Matchers}
import services.ConsoleUserInput

class ConsoleUserInputTest extends FlatSpec with Matchers{
  "readLine" should "return the input provided by the user" in {
    val conoleUserInput = new ConsoleUserInput
    val input = "test input"
    Console.withIn(new java.io.ByteArrayInputStream(input.getBytes)){
      val result = conoleUserInput.readLine("Enter input: ")
      result shouldBe(input)
    }
  }
}
