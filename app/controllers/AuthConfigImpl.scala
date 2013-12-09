package controllers

import jp.t2v.lab.play2.auth.AuthConfig
import scala.reflect._
import scala.concurrent.{ExecutionContext, Future}
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import play.api.mvc._
import play.api.mvc.Results._
import models._
import jp.t2v.lab.play2.stackc.{RequestWithAttributes, StackableController}
import play.api.data.Form
import play.api.data.Forms._

/**
 * Created by liaoshifu on 13-12-9.
 */
trait AuthConfigImpl extends AuthConfig {

  /**
   * A type that is used to identify a user.
   * 'String', 'Int', 'Long' and so on
   */
  type Id = String

  /**
   * A type that represents a user in application.
   * 'User', 'Account' and so on
   */
  type User = Account

  /**
   * A type that is defined by every action for authorization.
   * This sample uses the following trait.
   *
   * sealed trait Permission
   * case object Administrator extends Permission
   * case object NormalUser extends Permission
   */
  type Authority = User => Future[Boolean]

  /**
   * A 'ClassManifest' is used to retrieve an id from the Cache API.
   * Use something like this:
   */
  val idTag: ClassTag[Id] = classTag[Id]

  /**
   * The session timeout in seconds
   */
  val sessionTimeoutInSeconds: Int = 3600

  /**
   * A function that return a 'User' object  from an 'Id'.
   * can alter the procedure to suit the real application
   */
  def resolveUser(id: Id)(implicit ctx: ExecutionContext): Future[Option[User]] = Account.findById(id)

  /**
   * Where the redirect the user after a successful login.
   */
  def loginSucceeded(request: RequestHeader)(implicit ctx: ExecutionContext): Future[SimpleResult] = {
    //Future.successful(Redirect(routes.Messages.main))
    val uri = request.session.get("access_uri").getOrElse(routes.Messages.main.url.toString)
    Future.successful(Redirect(uri).withSession(request.session - "access_uri"))
  }

  /**
   * Where the redirect the user after logging out
   */
  def logoutSucceeded(request: RequestHeader)(implicit ctx: ExecutionContext) = Future.successful(Redirect(routes.Messages.main))

  /**
   * If the user is not logged in and tries to access a protected resources then redirect them as follows
   */
  def authenticationFailed(request: RequestHeader)(implicit ctx: ExecutionContext) = //Future.successful(Redirect(routes.Application.login))
    Future.successful(Redirect(routes.Application.login).withSession("access_uri" -> request.uri))

  /**
   * If authorization failed (usually incorrect password) redirect the user as follows.
   */
  def authorizationFailed(request: RequestHeader)(implicit ctx: ExecutionContext) = Future.successful(Forbidden("no permission"))

  /**
   * A function that determines what 'Authority' a user has.
   * should alter this procedure to suit real application
   */
  /*def authorize(user: User, authority: Authority)(implicit ctx: ExecutionContext): Future[Boolean] = Future.successful {
    (user.permission, authority) match {
      case (Administrator, _) => true
      case (NormalUser, NormalUser) => true
      case _  => false
    }
  }*/
  def authorize(user: User, authority: Authority)(implicit ctx: ExecutionContext): Future[Boolean] = authority(user)
}
