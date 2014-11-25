package scalikejdbc_for_mysql

import org.specs2.mutable.Specification
import scalikejdbc._
import scalikejdbc.config.DBs
import scalikejdbc.specs2.mutable.AutoRollback

class ScalikejdbcForMysqlSpec extends Specification with TestHelper {

  sequential
  DBs.setupAll()
  import scalikejdbc_for_mysql.ScalikejdbcForMysql._

  "SqlSyntaxPlus" should {
    "make groupConcat expression" in new AutoRollback {
      applyUpdate {
        insert.into(Groups).columns(c.name).valuesBulk(Seq("a"), Seq("b"), Seq("a"))
      }
      val r = withSQL {
        select(sqls.groupConcat(g.name, ",")).from(Groups as g).groupBy(g.name)
      }
        .map(_.string(1)).list().apply()
      r must_== List("a,a", "b")
    }
    "case statement" in new AutoRollback {
      applyUpdate {
        insert.into(Groups).columns(c.name).valuesBulk(Seq("a"), Seq("b"), Seq("c"))
      }
      val r = withSQL {
        select(
          sqls
            .caseA
            .when(sqls.eq(g.name, "a")).thenA("one")
            .when(sqls.eq(g.name, "b")).thenA("two")
            .elseA("other")
            .end
        ).from(Groups as g)
      }
        .map(_.string(1)).list().apply()
      r must_== List("one", "two", "other")

      val r2 = withSQL {
        select(
          sqls
            .caseA(g.name)
            .when("a").thenA("one")
            .when("b").thenA("two")
            .elseA("other")
            .end
        ).from(Groups as g)
      }
        .map(_.string(1)).list().apply()
      r2 must_== List("one", "two", "other")
    }
  }

  "InsertSQLBuilderPlus" should {
    "execute bulk insert" in new AutoRollback {
      applyUpdate {
        insert.into(Groups).columns(c.name).valuesBulk(Seq("a"), Seq(2))
      }
      groupsCount() must_== 2
    }

  }
}

// for test classes
// =================================
case class Groups(id: Long, name: String)

object Groups extends SQLSyntaxSupport[Groups] {
  def apply(g: ResultName[Groups])(rs: WrappedResultSet): Groups = autoConstruct(rs, g)
}

// test helper
// =================================
trait TestHelper {
  import sqls.count

  lazy val g = Groups.syntax("g")
  lazy val c = Groups.column

  def groupsCount()(implicit session: DBSession): Int = withSQL {
    select(count(g.id)).from(Groups as g)
  }.map(_.int(1)).single().apply().get
}