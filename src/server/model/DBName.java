package server.model;

/**
 * Created by Cristi on 1/26/2018.
 */
public enum DBName {
  IT("it"), COMPONENTE("componente");

  String id;

  DBName(String id) {
    this.id = id;
  }

  public static DBName getById(String id) {
    for (DBName e: values()) {
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
