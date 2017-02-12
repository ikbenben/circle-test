package controllers

import org.scalatest._
import org.scalatest.mock.MockitoSugar
import org.scalatestplus.play._

import play.api.test._
import play.api.test.Helpers._
import play.api.inject.guice.GuiceApplicationBuilder
import play.api.inject.bind

import org.mockito.Mockito._

import services.LinesService
import model.Line

class LinesControllerSpec extends PlaySpec with OneAppPerTest with MockitoSugar with BeforeAndAfter {

  var linesServiceMock: LinesService = _
  var controller: LinesController = _

  val lineIndex = 2
  val lineText = "test string"

  /**
   * test show method to ensure returning correct values
   */
  "LinesController#show" should {
    before {
      linesServiceMock = mock[LinesService]
      controller = new LinesController(linesServiceMock)
    }

    "return the string associated to the supplied index" in {
      when(linesServiceMock.get(lineIndex)) thenReturn Some(Line(lineIndex, lineText))

      val response = controller.show(lineIndex).apply(FakeRequest())

      status(response) mustBe OK
      contentType(response) mustBe Some("text/plain")
      contentAsString(response) must include (lineText)
    }

    "return status 413 when string is not found for supplied index" in {
      when(linesServiceMock.get(lineIndex)) thenReturn None

      val response = controller.show(lineIndex).apply(FakeRequest())

      status(response) mustBe 413
    }
  }

  /**
   * ensure route is configured correctly
   */
  "GET /lines/:index" should {
    "render the line associated to :index" in {

      // set mock expectations
      when(linesServiceMock.get(lineIndex)) thenReturn Some(Line(lineIndex, lineText))

      // create app that rebinds the line service object for dependency injection
      val app = new GuiceApplicationBuilder()
        .overrides(bind[LinesService].toInstance(linesServiceMock))
        .build

      // Need to specify Host header to get through AllowedHostsFilter
      val request = FakeRequest(GET, "/lines/" + lineIndex).withHeaders("Host" -> "localhost")
      val response = route(app, request).get

      status(response) mustBe OK
      contentType(response) mustBe Some("text/plain")
      contentAsString(response) must include (lineText)
    }
  }
}
