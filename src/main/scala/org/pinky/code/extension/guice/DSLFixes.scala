package org.pinky.code.extension.guice

;



import com.google.inject.Key
import com.google.inject.servlet._


import javax.servlet.http.{HttpServletResponse, HttpServletRequest, HttpServlet}

/**
 *  Holds implicit definitions to hide the calls to `with`.
 *  Import this object into the scope in which you need to do
 *  <code>serve("*.rss").with(classOf[ExampleRssServlet])</code>
 *  and do
 *  <code>serve("*.rss").by(classOf[ExampleRssServlet])</code> or
 *  <code>serve("*.rss").withClass(classOf[ExampleRssServlet])</code>
 *  instead.
 *
 *   This are the methods we will hide
 *   <code>
 *   public static interface ServletKeyBindingBuilder   {
 *       void with(Class<? extends HttpServlet> servletKey);
 *       void with(Key<? extends HttpServlet> servletKey);
 *       void with(Class<? extends HttpServlet> servletKey, Map<String, String> contextParams);
 *       void with(Key<? extends HttpServlet> servletKey, Map<String, String> contextParams);
 * }
 *   </code>
 *
 * @author fede silva gmail com   { fedesilva }
 *
 */
object DSLFixes {

  /**
   * The rich type.
   * @author fede silva gmail com   { fedesilva }
   */
  class RichBuilder(builder: ServletModule.ServletKeyBindingBuilder) {
    def by[T <: HttpServlet](servletKey: Class[T]) {builder.`with`(servletKey)};

    def by[T <: HttpServlet](
            servletKey: Class[T], params: java.util.Map[String, String]) {builder.`with`(servletKey, params)};


    def by[T <: HttpServlet](servletKey: Key[T]) {builder.`with`(servletKey)};

    def by[T <: HttpServlet, S](
            servletKey: Key[T],
            params: java.util.Map[String, String]) {builder.`with`(servletKey, params)};


    def withClass[T <: HttpServlet](servletKey: Class[T]) {
      builder.`with`(servletKey)
    };

    def withClass[T <: HttpServlet](
            servletKey: Class[T],
            params: java.util.Map[String, String]) {builder.`with`(servletKey, params)};

    def withClass[T <: HttpServlet](servletKey: Key[T]) {
      builder.`with`(servletKey)
    };

    def withClass[T <: HttpServlet](
            servletKey: Key[T],
            params: java.util.Map[String, String]) {builder.`with`(servletKey, params)};

  }

  /**
   * The implicit type conversion.
   * @author fede silva gmail com   { fedesilva }
   */
  implicit def builderToRichBuilder(
          builder: ServletModule.ServletKeyBindingBuilder) = new RichBuilder(builder);


}

