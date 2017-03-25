package net.minidev.json.actions.path;

/**
 * Encapsulates the delimiter '.' of the path parts when the path is specified in n-gram format.
 * For example: root.node1.node11.leaf
 *
 * @author adoneitan@gmail.com
 * @since 31 May 2016
 */
public class SlashDelimiter extends PathDelimiter {
	protected static final char DELIM_CHAR = '/';

	public SlashDelimiter() {
		super(DELIM_CHAR);
		super.withAcceptDelimiterInNodeName(false);
	}

	public String regex() {
		return "\\/";
	}
}
