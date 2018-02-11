package server.model;

/**
 * Created by Cristi on 1/26/2018.
 */
public class Operation {
  private String tId;
  private DBName dbName;
  private TableName table;
  private String params;
  private OperationType operationType;

  public Operation(String tId, String dbName, String table, String params) {
    this.tId = tId;
    this.dbName = DBName.getById(dbName);
    this.table = TableName.getById(table);
    this.params = params;
  }

  // used for commit
  public Operation(String tId) {
    this.tId = tId;
  }

  public String gettId() {
    return tId;
  }

  public DBName getDbName() {
    return dbName;
  }

  public TableName getTable() {
    return table;
  }

  public String getParams() {
    return params;
  }

  public OperationType getOperationType() {
    return operationType;
  }

  public void setOperationType(OperationType operationType) {
    this.operationType = operationType;
  }

  @Override
  public String toString() {
    return "Operation{" +
            "tId='" + tId + '\'' +
            ", dbName='" + dbName + '\'' +
            ", table='" + table + '\'' +
            ", params='" + params + '\'' +
            ", operationType=" + operationType +
            '}';
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    Operation operation = (Operation) o;

    if (tId != null ? !tId.equals(operation.tId) : operation.tId != null) return false;
    if (dbName != operation.dbName) return false;
    if (table != operation.table) return false;
    if (params != null ? !params.equals(operation.params) : operation.params != null) return false;
    return operationType == operation.operationType;
  }

  @Override
  public int hashCode() {
    int result = tId != null ? tId.hashCode() : 0;
    result = 31 * result + (dbName != null ? dbName.hashCode() : 0);
    result = 31 * result + (table != null ? table.hashCode() : 0);
    result = 31 * result + (params != null ? params.hashCode() : 0);
    result = 31 * result + (operationType != null ? operationType.hashCode() : 0);
    return result;
  }
}
