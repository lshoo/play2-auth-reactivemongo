package controllers

import jp.t2v.lab.play2.auth.AuthElement
import play.api._
import play.api.mvc._
import play.api.mvc.Results._

import views._
import models._
import scala.concurrent.Future

/**
 * Created by liaoshifu on 13-12-7.
 */
object Messages extends Controller with AuthElement with AuthConfigImpl {

  def requireNormalUser(account: Account): Future[Boolean] = Future.successful(UserRole.NORMAL.compare(account.role) <= 0)

  def requireAdministrator(account: Account): Future[Boolean] = Future.successful(UserRole.ADMIN.compare(account.role) <= 0)

  /**
   * StackAction method takes '(AuthorityKey, Authority)' as the first argument and a function
   * signature 'RequestWithAttributes[AnyContent] => Request' as th seconds argument
   * and return an 'Action
   *
   * thw 'LoggedIn' method returns current logged in user
   */
  def main = StackAction(AuthorityKey -> (requireNormalUser _)) {
    implicit request => {
      val user = loggedIn
      val title = "message main"
      Ok(html.message.main(title)(html.fullTemplate(user)))
    }
  }

  def list = StackAction(AuthorityKey -> requireNormalUser _) {
    implicit request => {
      val user = loggedIn
      val title = "all message"
      Ok(html.message.list(title)(html.fullTemplate(user)))
    }
  }

  def detail(id: Int) = StackAction(AuthorityKey -> requireNormalUser _) {
    implicit request => {
      val user = loggedIn
      val title = "messages detail"
      Ok(html.message.detail(title + id)(html.fullTemplate(user)))
    }
  }

  // Only Administrator can execute this action
  def write = StackAction(AuthorityKey -> requireAdministrator _) {
    implicit request => {
      val user = loggedIn
      val title = "write message"
      Ok(html.message.write(title)(html.fullTemplate(user)))
    }
  }

}
