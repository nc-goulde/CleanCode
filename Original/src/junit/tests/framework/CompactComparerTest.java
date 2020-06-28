package junit.tests.framework;

import junit.framework.CompactComparer;
import junit.framework.TestCase;

public class CompactComparerTest extends TestCase {

	public void testMessage() {
		String failure= new CompactComparer().compare("b", "c", 0, "a");
		assertTrue("a expected:<[b]> but was:<[c]>".equals(failure));
	}

	public void testStartSame() {
		String failure= new CompactComparer().compare("ba", "bc", 1, null);
		assertEquals("expected:<b[a]> but was:<b[c]>", failure);
	}

	public void testEndSame() {
		String failure= new CompactComparer().compare("ab", "cb", 1, null);
		assertEquals("expected:<[a]b> but was:<[c]b>", failure);
	}

	public void testSame() {
		String failure= new CompactComparer().compare("ab", "ab", 1, null);
		assertEquals("expected:<ab> but was:<ab>", failure);
	}

	public void testNoContextStartAndEndSame() {
		String failure= new CompactComparer().compare("abc", "adc", 0, null);
		assertEquals("expected:<...[b]...> but was:<...[d]...>", failure);
	}

	public void testStartAndEndContext() {
		String failure= new CompactComparer().compare("abc", "adc", 1, null);
		assertEquals("expected:<a[b]c> but was:<a[d]c>", failure);
	}

	public void testStartAndEndContextWithEllipses() {
		String failure= new CompactComparer().compare("abcde", "abfde", 1, null);
		assertEquals("expected:<...b[c]d...> but was:<...b[f]d...>", failure);
	}

	public void testComparisonErrorStartSameComplete() {
		String failure= new CompactComparer().compare("ab", "abc", 2, null);
		assertEquals("expected:<ab[]> but was:<ab[c]>", failure);
	}

	public void testComparisonErrorEndSameComplete() {
		String failure= new CompactComparer().compare("bc", "abc", 0, null);
		assertEquals("expected:<[]...> but was:<[a]...>", failure);
	}

	public void testComparisonErrorEndSameCompleteContext() {
		String failure= new CompactComparer().compare("bc", "abc", 2, null);
		assertEquals("expected:<[]bc> but was:<[a]bc>", failure);
	}

	public void testComparisonErrorOverlapingMatches() {
		String failure= new CompactComparer().compare("abc", "abbc", 0, null);
		assertEquals("expected:<...[]...> but was:<...[b]...>", failure);
	}

	public void testComparisonErrorOverlapingMatchesContext() {
		String failure= new CompactComparer().compare("abc", "abbc", 2, null);
		assertEquals("expected:<ab[]c> but was:<ab[b]c>", failure);
	}

	public void testComparisonErrorOverlapingMatches2() {
		String failure= new CompactComparer().compare("abcdde", "abcde", 0, null);
		assertEquals("expected:<...[d]...> but was:<...[]...>", failure);
	}

	public void testComparisonErrorOverlapingMatches2Context() {
		String failure= new CompactComparer().compare("abcdde", "abcde", 2, null);
		assertEquals("expected:<...cd[d]e> but was:<...cd[]e>", failure);
	}

	public void testComparisonErrorWithActualNull() {
		String failure= new CompactComparer().compare("a", null, 0, null);
		assertEquals("expected:<a> but was:<null>", failure);
	}

	public void testComparisonErrorWithActualNullContext() {
		String failure= new CompactComparer().compare("a", null, 2, null);
		assertEquals("expected:<a> but was:<null>", failure);
	}

	public void testComparisonErrorWithExpectedNull() {
		String failure= new CompactComparer().compare(null, "a", 0, null);
		assertEquals("expected:<null> but was:<a>", failure);
	}

	public void testComparisonErrorWithExpectedNullContext() {
		String failure= new CompactComparer().compare(null, "a", 2, null);
		assertEquals("expected:<null> but was:<a>", failure);
	}

	public void testBug609972() {
		String failure= new CompactComparer().compare("S&P500", "0", 10, null);
		assertEquals("expected:<[S&P50]0> but was:<[]0>", failure);
	}
}
