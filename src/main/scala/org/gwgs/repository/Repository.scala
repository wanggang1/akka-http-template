package org.gwgs.repository

import com.typesafe.scalalogging.StrictLogging
import org.gwgs.models.Employee
import reactivemongo.api.collections.bson.BSONCollection
import reactivemongo.bson.BSONDocument

import scala.concurrent.Future

sealed trait Response
case class Inserted(id: String) extends Response
case class Deleted(id: String) extends Response
case class UpdateResp(update: Either[String, Int]) extends Response

trait Repository[T] {
  def insert(entity: T): Future[Inserted]
  def get(id: String): Future[Option[Employee]]
  def update(entity: T): Future[UpdateResp]
  def delete(id: String): Future[Deleted]
}

object EmployeeRepository {
  val collection = "employee"
}

class EmployeeRepository extends Repository[Employee] with StrictLogging {
  import scala.concurrent.ExecutionContext.Implicits.global
  import Implicits._

  val collection: Future[BSONCollection] = DBDriver.dbCollection(EmployeeRepository.collection)

  def insert(entity: Employee): Future[Inserted] =
    for {
      coll <- collection
      result <- coll.insert(ordered = false).one(entity)
    } yield {
      logger.info(s"insert result: $result")
      Inserted(entity.name)
    }

  def get(id: String): Future[Option[Employee]] = {
    val selector = BSONDocument("name" -> id)
    val projection: Option[BSONDocument] = None

    for {
      coll <- collection
      empl <- coll.find(selector, projection).one[Employee]
    } yield empl
  }

  def update(entity: Employee): Future[UpdateResp] = {
    val selector = BSONDocument("name" -> entity.name)

    for {
      coll <- collection
      result <- coll.update(ordered = false).one(selector, entity, upsert = true)
    } yield {
      logger.info(s"insert result: $result")
      val updated = result.errmsg match {
        case Some(errMsg) =>
          logger.info(s"ERROR, original DB error: $errMsg")
          Left(errMsg)
        case _ =>
          val nModified = result.nModified
          if (nModified == 0) logger.info(s"WARN: no document modified for ${entity.name}")
          Right(nModified)
      }

      UpdateResp(updated)
    }
  }

  def delete(id: String): Future[Deleted] = {
    val selector = BSONDocument("name" -> id)

    for {
      coll <- collection
      result <- coll.delete.one(selector)
    } yield {
      logger.info(s"delete result: $result")
      Deleted(id)
    }
  }

}
