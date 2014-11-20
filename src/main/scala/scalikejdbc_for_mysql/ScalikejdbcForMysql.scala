package scalikejdbc_for_mysql

import scalikejdbc._

object ScalikejdbcForMysql {

  implicit class SqlSyntaxExtender(that: sqls.type) {
    /**
     * make `group_concat` expression
     * @param column target group column.
     * @param separator separator string.
     * @return (ex) group_concat(foo separator ',')
     */
    def groupConcat(column: SQLSyntax, separator: String): SQLSyntax = {
      sqls"group_concat(${column} separator ${sqls"${separator}"})"
    }
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
