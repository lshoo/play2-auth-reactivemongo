package models

import org.jboss.netty.buffer._
import reactivemongo.bson._
import org.joda.time.DateTime
import play.api.data._
import play.api.data.Forms._
import play.api.data.format.Formats._
import play.api.data.validation.Constraint._

/**
 * Created with IntelliJ IDEA.
 * User: liaoshifu
 * Date: 13-12-10
 * Time: 上午10:30
 * To change this template use File | Settings | File Templates.
 */
case class Article(id: Option[String],
                    title: String,
                    content: String,
                    publisher: String,
                    creationDate: Option[DateTime],
                    updateDate: Option[DateTime])

object Article {

}
/*case class Article(id: Option[BSONObjectID],
                    title: String,
                    content: String,
                    publisher: String,
                    creationDate: Option[DateTime],
                    updateDate: Option[DateTime])

object Article {
  implicit object ArticleBSONWriter extends BSONDocumentWriter[Article] {
    def write(article: Article): BSONDocument = BSONDocument(
      "_id" -> article.id.getOrElse(BSONObjectID.generate),
      "title" -> article.title,
      "content" -> article.content,
      "publisher" -> article.publisher,
      "creationDate" -> article.creationDate,
      "updateDate" -> article.updateDate
    )
  }

  implicit object ArticleBSONReader extends BSONDocumentReader[Article] {
    def read(doc: BSONDocument): Article = Article(
      doc.getAs[BSONObjectID]("_id"),
      doc.getAs[String]("title").get,
      doc.getAs[String]("content").get,
      doc.getAs[String]("publisher").get,
      doc.getAs[DateTime]("creationDate"),
      doc.getAs[DateTime]("updateDate")
    )
  }


  val form = Form (
    mapping (
      "id" -> optional(of[String] verifying pattern(
        """[a-fA-F0-9]{24}""".r,
        "constraint.objectId",
        "error.objectId"
      )),
      "title" -> nonEmptyText,
      "content" -> text,
      "pubisher" -> nonEmptyText,
      "creationDate" -> optional(of[Long]),
      "updateDate" -> optional(of[Long])
    ) {(id, title, content, publisher, creationDate,updateDate) => {
        Article(
          id.map(new BSONObjectID(_)),
          title,
          content,
          publisher,
          creationDate.map(new DateTime(_)),
          updateDate.map(new DateTime(_))
        )
    }} { article => {
      Some(
        (article.id.map(_.toString),
          article.title,
          article.content,
          article.publisher,
          article.creationDate.map(_.getMillis),
          article.updateDate.map(_.getMillis))
      )
    }}
  )
}*/
