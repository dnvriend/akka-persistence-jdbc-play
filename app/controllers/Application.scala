package controllers

import akka.actor.{ActorLogging, Props}
import akka.persistence.{RecoveryCompleted, PersistentActor}
import akka.util.Timeout
import play.api.Play.current
import play.api.libs.concurrent.Akka
import play.api.mvc._
import akka.pattern.ask

import scala.concurrent.Await
import scala.concurrent.duration._

object CounterActor {
  case object BumpCounter
  case object ResetCounter
  case object GetCounterState

  case class CounterChanged(state: Int)
}

class CounterActor extends PersistentActor with ActorLogging {
  import CounterActor._

  override def persistenceId: String = "counter"

  var counter: Int = 0

  def updateState(event: CounterChanged) = event match {
    case msg @ CounterChanged(state) =>
      log.info("(UPDATE_STATE): {}", msg)
      counter = state

    case msg =>
      log.info("(UPDATE_STATE): Dropping message: {}", msg)
  }

  override def receiveRecover: Receive = {
    case msg @ CounterChanged(state) =>
      log.info("(RECOVER): {}", msg)
      updateState(msg)

    case msg @ RecoveryCompleted =>
      log.info("(RECOVER): {}", msg)

    case msg =>
      log.info("(RECOVER): Dropping message: {}", msg)
  }

  override def receiveCommand: Receive = {
    case msg @ BumpCounter =>
      log.info("(RECEIVE_COMMAND): {}", msg)
      val event = CounterChanged(counter + 1)
      persist(event)(updateState)

    case msg @ ResetCounter =>
      log.info("(RECEIVE_COMMAND): {}", msg)
      persist(CounterChanged(0))(updateState)

    case msg @ GetCounterState =>
      log.info("(RECEIVE_COMMAND): {}", msg)
      sender() ! counter
  }
}

object Application extends Controller {

  implicit val timeout = Timeout(1.second)
  val counterActor = Akka.system.actorOf(Props(new CounterActor), "counterActor")

  def index = Action {
    val state: Int = Await.result((counterActor ? CounterActor.GetCounterState).mapTo[Int], 1.second)
    // minor business logic here
    if(state != 10)
      counterActor ! CounterActor.BumpCounter
    else counterActor ! CounterActor.ResetCounter
    Ok(views.html.index(s"Your new application is ready: ($state)"))
  }
}