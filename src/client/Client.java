package client;

import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;

public class Client extends Thread {

  private final String id;
  private final String transactionFile;

  public Client(String id, String transactionFile) {
    this.id = id;
    this.transactionFile = transactionFile;
  }

  public void readAndSend(PrintStream os) throws IOException, InterruptedException {
    BufferedReader br = new BufferedReader(new FileReader(transactionFile));
    String line = br.readLine();
    while(line != null) {
      os.println(id + " " + line);
      line = br.readLine();
//      Thread.sleep((long) (Math.random() * 100));
    }
  }

  @Override
  public void run() {
    Socket clientSocket = null;
    DataInputStream is = null;
    PrintStream os = null;

    try {
      clientSocket = new Socket("localhost", 2222);
      os = new PrintStream(clientSocket.getOutputStream());
      is = new DataInputStream(clientSocket.getInputStream());
    } catch (UnknownHostException e) {
      System.err.println("Don't know about host");
    } catch (IOException e) {
      System.err.println("Couldn't get I/O for the connection to host");
    }

    if (clientSocket != null && os != null && is != null) {
      try {
        // read transaction file and send to server
        readAndSend(os);
        // wait for response
        String responseLine;
        System.out.println("Response:");
        while ((responseLine = is.readLine()) != null) {
          System.out.println(responseLine);
          if (responseLine.indexOf("Ok") != -1) {
            break;
          }
        }
        os.close();
        is.close();
        clientSocket.close();
      } catch (UnknownHostException e) {
        System.err.println("Trying to connect to unknown host: " + e);
      } catch (IOException e) {
        System.err.println("IOException:  " + e);
      } catch (InterruptedException e) {
        System.err.println("InterruptedException: " + e);
      }
    }
  }
}