scalikejdbc-for-mysql
=====================

add mysql functions to scalikejdbc. like a `bulk insert`, `group_concat`, ... and more.

### bulk insert

```
insert.into(Groups).columns(c.name).valuesBulk(Seq("a"), Seq("b"), Seq("c"))
↓
insert into groups (name) values ('a'), ('b'), ('c');
```

### case expression

```
select(
  sqls
    .caseA(g.name)
    .when("a").thenA("one")
    .when("b").thenA("two")
    .elseA("other")
    .end
).from(Groups as g)
↓
select (case g.name when 'a' then 'one' when 'b' then 'two' else 'other' end) from groups g;
```

```
select(
  sqls
    .caseA
    .when(sqls.eq(g.name, "a")).thenA("one")
    .when(sqls.eq(g.name, "b")).thenA("two")
    .elseA("other")
    .end
).from(Groups as g)
↓
select (case when g.name = 'a' then 'one' when g.name = 'b' then 'two' else 'other' end) from groups g;
```

### functions

#### group concat

```
sqls.groupConcat(g.name, ",")
↓
group_concat(g.name separator ',')
```
