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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import universum.studios.android.test.local.RobolectricTestCase;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static universum.studios.android.intent.ContactTests.assertThatBuildThrowsExceptionWithMessage;

/**
 * @author Martin Albedinsky
 */
public final class EmailIntentTest extends RobolectricTestCase {

	@Test public void testUriScheme() {
		// Assert:
		assertThat(EmailIntent.URI_SCHEME, is("mailto"));
	}

	@Test public void testInstantiation() {
		// Act:
		final EmailIntent intent = new EmailIntent();
		// Assert:
		assertThat(intent.addresses(), is(Collections.EMPTY_LIST));
		assertThat(intent.ccAddresses(), is(Collections.EMPTY_LIST));
		assertThat(intent.bccAddresses(), is(Collections.EMPTY_LIST));
		assertThat(intent.subject(), is((CharSequence) ""));
		assertThat(intent.message(), is((CharSequence) ""));
	}

	@Test public void testToSingle() {
		// Arrange:
		final EmailIntent intent = new EmailIntent();
		// Act:
		intent.to("test1@android.com");
		intent.to("test2@android.com");
		// Assert:
		final List<String> emailAddresses = intent.addresses();
		assertThat(emailAddresses.size(), is(2));
		assertThat(emailAddresses.get(0), is("test1@android.com"));
		assertThat(emailAddresses.get(1), is("test2@android.com"));
		assertThat(intent.ccAddresses(), is(Collections.EMPTY_LIST));
		assertThat(intent.bccAddresses(), is(Collections.EMPTY_LIST));
	}

	@Test public void testToMultiple() {
		// Arrange:
		final EmailIntent intent = new EmailIntent();
		// Act:
		intent.to("test1@android.com", "test2@android.com");
		intent.to("test3@android.com", "test4@android.com");
		// Assert:
		final List<String> emailAddresses = intent.addresses();
		assertThat(emailAddresses.size(), is(4));
		assertThat(emailAddresses.get(0), is("test1@android.com"));
		assertThat(emailAddresses.get(1), is("test2@android.com"));
		assertThat(emailAddresses.get(2), is("test3@android.com"));
		assertThat(emailAddresses.get(3), is("test4@android.com"));
	}

	@Test public void testToWithEmptyList() {
		// Arrange:
		final EmailIntent intent = new EmailIntent();
		// Act:
		intent.to(new ArrayList<String>(0));
		// Assert:
		assertThat(intent.addresses(), is(Collections.EMPTY_LIST));
	}

	@Test public void testToWithNullList() {
		// Arrange:
		final EmailIntent intent = new EmailIntent();
		// Act:
		intent.to("test1@android.com", "test2@android.com");
		intent.to((List<String>) null);
		// Assert:
		assertThat(intent.addresses(), is(Collections.EMPTY_LIST));
	}

	@Test public void testToWithWrongAddress() {
		// Arrange:
		final EmailIntent intent = new EmailIntent();
		// Act:
		intent.to("test@android");
		// Assert:
		assertThat(intent.addresses(), is(Collections.EMPTY_LIST));
	}

	@Test public void testCcSingle() {
		// Arrange:
		final EmailIntent intent = new EmailIntent();
		// Act:
		intent.cc("test1.cc@android.com");
		intent.cc("test2.cc@android.com");
		// Assert:
		final List<String> ccEmailAddresses = intent.ccAddresses();
		assertThat(ccEmailAddresses.size(), is(2));
		assertThat(ccEmailAddresses.get(0), is("test1.cc@android.com"));
		assertThat(ccEmailAddresses.get(1), is("test2.cc@android.com"));
		assertThat(intent.addresses(), is(Collections.EMPTY_LIST));
		assertThat(intent.bccAddresses(), is(Collections.EMPTY_LIST));
	}

