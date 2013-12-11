package controllers

import play.api.mvc.WithFilters
import play.api._
import play.filters.csrf._
import models.{UserRole, Mongo, Account}

/**
 * Created by liaoshifu on 13-12-7.
 */
object Global extends WithFilters(CSRFFilter()) with GlobalSettings {

  override def onStart(app: play.api.Application) {
    Account.init()
  }
}
