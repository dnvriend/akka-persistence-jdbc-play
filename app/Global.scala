
import akka.event.Logging
import akka.persistence.jdbc.extension.ScalikeExtension
import play.api._
import play.api.libs.concurrent.Akka
import scalikejdbc.ConnectionPool
import scalikejdbc._
import play.api.Play.current

import scala.util.Try

object Global extends GlobalSettings {

  val journalQuery =
    """
      |CREATE TABLE IF NOT EXISTS public.journal (
      |  persistence_id VARCHAR(255) NOT NULL,
      |  sequence_number BIGINT NOT NULL,
      |  marker VARCHAR(255) NOT NULL,
      |  message TEXT NOT NULL,
      |  created TIMESTAMP NOT NULL,
      |  PRIMARY KEY(persistence_id, sequence_number)
      |)
    """.stripMargin

  val snapShotQuery =
    """
      |CREATE TABLE IF NOT EXISTS public.snapshot (
      |  persistence_id VARCHAR(255) NOT NULL,
      |  sequence_nr BIGINT NOT NULL,
      |  snapshot TEXT NOT NULL,
      |  created BIGINT NOT NULL,
      |  PRIMARY KEY (persistence_id, sequence_nr)
      |)
    """.stripMargin

  override def onStart(app: Application): Unit = {
    ScalikeExtension(Akka.system)
    val log = Logging(Akka.system, this.getClass)
    using(ConnectionPool.borrow()) { (conn: java.sql.Connection) =>
      log.info(s"Metadata: ${conn.getMetaData.getURL}")
      log.info("Creating Akka Persistence Schemas:")
      Try {
        conn.prepareStatement(snapShotQuery).execute()
        conn.prepareStatement(journalQuery).execute()
      }.recover { case t: Throwable =>
        t.printStackTrace()
        log.error(t, "Could not create JDBC journal and snapshot tables")
      }
    }
  }
}
