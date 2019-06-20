package org.gwgs

/**
  * cats validation basic types:
  *
  * Validated[E, A] has an instance of Applicative, which also has an constraint
  * of CommutativeApplicative[E] (E must be able to combine, and e1 + e2 = e2 + e1)
  *
  * sealed abstract class Validated[+E, +A] extends Product with Serializable {
  *   // Implementation elided
  * }
  * final case class Valid[+A](a: A) extends Validated[Nothing, A]
  * final case class Invalid[+E](e: E) extends Validated[E, Nothing]
  *
  * type ValidatedNel[+E, +A] = Validated[NonEmptyList[E], A]
  *
  * // a non empty list that ensure at least 1 element
  * final case class NonEmptyList[+A](head: A, tail: List[A])
  */
package object validation {

  import cats.data._

  abstract class ValidationFailure(val message: String)

  type ValidationResult[A] = ValidatedNel[ValidationFailure, A]

  trait HasValidate[A] {
    def validate: ValidationResult[A]
  }

}
