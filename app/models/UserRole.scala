package models


/**
 * Created by liaoshifu on 13-12-7.
 */

case class UserRole(name: String = "normal", value: Int = 5) {
  def compare(other: UserRole): Int = value - other.value
}

object UserRole {

  val NORMAL = UserRole()
  val ADMIN =  UserRole("admin", 10)

}
