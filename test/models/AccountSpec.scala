package models

import org.specs2.mutable.Specification
import scala.concurrent.ExecutionContext.Implicits.global

/**
 * Created with IntelliJ IDEA.
 * User: liaoshifu
 * Date: 13-12-9
 * Time: 下午2:42
 * To change this template use File | Settings | File Templates.
 */
class AccountSpec extends Specification {

  "Account object " should {
    "count method " in {
      val futureCount = Account.count(None)

      futureCount must be_== (2).await
    }

    "isEmpty method" in {
      val futureEmpty = Account.isEmpty

      futureEmpty must === (false).await
    }
  }
}
