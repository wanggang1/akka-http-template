package org.gwgs

package object validation {

  import cats.data._

  abstract class ValidationFailure(val message: String)

  type ValidationResult[A] = ValidatedNel[ValidationFailure, A]

  trait Validatable[A] {
    def validate: ValidationResult[A]
  }

}
