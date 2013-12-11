package controllers

import play.modules.reactivemongo.MongoController
import play.api.mvc._
import play.api.Logger
import play.api.Play.current
import play.api.libs.concurrent.Execution.Implicits.defaultContext

import org.joda.time.DateTime
import scala.concurrent.Future

import models.Article
import models.JsonFormats._
import play.modules.reactivemongo.json.collection.JSONCollection
import reactivemongo.api.gridfs.GridFS
import play.api.libs.json.Json
import reactivemongo.bson._

/**
 * Created with IntelliJ IDEA.
 * User: liaoshifu
 * Date: 13-12-10
 * Time: 上午11:56
 * To change this template use File | Settings | File Templates.
 */
object Articles extends Controller with MongoController {

  def collection: JSONCollection = db.collection[JSONCollection]("articles")

  val gridFS = new GridFS(db)

  // build an index on our gridfs chunks collection if none.
  gridFS.ensureIndex().onComplete {
    case index => Logger.info(s"Checked index, result is $index")
  }

  // list all articles and sort them
  def index = Action.async {
    implicit request => {
      // get a sort document (see getSort method for more method)
      val sort = getSort(request)
      // build a selection document with an empty query and a sort subdocument ('$orderby')
      val query = Json.obj("$sort" -> sort)

      val activeSort = request.queryString.get("sort").flatMap(_.headOption).getOrElse("none")
      // the future cursor  of document
      val found = collection.find(query).cursor[Article]
      // build (asynchronously) a list containing all the articles
      found.collect[List]().map { articles =>
        Ok(views.html.articles(articles, activeSort))
      }.recover {
        case e => {
          e.printStackTrace()
          BadRequest(e.getMessage)
        }

      }
    }
  }

  def showCreationForm = Action {
    Ok(views.html.editArticle(None, articleForm, None))
  }

  /*def showEditForm(id: String) = Action.async {
    val futureArticle = collection.find(Json.obj("id" -> id)).one[Article]

    for {
      maybeArticle <- futureArticle
      result <- maybeArticle.map {
        article => {
          gridFS.find(Json.obj("article" -> article.id.get)).collect[List]().map {
            files => {
              val filesWithId = files.map {
                file => {
                  file
                }
              }
            }
          }
        }
      }
    }
  }*/

  private def getSort(request: Request[_]) = {
    request.queryString.get("sort").map { fields =>
      val sortBy = for {
        order <- fields.map { field =>
          if (field.startsWith("-"))
            field.drop(1) -> -1
          else field -> 1
        }
        if order._1 == "title" || order._1 == "publisher" || order._1 == "creationDate" || order._1 == "updateDate"
      } yield order._1 -> order._2
      sortBy.map { sort =>
        Json.obj(sort._1 -> sort._2)
      }
    }
  }
  /*private def getSort(request: Request[_]) = {
    request.queryString.get("sort").map {
      fields => {
        val sortBy = for {
          order <- fields.map { field =>
            if (field.startsWith("-")) field.drop(1) -> -1
            else field -> 1
          }
          if (order._1) == "title" || order._1 == "publisher" || order._1 == "creationDate" || order._1 == "updateDate"
        } yield order._1 -> BSONInteger(order._2)

        Json.obj(sortBy)
      }
    }
  }*/
}
