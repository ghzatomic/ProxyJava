import java.net.*;
import java.io.*;

class Proxy {
	public static void main(String args[]) throws IOException {

		int localport = 3128;
		int remoteport = 3128;
		String remotehost = "192.168.1.1";
		boolean error = false;

		Socket incoming, outgoing = null;
		ServerSocket Server = null;

		if (localport <= 0) {
			System.err.println("Error: Invalid Local Port Specification "
					+ "\n");
			error = true;
		}
		if (remoteport <= 0) {
			System.err.println("Error: Invalid Remote Port Specification "
					+ "\n");
			error = true;
		}
		if (remotehost == null) {
			System.err.println("Error: Invalid Remote Host Specification "
					+ "\n");
			error = true;
		}

		// If any errors so far, exit program

		if (error)
			System.exit(-1);

		// Test and create a listening socket at proxy

		try {
			Server = new ServerSocket(localport);
		} catch (IOException e) {
			e.printStackTrace();
		}

		// Loop to listen for incoming connection, and accept if there is one

		while (true) {
			try {
				incoming = Server.accept();
				// Create the 2 threads for the incoming and outgoing traffic of
				// proxy server
				outgoing = new Socket(remotehost, remoteport);

				ProxyThread thread1 = new ProxyThread(incoming, outgoing);
				thread1.start();

				ProxyThread thread2 = new ProxyThread(outgoing, incoming);
				thread2.start();
			} catch (UnknownHostException e) {
				// Test and make connection to remote host
				System.err.println("Error: Unknown Host " + remotehost);
				System.exit(-1);
			} catch (IOException e) {
				System.exit(-2);// continue;
			}

			/*
			 * catch (IOException e) {
			 * System.err.println("Error: Couldn't Initiate I/O connection for "
			 * + remotehost); System.exit(-1); }
			 */
		}
	}

}
