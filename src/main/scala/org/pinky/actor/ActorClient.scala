package org.pinky.core

import org.pinky.actor.Client


trait ActorClient extends Client {
  def fireStarter(reqData: Map[String, AnyRef]): Map[String, AnyRef] = {
    if (workers != null) for (actor <- workers) if (!actor.isRunning) thisRef.startLink(actor)
    super.fireStarter(callback(reqData))
    reqData
  }

  def callback(reqData: Map[String, AnyRef])

}




