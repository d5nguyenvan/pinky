package org.pinky.code.util.jdbc




import collection.mutable.Map



import collection.mutable.ListBuffer
import java.sql._
/**
 * based on <a href="http://scala.sygneca.com/code/simplifying-jdbc">this article</a><br>
 * <br>
 * this class should provide an easier way to access jdbc
 * using prepared statements:
 * <pre>
 * for (conn &lt;- using (connect("jdbc:h2:mem:", "sa", ""))) {
 *   conn execute "CREATE TABLE...."
 *   val insertPerson = conn prepareStatement "insert into person(type, name) values(?, ?)"
 *   insertPerson  &lt;&lt; 1 &lt;&lt; "john" execute
 *   <br>
 *   val ret = conn.query("SELECT * FROM PERSON WHERE ID=?",1)
 *   ret.foreach( row =&gt; {row("NAME") should equal("john") } )
 *   conn execute("insert into person(type, name) values(?, ?)",2,"peter")
 *   <br>
 *   val ret2 = conn.query("SELECT * FROM PERSON WHERE ID=?", 2)
 *   ret2.toArray(0)("NAME") should equal("peter")
 *   <br>
 *   val selectStatement = conn.prepareStatement("SELECT * FROM PERSON WHERE ID=?")
 *   selectStatement &lt;&lt; 2
 *   selectStatement.query.toArray(0)("NAME") should equal("peter")
 * }
 *
 * </pre>
 *
 */
object Predef {

  implicit def anyRef2Boolean(a: AnyRef):Boolean = a.asInstanceOf[Boolean];

  implicit def anyRef2Byte(a: AnyRef):Byte = a.asInstanceOf[Byte];

  implicit def anyRef2Int(a: AnyRef):Int = a.asInstanceOf[Int];

  implicit def anyRef2Long(a: AnyRef):Long = a.asInstanceOf[Long];

  implicit def anyRef2Float(a: AnyRef):Float = a.asInstanceOf[Float];

  implicit def anyRef2Double(a: AnyRef):Double = a.asInstanceOf[Double];

  implicit def anyRef2String(a: AnyRef):String = a.asInstanceOf[String];

  implicit def anyRef2Date(a: AnyRef):Date = a.asInstanceOf[Date];
  
  //prepared statement
  implicit def ps2Rich(ps: PreparedStatement):RichPreparedStatement = new RichPreparedStatement(ps);

  implicit def rich2PS(r: RichPreparedStatement):PreparedStatement = r.ps;


  class RichPreparedStatement(val ps: PreparedStatement) {
    var pos = 1;
    private def inc = {pos = pos + 1; this}

    private def setStatementParams(params:Any*) = {
      var count = 1
      for (param <- params) {
        ps.setObject(count, param)
        count += 1
      }
    }
    //TODO: refactor this once default params in 2.8 get released
    private def collect(rs: ResultSet, l: ListBuffer[Map[String, AnyRef]]): List[Map[String, AnyRef]] = {
      if (rs.next) {
        var map = Map[String, AnyRef]()
        for (i <- 1 to rs.getMetaData.getColumnCount) map(rs.getMetaData.getColumnName(i).toLowerCase) = rs.getObject(i)
        l+=map
        collect(rs, l)
      } else {l.toList}
    }
    //TODO: refactor this once default params in 2.8 get released
    private def collectFor[X](rs: ResultSet, l: ListBuffer[X]): List[X] = {
      if (rs.next) {                                                          
        var args = new ListBuffer[AnyRef]()
        for (i <- 1 to rs.getMetaData.getColumnCount) args += rs.getObject(i)
        //for (
        //TODO:determine apply method for type X
        //based on that we should be able to do
        //l+= method.invoke(X,args.toArray:_*)
        collectFor(rs, l)
      } else l.toList
    }
    def queryWith(params:Any*): List[Map[String, AnyRef]] = {
      setStatementParams(params:_*)
      query
    }

    def query: List[Map[String, AnyRef]] = collect(ps.executeQuery, new ListBuffer[Map[String, AnyRef]]())

    def queryFor[T]:List[T] = collectFor[T](ps.executeQuery, new ListBuffer[T]())

    def executeWith(params: Any*) {
      setStatementParams(params:_*)
      ps.execute
    }

    def execute = {pos = 1; ps.execute}

    def <<(b: Boolean) = {ps.setBoolean(pos, b); inc}

    def <<(x: Byte) = {ps.setByte(pos, x); inc}

    def <<(i: Int) = {ps.setInt(pos, i); inc}

    def <<(x: Long) = {ps.setLong(pos, x); inc}

    def <<(f: Float) = {ps.setFloat(pos, f); inc}

    def <<(d: Double) = {ps.setDouble(pos, d); inc}

    def <<(o: String) = {ps.setString(pos, o); inc}

    def <<(x: Date) = {ps.setDate(pos, x); inc}
  }


  //connection
  implicit def conn2Rich(conn: Connection):RichConnection = new RichConnection(conn);

  class RichConnection(val conn: Connection) {
    //statements
    //execute a statement
    def execute(sql: String): Unit = new RichStatement(conn.createStatement) execute sql;
    //execute statements
    def execute(sql: Seq[String]): Unit = new RichStatement(conn.createStatement) execute sql;
    //prepared statements
    def execute(sql: String,  params:Any*) = new RichPreparedStatement(conn.prepareStatement(sql)).executeWith(params:_*)
    //prepared query for a List of Map
    def query(sql: String,  params:Any*): List[Map[String, AnyRef]] = new RichPreparedStatement(conn.prepareStatement(sql)).queryWith(params:_*)
    //prepared query for a List of Data classes
    //def queryFor[T](sql: String,  params:Any*): List[T] = new RichPreparedStatement(conn.prepareStatement(sql)).queryFor[T](params:_*)

  }


  //statement
  implicit def query[T](s: String)(implicit stat: Statement) = {
    stat.executeQuery(s);
  }

  implicit def st2Rich(s: Statement):RichStatement = new RichStatement(s);

  implicit def rich2St(rs: RichStatement):Statement = rs.s;

  class RichStatement(val s: Statement) {
    def execute(sql: String): Unit = {s.execute(sql); this}
    def execute(sql: Seq[String]): Unit = {for (val x <- sql) s.execute(x); this}
  }


}