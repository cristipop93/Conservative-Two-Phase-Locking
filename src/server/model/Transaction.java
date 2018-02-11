package server.model;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by Cristi on 1/26/2018.
 */
public class Transaction {
  private String id;
  private List<Operation> operations;

  public Transaction() {
    this.operations = new LinkedList<>();
  }

  public String getId() {
    return id;
  }

  public List<Operation> getOperations() {
    return operations;
  }

  public void addOperation(Operation op) {
    this.operations.add(op);
  }

  public void setId(String id) {
    this.id = id;
  }

  public List<Operation> getReadOperations() {
    List<Operation> result = new LinkedList<>();
    for (Operation operation : operations) {
      if (operation.getOperationType().equals(OperationType.READ)) {
        result.add(operation);
      }
    }
    return result;
  }

  public List<Operation> getWriteOperations() {
    List<Operation> result = new LinkedList<>();
    for (Operation operation : operations) {
      if (!operation.getOperationType().equals(OperationType.READ)) {
        result.add(operation);
      }
    }
    return result;
  }
}
