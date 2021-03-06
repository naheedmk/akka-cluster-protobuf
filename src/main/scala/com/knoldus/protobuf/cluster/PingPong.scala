package com.knoldus.protobuf.cluster

import akka.actor.{Actor, ActorLogging, ActorRef, ActorSystem, Props}
import com.knoldus.protobuf.models.example.Success
import com.typesafe.config.ConfigFactory

class PingPong extends Actor with ActorLogging {

    override def receive : Receive = {
        case GameMessage(msg, ref) =>
            log.info(s"\n ========================= GameMessage(${ref.path}) =======================")
            ref ! Success("Wo hoo, I got the success")
        case msg =>
            log.info("\n ------------------------- {} ---------------------------", msg)
    }
}

object PingPong{

    def main(args : Array[String]) : Unit = {
        val port = if (args.isEmpty) "0" else args(0)

        val config = ConfigFactory.parseString(s"akka.remote.netty.tcp.port=$port")
            .withFallback(ConfigFactory.parseString("akka.cluster.roles = [pingpong]"))
            .withFallback(ConfigFactory.load("application.conf"))

        val system = ActorSystem("GameZone", config)
        system.actorOf(Props(classOf[PingPong]), name = "PingPong")
    }

}

case class GameMessage(msg: String, ref: ActorRef)
