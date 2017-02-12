package services

import scala.io.Source
import scala.collection.JavaConversions._

import play.api.cache.CacheApi

import java.io.{FileReader, FileNotFoundException, IOException}

import javax.inject.{Inject, Singleton}

import resource._

import model.Line

/**
 * service that is responsible for managing lines
 */
trait LinesService {
  def get(id: Long): Option[Line]
}

/**
 * implementation of the LinesService trait that loads the lines from
 * the supplied file and caches them using the Play CacheApi
 *
 * not a fan of this implementation. I don't like putting code in the constructor
 * that initializes the state. Makes it hard to unit test properly and maintain
 *
 * Instead, would prefer to pull the file parsing code into its own object
 * which then returns the lines as a list of Strings. This class would be injected
 * into this class
 * However this still doesn't fully address the problem of not having the cache initialization
 * code in the constructor. I would still perfer some method that can be called during
 * app start up that would initialize the cache.
 *
 * Need to review Play / Guice DI documentation to get a better understanding of app startup
 */
@Singleton
class CachingLinesService @Inject()(cache: CacheApi) extends LinesService {

  private val logger = org.slf4j.LoggerFactory.getLogger(this.getClass)

  logger.info("initializing caching line service")
  loadData(filename)

  /**
   * loads the data from the supplied file.
   * Uses Scala ARM plugin to ensure file is closed after its been parsed
   */
  def loadData(filename: String) = {
    for (source <- managed(Source.fromFile(filename))) {
      for ((line, index) <- source.getLines().zipWithIndex) {
        logger.debug("adding new line to cache [index: " + index + "][text: " + line + "]")
        cache.set(cacheKey(index), Line(index, line))
      }
    }
  }

  /**
   * returns the line associated to the supplied incex
   */
  override def get(index: Long): Option[Line] = {
    cache.get[Line](cacheKey(index))
  }

  def filename: String = {
    val properties = System.getProperties()
    properties.get("config.linesfile").asInstanceOf[String]
  }

  def cacheKey(index: Long): String = {
    "line." + index
  }
}