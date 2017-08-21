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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import universum.studios.android.test.instrumented.InstrumentedTestCase;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsNot.not;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static universum.studios.android.intent.ContactTests.assertThatBuildThrowsExceptionWithMessage;

/**
 * @author Martin Albedinsky
 */
@RunWith(AndroidJUnit4.class)
public final class EmailIntentTest extends InstrumentedTestCase {

	@SuppressWarnings("unused")
	private static final String TAG = "EmailIntentTest";

	@Test
	public void testUriScheme() {
		assertThat(EmailIntent.URI_SCHEME, is("mailto"));
	}

	@Test
	public void testAddressesDefault() {
		assertThat(new EmailIntent().addresses(), is(Collections.EMPTY_LIST));
	}

	@Test
	public void testToSingle() {
		final EmailIntent intent = new EmailIntent();
		intent.to("test1@android.com");
		intent.to("test2@android.com");
		final List<String> emailAddresses = intent.addresses();
		assertThat(emailAddresses.size(), is(2));
		assertThat(emailAddresses.get(0), is("test1@android.com"));
		assertThat(emailAddresses.get(1), is("test2@android.com"));
		assertThat(intent.ccAddresses(), is(Collections.EMPTY_LIST));
		assertThat(intent.bccAddresses(), is(Collections.EMPTY_LIST));
	}

	@Test
	public void testToMultiple() {
		final EmailIntent intent = new EmailIntent();
		intent.to("test1@android.com", "test2@android.com");
		intent.to("test3@android.com", "test4@android.com");
		final List<String> emailAddresses = intent.addresses();
		assertThat(emailAddresses.size(), is(4));
		assertThat(emailAddresses.get(0), is("test1@android.com"));
		assertThat(emailAddresses.get(1), is("test2@android.com"));
		assertThat(emailAddresses.get(2), is("test3@android.com"));
		assertThat(emailAddresses.get(3), is("test4@android.com"));
	}

	@Test
	public void testToWithEmptyList() {
		final EmailIntent intent = new EmailIntent();
		intent.to(new ArrayList<String>(0));
		assertThat(intent.addresses(), is(Collections.EMPTY_LIST));
	}

	@Test
	public void testToWithNullList() {
		final EmailIntent intent = new EmailIntent();
		intent.to("test1@android.com", "test2@android.com");
		intent.to((List<String>) null);
		assertThat(intent.addresses(), is(Collections.EMPTY_LIST));
	}

	@Test
	public void testToWithWrongAddress() {
		final EmailIntent intent = new EmailIntent();
		intent.to("test@android");
		assertThat(intent.addresses(), is(Collections.EMPTY_LIST));
	}

	@Test
	public void testCcAddressesDefault() {
		assertThat(new EmailIntent().ccAddresses(), is(Collections.EMPTY_LIST));
	}

	@Test
	public void testCcSingle() {
		final EmailIntent intent = new EmailIntent();
		intent.cc("test1.cc@android.com");
		intent.cc("test2.cc@android.com");
		final List<String> ccEmailAddresses = intent.ccAddresses();
		assertThat(ccEmailAddresses.size(), is(2));
		assertThat(ccEmailAddresses.get(0), is("test1.cc@android.com"));
		assertThat(ccEmailAddresses.get(1), is("test2.cc@android.com"));
		assertThat(intent.addresses(), is(Collections.EMPTY_LIST));
		assertThat(intent.bccAddresses(), is(Collections.EMPTY_LIST));
	}

	@Test
	public void testCcMultiple() {
		final EmailIntent intent = new EmailIntent();
		intent.cc("test.cc1@android.com", "test.cc2@android.com");
		intent.cc("test.cc3@android.com", "test.cc4@android.com");
		final List<String> ccEmailAddresses = intent.ccAddresses();
		assertThat(ccEmailAddresses.size(), is(4));
		assertThat(ccEmailAddresses.get(0), is("test.cc1@android.com"));
		assertThat(ccEmailAddresses.get(1), is("test.cc2@android.com"));
		assertThat(ccEmailAddresses.get(2), is("test.cc3@android.com"));
		assertThat(ccEmailAddresses.get(3), is("test.cc4@android.com"));
	}

	@Test
	public void testCcWithEmptyList() {
		final EmailIntent intent = new EmailIntent();
		intent.cc(new ArrayList<String>(0));
		assertThat(intent.ccAddresses(), is(Collections.EMPTY_LIST));
	}

	@Test
	public void testCcWithNullList() {
		final EmailIntent intent = new EmailIntent();
		intent.cc("test.cc1@android.com", "test.cc2@android.com");
		intent.cc((List<String>) null);
		assertThat(intent.ccAddresses(), is(Collections.EMPTY_LIST));
	}

	@Test
	public void testBccAddressesDefault() {
		assertThat(new EmailIntent().bccAddresses(), is(Collections.EMPTY_LIST));
	}

	@Test
	public void testBccSingle() {
		final EmailIntent intent = new EmailIntent();
		intent.bcc("test1.bcc@android.com");
		intent.bcc("test2.bcc@android.com");
		final List<String> bccEmailAddresses = intent.bccAddresses();
		assertThat(bccEmailAddresses.size(), is(2));
		assertThat(bccEmailAddresses.get(0), is("test1.bcc@android.com"));
		assertThat(bccEmailAddresses.get(1), is("test2.bcc@android.com"));
		assertThat(intent.addresses(), is(Collections.EMPTY_LIST));
		assertThat(intent.ccAddresses(), is(Collections.EMPTY_LIST));
	}

