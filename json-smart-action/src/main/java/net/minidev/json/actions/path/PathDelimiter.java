package net.minidev.json.actions.path;

/**
 * Encapsulates the delimiter of the path parts when given in n-gram format.
 *
 * @author adoneitan@gmail.com
 * @since 31 May 2016
 */
public abstract class PathDelimiter {
  protected char delimChar;
  protected String delimStr;
  protected boolean acceptDelimInKey;

  public PathDelimiter(char delim) {
    this.delimChar = delim;
    this.delimStr = String.valueOf(delim);
  }

  public PathDelimiter withAcceptDelimiterInNodeName(boolean acceptDelimInKey) {
    this.acceptDelimInKey = acceptDelimInKey;
    return this;
  }

  public boolean accept(String key) {
    if (!acceptDelimInKey && key.contains(delimStr)) return false;
    return true;
  }

  public String str() {
    return delimStr;
  }

  public char chr() {
    return delimChar;
  }

  public abstract String regex();
}
