package controllers

import javax.inject._
import play.api._
import play.api.mvc._

import services.LinesService

/**
 * This controller creates an `Action` to handle HTTP requests to the
 * lines REST api
 */
@Singleton
class LinesController @Inject()(linesService: LinesService) extends Controller {

  /**
   * Action that returns the string associated to the supplied index
   *
   * The configuration in the `routes` file means that this method
   * will be called when the application receives a `GET` request with
   * a path of `/lines/:index`.
   */
  def show(index: Long) = Action { implicit request =>
    linesService.get(index) match {
      case Some(line) => Ok(line.text)
      case None => Status(413)
    }
  }
}
