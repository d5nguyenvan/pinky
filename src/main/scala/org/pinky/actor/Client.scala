package org.pinky.actor

import java.util.regex.Pattern
import java.util.concurrent.{TimeUnit, ThreadPoolExecutor, ScheduledThreadPoolExecutor}
import se.scalablesolutions.akka.actor.Actor._
import org.pinky.annotation.Every
import se.scalablesolutions.akka.actor.{ActorRef, Actor}

case object UnSchedule
case object Resume
case object Stop

trait Client extends Actor {
  var workers: Seq[ActorRef] = _
  val thisRef = actorOf(this)

  val executor = new ScheduledThreadPoolExecutor(10, new ThreadPoolExecutor.AbortPolicy())
  val delay: Every = this.getClass().getAnnotation(classOf[Every])

  def fireStarter(block: => Unit): Unit = {
    if (!thisRef.isRunning)  thisRef.startLink(thisRef)
    if (delay != null) {
      executor.scheduleWithFixedDelay(new java.lang.Runnable {def run = block}, parseDuration(delay.value()), parseDuration(delay.value()), TimeUnit.SECONDS)
    } else {
      block
    }
  }


  override def shutdown = {
    if (workers != null) for (worker <- workers if worker.isRunning) thisRef.unlink(worker)
    if (thisRef.isRunning) thisRef.unlink(thisRef)
    executor.shutdown
  }

  override def receive = {
    case UnSchedule => shutdown
    case Stop => thisRef.stop
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