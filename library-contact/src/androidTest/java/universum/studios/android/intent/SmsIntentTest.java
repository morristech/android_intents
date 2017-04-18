/*
 * =================================================================================================
 *                             Copyright (C) 2017 Universum Studios
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

import org.junit.Test;
import org.junit.runner.RunWith;

import universum.studios.android.test.BaseInstrumentedTest;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsNot.not;
import static universum.studios.android.intent.ContactTests.assertThatBuildThrowsExceptionWithMessage;

/**
 * @author Martin Albedinsky
 */
@RunWith(AndroidJUnit4.class)
public final class SmsIntentTest extends BaseInstrumentedTest {

	@SuppressWarnings("unused")
	private static final String TAG = "SmsIntentTest";

	private SmsIntent mIntent;

	@Override
	public void beforeTest() throws Exception {
		super.beforeTest();
		this.mIntent = new SmsIntent();
	}

	@Override
	public void afterTest() throws Exception {
		super.afterTest();
		this.mIntent = null;
	}

	@Test
	public void testUriScheme() {
		assertThat(SmsIntent.URI_SCHEME, is("sms"));
	}

	@Test
	public void testDefaultPhoneNumber() {
		assertThat(mIntent.phoneNumber(), is(""));
	}

	@Test
	public void testPhoneNumber() {
		mIntent.phoneNumber("00124456");
		assertThat(mIntent.phoneNumber(), is("00124456"));
	}

	@Test
	public void testDefaultBody() {
		assertThat(mIntent.body().toString(), is(""));
	}

	@Test
	public void testBody() {
		mIntent.body("Sms body.");
		assertThat(mIntent.body().toString(), is("Sms body."));
	}

	@Test
	public void testNullBody() {
		mIntent.body(null);
		assertThat(mIntent.body().toString(), is(""));
	}

	@Test
	public void testBuild() {
		mIntent.phoneNumber("02644569874");
		final Intent intent = mIntent.build(mContext);
		assertThat(intent, is(not(nullValue())));
		assertThat(intent.getAction(), is(Intent.ACTION_VIEW));
		assertThat(intent.getData(), is(Uri.parse("sms:02644569874")));
	}

	@Test
	public void testBuildWithBody() {
		mIntent.phoneNumber("02644569874");
		mIntent.body("Sms body content.");
		final Intent intent = mIntent.build(mContext);
		assertThat(intent, is(not(nullValue())));
		assertThat(intent.getAction(), is(Intent.ACTION_VIEW));
		assertThat(intent.getData(), is(Uri.parse("sms:02644569874")));
		assertThat(intent.getStringExtra("sms_body"), is("Sms body content."));
	}

	@Test
	public void testBuildWithoutNumber() {
		assertThatBuildThrowsExceptionWithMessage(
				mContext,
				mIntent,
				"No phone number specified."
		);
	}
}
