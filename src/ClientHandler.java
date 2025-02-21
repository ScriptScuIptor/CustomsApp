import java.io.*;
import java.net.Socket;

public class ClientHandler extends Thread {
    private Socket clientSocket;

    public ClientHandler(Socket socket) {
        this.clientSocket = socket;
    }

    @Override
    public void run() {
        try (
                InputStream input = clientSocket.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(input));
                OutputStream output = clientSocket.getOutputStream();
                PrintWriter writer = new PrintWriter(output, true)
        ) {
            writer.println("✅ Подключение успешно! Введите команду:");

            String command;
            while ((command = reader.readLine()) != null) {
                System.out.println("📥 Получена команда: " + command);

                String[] parts = command.split(" ");
                if (parts.length >= 3 && "ADD_DRIVER".equals(parts[0])) {
                    String name = parts[1] + " " + parts[2];
                    String licenseNumber = parts[3];

                    DatabaseManager.addDriver(name, licenseNumber);
                    writer.println("✅ Водитель " + name + " добавлен с номером " + licenseNumber);
                } else {
                    writer.println("❌ Неизвестная команда. Используйте: ADD_DRIVER [Фамилия Имя] [Номер_лицензии]");
                }
            }

            clientSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
