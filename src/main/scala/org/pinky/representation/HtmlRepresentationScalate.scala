package org.pinky.representation

import java.io.{OutputStream, OutputStreamWriter}
import javax.servlet.ServletContext
import com.google.inject.Inject
import org.fusesource.scalate.TemplateEngine
import org.fusesource.scalate.support.TemplateFinder

/**
 * HTML representation which uses Scalate to do its heavy lifting.
 *
 * @param ctx ServletContext instance
 * @author max@bumnetworks.com
 */
class HtmlRepresentationScalate @Inject()(ctx: ServletContext) extends Representation {
  val roots: List[String] = ctx.getRealPath("template") :: Nil
  val defaultExtension: Option[String] = None

  protected def engine0 =
    Some(new TemplateEngine).map {
      e =>
        e.templateDirectories = roots
      e.allowReload = true
      e.allowCaching = false
      e
    }.get

  lazy val engine = engine0
  private lazy val finder = new TemplateFinder(engine)

  def write(data: Map[String, AnyRef], out: OutputStream): Unit =
    Some(new OutputStreamWriter(out)).map {
      writer =>
        val path = data.get("template").map(_.asInstanceOf[String]).map {
          p => if (p.contains(".")) p else "%s%s".format(p, defaultExtension.map(".%s".format(_)).getOrElse(""))
        }.getOrElse(throw new IllegalArgumentException("no 'template' key found"))
      writer.write(engine.layout(finder.findTemplate(path).getOrElse(throw new Exception("failed to find template %s".format(path))), data))
      writer.flush
    }
}
