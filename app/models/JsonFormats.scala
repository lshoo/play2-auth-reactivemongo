package models

/**
 * Created with IntelliJ IDEA.
 * User: liaoshifu
 * Date: 13-12-8
 * Time: 下午11:17
 * To change this template use File | Settings | File Templates.
 */
object JsonFormats {
  import play.api.libs.json.Json
  import play.api.data._
  import play.api.data.Forms._

  implicit val userRoleFormat = Json.format[UserRole]
  implicit val AccountFormat = Json.format[Account]

}