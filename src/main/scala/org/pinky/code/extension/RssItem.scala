package org.pinky.code.extension
/**
 * Provides case classes which serve as data containers for RSS
 *
 * @author peter hausel gmail com (Peter Hausel)
 */
case class RssItem(itemtitle:String, itemlink:String,itemdescription:String,
                  itempubdate:String, itemguid:String)    {}
case class RssHeader(title:String, link:String,description:String, pubdate:String,
                    lastdate:String, copyright:String, language:String)    {}
