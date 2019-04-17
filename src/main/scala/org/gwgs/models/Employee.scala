package org.gwgs.models

final case class Employee(name: String, yearsWorked: Int, countryOfResidence: String)
final case class Employees(users: Seq[Employee])