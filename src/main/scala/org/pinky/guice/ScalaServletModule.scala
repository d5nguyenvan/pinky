package org.pinky.guice

import com.google.inject.servlet.ServletModule
import scala.reflect.Manifest
import javax.servlet.http.HttpServlet
import javax.servlet.Filter
import com.google.inject.Key
trait ScalaServletModule {
  self: ServletModule => 

  def serveWith[T <: HttpServlet : Manifest](url: String) { 
    serve(url).by(new Key[T])
  }
  def serveRegexWith[T <: HttpServlet : Manifest](url: String) { 
    serveRegex(url).by(new Key[T])
  }
  def filterThrough[T <: Filter : Manifest](url: String) {
    filter(url).through(new Key[T])
  } 
  def filterRegexThrough[T <: Filter : Manifest](url: String) {
    filterRegex(url).through(new Key[T])
  }
  
}
// vim: set ts=4 sw=4 et:
