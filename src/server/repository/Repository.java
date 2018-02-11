package server.repository;

import server.model.DBName;
import server.model.Operation;
import server.model.OperationType;
import server.model.TableName;

import java.sql.*;

/**
 * Created by Cristi on 1/27/2018.
 */
public class Repository {
  private Connection connection;
  private StringBuilder result;
  private DBName dbname;

  public Repository(StringBuilder result, String url, DBName dbName) {
    this.result = result;
    this.dbname = dbName;
    try {
      Class.forName("com.mysql.jdbc.Driver");
      connection = DriverManager.getConnection(url, "root", "root");
    } catch (Exception e) {
      System.err.println(e);
    }
  }


  public void runOperation(Operation operation, String tId) {
    if (operation.getOperationType() == OperationType.READ) {
      result.append(tId + " " +read(operation.getTable()) + "\n");
      close();
    } else if (operation.getOperationType() == OperationType.WRITE) {
      write(operation.getTable(), operation.getParams());
      result.append(tId + " " +"Wrote in " + operation.getTable() + "\n");
      close();
    } else if (operation.getOperationType() == OperationType.UPDATE) {
      update(operation.getTable(), operation.getParams());
      result.append(tId + " " +"Updated " + operation.getTable() + "\n");
      close();
    } else if (operation.getOperationType() == OperationType.DELETE) {
      delete(operation.getTable(), operation.getParams());
      result.append(tId + " " +"Deleted from " + operation.getTable() + " id: " + operation.getParams() + "\n");
      close();
    }
  }

  private void delete(TableName table, String params) {
    PreparedStatement statement = null;
    try {
      String query = "DELETE FROM " + table +
              " WHERE id = " + params;
      statement = connection.prepareStatement(query);
      statement.executeUpdate();
    } catch (SQLException e) {
      e.printStackTrace();
    } finally {
      try {
        if (statement != null) {
          statement.close();
        }
      } catch (SQLException e) {
        System.err.println(e);
      }
    }
  }

  private void update(TableName table, String params) {
    delete(table, params.split(" ")[0]);
    write(table, params);
  }

  private void write(TableName table, String params) {
    PreparedStatement statement = null;
    try {
      String fields = "(id, denumire, pret)";
      if (dbname.equals(DBName.IT) && table == TableName.LAPTOP) {
        fields = "(id, denumire, categorie, pret)";
      } else if (dbname.equals(DBName.COMPONENTE) && table == TableName.MEMORII) {
        fields = "(id, denumire, frecventa, pret)";
      }
      String values = params.replace(" ", ", ");
      String query = "insert into " + table + fields + " values (" + values + ")";
      statement = connection.prepareStatement(query);
      statement.executeUpdate();
    } catch (SQLException e) {
      e.printStackTrace();
    } finally {
      try {
        if (statement != null) {
          statement.close();
        }
      } catch (SQLException e) {
        System.err.println(e);
      }
    }
  }

  private String read(TableName table) {
    Statement statement = null;
    ResultSet rs = null;
    String result = "";
    try {
      statement = connection.createStatement();
      String query = "select * from " + table.getId();
      rs = statement.executeQuery(query);
      ResultSetMetaData meta = rs.getMetaData();
      int colCount = meta.getColumnCount();
      while (rs.next()) {
        String tempResult = "";
        for (int i = 1; i <= colCount; i++) {
          tempResult += rs.getString(i) + (i != colCount ? "," : "");
        }
        result += tempResult + "\n";
      }
    } catch (Exception e) {
      System.err.println(e.getMessage());
    } finally {
      try {
        if (rs != null) {
          rs.close();
        }
        if (statement != null) {
          statement.close();
        }
      } catch (SQLException e) {
        System.err.println(e);
      }
    }
    return result;
  }

  private void close() {
    try {
      if (connection != null)
        connection.close();
    } catch (SQLException e) {
      // connection close failed.
      System.err.println(e);
    }
  }
}