	@Test public void testCcMultiple() {
		// Arrange:
		final EmailIntent intent = new EmailIntent();
		// Act:
		intent.cc("test.cc1@android.com", "test.cc2@android.com");
		intent.cc("test.cc3@android.com", "test.cc4@android.com");
		// Assert:
		final List<String> ccEmailAddresses = intent.ccAddresses();
		assertThat(ccEmailAddresses.size(), is(4));
		assertThat(ccEmailAddresses.get(0), is("test.cc1@android.com"));
		assertThat(ccEmailAddresses.get(1), is("test.cc2@android.com"));
		assertThat(ccEmailAddresses.get(2), is("test.cc3@android.com"));
		assertThat(ccEmailAddresses.get(3), is("test.cc4@android.com"));
	}

	@Test public void testCcWithEmptyList() {
		// Arrange:
		final EmailIntent intent = new EmailIntent();
		// Act:
		intent.cc(new ArrayList<String>(0));
		// Assert:
		assertThat(intent.ccAddresses(), is(Collections.EMPTY_LIST));
	}

	@Test public void testCcWithNullList() {
		// Arrange:
		final EmailIntent intent = new EmailIntent();
		// Act:
		intent.cc("test.cc1@android.com", "test.cc2@android.com");
		intent.cc((List<String>) null);
		// Assert:
		assertThat(intent.ccAddresses(), is(Collections.EMPTY_LIST));
	}

	@Test public void testBccSingle() {
		// Arrange:
		final EmailIntent intent = new EmailIntent();
		// Act:
		intent.bcc("test1.bcc@android.com");
		intent.bcc("test2.bcc@android.com");
		// Assert:
		final List<String> bccEmailAddresses = intent.bccAddresses();
		assertThat(bccEmailAddresses.size(), is(2));
		assertThat(bccEmailAddresses.get(0), is("test1.bcc@android.com"));
		assertThat(bccEmailAddresses.get(1), is("test2.bcc@android.com"));
		assertThat(intent.addresses(), is(Collections.EMPTY_LIST));
		assertThat(intent.ccAddresses(), is(Collections.EMPTY_LIST));
	}

	@Test public void testBccMultiple() {
		// Arrange:
		final EmailIntent intent = new EmailIntent();
		// Act:
		intent.bcc("test.bcc1@android.com", "test.bcc2@android.com");
		intent.bcc("test.bcc3@android.com", "test.bcc4@android.com");
		// Assert:
		final List<String> bccEmailAddresses = intent.bccAddresses();
		assertThat(bccEmailAddresses.size(), is(4));
		assertThat(bccEmailAddresses.get(0), is("test.bcc1@android.com"));
		assertThat(bccEmailAddresses.get(1), is("test.bcc2@android.com"));
		assertThat(bccEmailAddresses.get(2), is("test.bcc3@android.com"));
		assertThat(bccEmailAddresses.get(3), is("test.bcc4@android.com"));
	}

	@Test public void testBccWithEmptyList() {
		// Arrange:
		final EmailIntent intent = new EmailIntent();
		// Act:
		intent.bcc(new ArrayList<String>(0));
		// Assert:
		assertThat(intent.bccAddresses(), is(Collections.EMPTY_LIST));
	}

	@Test public void testBccWithNullList() {
		// Arrange:
		final EmailIntent intent = new EmailIntent();
		// Act:
		intent.bcc("test.bcc1@android.com", "test.bcc2@android.com");
		intent.bcc((List<String>) null);
		// Assert:
		assertThat(intent.bccAddresses(), is(Collections.EMPTY_LIST));
	}

	@Test public void testSubject() {
		// Arrange:
		final EmailIntent intent = new EmailIntent();
		// Act:
		intent.subject("Email subject");
		// Assert:
		assertThat(intent.subject().toString(), is("Email subject"));
	}

	@Test public void testMessage() {
		// Arrange:
		final EmailIntent intent = new EmailIntent();
		// Act:
		intent.message("Email message.");
		// Assert:
		assertThat(intent.message().toString(), is("Email message."));
	}

