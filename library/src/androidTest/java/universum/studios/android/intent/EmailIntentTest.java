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

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsNot.not;

/**
 * @author Martin Albedinsky
 */
@RunWith(AndroidJUnit4.class)
public final class EmailIntentTest extends IntentBaseTest<EmailIntent> {

	@SuppressWarnings("unused")
	private static final String TAG = "EmailIntentTest";

	public EmailIntentTest() {
		super(EmailIntent.class);
	}

	@Test
	public void testUriScheme() {
		assertThat(EmailIntent.URI_SCHEME, is("mailto"));
	}

	@Test
	public void testDefaultAddresses() {
		assertThat(mIntent.addresses(), is(Collections.EMPTY_LIST));
	}

	@Test
	public void testToSingle() {
		mIntent.to("test1@android.com");
		mIntent.to("test2@android.com");
		final List<String> emailAddresses = mIntent.addresses();
		assertThat(emailAddresses.size(), is(2));
		assertThat(emailAddresses.get(0), is("test1@android.com"));
		assertThat(emailAddresses.get(1), is("test2@android.com"));
		assertThat(mIntent.ccAddresses(), is(Collections.EMPTY_LIST));
		assertThat(mIntent.bccAddresses(), is(Collections.EMPTY_LIST));
	}

	@Test
	public void testToMultiple() {
		mIntent.to("test1@android.com", "test2@android.com", "test3@android.com");
		final List<String> emailAddresses = mIntent.addresses();
		assertThat(emailAddresses.size(), is(3));
		assertThat(emailAddresses.get(0), is("test1@android.com"));
		assertThat(emailAddresses.get(1), is("test2@android.com"));
		assertThat(emailAddresses.get(2), is("test3@android.com"));
	}

	@Test
	public void testToWithEmptyList() {
		mIntent.to(new ArrayList<String>(0));
		assertThat(mIntent.addresses(), is(Collections.EMPTY_LIST));
	}

	@Test
	public void testToWithWrongAddress() {
		mIntent.to("test@android");
		assertThat(mIntent.addresses(), is(Collections.EMPTY_LIST));
	}

	@Test
	public void testDefaultCcAddresses() {
		assertThat(mIntent.ccAddresses(), is(Collections.EMPTY_LIST));
	}

	@Test
	public void testCcSingle() {
		mIntent.cc("test1.cc@android.com");
		mIntent.cc("test2.cc@android.com");
		final List<String> ccEmailAddresses = mIntent.ccAddresses();
		assertThat(ccEmailAddresses.size(), is(2));
		assertThat(ccEmailAddresses.get(0), is("test1.cc@android.com"));
		assertThat(ccEmailAddresses.get(1), is("test2.cc@android.com"));
		assertThat(mIntent.addresses(), is(Collections.EMPTY_LIST));
		assertThat(mIntent.bccAddresses(), is(Collections.EMPTY_LIST));
	}

	@Test
	public void testCcMultiple() {
		mIntent.cc("test.cc1@android.com", "test.cc2@android.com");
		final List<String> ccEmailAddresses = mIntent.ccAddresses();
		assertThat(ccEmailAddresses.size(), is(2));
		assertThat(ccEmailAddresses.get(0), is("test.cc1@android.com"));
		assertThat(ccEmailAddresses.get(1), is("test.cc2@android.com"));
	}

	@Test
	public void testCcWithEmptyList() {
		mIntent.cc(new ArrayList<String>(0));
		assertThat(mIntent.ccAddresses(), is(Collections.EMPTY_LIST));
	}

	@Test
	public void testDefaultBccAddresses() {
		assertThat(mIntent.bccAddresses(), is(Collections.EMPTY_LIST));
	}

	@Test
	public void testBccSingle() {
		mIntent.bcc("test1.bcc@android.com");
		mIntent.bcc("test2.bcc@android.com");
		final List<String> bccEmailAddresses = mIntent.bccAddresses();
		assertThat(bccEmailAddresses.size(), is(2));
		assertThat(bccEmailAddresses.get(0), is("test1.bcc@android.com"));
		assertThat(bccEmailAddresses.get(1), is("test2.bcc@android.com"));
		assertThat(mIntent.addresses(), is(Collections.EMPTY_LIST));
		assertThat(mIntent.ccAddresses(), is(Collections.EMPTY_LIST));
	}

	@Test
	public void testBccMultiple() {
		mIntent.bcc("test.bcc1@android.com", "test.bcc2@android.com", "test.bcc3@android.com", "test.bcc4@android.com");
		final List<String> bccEmailAddresses = mIntent.bccAddresses();
		assertThat(bccEmailAddresses.size(), is(4));
		assertThat(bccEmailAddresses.get(0), is("test.bcc1@android.com"));
		assertThat(bccEmailAddresses.get(1), is("test.bcc2@android.com"));
		assertThat(bccEmailAddresses.get(2), is("test.bcc3@android.com"));
		assertThat(bccEmailAddresses.get(3), is("test.bcc4@android.com"));
	}

	@Test
	public void testBccWithEmptyList() {
		mIntent.bcc(new ArrayList<String>(0));
		assertThat(mIntent.bccAddresses(), is(Collections.EMPTY_LIST));
	}

	@Test
	public void testDefaultSubject() {
		assertThat(mIntent.subject(), is(not(nullValue())));
		assertThat(mIntent.subject().length(), is(0));
	}

	@Test
	public void testSubject() {
		mIntent.subject("Email subject");
		assertThat(mIntent.subject().toString(), is("Email subject"));
	}

	@Test
	public void testDefaultMessage() {
		assertThat(mIntent.message(), is(not(nullValue())));
		assertThat(mIntent.message().length(), is(0));
	}

	@Test
	public void testMessage() {
		mIntent.message("Email message.");
		assertThat(mIntent.message().toString(), is("Email message."));
	}

	@Test
	public void testBuild() {
		mIntent.to("test@android.com");
		mIntent.subject("Email subject");
		mIntent.message("Email message.");
		final Intent intent = mIntent.build(mContext);
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
		mIntent.to("test@android.com");
		mIntent.cc("test.cc1@android.com", "test.cc2@android.com");
		final Intent intent = mIntent.build(mContext);
		final String[] ccAddresses = intent.getStringArrayExtra(Intent.EXTRA_CC);
		assertThat(ccAddresses, is(not(nullValue())));
		assertThat(ccAddresses.length, is(2));
		assertThat(ccAddresses[0], is("test.cc1@android.com"));
		assertThat(ccAddresses[1], is("test.cc2@android.com"));
		assertThat(intent.getStringArrayExtra(Intent.EXTRA_BCC), is(nullValue()));
	}

	@Test
	public void testBuildWithBccAddresses() {
		mIntent.to("test@android.com");
		mIntent.bcc("test.bcc@android.com");
		final Intent intent = mIntent.build(mContext);
		final String[] bccAddresses = intent.getStringArrayExtra(Intent.EXTRA_BCC);
		assertThat(bccAddresses, is(not(nullValue())));
		assertThat(bccAddresses.length, is(1));
		assertThat(bccAddresses[0], is("test.bcc@android.com"));
		assertThat(intent.getStringArrayExtra(Intent.EXTRA_CC), is(nullValue()));
	}

	@Test
	public void testBuildWithoutAddresses() {
		assertThatBuildThrowsExceptionWithCause(
				mIntent,
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
}