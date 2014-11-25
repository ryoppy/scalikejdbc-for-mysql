package scalikejdbc_for_mysql

import scalikejdbc._
import scalikejdbc_for_mysql.builder.CaseBuilder

object ScalikejdbcForMysql {

  implicit class SqlSyntaxExtender(that: sqls.type) {
    /**
     * make `group_concat` expression
     *
     * {{{
     * sqls.groupConcat(g.name, ",")
     * // group_concat(g.name separator ',')
     * }}}
     *
     * @param column target group column.
     * @param separator separator string.
     * @return (ex) group_concat(foo separator ',')
     */
    def groupConcat(column: SQLSyntax, separator: String): SQLSyntax = {
      sqls"group_concat(${column} separator ${sqls"${separator}"})"
    }

    /**
     * case expression
     * `case` or caseA.
     *
     * {{{
     * sqls
     *   .caseA
     *   .when(sqls.eq(g.id, 1)).thenA("one")
     *   .when(sqls.eq(g.id, 2)).thenA("two")
     *   .elseA("other")
     *   .end
     * // (case when g.id = 1 then 'one' when g.id = 2 then 'two' else 'other' end)
     * }}}
     *
     * @param sql
     * @tparam A
     * @return
     */
    def `case`[A](sql: SQLSyntax): CaseBuilder[A] = CaseBuilder[A](sqls"(case ${sql}")
    def `case`[A]: CaseBuilder[A] = CaseBuilder[A](sqls"(case ")
    def caseA[A](sql: SQLSyntax): CaseBuilder[A] = `case`[A](sql)
    def caseA[A]: CaseBuilder[A] = `case`[A]
  }

  implicit class InsertSQLBuilderExtender(that: InsertSQLBuilder) {
    /**
     * bulk insert expression.
     * @param vss values of list
     * @return (ex) insert into groups (name) values ('a'), (2);
     */
    def valuesBulk(vss: Seq[Any]*): InsertSQLBuilder = {
      val r = vss.map { vs: Seq[Any] =>
        val t: SQLSyntax = sqls.csv(vs.map(v => sqls"${v}"): _*)
        sqls"(${t})"
      }
      that append sqls" values ${r}"
    }

    /**
     * on duplicate key update expression.
     * @param cvs column and value list
     * @return (ex) insert into groups (id, name) values (1, 'f') on duplicate key update name = 'g';
     */
    def onDuplicateKeyUpdate(cvs: (SQLSyntax, Any)*): InsertSQLBuilder = {
      val ss: Seq[SQLSyntax] = cvs.map { case (c, v) => sqls"${c} = ${v}"}
      val s = sqls.csv(ss: _*)
      that append sqls"on duplicate key update ${s}"
    }
  }
}