package org.gwgs.repository

import com.typesafe.scalalogging.StrictLogging
import reactivemongo.api.collections.bson.BSONCollection
import reactivemongo.api.{ MongoConnection, MongoDriver }

import scala.concurrent.Future
import scala.concurrent.duration._

/**
 * Handle MongoDB operations
 */
object DBDriver extends StrictLogging {
  import scala.concurrent.ExecutionContext.Implicits.global
  private val databaseName = "gwgs"

  private val mDriver: MongoDriver = new reactivemongo.api.MongoDriver

  logger.info(s"MongoDB connection: 127.0.0.1:27017")
  private val mConnection: MongoConnection = mDriver.connection(List("127.0.0.1:27017"))

  /*
   * get collection by name
   */
  def dbCollection(collectionName: String): Future[BSONCollection] =
    mConnection.database(databaseName).map(_.collection(collectionName))

  /*
   * close mongodb connection
   */
  def close() = {
    implicit val timeout = FiniteDuration(1000, MILLISECONDS)
    mConnection.askClose()
    mDriver.close()
  }

}

