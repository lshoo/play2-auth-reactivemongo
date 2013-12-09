package models

import org.mindrot.jbcrypt.BCrypt
import scala.concurrent.{Await, Future}
import scala.concurrent.duration._
import play.modules.reactivemongo.json.collection.JSONCollection
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import play.api.libs.iteratee.Enumerator
import reactivemongo.core.commands._
import reactivemongo.bson._
import play.api.libs.json._

import JsonFormats._

/**
 * Created by liaoshifu on 13-12-7.
 */
case class Account(id: String, email: String, name: String, password: String, role: UserRole)

object Account {


  def collection: JSONCollection = Mongo.db.collection("accounts")

  def insertAll(accounts: Seq[Account]) = collection.bulkInsert(Enumerator(accounts))

  def isEmpty: Future[Boolean] = count(None).map{ count => {
    if (count > 0) false
    else true
  }}

  def count(query: Option[BSONDocument]): Future[Int] = Mongo.db.command(Count(collection.name, query))

  def authenticate(email: String, password: String): Option[Account] = {
    val optionAccount = Await.result(findByEmailAndPassword(email, password), 5 seconds)
    optionAccount
  }

  def findByEmailAndPassword(email: String, password: String): Future[Option[Account]] = collection.find(Json.obj("email" -> email, "password" -> password)).one[Account]

  def findById(id: String): Future[Option[Account]] = collection.find(Json.obj("id" -> id)).one[Account]

  def findByEmail(email: String): Future[Option[Account]] = collection.find(Json.obj("email" -> email)).one[Account]

  // 本意是初始化mongodb的，但实际没有运行，只能手动插入数据
  // 原因不明
  def init(): Unit = {

    val empty = Await.result(isEmpty, 5 seconds)

    if (empty) {
      Mongo.db.command(new CreateCollection("accounts"))
      collection.insert(Account("1", "abc@126.com", "abc", "abc", UserRole.ADMIN))
      collection.insert(Account("2", "bcd@126.com", "bcd", "bcd", UserRole.NORMAL))
    }

  }
}
