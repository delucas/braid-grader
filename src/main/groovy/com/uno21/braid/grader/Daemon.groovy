package com.uno21.braid.grader

import groovy.json.JsonSlurper

import com.rabbitmq.client.Channel
import com.rabbitmq.client.Connection
import com.rabbitmq.client.ConnectionFactory
import com.rabbitmq.client.QueueingConsumer


class Daemon {

	static main(args) {

		String uri = System.getenv("CLOUDAMQP_URL");
		if (uri == null) uri = "amqp://guest:guest@localhost";
		
		println "** Conectándose a ${uri} **"

		String queueName = 'graderQueue'

		ConnectionFactory factory = new ConnectionFactory();
		factory.setUri(uri);

		while (true) {

			try {

				Connection connection = factory.newConnection();
				Channel channel = connection.createChannel();

				channel.queueDeclare(queueName, true, false, false, null);

				QueueingConsumer consumer = new QueueingConsumer(channel);
				channel.basicConsume(queueName, true, consumer);

				QueueingConsumer.Delivery delivery = consumer.nextDelivery();
				String message = new String(delivery.getBody())
				println "-> Recibido '${message}'"

				channel.close()
				connection.close()
				
				def json = new JsonSlurper().parseText(message)

				def grader = new JavaGrader()

				String grade = grader.grade(json)

				connection = factory.newConnection();
				channel = connection.createChannel();
				channel.queueDeclare("alreadyGradedQueue", true, false, false, null);
				channel.basicPublish("", "alreadyGradedQueue", null, grade.getBytes());
				println "<- Enviado '${grade}'"
				
				channel.close()
				connection.close()
				
			} catch (Exception e) {
				println e
				println '** Nueva conexión a la cola (por timeout) **'
			}
		}
	}
}
