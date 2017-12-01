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

import org.junit.Test;

import universum.studios.android.test.local.RobolectricTestCase;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsNot.not;
import static universum.studios.android.intent.ContactTests.assertThatBuildThrowsExceptionWithMessage;

/**
 * @author Martin Albedinsky
 */
public final class SmsIntentTest extends RobolectricTestCase {

	@Test
	public void testUriScheme() {
		assertThat(SmsIntent.URI_SCHEME, is("sms"));
	}

	@Test
	public void testPhoneNumberDefault() {
		assertThat(new SmsIntent().phoneNumber(), is(""));
	}

	@Test
	public void testPhoneNumber() {
		final SmsIntent intent = new SmsIntent();
		intent.phoneNumber("00124456");
		assertThat(intent.phoneNumber(), is("00124456"));
	}

	@Test
	public void testBodyDefault() {
		assertThat(new SmsIntent().body().toString(), is(""));
	}

	@Test
	public void testBody() {
		final SmsIntent intent = new SmsIntent();
		intent.body("Sms body.");
		assertThat(intent.body().toString(), is("Sms body."));
	}

	@Test
	public void testNullBody() {
		final SmsIntent intent = new SmsIntent();
		intent.body(null);
		assertThat(intent.body().toString(), is(""));
	}

	@Test
	public void testBuild() {
		final SmsIntent smsIntent = new SmsIntent();
		smsIntent.phoneNumber("02644569874");
		final Intent intent = smsIntent.build(mApplication);
		assertThat(intent, is(not(nullValue())));
		assertThat(intent.getAction(), is(Intent.ACTION_VIEW));
		assertThat(intent.getData(), is(Uri.parse("sms:02644569874")));
	}

	@Test
	public void testBuildWithBody() {
		final SmsIntent smsIntent = new SmsIntent();
		smsIntent.phoneNumber("02644569874");
		smsIntent.body("Sms body content.");
		final Intent intent = smsIntent.build(mApplication);
		assertThat(intent, is(not(nullValue())));
		assertThat(intent.getAction(), is(Intent.ACTION_VIEW));
		assertThat(intent.getData(), is(Uri.parse("sms:02644569874")));
		assertThat(intent.getStringExtra("sms_body"), is("Sms body content."));
	}

	@Test
	public void testBuildWithoutNumber() {
		assertThatBuildThrowsExceptionWithMessage(
				mApplication,
				new SmsIntent(),
				"No phone number specified."
		);
	}
}