	@Test public void testBuild() {
		// Arrange:
		final EmailIntent emailIntent = new EmailIntent();
		emailIntent.to("test@android.com");
		emailIntent.subject("Email subject");
		emailIntent.message("Email message.");
		// Act:
		final Intent intent = emailIntent.build(context);
		// Assert:
		assertThat(intent, is(notNullValue()));
		assertThat(intent.getAction(), is(Intent.ACTION_SENDTO));
		assertThat(
				intent.getData(),
				is(Uri.fromParts(
						"mailto",
						"test@android.com",
						null
				))
		);
		assertThat(intent.getCharSequenceExtra(Intent.EXTRA_SUBJECT).toString(), is("Email subject"));
		assertThat(intent.getCharSequenceExtra(Intent.EXTRA_TEXT).toString(), is("Email message."));
	}

	@Test public void testBuildWithCcAddresses() {
		// Arrange:
		final EmailIntent emailIntent = new EmailIntent();
		emailIntent.to("test@android.com");
		emailIntent.cc("test.cc1@android.com", "test.cc2@android.com");
		// Act:
		final Intent intent = emailIntent.build(context);
		// Assert:
		final String[] ccAddresses = intent.getStringArrayExtra(Intent.EXTRA_CC);
		assertThat(ccAddresses, is(notNullValue()));
		assertThat(ccAddresses.length, is(2));
		assertThat(ccAddresses[0], is("test.cc1@android.com"));
		assertThat(ccAddresses[1], is("test.cc2@android.com"));
		assertThat(intent.getStringArrayExtra(Intent.EXTRA_BCC), is(nullValue()));
	}

	@Test public void testBuildWithBccAddresses() {
		// Arrange:
		final EmailIntent emailIntent = new EmailIntent();
		emailIntent.to("test@android.com");
		emailIntent.bcc("test.bcc@android.com");
		// Act:
		final Intent intent = emailIntent.build(context);
		// Assert:
		final String[] bccAddresses = intent.getStringArrayExtra(Intent.EXTRA_BCC);
		assertThat(bccAddresses, is(notNullValue()));
		assertThat(bccAddresses.length, is(1));
		assertThat(bccAddresses[0], is("test.bcc@android.com"));
		assertThat(intent.getStringArrayExtra(Intent.EXTRA_CC), is(nullValue()));
	}

	@Test public void testBuildWithoutAddresses() {
		assertThatBuildThrowsExceptionWithMessage(
				context,
				new EmailIntent(),
				"No e-mail address/-es specified."
		);
	}

	@SuppressWarnings("ConstantConditions")
	@Test public void testCreateUriForSingleAddress() {
		// Arrange:
		final List<String> addresses = new ArrayList<>(1);
		addresses.add("test@android.com");
		// Act:
		final Uri uri = EmailIntent.createUri(addresses);
		// Assert:
		assertThat(uri, is(notNullValue()));
		assertThat(uri.getScheme(), is(EmailIntent.URI_SCHEME));
		assertThat(
				uri,
				is(Uri.fromParts(
						"mailto",
						"test@android.com",
						null
				))
		);
	}

	@SuppressWarnings("ConstantConditions")
	@Test public void testCreateUriForMultipleAddresses() {
		// Arrange:
		final List<String> addresses = new ArrayList<>(2);
		addresses.add("test1@android.com");
		addresses.add("test2@android.com");
		// Act:
		final Uri uri = EmailIntent.createUri(addresses);
		// Assert:
		assertThat(uri, is(notNullValue()));
		assertThat(uri.getScheme(), is("mailto"));
		assertThat(
				uri,
				is(Uri.fromParts(
						"mailto",
						"test1@android.com,test2@android.com",
						null
				))
		);
	}

	@Test public void testCreateUriForEmptyAddresses() {
		// Act + Assert:
		assertThat(EmailIntent.createUri(new ArrayList<String>(0)), is(nullValue()));
	}

	@Test public void testOnStartWith() {
		// Arrange:
		final EmailIntent emailIntent = new EmailIntent();
		emailIntent.to("test1@android.com");
		final Intent intent = emailIntent.build(context);
		final IntentStarter mockIntentStarter = mock(IntentStarter.class);
		// Act:
		emailIntent.onStartWith(mockIntentStarter, intent);
		// Assert:
		verify(mockIntentStarter).startIntent(any(Intent.class));
	}
}