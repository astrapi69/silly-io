/**
 * The MIT License
 *
 * Copyright (C) 2015 Asterios Raptis
 *
 * Permission is hereby granted, free of charge, to any person obtaining
 * a copy of this software and associated documentation files (the
 * "Software"), to deal in the Software without restriction, including
 * without limitation the rights to use, copy, modify, merge, publish,
 * distribute, sublicense, and/or sell copies of the Software, and to
 * permit persons to whom the Software is furnished to do so, subject to
 * the following conditions:
 *
 * The above copyright notice and this permission notice shall be
 * included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
 * MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE
 * LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION
 * OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION
 * WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package io.github.astrapi69.io.file;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Locale;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

/**
 * The unit test class for the enum {@link CaseSensitivity}
 */
class CaseSensitivityTest
{

	@ParameterizedTest(name = "{0} starts with {1}: sensitive {2}, insensitive {3}")
	@CsvSource({ "report-1.txt, report, true, true", "Report-1.txt, report, false, true",
			"report-1.txt, REPORT, false, true", "REPORT-1.TXT, rePort, false, true",
			"1-report.txt, report, false, false", "report, report-1.txt, false, false",
			"report-1.txt, '', true, true" })
	void startsWith_comparesWithOrWithoutCase(final String name, final String prefix,
		final boolean sensitive, final boolean insensitive)
	{
		assertEquals(sensitive, CaseSensitivity.SENSITIVE.startsWith(name, prefix));
		assertEquals(insensitive, CaseSensitivity.INSENSITIVE.startsWith(name, prefix));
	}

	/**
	 * In the Turkish locale an upper-case I lowercases to a dotless i, so a comparison that
	 * lowercased with the default locale would read the prefix {@code INI} as {@code ını}
	 */
	@Test
	void startsWith_withoutCase_isTheSameInTheTurkishDefaultLocale()
	{
		final Locale previous = Locale.getDefault();
		final Locale previousDisplay = Locale.getDefault(Locale.Category.DISPLAY);
		final Locale previousFormat = Locale.getDefault(Locale.Category.FORMAT);
		Locale.setDefault(Locale.forLanguageTag("tr-TR"));
		try
		{
			assertTrue(CaseSensitivity.INSENSITIVE.startsWith("ini-files.txt", "INI"));
			assertTrue(CaseSensitivity.INSENSITIVE.startsWith("INI-FILES.TXT", "ini"));
			assertFalse(CaseSensitivity.SENSITIVE.startsWith("ini-files.txt", "INI"));
		}
		finally
		{
			Locale.setDefault(previous);
			Locale.setDefault(Locale.Category.DISPLAY, previousDisplay);
			Locale.setDefault(Locale.Category.FORMAT, previousFormat);
		}
	}
}
