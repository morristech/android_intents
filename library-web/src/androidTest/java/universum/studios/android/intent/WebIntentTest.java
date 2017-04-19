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
import android.support.test.runner.AndroidJUnit4;

import org.hamcrest.CoreMatchers;
import org.junit.Test;
import org.junit.runner.RunWith;

import universum.studios.android.test.BaseInstrumentedTest;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsNot.not;
import static org.hamcrest.core.IsNull.nullValue;
import static universum.studios.android.intent.WebTests.assertThatBuildThrowsExceptionWithMessage;

/**
 * @author Martin Albedinsky
 */
@RunWith(AndroidJUnit4.class)
public final class WebIntentTest extends BaseInstrumentedTest {

	@SuppressWarnings("unused")
	private static final String TAG = "WebIntentTest";

	private WebIntent mIntent;

	@Override
	public void beforeTest() throws Exception {
		super.beforeTest();
		this.mIntent = new WebIntent();
	}

	@Override
	public void afterTest() throws Exception {
		super.afterTest();
		this.mIntent = null;
	}

	@Test
	public void testDefaultUrl() {
		assertThat(mIntent.url(), is(not(nullValue())));
		assertThat(mIntent.url().length(), is(0));
	}

	@Test
	public void testUrlText() {
		mIntent.url("http://www.google.com");
		assertThat(mIntent.url(), is("http://www.google.com"));
	}

	@Test
	public void testUrlTextWithoutPrefix() {
		mIntent.url("www.google.com");
		assertThat(mIntent.url(), is("http://www.google.com"));
	}

	@Test
	public void testUrlTextWithInvalidValue() {
		mIntent.url("google");
		assertThat(mIntent.url(), is(""));
	}

	@Test
	public void testBuild() {
		mIntent.url("inbox.google.com");
		final Intent intent = mIntent.build(mContext);
		assertThat(intent, is(not(CoreMatchers.nullValue())));
		assertThat(intent.getAction(), is(Intent.ACTION_VIEW));
		assertThat(intent.getData(), is(Uri.parse("http://inbox.google.com")));
	}

	@Test
	public void testBuildWithoutUrl() {
		assertThatBuildThrowsExceptionWithMessage(
				mContext,
				mIntent,
				"No or invalid URL specified."
		);
	}
}
