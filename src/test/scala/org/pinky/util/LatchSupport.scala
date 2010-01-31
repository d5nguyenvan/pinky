package org.pinky.code.util

import java.util.concurrent.CountDownLatch
import se.scalablesolutions.akka.actor.Actor

/**
 * Created by IntelliJ IDEA.
 * User: phausel
 * Date: Jan 20, 2010
 * Time: 12:23:21 AM
 * To change this template use File | Settings | File Templates.
 */

trait LatchSupport {
  var latch:CountDownLatch =  _
    trait CountDown extends Actor {
      abstract override def receive = {
         latch.countDown
         super.receive
      }
    }
}
object LatchSupport extends LatchSupport