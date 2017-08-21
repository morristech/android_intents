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

import org.junit.Test;
import org.junit.runner.RunWith;

import universum.studios.android.test.instrumented.InstrumentedTestCase;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsNot.not;
import static universum.studios.android.intent.ContactTests.assertThatBuildThrowsExceptionWithMessage;

/**
 * @author Martin Albedinsky
 */
@RunWith(AndroidJUnit4.class)
public final class DialerIntentTest extends InstrumentedTestCase {

	@SuppressWarnings("unused")
	private static final String TAG = "DialerIntentTest";

	@Test
	public void testUriScheme() {
		assertThat(DialerIntent.URI_SCHEME, is("tel"));
	}

	@Test
	public void testPhoneNumberDefault() {
		assertThat(new DialerIntent().phoneNumber(), is(""));
	}

	@Test
	public void testPhoneNumber() {
		final DialerIntent intent = new DialerIntent();
		intent.phoneNumber("00124456");
		assertThat(intent.phoneNumber(), is("00124456"));
	}

	@Test
	public void testBuild() {
		final DialerIntent dialerIntent = new DialerIntent();
		dialerIntent.phoneNumber("02644569874");
		final Intent intent = dialerIntent.build(mContext);
		assertThat(intent, is(not(nullValue())));
		assertThat(intent.getAction(), is(Intent.ACTION_DIAL));
		assertThat(intent.getData(), is(Uri.parse("tel:02644569874")));
	}

	@Test
	public void testBuildWithoutNumber() {
		assertThatBuildThrowsExceptionWithMessage(
				mContext,
				new DialerIntent(),
				"No phone number specified."
		);
	}
}
