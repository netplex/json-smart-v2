package net.minidev.json.actions.navigate;


import net.minidev.json.JSONObject;

import java.util.Arrays;
import java.util.List;
import java.util.ListIterator;

/**
 * {@link JSONPath} represents an n-gram formatted path
 * corresponding to a branch in a {@link JSONObject}
 * <p>
 * See package-info for more details
 *
 * @author adoneitan@gmail.com
 */
public class JSONPath
{
	protected enum Step {NONE, NEXT, PREV}
	protected final String path;
	protected List<String> keys;
	protected ListIterator<String> keysItr;
	protected String currKey;
	protected Step lastStep;
	protected StringBuilder origin;
	protected StringBuilder remainder;

	public JSONPath(String path)
	{
		checkPath(path);
		this.path = path;
		this.keys = Arrays.asList(path.split("\\."));
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
		else if (remainder.indexOf(".") < 0)
			remainder = new StringBuilder("");
		else
			remainder.delete(0, remainder.indexOf(".") + 1);
	}

	private void originDecrement()
	{
		if (length() == 1)
			origin = new StringBuilder("");
		else if (origin.indexOf(".") < 0)
			origin = new StringBuilder("");
		else
			origin.delete(origin.lastIndexOf("."), origin.length());
	}

	private void originIncrement()
	{
		if (origin.length() != 0) {
			origin.append('.');
		}
		origin.append(currKey);
	}

	private void remainderIncrement(String prev)
	{
		if (remainder.length() == 0)
			remainder = new StringBuilder(prev);
		else
			remainder = new StringBuilder(prev).append('.').append(remainder);
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
				sb.append('.');
			}
		}
		sb.trimToSize();
		return sb.toString();
	}

	private void checkPath(String path)
	{
		if (path == null || path.equals(""))
			throw new IllegalArgumentException("path cannot be null or empty");
		if (path.startsWith(".") || path.endsWith(".") || path.contains("src/main"))
			throw new IllegalArgumentException("path cannot start or end with '.' or contain '..'");
	}

	@Override
	public JSONPath clone() throws CloneNotSupportedException
	{
		JSONPath cloned = new JSONPath(this.path);
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

		JSONPath jsonPath = (JSONPath) o;

		return path().equals(jsonPath.path()) &&
			hasNext() == jsonPath.hasNext() &&
			hasPrev() == jsonPath.hasPrev() &&
			curr().equals(jsonPath.curr()) &&
			origin().equals(jsonPath.origin()) &&
			remainder().equals(jsonPath.remainder()) &&
			lastStep == jsonPath.lastStep;

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
		return result;
	}
}


