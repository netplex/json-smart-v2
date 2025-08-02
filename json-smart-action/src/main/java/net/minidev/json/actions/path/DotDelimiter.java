package net.minidev.json.actions.path;

/**
 * Encapsulates the delimiter '.' of the path parts when the path is specified in n-gram format. For
 * example: root.node1.node11.leaf
 *
 * @author adoneitan@gmail.com
 * @since 31 May2016
 */
public class DotDelimiter extends PathDelimiter {
  /** The dot delimiter character */
  protected static final char DELIM_CHAR = '.';

  /** Creates a new dot delimiter */
  public DotDelimiter() {
    super(DELIM_CHAR);
  }

  public String regex() {
    return "\\.";
  }
}
