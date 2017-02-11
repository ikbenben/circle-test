package services

import model.Line

import javax.inject.Inject

import play.api.cache.CacheApi

import scala.collection.mutable
import scala.concurrent.duration._

trait LinesService {
  def get(id: Long): Option[Line]
}

class CachingLinesService @Inject()(cache: CacheApi) extends LinesService {

  // TODO: initialize lines cache using file from initialization
  /*
  //private val books = mutable.Map(
  //  1 -> Book(1, "Twilight"),
  //  2 -> Book(2, "50 Shades of Grey"))

  //private def fetchFreshLines(): Seq[Line] = {
  //  lines.values.toSeq
  //}

  override def list: Seq[Line] = {
    cache.getOrElse("lines") {
      def freshLines = fetchFreshLines()
      cache.set("lines", freshLines, 2.minutes)
      freshLines
    }
  }
  */

  override def get(index: Long): Option[Line] = {
    Some(Line(index, "blah blah blah " + index))
    /*
    try {
        Some(Integer.parseInt(in.trim))
    } catch {
        case e: NumberFormatException => None
    }
    */    
  }
}