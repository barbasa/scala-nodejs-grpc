import java.util.logging.Logger

import demogrpc.restaurants._
import demogrpc.restaurants.SearchRestaurantServiceGrpc.SearchRestaurantService
import io.grpc.{Server, ServerBuilder}

import scala.concurrent.{ExecutionContext, Future}


object RestaurantServer {
  private val logger = Logger.getLogger(classOf[RestaurantServer].getName)

  def main(args: Array[String]): Unit = {
    val server = new RestaurantServer(ExecutionContext.global)
    server.start()
    server.blockUntilShutdown()
  }

  private val port = 50051
}


class RestaurantServer(executionContext: ExecutionContext) { self =>
  private[this] var server: Server = null

  private def start(): Unit = {
    server = ServerBuilder.forPort(RestaurantServer.port).addService(SearchRestaurantServiceGrpc.bindService(new RestaurantServerImpl, executionContext)).build.start
    RestaurantServer.logger.info("Server started, listening on " + RestaurantServer.port)
    sys.addShutdownHook {
      System.err.println("*** shutting down gRPC server since JVM is shutting down")
      self.stop()
      System.err.println("*** server shut down")
    }
  }

  private def stop(): Unit = {
    if (server != null) {
      server.shutdown()
    }
  }

  private def blockUntilShutdown(): Unit = {
    if (server != null) {
      server.awaitTermination()
    }
  }

  private class RestaurantServerImpl extends SearchRestaurantServiceGrpc.SearchRestaurantService {

    val restaurant1 = Restaurant(
      id = "123456",
      name = "Pizza Roma",
      address = "Uxbridge road",
      cuisineKeywords = Seq("pizza", "pasta", "mandolino"),
      logo = "www.pizzaromalog.png",
      distanceMeters = 10L,
      Some(GeoPoint(51.3, 0.23))
    )

    val restaurant2 = Restaurant(
      id = "123456",
      name = "Pizza Roma",
      address = "Uxbridge road",
      cuisineKeywords = Seq("pizza", "pasta", "mandolino"),
      logo = "www.pizzaromalog.png",
      distanceMeters = 10L,
      Some(GeoPoint(51.3, 0.23))
    )

    override def searchRestaurant(request: SearchRestaurantRequest): Future[SearchRestaurantResponse] = Future.successful(SearchRestaurantResponse(restaurants = Seq(restaurant1, restaurant2), info = Some(SearchRestaurantResponseInfo(totalHits = 2, timeTaken = 3L))))

    override def lookupRestaurant(request: LookupRestaurantRequest): Future[LookupRestaurantResponse] = ???
  }

}



