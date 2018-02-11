package client;

/**
 * Created by Cristi on 1/26/2018.
 */
public class MainClient {

//  public static int N=2;
//  public static String[] transactionFiles = new String[]{"transaction1.txt", "transaction2.txt"};

  public static int N=2;
  public static String[] transactionFiles = new String[]{"transaction3.txt", "transaction4.txt"};

  public static void main(String[] args) {

    for (int i=0; i<N; i++) {
      Client c = new Client("t"+i, transactionFiles[i]);
      c.start();
    }
  }
}
