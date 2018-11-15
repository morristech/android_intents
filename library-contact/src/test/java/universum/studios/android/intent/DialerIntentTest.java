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
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static universum.studios.android.intent.ContactTests.assertThatBuildThrowsExceptionWithMessage;

/**
 * @author Martin Albedinsky
 */
public final class DialerIntentTest extends RobolectricTestCase {

	@Test public void testUriScheme() {
		// Assert:
		assertThat(DialerIntent.URI_SCHEME, is("tel"));
	}

	@Test public void testInstantiation() {
		// Act:
		final DialerIntent intent = new DialerIntent();
		// Assert:
		assertThat(intent.phoneNumber(), is(""));
	}

	@Test public void testPhoneNumber() {
		// Arrange:
		final DialerIntent intent = new DialerIntent();
		// Act:
		intent.phoneNumber("00124456");
		// Assert:
		assertThat(intent.phoneNumber(), is("00124456"));
	}

	@Test public void testBuild() {
		// Arrange:
		final DialerIntent dialerIntent = new DialerIntent();
		dialerIntent.phoneNumber("02644569874");
		// Act:
		final Intent intent = dialerIntent.build(context);
		// Assert:
		assertThat(intent, is(notNullValue()));
		assertThat(intent.getAction(), is(Intent.ACTION_DIAL));
		assertThat(intent.getData(), is(Uri.parse("tel:02644569874")));
	}

	@Test public void testBuildWithoutNumber() {
		assertThatBuildThrowsExceptionWithMessage(
				context,
				new DialerIntent(),
				"No phone number specified."
		);
	}
}