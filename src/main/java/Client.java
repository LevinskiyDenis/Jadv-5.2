import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class Client {

    public static void main(String[] args) {
        try {
            Client.startClient();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void startClient() throws IOException {

        final SocketChannel csc = SocketChannel.open();
        csc.connect(new InetSocketAddress("localhost", 23344));

        try (Scanner scanner = new Scanner(System.in)) {

            final ByteBuffer buffer = ByteBuffer.allocate(2048);
            String msg;

            while (true) {
                System.out.println("Введите сообщение");
                msg = scanner.nextLine();
                if ("end".equalsIgnoreCase(msg)) break;

                csc.write(ByteBuffer.wrap(msg.getBytes(StandardCharsets.UTF_8)));

                Thread.sleep(2000);

                int bytesCount = csc.read(buffer);
                String response = new String(buffer.array(), 0, bytesCount, StandardCharsets.UTF_8);
                System.out.println(response);
                buffer.clear();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            csc.close();
        }
    }
}

