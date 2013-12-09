package models

import reactivemongo.api._

import scala.concurrent.ExecutionContext.Implicits.global
/**
 * Created with IntelliJ IDEA.
 * User: liaoshifu
 * Date: 13-12-9
 * Time: 上午9:22
 * To change this template use File | Settings | File Templates.
 */
object Mongo {
  val driver = new MongoDriver()
  val connection = driver.connection(List("localhost:27017"))
  val db = connection("reactivemongo-app")
}
