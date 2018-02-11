package server;

import server.model.Operation;
import server.model.OperationType;
import server.model.Sleep;
import server.model.Transaction;

import java.io.*;
import java.net.Socket;

public class ClientHandler extends Thread {
  private Socket socket;
  private TransactionManager transactionManager;

  public ClientHandler(Socket clientSocket, TransactionManager transactionManager) {
    this.socket = clientSocket;
    this.transactionManager = transactionManager;
  }

  public void run() {
    InputStream inp = null;
    BufferedReader brinp = null;
    DataOutputStream out = null;
    try {
      inp = socket.getInputStream();
      brinp = new BufferedReader(new InputStreamReader(inp));
      out = new DataOutputStream(socket.getOutputStream());
    } catch (IOException e) {
      return;
    }
    String line;
    Transaction t = new Transaction();
    while (true) {
      try {
        line = brinp.readLine();
        // create operation
        String[] split = line.split(" ");
        Operation op = null;
        if (split.length == 2) { // 'commit operation'
          op = new Operation(split[0]);
          op.setOperationType(OperationType.COMMIT);
        } else if (split.length == 4 && split[3].equals("r")) {  // 'read' operation
          op = new Operation(split[0], split[1], split[2], "");
          op.setOperationType(OperationType.READ);
        } else if (split.length > 4 && split[3].equals("w")) {  // 'write' operation
          String params = unSplit(split);
          op = new Operation(split[0], split[1], split[2], params);
          op.setOperationType(OperationType.WRITE);
        } else if (split.length > 4 && split[3].equals("u")) {  // 'update' operation
          String params = unSplit(split);
          op = new Operation(split[0], split[1], split[2], params);
          op.setOperationType(OperationType.UPDATE);
        } else if (split.length > 4 && split[3].equals("d")) {  // 'delete' operation
          op = new Operation(split[0], split[1], split[2], split[4]);
          op.setOperationType(OperationType.DELETE);
        }
        if (op.getOperationType().equals(OperationType.COMMIT)) { // send to transaction manager
          t.setId(op.gettId());
          out.writeBytes(transactionManager.processTransaction(t, new Sleep() {
            @Override
            public void sleep() {
              try {
                Thread.sleep((long) (Math.random() * 100));
              } catch (InterruptedException e) {
                System.out.println("Can't sleep.");
              }
            }
          }) + "\n\r");
          out.flush();
          return;
        } else {
          t.addOperation(op);
        }


        System.out.println(op != null ? op.toString() : "null");

        System.out.println(line);




//        out.writeBytes("server: " + line + "\n\r");
//
//        out.flush();
//        if(line.contains("commit")) {
//          socket.close();
//          return;
//        }
      } catch (IOException e) {
        e.printStackTrace();
        return;
      }
    }
  }

  private String unSplit(String[] split) {
    String params = "";
    for (int i = 4; i<split.length; i++) {  // unsplit params
      params += split[i];
      if (i <split.length-1) {
        params += " ";
      }
    }
    return params;
  }
}