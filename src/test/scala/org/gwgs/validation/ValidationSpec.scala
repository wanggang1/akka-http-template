package org.gwgs.validation

import cats.data.Validated.{ Invalid, Valid }
import org.gwgs.models.SearchParams
import org.gwgs.validation.Validations._
import org.scalatest.{ Matchers, WordSpec }

class ValidationSpec extends WordSpec with Matchers {

  "Validation" should {

    "validate SearchParams" in {
      val params = SearchParams(Some("blah"), Some(1), Some(2))

      params.validate match {
        case Valid(p) => p shouldBe params
        case _ => fail("Expecting valid SearchParams")
      }
    }

    "validate SearchParams with None minYearsWorked or maxYearsWorked" in {
      val params = SearchParams(Some("blah"), None, None)

      params.validate match {
        case Valid(p) => p shouldBe params
        case Invalid(failures) => fail("Expecting valid SearchParams with None")
      }
    }

    "validate SearchParams when minYearsWorked = maxYearsWorked" in {
      val params = SearchParams(Some("blah"), Some(2), Some(2))

      params.validate match {
        case Valid(p) => p shouldBe params
        case Invalid(failures) => fail("Expecting valid SearchParams with None")
      }
    }

    "fail SearchParams validation when name is None" in {
      val params = SearchParams(None, Some(1), Some(2))

      params.validate match {
        case Invalid(failures) =>
          val errors = failures.toList
          errors.size shouldBe 1
          errors.contains(MissingName) shouldBe true
        case _ => fail("Expecting Invalid SearchParams")
      }
    }

    "fail SearchParams validation when name is empty" in {
      val params = SearchParams(Some(" "), Some(1), Some(2))

      params.validate match {
        case Invalid(failures) =>
          val errors = failures.toList
          errors.size shouldBe 1
          errors.contains(InvalidName) shouldBe true
        case _ => fail("Expecting Invalid SearchParams")
      }
    }

    "fail SearchParams validation with empty name, negative minYearsWorked/maxYearsWorked" in {
      val params = SearchParams(Some(" "), Some(-1), Some(-2))

      params.validate match {
        case Invalid(failures) =>
          val errors = failures.toList
          errors.size shouldBe 3
          errors.contains(InvalidName) shouldBe true
          errors.contains(InvalidMaxYears) shouldBe true
          errors.contains(InvalidMinYears) shouldBe true
        case _ => fail("Expecting Invalid SearchParams")
      }
    }

    "fail SearchParams validation when minYearsWorked > maxYearsWorked" in {
      val params = SearchParams(Some("blah"), Some(2), Some(1))

      params.validate match {
        case Invalid(failures) =>
          val errors = failures.toList
          errors.size shouldBe 1
          errors.contains(InvalidYearRange) shouldBe true
        case _ => fail("Expecting Invalid SearchParams")
      }
    }

  }

}