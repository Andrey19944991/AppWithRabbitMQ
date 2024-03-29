import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.MessageProperties;

import java.util.Scanner;

public class NewTask {

    private static final String TASK_QUEUE_NAME = "task_queue";

    public static void main(String[] argv) throws Exception {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        try (Connection connection = factory.newConnection();
             Channel channel = connection.createChannel()) {
            channel.queueDeclare(TASK_QUEUE_NAME, true, false, false, null);//объявление очереди

            Scanner scanner=new Scanner(System.in);

                System.out.print("Your message: ");
                String message = scanner.nextLine();
                //String message = String.join(" ", argv);

            channel.basicPublish ( "" , TASK_QUEUE_NAME , MessageProperties.PERSISTENT_TEXT_PLAIN ,
                    message.getBytes ("UTF-8"));
            System.out.println(" [x] Sent '" + message + "'");
        }
    }

}
