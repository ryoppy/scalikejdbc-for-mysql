package scalikejdbc_for_mysql.builder

import scalikejdbc._

class RawSQLBuilder[A](val sql: SQLSyntax) extends SQLBuilder[A] {
  override def append(part: SQLSyntax): SQLBuilder[A] = throw new IllegalStateException("This must be a library bug.")
}