import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class SimpleClient {
    public static void main(String[] args) {
        String serverAddress = "localhost";
        int port = 12345;

        try (Socket socket = new Socket(serverAddress, port);
             InputStream input = socket.getInputStream();
             BufferedReader reader = new BufferedReader(new InputStreamReader(input));
             OutputStream output = socket.getOutputStream();
             PrintWriter writer = new PrintWriter(output, true);
             Scanner scanner = new Scanner(System.in)) {

            System.out.println(reader.readLine());
            String username = scanner.nextLine();
            writer.println(username);

            System.out.println(reader.readLine());
            String password = scanner.nextLine();
            writer.println(password);

            System.out.println(reader.readLine());
            System.out.println(reader.readLine());

            while (true) {
                System.out.print("Введите команду (например, ADD_DRIVER Иван Иванов AB1234567): ");
                String command = scanner.nextLine();
                writer.println(command);

                String response = reader.readLine();
                System.out.println("Ответ сервера: " + response);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}