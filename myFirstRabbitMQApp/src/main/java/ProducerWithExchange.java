import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.MessageProperties;

import java.util.Scanner;

public class ProducerWithExchange {
    //Будет генерироваться очерить сама, поэтому комментим строчку ниже
    //private static final String TASK_QUEUE_NAME = "task_queue";
    private static final String EXCHANGE_NAME = "exchange";//название обмена

    public static void main(String[] argv) throws Exception {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        try (Connection connection = factory.newConnection();
             Channel channel = connection.createChannel()) {
            channel.exchangeDeclare(EXCHANGE_NAME,"fanout");//тип обмена - fanout (разветвление)
            //также бывают типы: direct, topic, headers

            Scanner scanner=new Scanner(System.in);

            System.out.print("Your message: ");
            String message = scanner.nextLine();
            //String message = String.join(" ", argv);

            channel.basicPublish ( EXCHANGE_NAME,"" , null, message.getBytes ("UTF-8"));
            System.out.println("  Sent '" + message + "'");
        }
    }

}
