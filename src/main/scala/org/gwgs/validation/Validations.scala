package org.gwgs.validation

object Validations {
  case object MissingName extends ValidationFailure("name is required")
  case object InvalidName extends ValidationFailure("name is empty")
  case object InvalidMinYears extends ValidationFailure("minYearsWorked must be greater than zero")
  case object InvalidMaxYears extends ValidationFailure("maxYearsWorked must be greater than zero")
  case object InvalidYearRange extends ValidationFailure("minYearsWorked must not be greater than maxYearsWorked")
}
