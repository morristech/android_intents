/*
 * *************************************************************************************************
 *                                 Copyright 2016 Universum Studios
 * *************************************************************************************************
 *                  Licensed under the Apache License, Version 2.0 (the "License")
 * -------------------------------------------------------------------------------------------------
 * You may not use this file except in compliance with the License. You may obtain a copy of the
 * License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied.
 *
 * See the License for the specific language governing permissions and limitations under the License.
 * *************************************************************************************************
 */
package universum.studios.android.intent;

import android.content.Intent;
import android.net.Uri;

import org.hamcrest.CoreMatchers;
import org.junit.Test;

import universum.studios.android.test.local.RobolectricTestCase;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsNot.not;
import static org.hamcrest.core.IsNull.nullValue;
import static universum.studios.android.intent.WebTests.assertThatBuildThrowsExceptionWithMessage;

/**
 * @author Martin Albedinsky
 */
public final class WebIntentTest extends RobolectricTestCase {

	@Test public void testWebUrlMatcher() {
		// Assert:
		assertThat(WebIntent.WEB_URL_MATCHER, is(notNullValue()));
	}

	@Test public void testHttpPrefix() {
		// Assert:
		assertThat(WebIntent.HTTP_PREFIX, is("http://"));
	}

	@Test public void testHttpMatcher() {
		// Act + Assert:
		assertThat(WebIntent.HTTP_FORMAT_MATCHER.reset("http://www.google.com").matches(), is(true));
		assertThat(WebIntent.HTTP_FORMAT_MATCHER.reset("https://www.google.com").matches(), is(true));
		assertThat(WebIntent.HTTP_FORMAT_MATCHER.reset("www.google.com").matches(), is(false));
	}

	@Test public void testInstantiation() {
		// Act:
		final WebIntent intent = new WebIntent();
		// Assert:
		assertThat(intent.url(), is(not(nullValue())));
		assertThat(intent.url().length(), is(0));
	}

	@Test public void testUrlText() {
		// Arrange:
		final WebIntent intent = new WebIntent();
		// Act:
		intent.url("http://www.google.com");
		// Assert:
		assertThat(intent.url(), is("http://www.google.com"));
	}

	@Test public void testUrlTextWithoutPrefix() {
		// Arrange:
		final WebIntent intent = new WebIntent();
		// Act:
		intent.url("www.google.com");
		// Assert:
		assertThat(intent.url(), is("http://www.google.com"));
	}

	@Test public void testUrlTextWithInvalidValue() {
		// Arrange:
		final WebIntent intent = new WebIntent();
		// Act:
		intent.url("google");
		// Assert:
		assertThat(intent.url(), is(""));
	}

	@Test public void testBuild() {
		// Arrange:
		final WebIntent webIntent = new WebIntent();
		webIntent.url("inbox.google.com");
		// Act:
		final Intent intent = webIntent.build(application);
		// Assert:
		assertThat(intent, is(not(CoreMatchers.nullValue())));
		assertThat(intent.getAction(), is(Intent.ACTION_VIEW));
		assertThat(intent.getData(), is(Uri.parse("http://inbox.google.com")));
	}

	@Test public void testBuildWithoutUrl() {
		assertThatBuildThrowsExceptionWithMessage(
				application,
				new WebIntent(),
				"No or invalid URL specified."
		);
	}
}