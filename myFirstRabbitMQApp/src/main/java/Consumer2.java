import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;


public class Consumer2 {

    private static final String EXCHANGE_NAME = "exchange"; //название обмена

    public static void main(String[] argv) throws Exception {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        channel.exchangeDeclare(EXCHANGE_NAME,"fanout");
        String queueName=channel.queueDeclare().getQueue();
        channel.queueBind(queueName, EXCHANGE_NAME, ""); //связывание очереди и обмена
        System.out.println(" [*] Waiting for messages");

        channel.basicQos(1); //принимаем только одно сообщение, новое поступит, когда обработает предыдущее

        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            String message = new String(delivery.getBody(), "UTF-8");

            System.out.println(" [x] Received '" + message + "'");
            try {
                doWork(message);
            } finally {
                System.out.println(" [x] Done");
                System.out.println(queueName);
                channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);//чтобы сообщения (или подтверждения
                // не совсем понял) повторно не доставлялись
            }
        };
        channel.basicConsume(queueName, false, deliverCallback, consumerTag -> { });//второй аргумент-
        //это autoAck=false, это для отправки подтверждения на рабит, что сообщение обработано и его можно удалять
    }
    //Для иммитации работы, если в сообшении есть '.', то будет пауза
    private static void doWork(String task) {
        for (char ch : task.toCharArray()) {
            if (ch == '.') {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException _ignored) {
                    Thread.currentThread().interrupt();
                }
            }
        }
    }
}


