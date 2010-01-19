package org.pinky.code.extension.controlstructure

import _root_.javax.servlet.http.{HttpServletResponse, HttpServletRequest}
import scala.collection.jcl._
import reflect.Manifest
import se.scalablesolutions.akka.actor.{ScheduleActor, SchedulerException, Actor}
import java.util.concurrent.{TimeUnit, ScheduledFuture, ScheduledThreadPoolExecutor, ThreadPoolExecutor}
import java.util.regex.{Matcher, Pattern}
import org.pinky.code.annotation.Every

/**
 * Trait which defines the control structure layout
 *
 * @author peter hausel gmail com (Peter Hausel)
 */


trait Dispatch {
  def call(request: HttpServletRequest, response: HttpServletResponse)(block: => Map[String, AnyRef])
}
trait ActorDispatch {
  def launch[T <: ActorClient](implicit m: Manifest[T]) = new ActorHolder(m)
}



class ActorHolder[T](m: Manifest[T]) {
  def using(data: Map[String, AnyRef], actors: Actor*): Map[String, AnyRef] = {
    //start actors
    for (actor <- actors) if (!actor.isRunning) actor.start
    //start client
    createInstanceOfActorClientAndLaunchItWith(data, actors: _*)
    data
  }

  private def createInstanceOfActorClientAndLaunchItWith(data: Map[String, AnyRef], actors: Actor*) = {
    try {
      val constructor = m.erasure.getConstructors()(0)
      constructor.newInstance(data, actors.toArray).asInstanceOf[ActorClient]
    } catch {
      case e: Exception => {
        println("error creating Actor client. params:" + data, actors.toArray.toString + " type:" + m.erasure.toString+ "\n\n")
        e.printStackTrace
        throw new Exception
      }
    }
  }

}


/**
 * scheduler is modelled after <a href="http://playframework.org">Play!</a>
 */
abstract class ActorClient(reqData: Map[String, AnyRef], actors: Actor*) extends Actor {
  val executor = new ScheduledThreadPoolExecutor(10, new ThreadPoolExecutor.AbortPolicy())
  val delay:Every = (this.getClass().getAnnotation(classOf[Every]))

  def fireStarter(block: => Unit): Unit = {
    if (delay != null) {
      executor.scheduleWithFixedDelay(new java.lang.Runnable {def run = block}, parseDuration(delay.value()), parseDuration(delay.value()), TimeUnit.SECONDS)
    } else {
      block
    }
  }

  override def shutdown = {
    actors.foreach(unlink(_))
    executor.shutdown
  }

  def receive = {
    case _ => {} // ignore all messages
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


