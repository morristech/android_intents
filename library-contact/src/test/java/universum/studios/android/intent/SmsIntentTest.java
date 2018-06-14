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

	@Test public void testUriScheme() {
		// Assert:
		assertThat(SmsIntent.URI_SCHEME, is("sms"));
	}

	@Test public void testInstantiation() {
		// Act:
		final SmsIntent intent = new SmsIntent();
		// Assert:
		assertThat(intent.phoneNumber(), is(""));
		assertThat(intent.body(), is((CharSequence) ""));
	}

	@Test public void testPhoneNumber() {
		// Arrange:
		final SmsIntent intent = new SmsIntent();
		// Act:
		intent.phoneNumber("00124456");
		// Assert:
		assertThat(intent.phoneNumber(), is("00124456"));
	}

	@Test public void testBody() {
		// Arrange:
		final SmsIntent intent = new SmsIntent();
		// Act:
		intent.body("Sms body.");
		// Assert:
		assertThat(intent.body().toString(), is("Sms body."));
	}

	@Test public void testNullBody() {
		// Arrange:
		final SmsIntent intent = new SmsIntent();
		// Act:
		intent.body(null);
		// Assert:
		assertThat(intent.body().toString(), is(""));
	}

	@Test public void testBuild() {
		// Arrange:
		final SmsIntent smsIntent = new SmsIntent();
		smsIntent.phoneNumber("02644569874");
		// Act:
		final Intent intent = smsIntent.build(application);
		// Assert:
		assertThat(intent, is(not(nullValue())));
		assertThat(intent.getAction(), is(Intent.ACTION_VIEW));
		assertThat(intent.getData(), is(Uri.parse("sms:02644569874")));
	}

	@Test public void testBuildWithBody() {
		// Arrange:
		final SmsIntent smsIntent = new SmsIntent();
		smsIntent.phoneNumber("02644569874");
		smsIntent.body("Sms body content.");
		// Act:
		final Intent intent = smsIntent.build(application);
		// Assert:
		assertThat(intent, is(not(nullValue())));
		assertThat(intent.getAction(), is(Intent.ACTION_VIEW));
		assertThat(intent.getData(), is(Uri.parse("sms:02644569874")));
		assertThat(intent.getStringExtra("sms_body"), is("Sms body content."));
	}

	@Test public void testBuildWithoutNumber() {
		assertThatBuildThrowsExceptionWithMessage(
				application,
				new SmsIntent(),
				"No phone number specified."
		);
	}
}