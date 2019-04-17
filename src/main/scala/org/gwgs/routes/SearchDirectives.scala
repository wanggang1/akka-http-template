package org.gwgs.routes

import akka.http.scaladsl.server.directives.RouteDirectives
import akka.http.scaladsl.server.{ Directive, Directive1, ValidationRejection }
import cats.data.Validated.{ Invalid, Valid }
import org.gwgs.models.SearchParams

trait SearchDirectives {
  import RouteDirectives._

  /**
   * Checks the given condition before running its inner route.
   * If the condition fails the route is rejected with a [[ValidationRejection]].
   */
  def validateParams(ageParam: SearchParams): Directive1[SearchParams] =
    Directive { inner =>
      ageParam.validate match {
        case Valid(p) =>
          inner(Tuple1(p))
        case Invalid(errors) =>
          val errorMsg = errors.toList.map(_.message).mkString(", ")
          reject(ValidationRejection(errorMsg))
      }
    }

}
