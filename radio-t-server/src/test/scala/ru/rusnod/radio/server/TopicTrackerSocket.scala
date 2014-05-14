package org.rusnod.radio.server

import org.eclipse.jetty.websocket.api.annotations.{OnWebSocketMessage, OnWebSocketConnect, WebSocket}
import org.eclipse.jetty.websocket.api.Session

import org.json4s._
import org.json4s.jackson.JsonMethods._

@WebSocket
class TopicTrackerSocket {
  implicit val formats = DefaultFormats

  var topic: Topic = Topic("")
  private var session: Session = null

  @OnWebSocketConnect
  def onConnect(session: Session) {
    this.session = session
    session.getRemote.sendString("get")
  }

  def fromJson(json: String) = parse(json).extract[Topic]

  @OnWebSocketMessage
  def onMessage(msg: String) {
    topic = fromJson(msg)
  }

  def isConnected = (session != null) && session.isOpen
}
