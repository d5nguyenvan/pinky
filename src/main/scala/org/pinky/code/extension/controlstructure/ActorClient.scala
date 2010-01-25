package org.pinky.code.extension.controlstructure

import scala.collection.jcl.Map
import org.pinky.code.annotation.Every
import java.util.regex.Pattern
import java.util.concurrent.{TimeUnit, ThreadPoolExecutor, ScheduledThreadPoolExecutor}
import javax.servlet.http.{HttpServletResponse, HttpServlet, HttpServletRequest}
import org.atmosphere.cpr.Meteor
import se.scalablesolutions.akka.actor.{ActorRegistry, Actor}

case object UnSchedule
case object Stop

trait Client extends Actor {
  var workers: Seq[Actor] = _

  val executor = new ScheduledThreadPoolExecutor(10, new ThreadPoolExecutor.AbortPolicy())
  val delay: Every = this.getClass().getAnnotation(classOf[Every])

  def fireStarter(block: => Unit): Unit = {
    if (!this.isRunning) startLink(this)
    if (delay != null) {
      executor.scheduleWithFixedDelay(new java.lang.Runnable {def run = block}, parseDuration(delay.value()), parseDuration(delay.value()), TimeUnit.SECONDS)
    } else {
      block
    }
  }


  override def shutdown = {
    if (workers != null) for (worker <- workers if worker.isRunning) unlink(worker)
    if (this.isRunning) unlink(this)
    executor.shutdown
  }

  override def receive = {
    case UnSchedule => shutdown
    case Stop => stop
    case _ => println("could not understand this message")
  }
  

  /**
   ** scheduler is modelled after <a href="http://playframework.org">Play!</a>
   **/
  def parseDuration(duration: String): Int = {
    import SchedulerPatterns._

    if (duration == null) {
      60 * 60 * 24 * 365
    }
    var toAdd = -1
    if (days.matcher(duration).matches()) {
      val matcher = days.matcher(duration)
      matcher.matches()
      toAdd = Integer.parseInt(matcher.group(1)) * (60 * 60) * 24
    } else if (hours.matcher(duration).matches()) {
      val matcher = hours.matcher(duration)
      matcher.matches()
      toAdd = Integer.parseInt(matcher.group(1)) * (60 * 60)
    } else if (minutes.matcher(duration).matches()) {
      val matcher = minutes.matcher(duration)
      matcher.matches()
      toAdd = Integer.parseInt(matcher.group(1)) * (60)
    } else if (seconds.matcher(duration).matches()) {
      val matcher = seconds.matcher(duration)
      matcher.matches()
      toAdd = Integer.parseInt(matcher.group(1))
    }
    if (toAdd == -1) {
      throw new IllegalArgumentException("Invalid duration pattern : " + duration)
    }
    toAdd
  }
  private object SchedulerPatterns {
    val days = Pattern.compile("^([0-9]+)d$")
    val hours = Pattern.compile("^([0-9]+)h$")
    val minutes = Pattern.compile("^([0-9]+)mn$")
    val seconds = Pattern.compile("^([0-9]+)s$")
  }
}

trait ActorClient extends Client {

  def fireStarter(reqData: Map[String, AnyRef]): Map[String, AnyRef] = {
    if (workers != null) for (actor <- workers) if (!actor.isRunning) startLink(actor)
    super.fireStarter(callback(reqData))
    reqData
  }
  def callback(reqData: Map[String, AnyRef])

}


case object Resume
case class MeteorHolder(meteor: Meteor)
/*
class Comet(meteor: Meteor, request: HttpServletRequest, response: HttpServletResponse) extends ActorCometClient (meteor, request, response) {
 def receive = handler {
    case "test" =>
 }
   def callback = {
        this !! "test"
   }
}

 */
abstract class ActorCometClient(meteor: Meteor, request: HttpServletRequest, response: HttpServletResponse,actors: Actor*) extends Client {
  def this(meteor: Meteor, request: HttpServletRequest, response: HttpServletResponse) = this (meteor, request, response,null)
  workers = actors
  if (workers != null) for (actor <- workers if !actor.isRunning) startLink(actor)

  def callback
  
  def handler(messageHandler: PartialFunction[Any, Unit]): PartialFunction[Any, Unit] = {
    val resumeHandler: PartialFunction[Any, Unit] = {
      case Resume => {
        shutdown
        if (workers != null) workers.foreach(unlink(_))
        meteor.resume()
      }
    }
    messageHandler orElse super.receive orElse resumeHandler
  }


}


