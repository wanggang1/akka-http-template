package org.gwgs

import scala.concurrent.{ Await, ExecutionContext, Future }
import scala.concurrent.duration.Duration
import scala.util.{ Failure, Success }
import akka.actor.{ ActorRef, ActorSystem }
import akka.http.scaladsl.Http
import akka.http.scaladsl.server.Route
import akka.stream.ActorMaterializer
import org.gwgs.actors.UserRegistryActor
import org.gwgs.routes.UserRoutes

object Main extends App with UserRoutes {

  //abstract in UserRoutes
  implicit val system: ActorSystem = ActorSystem("AkkaHttpServer")

  implicit val materializer: ActorMaterializer = ActorMaterializer()
  implicit val executionContext: ExecutionContext = system.dispatcher

  //abstract in UserRoutes
  val userRegistryActor: ActorRef = system.actorOf(UserRegistryActor.props, "userRegistryActor")

  lazy val routes: Route = userRoutes

  val serverBinding: Future[Http.ServerBinding] = Http().bindAndHandle(routes, "localhost", 8080)

  serverBinding.onComplete {
    case Success(bound) =>
      log.info(s"Server online at http://${bound.localAddress.getHostString}:${bound.localAddress.getPort}/")
    case Failure(e) =>
      log.error(s"Server could not start!")
      e.printStackTrace()
      Await.result(system.terminate, Duration.Inf)
  }

  // Correctly handle Ctrl+C and docker container stop
  sys.addShutdownHook({
    log.info("Shutdown ...")
    Await.result(system.terminate, Duration.Inf)
  })
}
