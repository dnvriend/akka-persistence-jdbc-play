package journal

import akka.persistence.PersistentRepr
import akka.persistence.jdbc.serialization.journal.JsonJournalConverter
import akka.serialization.Serialization
import controllers.CounterActor.CounterChanged
import spray.json.DefaultJsonProtocol
import spray.json._

class JsonJournalSerializer extends JsonJournalConverter with DefaultJsonProtocol {

  case class Wrapper(json: String, name: String)
  implicit val wrapperFormat = jsonFormat2(Wrapper)
  implicit val counterChangedFormat = jsonFormat1(CounterChanged)

  override def marshal(value: PersistentRepr)(implicit serialization: Serialization): String =
    value.payload match {
      case msg @ CounterChanged(_) =>
        val json = Wrapper(msg.toJson.compactPrint, msg.getClass.getSimpleName).toJson.compactPrint
        super.marshal(value.withPayload(json).asInstanceOf[PersistentRepr])

      case _ => super.marshal(value)
    }

  override def unmarshal(value: String)(implicit serialization: Serialization): PersistentRepr = {
    val repr = super.unmarshal(value)
    repr.payload.asInstanceOf[String].parseJson.convertTo[Wrapper] match {
      case Wrapper(json, "CounterChanged") =>
        repr.withPayload(json.parseJson.convertTo[CounterChanged]).asInstanceOf[PersistentRepr]
      case _ => repr
    }
  }
}
