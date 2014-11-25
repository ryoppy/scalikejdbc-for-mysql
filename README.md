scalikejdbc-for-mysql
=====================

add mysql functions to scalikejdbc. like a `bulk insert`, `group_concat`, ... and more.

### bulk insert

```scala
insert.into(Groups).columns(c.name).valuesBulk(Seq("a"), Seq("b"), Seq("c"))
```
↓
```sql
insert into groups (name) values ('a'), ('b'), ('c');
```

### case expression

```scala
select(
  sqls
    .caseA(g.name)
    .when("a").thenA("one")
    .when("b").thenA("two")
    .elseA("other")
    .end
).from(Groups as g)
```
↓
```sql
select (case g.name when 'a' then 'one' when 'b' then 'two' else 'other' end) from groups g;
```

```scala
select(
  sqls
    .caseA
    .when(sqls.eq(g.name, "a")).thenA("one")
    .when(sqls.eq(g.name, "b")).thenA("two")
    .elseA("other")
    .end
).from(Groups as g)
```
↓
```sql
select (case when g.name = 'a' then 'one' when g.name = 'b' then 'two' else 'other' end) from groups g;
```

### functions

#### group concat

```scala
sqls.groupConcat(g.name, ",")
```
↓
```sql
group_concat(g.name separator ',')
```
