import sbt._

/**
 * for akka:
 *  val akka_netty = "akka netty" at "https://repository.jboss.org/nexus/content/repositories/releases"
 *  val akka_repo = "akka maven repository" at "http://scalablesolutions.se/akka/repository"
 *  val akka_databinder = "DataBinder" at "http://databinder.net/repo"
 *  val akka_configgy = "Configgy" at "http://www.lag.net/repo"
 *  val akka_multiverse = "Multiverse" at "http://multiverse.googlecode.com/svn/maven-repository/releases"
 *  val akka_jBoss = "jBoss" at "http://repository.jboss.org/maven2"
 * for comet:
 *  val jetty_repo = "jetty repository" at "http://oss.sonatype.org/content/groups/jetty"
 *  val jettyComet = "org.eclipse.jetty" % "jetty-continuation" % "7.0.2-SNAPSHOT" % "compile"
 * for freemarker:
 *  val freemarker = "org.freemarker" % "freemarker" % "2.3.15" % "compile"
 * for json lib:
 *  val json = "org.json" % "json" % "20080701" % "compile"
 */
//class PinkyProject(info: ProjectInfo) extends DefaultWebProject(info) {
class PinkyProject(info: ProjectInfo) extends DefaultProject(info) {
    override def compileOptions = super.compileOptions ++ Seq(Unchecked)

    override def ivyXML =
    <dependencies>
        <exclude module="guice-all"/>
    </dependencies>

    //---------
    //REPOSITORIES
    //---------
    //core
    val guiceMaven = "guice maven repository" at "http://guice-maven.googlecode.com/svn/trunk"
    //akka repositories
    val akka_netty = "akka netty" at "https://repository.jboss.org/nexus/content/repositories/releases"
    val akka_repo = "akka maven repository" at "http://scalablesolutions.se/akka/repository"
    val akka_databinder = "DataBinder" at "http://databinder.net/repo"
    val akka_configgy = "Configgy" at "http://www.lag.net/repo"
    val akka_multiverse = "Multiverse" at "http://multiverse.googlecode.com/svn/maven-repository/releases"
    val akka_jBoss = "jBoss" at "http://repository.jboss.org/maven2"
    //commet
    val jetty_repo = "jetty repository" at "http://oss.sonatype.org/content/groups/jetty"

    //---------
    //LIBRARIES
    //---------
    //comet
    val jettyComet = "org.eclipse.jetty" % "jetty-continuation" % "7.0.2-SNAPSHOT" % "compile"
    //akka
    val akka = "se.scalablesolutions.akka" % "akka-core_2.8.0.RC3" % "0.9.1" % "compile"

    //guice
    val guiceServlet = "com.google.code.guice" % "guice-servlet" % "3.0-r1291" % "compile" 
    val scalaGuice = "scala-guice" % "scala-guice" % "0.1" from "http://guice-maven.googlecode.com/svn/trunk/scala-guice/scala-guice_2.8.0-0.1.jar"
    val servlet = "javax.servlet" % "servlet-api" % "2.5" % "provided"

    //representations
    val oval = "net.sf.oval" % "oval" % "1.32" % "compile"
    val log4j = "log4j" % "log4j" % "1.2.14" % "compile"
    val freemarker = "org.freemarker" % "freemarker" % "2.3.15" % "compile"
    val velocity = "org.apache.velocity" % "velocity" % "1.6.1" % "compile"
    val xstream = "com.thoughtworks.xstream" % "xstream" % "1.3.1" % "compile"
    val json = "org.json" % "json" % "20080701" % "compile"
    val jettison = "org.codehaus.jettison" % "jettison" % "1.1" % "compile"
  
    //testing
    val mockito = "org.mockito" % "mockito-core" % "1.6" % "test->default"
    val h2database = "com.h2database" % "h2" % "1.0.20070617" % "test->default"
    val scalatest = "org.scalatest" % "scalatest" % "1.2" % "test->default"
  
    override def packageDocsJar = defaultJarPath("-javadoc.jar")
    override def packageSrcJar= defaultJarPath("-sources.jar")

}
