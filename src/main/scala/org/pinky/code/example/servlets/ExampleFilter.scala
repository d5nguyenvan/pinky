package org.pinky.code.example.servlets

import _root_.javax.servlet._
import com.google.inject._
/**
 * Filter example which demonstrates a guice managed filter
 *
 * @author peter hausel gmail com (Peter Hausel)
 */


@Singleton
class ExampleFilter extends Filter {
  def doFilter(request: ServletRequest,
              response: ServletResponse,
              chain: FilterChain) {
    print("Within Simple Filter ... ");
    println("Filtering the Request ...");

    chain.doFilter(request, response);

    print("Within Simple Filter ... ");
    println("Filtering the Response ...");
  }

  def destroy() {}
  def init(config:FilterConfig) {}
}