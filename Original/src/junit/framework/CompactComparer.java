package junit.framework;

public class CompactComparer {

	private static final String ELLIPSIS= "...";
	private static final String DELTA_END= "]";
	private static final String DELTA_START= "[";

	public static String compare(String expected, String actual, int contextLength) {
		return compare(expected, actual, contextLength, null);
	}

	public static String compare(String expected, String actual, int contextLength, String message) {
		if (isSimpleComparison(expected, actual))
			return Assert.format(message, expected, actual);

		String commonPrefix = findCommonPrefix(expected, actual);
		String commonSuffix = findCommonSuffix(expected, actual, commonPrefix);

		String contextualPrefix = trimToContext(commonPrefix, contextLength);
		String contextualSuffix = reverse(trimToContext(reverse(commonSuffix), contextLength));

		String uniqueExpectedPart = getUniquePart(expected, commonPrefix, commonSuffix);
		String uniqueActualPart = getUniquePart(actual, commonPrefix, commonSuffix);

		String formattedExpected= applyCompactFormat(contextualPrefix, uniqueExpectedPart, contextualSuffix);
		String formattedActual= applyCompactFormat(contextualPrefix, uniqueActualPart, contextualSuffix);

		return Assert.format(message, formattedExpected, formattedActual);
	}

	private static boolean isSimpleComparison(String expected, String actual) {
		return expected == null || actual == null || expected.equals(actual);
	}

	private static String findCommonPrefix(String expected, String actual) {
		int end= Math.min(expected.length(), actual.length());

		int prefixIndex;
		for (prefixIndex = 0; prefixIndex < end; prefixIndex++) {
			if (expected.charAt(prefixIndex) != actual.charAt(prefixIndex))
				break;
		}

		return expected.substring(0, prefixIndex);
	}

	private static String findCommonSuffix(String expected, String actual, String commonPrefix) {
		String commonSuffix = reverse(findCommonPrefix(reverse(expected), reverse(actual)));

		int commonLength = commonPrefix.length() + commonSuffix.length();
		boolean hasOverlap = commonLength > expected.length() || commonLength > actual.length();

		return hasOverlap ? commonSuffix.substring(1) : commonSuffix;
	}

	private static String trimToContext(String commonPart, int contextLength) {
		if (contextLength >= commonPart.length())
			return commonPart;
		else
			return ELLIPSIS + commonPart.substring(commonPart.length() - contextLength);
	}

	private static String reverse(String str) {
		return new StringBuilder(str).reverse().toString();
	}

	private static String getUniquePart(String str, String commonPrefix, String commonSuffix) {
		int uniqueStartIndex = commonPrefix.length();
		int uniqueEndIndex = str.length() - commonSuffix.length();
		return str.substring(uniqueStartIndex, uniqueEndIndex);
	}

	private static String applyCompactFormat(String contextualPrefix, String uniquePart, String contextualSuffix) {
		return contextualPrefix + DELTA_START + uniquePart + DELTA_END + contextualSuffix;
	}
}
