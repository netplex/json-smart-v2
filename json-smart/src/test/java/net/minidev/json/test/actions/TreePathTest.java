package net.minidev.json.test.actions;

import net.minidev.json.actions.path.DotDelimiter;
import net.minidev.json.actions.path.TreePath;
import net.minidev.json.actions.path.PathDelimiter;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * @author adoneitan@gmail.com
 */
public class TreePathTest
{
	private static final PathDelimiter delim = new DotDelimiter().withAcceptDelimiterInNodeName(true);

	@Test
	public void testIterator()
	{
		TreePath jp = new TreePath("a.b.c", delim);
		assertTrue(jp.nextIndex() == 0);
		assertTrue(jp.prevIndex() == -1);
		assertTrue("".equals(jp.curr()));
		assertTrue("".equals(jp.origin()));
		assertTrue("a.b.c".equals(jp.remainder()));
		assertTrue(jp.hasNext());
		assertFalse(jp.hasPrev());

		jp.next();
		assertTrue("a".equals(jp.curr()));
		assertTrue("a".equals(jp.origin()));
		assertTrue("b.c".equals(jp.remainder()));
		assertTrue(jp.hasNext());
		assertTrue(jp.hasPrev());

		jp.next();
		assertTrue("b".equals(jp.curr()));
		assertTrue("a.b".equals(jp.origin()));
		assertTrue("c".equals(jp.remainder()));
		assertTrue(jp.hasNext());
		assertTrue(jp.hasPrev());

		jp.next();
		assertTrue("c".equals(jp.curr()));
		assertTrue("a.b.c".equals(jp.origin()));
		assertTrue("".equals(jp.remainder()));
		assertFalse(jp.hasNext());
		assertTrue(jp.hasPrev());

		/** the first prev() after a next only changes direction. see {@link ListIterator} for details */
		jp.prev();
		assertTrue("c".equals(jp.curr()));
		assertTrue("a.b.c".equals(jp.origin()));
		assertTrue("".equals(jp.remainder()));
		assertTrue(jp.hasNext());
		assertTrue(jp.hasPrev());

		jp.prev();
		assertTrue("b".equals(jp.curr()));
		assertTrue("a.b".equals(jp.origin()));
		assertTrue("c".equals(jp.remainder()));
		assertTrue(jp.hasNext());
		assertTrue(jp.hasPrev());

		jp.prev();
		assertTrue("a".equals(jp.curr()));
		assertTrue("a".equals(jp.origin()));
		assertTrue("b.c".equals(jp.remainder()));
		assertTrue(jp.hasNext());
		assertFalse(jp.hasPrev());
	}

	@Test
	public void testSubPath()
	{
		TreePath jp = new TreePath("a.b.c", delim);
		assertTrue(jp.subPath(1,2).equals("b.c"));
	}

	@Test
	public void testClone() throws CloneNotSupportedException
	{
		TreePath jp1 = new TreePath("a.b.c", delim);
		TreePath jp2 = jp1.clone();
		assertTrue(jp1.equals(jp2));

		jp1.next();
		TreePath jp3 = jp1.clone();
		assertTrue(jp1.equals(jp3));

		jp1.prev();
		TreePath jp4 = jp1.clone();
		assertTrue(jp1.equals(jp4));

	}
}