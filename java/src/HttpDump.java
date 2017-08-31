import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

import nohorjo.delegation.Action;
import nohorjo.socket.SocketServer;

public class HttpDump {
	public static void main(String[] args) throws IOException {
		try {
			int port = Integer.parseInt(args[0]);
			try (SocketServer server = new SocketServer(port)) {
				Action onReceive = new Action() {

					@Override
					public Object run(Object... args) {
						System.out.print(new String(new byte[] { (Byte) args[0] }));
						return null;
					}
				};
				Action onNewConnection = new Action() {

					@Override
					public Object run(Object... args) {
						Socket s = (Socket) args[0];
						System.out.println("Connected to " + s.getRemoteSocketAddress());
						return null;
					}
				};
				Action onDisconnect = new Action() {

					@Override
					public Object run(Object... args) {
						Socket s = (Socket) args[0];
						System.out.println("Disconnected from " + s.getRemoteSocketAddress());
						return null;
					}
				};
				server.setActions(onReceive, onNewConnection, onDisconnect);
				server.start();
				while (true) {
					try (Scanner input = new Scanner(System.in)) {
						while (input.hasNext()) {
							String line = input.nextLine().trim();
							if (line.equals("")) {
								for (String conn : server.getConnections()) {
									server.disconnect(conn);
								}
							} else {
								if (line.equals("\\n"))
									line = "\n";
								line += "\n";
								server.sendAll(line.getBytes());
							}
						}
					}
				}
			}
		} catch (IndexOutOfBoundsException | NumberFormatException e) {
			System.out.println("Usage:\n\thttpdump <port>");
		}
	}
}
