package org.pinky.util

class AsClassHolder[T <: AnyRef](x : T) {
  def asClass : Class[T] = x.getClass.asInstanceOf[Class[T]]
}

object AsClass {
  implicit def toAsClass[T <: AnyRef](x : T) = new AsClassHolder(x)
}
// vim: set ts=4 sw=4 et:
