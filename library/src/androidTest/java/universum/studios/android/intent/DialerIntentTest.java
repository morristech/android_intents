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

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsNot.not;

/**
 * @author Martin Albedinsky
 */
@RunWith(AndroidJUnit4.class)
public final class DialerIntentTest extends IntentBaseTest<DialerIntent> {

	@SuppressWarnings("unused")
	private static final String TAG = "DialerIntentTest";

	public DialerIntentTest() {
		super(DialerIntent.class);
	}

	@Test
	public void testUriScheme() {
		assertThat(DialerIntent.URI_SCHEME, is("tel"));
	}

	@Test
	public void testDefaultPhoneNumber() {
		assertThat(mIntent.phoneNumber(), is(not(nullValue())));
		assertThat(mIntent.phoneNumber().length(), is(0));
	}

	@Test
	public void testPhoneNumberText() {
		mIntent.phoneNumber("00124456");
		assertThat(mIntent.phoneNumber(), is("00124456"));
	}

	@Test
	public void testBuild() {
		mIntent.phoneNumber("02644569874");
		final Intent intent = mIntent.build(mContext);
		assertThat(intent, is(not(nullValue())));
		assertThat(intent.getAction(), is(Intent.ACTION_DIAL));
		assertThat(intent.getData(), is(Uri.parse("tel:02644569874")));
	}

	@Test
	public void testBuildWithoutNumber() {
		assertThatBuildThrowsExceptionWithCause(
				mIntent,
				"No phone number specified."
		);
	}
}