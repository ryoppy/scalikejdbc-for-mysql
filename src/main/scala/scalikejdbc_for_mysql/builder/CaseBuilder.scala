package scalikejdbc_for_mysql.builder

import scalikejdbc._

trait CaseBuilder[A] extends SQLBuilder[A] {
  override def append(part: SQLSyntax): CaseBuilder[A] = CaseBuilder[A](sqls"${sql} ${part}")
  def when(s: Any): CaseBuilder[A] = CaseBuilder[A](sqls"${sql} when ${s}")
  def when(s: SQLSyntax): CaseBuilder[A] = CaseBuilder[A](sqls"${sql} when ${s}")

  def `then`(s: Any): CaseBuilder[A] = CaseBuilder[A](sqls"${sql} then ${s}")
  def thenA(s: Any): CaseBuilder[A] = `then`(s)

  def `else`(s: Any): CaseBuilder[A] = CaseBuilder[A](sqls"${sql} else ${s}")
  def elseA(s: Any): CaseBuilder[A] = `else`(s)

  def end: SQLSyntax = sqls"${sql} end)"
}
object CaseBuilder {
  def apply[A](sql :SQLSyntax): CaseBuilder[A] = new RawSQLBuilder[A](sql) with CaseBuilder[A]
}