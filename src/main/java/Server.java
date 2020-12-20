import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;

public class Server {

    public static void main(String[] args) {
        try {
            Server.startServer();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void startServer() throws IOException {

        final ServerSocketChannel ssc = ServerSocketChannel.open();
        ssc.bind(new InetSocketAddress("localhost", 23344));

        while (true) {

            System.out.println("Сервер ждет подключения клиента");

            try (SocketChannel socketChannel = ssc.accept()) {

                final ByteBuffer buffer = ByteBuffer.allocate(2048);

                while (socketChannel.isConnected()) {
                    int bytesCount = socketChannel.read(buffer);

                    if (bytesCount == -1) break;

                    final String msg = new String(buffer.array(), 0, bytesCount, StandardCharsets.UTF_8);

                    buffer.clear();
                    System.out.println("Сообщение от клиента: " + msg);
                    String response = msg.trim();
                    response = response.replaceAll("\\s{2,}", " ");
                    socketChannel.write(buffer.wrap(("Ответ сервера: " + response).getBytes(StandardCharsets.UTF_8)));
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
