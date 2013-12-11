package models

import play.api.libs.json.JsObject

/**
 * Created with IntelliJ IDEA.
 * User: liaoshifu
 * Date: 13-12-8
 * Time: 下午11:17
 * To change this template use File | Settings | File Templates.
 */
object JsonFormats {
  import play.api.libs.json._
  import play.api.data._
  import play.api.data.Forms._
  import play.api.data.format.Formats._
  import org.joda.time.DateTime

  implicit val userRoleFormat = Json.format[UserRole]
  implicit val accountFormat = Json.format[Account]

  implicit val articleFormat = Json.format[Article]


  val articleForm = Form (
    mapping (
      "id" -> optional(of[String]),
      "title" -> nonEmptyText,
      "content" -> nonEmptyText,
      "publisher" -> nonEmptyText,
      "creationDate" -> optional(of[Long]),
      "updateDate" -> optional(of[Long])
    ) ((id, title, content, publisher, creationDate, updateDate) => {
      Article(id, title, content, publisher, creationDate.map(new DateTime(_)), updateDate.map(new DateTime(_)))
    }) (article => {
      Some(
        (article.id, article.title, article.content, article.publisher, article.creationDate.map(_.getMillis), article.updateDate.map(_.getMillis))
      )
    })
  )
}