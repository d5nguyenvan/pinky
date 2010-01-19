package org.pinky.code.extension

import _root_.javax.servlet.ServletContext
import _root_.org.apache.velocity.app.VelocityEngine
import _root_.org.apache.velocity.VelocityContext
import _root_.scala.collection.jcl.{MapWrapper, HashMap, Map}
import com.google.inject.Inject
import java.io.{BufferedWriter, OutputStreamWriter, OutputStream}

/**
 * Provides Velocity rendering, which is actually the default html rendering
 *
 * @param ctx the ServletContext is needed for the webapp path
 * @author peter hausel gmail com (Peter Hausel)
 */
class HtmlRepresentationVelocity @Inject()(ctx: ServletContext) extends Representation {
  val engine = new VelocityEngine()
  engine.setProperty("runtime.log.logsystem.class", "org.apache.velocity.runtime.log.JdkLogChute")
  engine.setProperty("file.resource.loader.path", ctx.getRealPath("/") + "template")
  // Initialize the engine
  engine.init()

  /**
   * @param data data coming from the user
   * @param out outputstream used to print out the response
   */
  def write(data: Map[String, AnyRef], out: OutputStream) = {
    // Create the context
    try {
      val context = new VelocityContext(data.asInstanceOf[MapWrapper[String, AnyRef]].underlying);

      // Load the template
      val templateFile = if (data("template").asInstanceOf[String].endsWith(".vm"))
        data("template").asInstanceOf[String]
      else data("template").asInstanceOf[String] + ".vm"

      val template = engine.getTemplate(templateFile);
      val tmplWriter = new BufferedWriter(new OutputStreamWriter(out, template.getEncoding()));

      // Process the template
      template.merge(context, tmplWriter);
      tmplWriter.flush();
    } catch {case e: Exception => {e.printStackTrace; throw e}}

  }

}
