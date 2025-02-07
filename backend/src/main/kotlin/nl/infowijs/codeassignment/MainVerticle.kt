package nl.infowijs.codeassignment

import io.vertx.core.AbstractVerticle
import io.vertx.core.Promise
import io.vertx.core.Vertx
import io.vertx.ext.web.Router
import nl.infowijs.codeassignment.controllers.HealthController
import nl.infowijs.codeassignment.controllers.MessagesController

class MainVerticle : AbstractVerticle() {
  fun createRouter(vertx: Vertx) = Router.router(vertx).apply {
    val messagesController = MessagesController()
    get("/healthz").handler(HealthController.healthCheck)
    get("/messages").handler(messagesController::listMessages)
  }

  override fun start(startPromise: Promise<Void>) {
    val router = createRouter(vertx)

    vertx
      .createHttpServer()
      .requestHandler(router)
      .listen(8888) { http ->
        if (http.succeeded()) {
          startPromise.complete()
          println("HTTP server started on port 8888")
        } else {
          startPromise.fail(http.cause());
        }
      }
  }
}
