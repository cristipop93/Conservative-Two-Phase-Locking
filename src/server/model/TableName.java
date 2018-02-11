package server.model;

/**
 * Created by Cristi on 1/26/2018.
 */
public enum TableName {
  MEMORII("memorii"),
  PLACIVIDEO("placivideo"),
  PROCESOARE("procesoare"),
  LAPTOP("laptop"),
  TABLETE("tablete"),
  TELEFOANE("telefoane");

  String id;

  TableName(String id) {
    this.id = id;
  }

  public static TableName getById(String id) {
    for (TableName e: values()) {
      if (e.id.equals(id)) {
        return e;
      }
    }
    return null;
  }

  public String getId() {
    return id;
  }
}
