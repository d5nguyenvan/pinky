package org.pinky.code.extension

import _root_.javax.servlet.ServletContext
import _root_.scala.collection.jcl.{MapWrapper, HashMap, Map}
import com.google.inject.Inject
import freemarker.template.{DefaultObjectWrapper, Configuration}
import java.io.{File, BufferedWriter, OutputStreamWriter, OutputStream}

/**
 * Provides FreeMarker rendering
 *
 * @param ctx the ServletContext is needed for the webapp path
 * @author peter hausel gmail com (Peter Hausel)
 */
class HtmlRepresentationFreeMarker @Inject()(ctx: ServletContext) extends Representation {
  val cfg = new Configuration

  cfg.setObjectWrapper(new DefaultObjectWrapper())
  if (ctx != null)
    cfg.setServletContextForTemplateLoading(ctx, "template")
  else {
    cfg.setDirectoryForTemplateLoading(new File(new File(".").getCanonicalPath() + "/src/main/webapp/template"))
  }

  /**
   * @param data data coming from the user
   * @param out outputstream used to print out the response
   */
  def write(data: Map[String, AnyRef], out: OutputStream) = {
    try {
      val templateFile = if (data("template").asInstanceOf[String].endsWith("ftl"))
        data("template").asInstanceOf[String]
      else
        data("template").asInstanceOf[String] + ".ftl"
      val template = cfg.getTemplate(templateFile)
      val tmplWriter = new BufferedWriter(new OutputStreamWriter(out));
      template.process(data.asInstanceOf[MapWrapper[String, AnyRef]].underlying, tmplWriter)
      // Process the template
      tmplWriter.flush();
    } catch {case e: Exception => {e.printStackTrace; throw e}}

  }

}