	@Test
	public void testBccMultiple() {
		final EmailIntent intent = new EmailIntent();
		intent.bcc("test.bcc1@android.com", "test.bcc2@android.com");
		intent.bcc("test.bcc3@android.com", "test.bcc4@android.com");
		final List<String> bccEmailAddresses = intent.bccAddresses();
		assertThat(bccEmailAddresses.size(), is(4));
		assertThat(bccEmailAddresses.get(0), is("test.bcc1@android.com"));
		assertThat(bccEmailAddresses.get(1), is("test.bcc2@android.com"));
		assertThat(bccEmailAddresses.get(2), is("test.bcc3@android.com"));
		assertThat(bccEmailAddresses.get(3), is("test.bcc4@android.com"));
	}

	@Test
	public void testBccWithEmptyList() {
		final EmailIntent intent = new EmailIntent();
		intent.bcc(new ArrayList<String>(0));
		assertThat(intent.bccAddresses(), is(Collections.EMPTY_LIST));
	}

	@Test
	public void testBccWithNullList() {
		final EmailIntent intent = new EmailIntent();
		intent.bcc("test.bcc1@android.com", "test.bcc2@android.com");
		intent.bcc((List<String>) null);
		assertThat(intent.bccAddresses(), is(Collections.EMPTY_LIST));
	}

	@Test
	public void testSubjectDefault() {
		final EmailIntent intent = new EmailIntent();
		assertThat(intent.subject(), is(not(nullValue())));
		assertThat(intent.subject().length(), is(0));
	}

	@Test
	public void testSubject() {
		final EmailIntent intent = new EmailIntent();
		intent.subject("Email subject");
		assertThat(intent.subject().toString(), is("Email subject"));
	}

	@Test
	public void testMessageDefault() {
		final EmailIntent intent = new EmailIntent();
		assertThat(intent.message(), is(not(nullValue())));
		assertThat(intent.message().length(), is(0));
	}

	@Test
	public void testMessage() {
		final EmailIntent intent = new EmailIntent();
		intent.message("Email message.");
		assertThat(intent.message().toString(), is("Email message."));
	}

	@Test
	public void testBuild() {
		final EmailIntent emailIntent = new EmailIntent();
		emailIntent.to("test@android.com");
		emailIntent.subject("Email subject");
		emailIntent.message("Email message.");
		final Intent intent = emailIntent.build(mContext);
		assertThat(intent, is(not(nullValue())));
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

	@Test
	public void testBuildWithCcAddresses() {
		final EmailIntent emailIntent = new EmailIntent();
		emailIntent.to("test@android.com");
		emailIntent.cc("test.cc1@android.com", "test.cc2@android.com");
		final Intent intent = emailIntent.build(mContext);
		final String[] ccAddresses = intent.getStringArrayExtra(Intent.EXTRA_CC);
		assertThat(ccAddresses, is(not(nullValue())));
		assertThat(ccAddresses.length, is(2));
		assertThat(ccAddresses[0], is("test.cc1@android.com"));
		assertThat(ccAddresses[1], is("test.cc2@android.com"));
		assertThat(intent.getStringArrayExtra(Intent.EXTRA_BCC), is(nullValue()));
	}

	@Test
	public void testBuildWithBccAddresses() {
		final EmailIntent emailIntent = new EmailIntent();
		emailIntent.to("test@android.com");
		emailIntent.bcc("test.bcc@android.com");
		final Intent intent = emailIntent.build(mContext);
		final String[] bccAddresses = intent.getStringArrayExtra(Intent.EXTRA_BCC);
		assertThat(bccAddresses, is(not(nullValue())));
		assertThat(bccAddresses.length, is(1));
		assertThat(bccAddresses[0], is("test.bcc@android.com"));
		assertThat(intent.getStringArrayExtra(Intent.EXTRA_CC), is(nullValue()));
	}

	@Test
	public void testBuildWithoutAddresses() {
		assertThatBuildThrowsExceptionWithMessage(
				mContext,
				new EmailIntent(),
				"No e-mail address/-es specified."
		);
	}

	@Test
	@SuppressWarnings("ConstantConditions")
	public void testCreateUriForSingleAddress() {
		final List<String> addresses = new ArrayList<>(1);
		addresses.add("test@android.com");
		final Uri uri = EmailIntent.createUri(addresses);
		assertThat(uri, is(not(nullValue())));
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

	@Test
	@SuppressWarnings("ConstantConditions")
	public void testCreateUriForMultipleAddresses() {
		final List<String> addresses = new ArrayList<>(2);
		addresses.add("test1@android.com");
		addresses.add("test2@android.com");
		final Uri uri = EmailIntent.createUri(addresses);
		assertThat(uri, is(not(nullValue())));
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

	@Test
	public void testCreateUriForEmptyAddresses() {
		assertThat(EmailIntent.createUri(new ArrayList<String>(0)), is(nullValue()));
	}

	@Test
	public void testOnStartWith() {
		final EmailIntent emailIntent = new EmailIntent();
		emailIntent.to("test1@android.com");
		final Intent intent = emailIntent.build(mContext);
		final IntentStarter mockIntentStarter = mock(IntentStarter.class);
		emailIntent.onStartWith(mockIntentStarter, intent);
		verify(mockIntentStarter, times(1)).startIntent(any(Intent.class));
	}
}
