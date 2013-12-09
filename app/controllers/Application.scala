package controllers

import play.api._
import play.api.mvc._
import play.api.data._
import play.api.data.Forms._
import play.api.libs.concurrent.Execution.Implicits.defaultContext

import models._
import views._
import jp.t2v.lab.play2.auth.LoginLogout
import scala.concurrent.Future

object Application extends Controller with LoginLogout with AuthConfigImpl{

  def index = Action {
    Ok(views.html.index("Your new application is ready."))
  }

  /**
   * the application's login form. Alter it to fit real application
   */
  val loginForm = Form {
    mapping(
      "email" -> email,
      "password" -> text
    )(Account.authenticate)(_.map(u => (u.email, ""))).verifying("Invalid email or password", result => result.isDefined)
  }

  /**
   * After the login page action to suit application
   */
  def login = Action {
    implicit request => {
      Ok(html.login(loginForm))
    }
  }

  /**
   * Return the 'gotoLogoutSucceeded' method's result in the logout action.
   *
   * Since the 'gotoLogoutSucceeded' returns 'Future[SimpleResult]',
   * so can add a procedure like the following.
   *
   *  gotoLogoutSucceeded.map(_.flashing("success" -> "You're been logged out"))
   */
  def logout = Action.async {
    implicit request => {
      gotoLogoutSucceeded
    }
  }

  /**
   * Return the 'gotoLoginSucceeded' method's result in the login action.
   *
   * Since the 'gotoLoginSucceeded' return 'Future[SimpleResult]'.
   * so can add a procedure like the 'gotoLogoutSucceeded'.
   */
  def authenticate = Action.async {
    implicit request => {
      loginForm.bindFromRequest.fold(
        formWithErrors => Future.successful(BadRequest(html.login(formWithErrors))),
        user => gotoLoginSucceeded(user.get.id)
      )
    }
  }

}