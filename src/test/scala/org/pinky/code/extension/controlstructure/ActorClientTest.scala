package org.pinky.code.extension.controlstructure

import  scala.collection.jcl._
import org.scalatest.Spec
import org.scalatest.matchers.ShouldMatchers
import org.pinky.code.example.servlets._
import org.pinky.code.extension.controlstructure.ActorDispatch

/**
 * Created by IntelliJ IDEA.
 * User: phausel
 * Date: Jan 18, 2010
 * Time: 7:07:17 PM
 * To change this template use File | Settings | File Templates.
 */

class ActorClientTest extends Spec with ShouldMatchers with ActorDispatch {
    //right now this is more like a functional test but i still do not know how to look into
    //actors internals
    describe("an ActorClient") {
    it("should run actors sucessfully") {
        val data:Map[String,Object] = new HashMap
        data += "name"->"Jonas"
        launch[PingPongClient] using (data,PingActor,PongActor)        
    }
    }
}