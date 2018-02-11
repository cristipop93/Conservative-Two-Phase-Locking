package server;

import server.model.*;
import server.repository.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by Cristi on 1/26/2018.
 */
public class TransactionManager {
  private Map<TableName, Operation> writeLock = new HashMap<>();
  private Map<TableName, List<Operation>> readLock = new HashMap<>();
  private ReentrantLock lock = new ReentrantLock();

  public String processTransaction(Transaction t, Sleep sleep) {
    List<Operation> readOp = t.getReadOperations();
    List<Operation> writeOp = t.getWriteOperations();
    StringBuilder result = new StringBuilder();

    while (true) {
      lock.lock();
      if (alreadyExistsWriteLock(t.getOperations())) { // a writelock already exists on the operations
        System.out.println(t.getId() + " Cannot obtain locks, try again");
        result.append(t.getId() + " Cannot obtain locks, try again \n");
        lock.unlock();
        sleep.sleep();
        continue;
      }

      addWriteLocks(writeOp); // obtain writeLocks
      addReadLocks(readOp); // obtain readLocks
      lock.unlock();
//      sleep.sleep();

      result.append(t.getId() + "Locks Obtained \n");

      for (Operation operation : t.getOperations()) {
        runOperation(operation, result, t.getId());
      }
      lock.lock();
      releaseLocks(t.getOperations());
      lock.unlock();
      result.append(t.getId() + "Locks Released");
      break;
    }
    return result.toString();
  }

  private void releaseLocks(List<Operation> operations) {
    for (Operation operation : operations) {
      if (!operation.getOperationType().equals(OperationType.READ) && writeLock.get(operation.getTable()) != null && writeLock.get(operation.getTable()).equals(operation)) {
        System.out.println("write unlocked");
        writeLock.put(operation.getTable(), null);
      }
      if ( (operation.getOperationType().equals(OperationType.READ)) && readLock.get(operation.getTable()) != null && readLock.get(operation.getTable()).equals(operation)) {
        System.out.println("read unlocked");
        readLock.get(operation.getTable()).remove(operation);
      }
    }
  }

  private void runOperation(Operation operation, StringBuilder result, String tId) {
    Repository repo = new Repository(result, "jdbc:mysql://localhost/" + operation.getDbName().getId(), operation.getDbName());
    repo.runOperation(operation, tId);
  }

  private void addReadLocks(List<Operation> readOp) {
    for (Operation operation : readOp) {
      readLock.computeIfAbsent(operation.getTable(), tableName -> new ArrayList<>()).add(operation);
    }
  }

  private void addWriteLocks(List<Operation> writeOp) {
    for (Operation operation : writeOp) {
      writeLock.put(operation.getTable(), operation);
    }
  }

  private boolean alreadyExistsWriteLock(List<Operation> writeOp) {
    for (Operation operation : writeOp) {
      if (writeLock.get(operation.getTable()) != null) {
        return true;
      }

    }
    return false;
  }
}
