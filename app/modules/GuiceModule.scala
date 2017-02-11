package modules

import com.google.inject.AbstractModule
import play.api.{Configuration, Environment}
import services.{LinesService, CachingLinesService}

class GuiceModule(environment: Environment, configuration: Configuration) extends AbstractModule {
  override def configure() = {
    bind(classOf[LinesService]).to(classOf[CachingLinesService])
  }
}