package net.minidev.json.actions.path;

/**
 * Encapsulates the delimiter of the path parts when given in n-gram format.
 *
 * @author adoneitan@gmail.com
 * @since 31 May 2016
 */
public abstract class PathDelimiter {
  /** The delimiter character */
  protected char delimChar;
  /** The delimiter as a string */
  protected String delimStr;
  /** Whether to accept delimiter characters in keys */
  protected boolean acceptDelimInKey;

  /** Creates a path delimiter with the specified character */
  public PathDelimiter(char delim) {
    this.delimChar = delim;
    this.delimStr = String.valueOf(delim);
  }

  /** Configures whether to accept delimiter characters in node names */
  public PathDelimiter withAcceptDelimiterInNodeName(boolean acceptDelimInKey) {
    this.acceptDelimInKey = acceptDelimInKey;
    return this;
  }

  /** Checks if the given key is acceptable based on delimiter rules */
  public boolean accept(String key) {
    if (!acceptDelimInKey && key.contains(delimStr)) return false;
    return true;
  }

  /** Returns the delimiter as a string */
  public String str() {
    return delimStr;
  }

  /** Returns the delimiter character */
  public char chr() {
    return delimChar;
  }

  /** Returns the regex pattern for this delimiter */
  public abstract String regex();
}
