package controllers

import play.api._
import play.api.mvc._
import play.api.libs.json._
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import scala.concurrent.Future

import reactivemongo.api._

import play.modules.reactivemongo.MongoController
import play.modules.reactivemongo.json.collection.JSONCollection
import reactivemongo.bson.BSONDocument

/**
 * Created by liaoshifu on 13-12-7.
 */
object ReactiveMongoController extends Controller with MongoController {

  /**
   * Get a JSONCollection (a Collection implementation that is designed to work with JsObject, Reads and Writes.
   * Note that the 'collection' is not a 'val', but a 'def', we do not store the collection
   * reference to avoid potential problems in development with Play hot-reloading.
   */
  def collection: JSONCollection = db.collection[JSONCollection]("persons")

  def index = Action { Ok("ReactiveMongo works!") }

  def create(name: String, age: Int) = Action.async {
    val json = Json.obj(
      "name" -> name,
      "age" -> age,
      "created" -> new java.util.Date().getTime()
    )

    collection.insert(json).map(lastError => {
      Ok("Mongo LastError: %s".format(lastError))
    })
  }

  def createFromJson = Action.async(parse.json) {
    request => {
      case class Bod(s: String)

      collection.insert(request.body).map( lastError => {
        Ok("Mongo LastError: %s from createFromJson".format(lastError))
      })
    }
  }

  // queries for a person name
  def findByName(name: String) = Action.async {
    val cursor: Cursor[JsObject] = collection.find(Json.obj("name" -> name)).sort(Json.obj("created" -> -1)).cursor[JsObject]

    // gather all the JsObjects in a list
    val futurePersonsList: Future[List[JsObject]] = cursor.collect[List]()

    // transform the list into a JsArray
    val futurePersonsJsonArray: Future[JsArray] = futurePersonsList.map { persons => Json.arr(persons) }

    futurePersonsJsonArray.map { persons =>
      Ok(persons)
    }

  }

  def allPersons = Action.async {
    val cursor: Cursor[JsObject] = collection.find(Json.obj("age" -> Json.obj("$gt" -> 0))).cursor[JsObject]
    val futurePersonsList = cursor.collect[List]()

    futurePersonsList.map {
      persons => Json.arr(persons)
    }.map {
      persons => {
        Ok(persons)
      }
    }
  }

  /**
   * Using case class + Json Writes and Reads
   */
  /*import play.api.data.Form
  import models._
  import models.JsonFormats._

  def createCC = Action.async {
    val user = User(29, "John", "Smith", List(Feed("Slashdot news", "http://slashdot.org/slashdot.rdf")))
    val futureResult = collection.insert(user)

    futureResult.map (_ => Ok)
  }

  def findByNameCC(lastName: String) = Action.async {
    val cursor: Cursor[User] = collection.find(Json.obj("lastName" -> lastName)).sort(Json.obj("created" -> -1)).cursor[User]
    val futureUsersList: Future[List[User]] = cursor.collect[List]()

    futureUsersList.map {
      persons => {
        Ok(persons.toString)
      }
    }
  }*/
}
