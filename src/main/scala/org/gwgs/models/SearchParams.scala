package org.gwgs.models

import org.gwgs.validation.Validations._
import org.gwgs.validation._

case class SearchParams(name: Option[String], minYearsWorked: Option[Int], maxYearsWorked: Option[Int])

object SearchParams {
  import cats.implicits._

  /**
   * Validation rules:
   *   name is required
   *   name must not empty
   *   minYearsWorked and maxYearsWorked are optional, but when presenting they must
   *   minYearsWorked > 0
   *   maxYearsWorked > 0
   *   minYearsWorked <= maxYearsWorked
   */
  implicit class SearchParamsValidation(params: SearchParams) extends Validatable[SearchParams] {

    def validate: ValidationResult[SearchParams] = {
      (validateName, validateMinAge, validateMaxAge).mapN(SearchParams(_, _, _)) andThen validateMinMax
    }

    private def validateName: ValidationResult[Option[String]] =
      if (params.name.isEmpty)
        MissingName.invalidNel
      else if (params.name.get.trim.isEmpty)
        InvalidName.invalidNel
      else
        params.name.validNel

    private def validateMinAge: ValidationResult[Option[Int]] =
      if (params.minYearsWorked.isEmpty || params.minYearsWorked.get > 0)
        params.minYearsWorked.validNel
      else
        InvalidMinYears.invalidNel

    private def validateMaxAge: ValidationResult[Option[Int]] =
      if (params.maxYearsWorked.isEmpty || params.maxYearsWorked.get > 0)
        params.maxYearsWorked.validNel
      else
        InvalidMaxYears.invalidNel

    private def validateMinMax(p: SearchParams): ValidationResult[SearchParams] =
      (p.minYearsWorked, p.maxYearsWorked) match {
        case (Some(minYears), Some(maxYears)) if minYears > maxYears => InvalidYearRange.invalidNel
        case _ => p.validNel
      }
  }

}