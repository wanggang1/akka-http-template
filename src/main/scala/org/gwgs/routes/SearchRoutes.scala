package org.gwgs.routes

import akka.actor.ActorSystem
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server._
import akka.http.scaladsl.server.directives.MethodDirectives.get
import akka.http.scaladsl.server.directives.RouteDirectives.complete
import org.gwgs.models.{ SearchParams, Employee, Employees }
import org.gwgs.utils.JsonSupport

import scala.concurrent.Future

/**
 * custom validation directive validateParams is used instead of the standard validate,
 * https://doc.akka.io/docs/akka-http/current/routing-dsl/directives/misc-directives/validate.html
 */
trait SearchRoutes extends SearchDirectives with JsonSupport {

  // we leave these abstract, since they will be provided by the App
  implicit def system: ActorSystem

  lazy val searchRoute: Route =
    pathPrefix("search") {
      pathEndOrSingleSlash {
        get {
          parameters(
            "name".as[String].?,
            'minYearsWorked.as[Int].?,
            'maxYearsWorked.as[Int].?) {
              (name, minYearsWorked, maxYearsWorked) =>
                validateParams(SearchParams(name, minYearsWorked, maxYearsWorked)) { param =>
                  val resp: Future[Employees] = Future.successful(Employees(List.empty[Employee]))
                  complete(resp)
                }
            }
        }
      }
    }
}