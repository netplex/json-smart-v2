package net.minidev.json.test.parser;

import java.util.List;
import java.util.Map;

/** Represents a transaction data structure parsed from JSON. */
public class Transaction {
  private String tranid;
  private User user;
  private List<Map<String, Object>> friends;

  // Getters and Setters
  public String getTranid() {
    return tranid;
  }

  public void setTranid(String tranid) {
    this.tranid = tranid;
  }

  public User getUser() {
    return user;
  }

  public void setUser(User user) {
    this.user = user;
  }

  public List<Map<String, Object>> getFriends() {
    return friends;
  }

  public void setFriends(List<Map<String, Object>> friends) {
    this.friends = friends;
  }

  @Override
  public String toString() {
    return "Transaction{"
        + "tranid='"
        + tranid
        + '\''
        + ", user="
        + user
        + ", friends="
        + friends
        + '}';
  }

  /** Represents the user object within the transaction. */
  public static class User {
    private String name;
    private String addr;

    // Getters and Setters
    public String getName() {
      return name;
    }

    public void setName(String name) {
      this.name = name;
    }

    public String getAddr() {
      return addr;
    }

    public void setAddr(String addr) {
      this.addr = addr;
    }

    @Override
    public String toString() {
      return "User{" + "name='" + name + '\'' + ", addr='" + addr + '\'' + '}';
    }
  }
}
