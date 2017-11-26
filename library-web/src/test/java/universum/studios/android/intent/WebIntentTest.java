/*
 * =================================================================================================
 *                             Copyright (C) 2016 Universum Studios
 * =================================================================================================
 *         Licensed under the Apache License, Version 2.0 or later (further "License" only).
 * -------------------------------------------------------------------------------------------------
 * You may use this file only in compliance with the License. More details and copy of this License
 * you may obtain at
 *
 * 		http://www.apache.org/licenses/LICENSE-2.0
 *
 * You can redistribute, modify or publish any part of the code written within this file but as it
 * is described in the License, the software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES or CONDITIONS OF ANY KIND.
 *
 * See the License for the specific language governing permissions and limitations under the License.
 * =================================================================================================
 */
package universum.studios.android.intent;

import android.content.Intent;
import android.net.Uri;

import org.hamcrest.CoreMatchers;
import org.junit.Test;

import universum.studios.android.test.local.RobolectricTestCase;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsNot.not;
import static org.hamcrest.core.IsNull.nullValue;
import static universum.studios.android.intent.WebTests.assertThatBuildThrowsExceptionWithMessage;

/**
 * @author Martin Albedinsky
 */
public final class WebIntentTest extends RobolectricTestCase {

	@Test
	public void testDefaultUrl() {
		final WebIntent intent = new WebIntent();
		assertThat(intent.url(), is(not(nullValue())));
		assertThat(intent.url().length(), is(0));
	}

	@Test
	public void testUrlText() {
		final WebIntent intent = new WebIntent();
		intent.url("http://www.google.com");
		assertThat(intent.url(), is("http://www.google.com"));
	}

	@Test
	public void testUrlTextWithoutPrefix() {
		final WebIntent intent = new WebIntent();
		intent.url("www.google.com");
		assertThat(intent.url(), is("http://www.google.com"));
	}

	@Test
	public void testUrlTextWithInvalidValue() {
		final WebIntent intent = new WebIntent();
		intent.url("google");
		assertThat(intent.url(), is(""));
	}

	@Test
	public void testBuild() {
		final WebIntent webIntent = new WebIntent();
		webIntent.url("inbox.google.com");
		final Intent intent = webIntent.build(mApplication);
		assertThat(intent, is(not(CoreMatchers.nullValue())));
		assertThat(intent.getAction(), is(Intent.ACTION_VIEW));
		assertThat(intent.getData(), is(Uri.parse("http://inbox.google.com")));
	}

	@Test
	public void testBuildWithoutUrl() {
		assertThatBuildThrowsExceptionWithMessage(
				mApplication,
				new WebIntent(),
				"No or invalid URL specified."
		);
	}
}
