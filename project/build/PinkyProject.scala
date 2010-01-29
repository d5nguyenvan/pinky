import sbt._

class PinkyProject(info: ProjectInfo) extends DefaultProject(info) {
    val guiceMaven = "guice maven repository" at "http://guice-maven.googlecode.com/svn/trunk"
    
    val akka_databinder = "DataBinder" at "http://databinder.net/repo"
    val akka_configgy = "Configgy" at "http://www.lag.net/repo"
    val akka_multiverse = "Multiverse" at "http://multiverse.googlecode.com/svn/maven-repository/releases"
    val akka_jBoss = "jBoss" at "http://repository.jboss.org/maven2"
    val jetty_repo = "jetty repository" at "http://oss.sonatype.org/content/groups/jetty"

    val akka = "se.scalablesolutions.akka" % "akka-core" % "0.6" % "compile"
    val jettyComet = "org.eclipse.jetty" % "jetty-continuation" % "7.0.2-SNAPSHOT" % "compile" 
    val io = "commons-io" % "commons-io" % "1.5-SNAPSHOT" % "compile"
    
    val guice = "com.google.code.guice" % "guice" % "2.0.1" % "compile" 
    val guiceServlet = "com.google.code.guice" % "guice-servlet" % "2.0.1" % "compile" 
    val aopalliance = "aopalliance" % "aopalliance" % "1.0" % "compile"
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
    val servlet = "javax.servlet" % "servlet-api" % "2.5" % "provided" 
    val scalatest = "org.scalatest" % "scalatest" % "1.0" % "test->default" 
    override def packageDocsJar = defaultJarPath("-javadoc.jar")
    override def packageSrcJar= defaultJarPath("-sources.jar")

}
