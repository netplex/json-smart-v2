package net.minidev.json.actions.path;


import java.util.Arrays;
import java.util.Map;
import java.util.List;
import java.util.ListIterator;

/**
 * {@link TreePath} represents an n-gram formatted path
 * corresponding to a branch in a tree of {@link Map}s
 * and {@link List}s
 * <p>
 * See package-info for more details
 *
 * @author adoneitan@gmail.com
 */
public class TreePath
{

	protected enum Step {NONE, NEXT, PREV}
	protected final String path;
	protected List<String> keys;
	protected ListIterator<String> keysItr;
	protected String currKey;
	protected Step lastStep;
	protected StringBuilder origin;
	protected StringBuilder remainder;
	protected PathDelimiter delim;

	public TreePath(String path, PathDelimiter delim)
	{
		this.delim = delim;
		checkPath(path);
		this.path = path;
		this.keys = Arrays.asList(path.split(delim.regex()));
		reset();
	}

	public void reset()
	{
		keysItr = keys.listIterator();
		currKey = "";
		lastStep = Step.NONE;
		origin = new StringBuilder("");
		remainder = new StringBuilder(path);
	}

	public boolean hasNext() {
		return keysItr.hasNext();
	}

	public int nextIndex() {
		return keysItr.nextIndex();
	}

	public String next()
	{
		currKey = keysItr.next();
		/** when changing direction the {@link ListIterator} does not really
		 * move backward so an extra step is performed */
		if (!lastStep.equals(Step.PREV)) {
			originIncrement();
			remainderDecrement();
		}
		lastStep = Step.NEXT;
		return currKey;
	}

	public boolean hasPrev() {
		return keysItr.hasPrevious();
	}

	public int prevIndex() {
		return keysItr.previousIndex();
	}

	public String prev()
	{
		String temp = currKey;
		currKey = keysItr.previous();
		/** when changing direction the {@link ListIterator} does not really
		 * move backward so an extra step is performed */
		if (!lastStep.equals(Step.NEXT)) {
			remainderIncrement(temp);
			originDecrement();
		}
		lastStep = Step.PREV;
		return currKey;
	}

	private void remainderDecrement()
	{
		if (length() == 1)
			remainder = new StringBuilder("");
		else if (remainder.indexOf(delim.str()) < 0)
			remainder = new StringBuilder("");
		else
			remainder.delete(0, remainder.indexOf(delim.str()) + 1);
	}

	private void originDecrement()
	{
		if (length() == 1)
			origin = new StringBuilder("");
		else if (origin.indexOf(delim.str()) < 0)
			origin = new StringBuilder("");
		else
			origin.delete(origin.lastIndexOf(delim.str()), origin.length());
	}

	private void originIncrement()
	{
		if (origin.length() != 0) {
			origin.append(delim.chr());
		}
		origin.append(currKey);
	}

	private void remainderIncrement(String prev)
	{
		if (remainder.length() == 0)
			remainder = new StringBuilder(prev);
		else
			remainder = new StringBuilder(prev).append(delim.chr()).append(remainder);
	}

	/**
	 * @return An n-gram path from the first key to the current key (inclusive)
	 */
	public String path() {
		return path;
	}

	/**
	 * @return An n-gram path from the first key to the current key (inclusive)
	 */
	public String origin() {
		return origin.toString();
	}

	/**
	 * @return An n-gram path from the current key to the last key (inclusive)
	 */
	public String remainder() {
		return remainder.toString();
	}

	/**
	 * @return first element in the JSONPath
	 */
	public String first() {
		return keys.get(0);
	}

	/**
	 * @return last element in the JSONPath
	 */
	public String last() {
		return keys.get(keys.size() - 1);
	}

	/**
	 * @return current element pointed to by the path iterator
	 */
	public String curr() {
		return currKey;
	}

	public int length() {
		return keys.size();
	}

	public String subPath(int firstIndex, int lastIndex)
	{
		if (lastIndex < firstIndex) {
			throw new IllegalArgumentException("bad call to subPath");
		}
		StringBuilder sb = new StringBuilder(path.length());
		for (int i = firstIndex; i <= lastIndex; i++)
		{
			sb.append(keys.get(i));
			if (i < lastIndex) {
				sb.append(delim.chr());
			}
		}
		sb.trimToSize();
		return sb.toString();
	}

	private void checkPath(String path)
	{
		if (path == null || path.equals(""))
			throw new IllegalArgumentException("path cannot be null or empty");
		if (path.startsWith(delim.str()) || path.endsWith(delim.str()) || path.contains(delim.str() + delim.str()))
			throw new IllegalArgumentException(String.format("path cannot start or end with %s or contain '%s%s'", delim.str(), delim.str(), delim.str()));
	}

	@Override
	public TreePath clone() throws CloneNotSupportedException
	{
		TreePath cloned = new TreePath(this.path, this.delim);
		while (cloned.nextIndex() != this.nextIndex()) {
			cloned.next();
		}
		if (cloned.prevIndex() != this.prevIndex()) {
			cloned.prev();
		}
		cloned.lastStep = this.lastStep;
		cloned.currKey = new String(this.currKey);
		cloned.origin = new StringBuilder(this.origin);
		cloned.remainder = new StringBuilder(this.remainder);
		return cloned;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		TreePath treePath = (TreePath) o;

		return path().equals(treePath.path()) &&
				hasNext() == treePath.hasNext() &&
				hasPrev() == treePath.hasPrev() &&
				curr().equals(treePath.curr()) &&
				origin().equals(treePath.origin()) &&
				remainder().equals(treePath.remainder()) &&
				lastStep == treePath.lastStep &&
				delim.equals(treePath.delim);

	}

	@Override
	public int hashCode() {
		int result = path.hashCode();
		result = 31 * result + keys.hashCode();
		result = 31 * result + keysItr.hashCode();
		result = 31 * result + currKey.hashCode();
		result = 31 * result + lastStep.hashCode();
		result = 31 * result + origin.hashCode();
		result = 31 * result + remainder.hashCode();
		result = 31 * result + delim.hashCode();
		return result;
	}
}


