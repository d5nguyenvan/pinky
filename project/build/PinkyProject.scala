import sbt._

class PinkyProject(info: ProjectInfo) extends DefaultWebProject(info) {
    val guiceyfruit = "GuiceyFruit" at "http://guiceyfruit.googlecode.com/svn/repo/releases/"
    val databinder = "DataBinder" at "http://databinder.net/repo"
    val configgy = "Configgy" at "http://www.lag.net/repo"
    val multiverse = "Multiverse" at "http://multiverse.googlecode.com/svn/maven-repository/releases"
    val jBoss = "jBoss" at "http://repository.jboss.org/maven2"
    val akka_repo = "Akka Maven Repository" at "http://scalablesolutions.se/akka/repository"
    val guiceMaven = "guice maven repository" at "http://guice-maven.googlecode.com/svn/trunk"
    val atmosphere = "atmosphere repo" at "http://download.java.net/maven/2"
    
    val atmoshpere = "org.atmosphere" % "atmosphere-jersey" % "0.5" % "compile" 
    val akka = "se.scalablesolutions.akka" % "akka-core" % "0.6" % "compile"
    //val jettyComet = "org.mortbay.jetty" % "jetty-util" % "6.1.22" % "compile" 

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
    val mockito = "org.mockito" % "mockito-core" % "1.6" % "test->default"
    val h2database = "com.h2database" % "h2" % "1.0.20070617" % "test->default"
    val servlet = "javax.servlet" % "servlet-api" % "2.5" % "provided" 
    val scalatest = "org.scalatest" % "scalatest" % "1.0" % "test->default" 
    override def packageDocsJar = defaultJarPath("-javadoc.jar")
    override def packageSrcJar= defaultJarPath("-sources.jar")

}
