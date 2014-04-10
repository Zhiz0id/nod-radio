package org.dandelion.radiot.server

class TopicFilter(val topicStarter: String, val consumer: (String) => Unit)
  extends ((String, String) => Unit) {
  val ChatNickname = ".*/(.*)$".r

  override def apply(sender: String, message: String) = sender match {
    case ChatNickname(`topicStarter`) => consumer(message)
    case _ =>
  }
}