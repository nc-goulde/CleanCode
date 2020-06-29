package junit.framework;

data class ComparisonCompactor(
        val surroundingContext: Int,
        val expectedString: String?,
        val actualString: String?
) {

    companion object {
        private const val DELTA_END = "]"
        private const val DELTA_START = "["
    }

    fun compact(message: String?): String {
        return if (expectedString == null || actualString == null || areStringsEqual()) {
            Assert.format(message, expectedString, actualString)
        } else {
            val prefixSuffixContainer = PrefixSuffixContainer.build(expectedString, actualString)

            val expected = compactString(expectedString, prefixSuffixContainer)
            val actual = compactString(actualString, prefixSuffixContainer)

            Assert.format(message, expected, actual)
        }
    }

    private fun compactString(source: String, container: PrefixSuffixContainer): String {
        val trimmedSource = container.trimPrefixAndSuffixFrom(source)
        val result = "$DELTA_START$trimmedSource$DELTA_END"

        val trimmedPrefix = container.getTrimmedPrefix(surroundingContext)
        val trimmedSuffix = container.getTrimmedSuffix(surroundingContext)

        return "$trimmedPrefix$result$trimmedSuffix"
    }

    private fun areStringsEqual(): Boolean {
        return expectedString == actualString
    }

    private data class PrefixSuffixContainer(
            val commonPrefix: String,
            val commonSuffix: String
    ) {
        companion object {
            fun build(actual: String, expected: String): PrefixSuffixContainer {
                val prefix = actual.commonPrefixWith(expected)

                val actualWithoutPrefix = actual.removePrefix(prefix)
                val expectedWithoutPrefix = expected.removePrefix(prefix)

                val suffix = actualWithoutPrefix.commonSuffixWith(expectedWithoutPrefix)

                return PrefixSuffixContainer(prefix, suffix)
            }

            private const val ELLIPSIS = "..."
        }

        fun getTrimmedSuffix(contextLength: Int) =
                "${commonSuffix.take(contextLength)}${fillerCharacter(commonSuffix, contextLength)}"

        fun getTrimmedPrefix(contextLength: Int): String =
                "${fillerCharacter(commonPrefix, contextLength)}${commonPrefix.takeLast(contextLength)}"

        private fun fillerCharacter(surroundingText: String,contextLength: Int): String =
                if (surroundingText.length > contextLength) {
                    ELLIPSIS
                } else {
                    ""
                }

        fun trimPrefixAndSuffixFrom(string: String) =
                string.removeSurrounding(prefix = commonPrefix, suffix = commonSuffix)
    }

}
