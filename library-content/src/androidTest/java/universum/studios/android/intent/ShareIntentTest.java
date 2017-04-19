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

import java.util.Collections;
import java.util.List;

import universum.studios.android.test.BaseInstrumentedTest;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsNot.not;
import static org.hamcrest.core.IsNull.nullValue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static universum.studios.android.intent.ContentTests.assertThatBuildThrowsExceptionWithMessage;

/**
 * @author Martin Albedinsky
 */
@RunWith(AndroidJUnit4.class)
@SuppressWarnings("ResourceType")
public final class ShareIntentTest extends BaseInstrumentedTest {

	@SuppressWarnings("unused")
	private static final String TAG = "ShareIntentTest";

	private ShareIntent mIntent;

	@Override
	public void beforeTest() throws Exception {
		super.beforeTest();
		this.mIntent = new ShareIntent();
	}

	@Override
	public void afterTest() throws Exception {
		super.afterTest();
		this.mIntent = null;
	}

	@Test
	public void testDefaultContent() {
		assertThat(mIntent.content(), is(not(nullValue())));
		assertThat(mIntent.content().length(), is(0));
	}

	@Test
	public void testContent() {
		mIntent.content("Sharing content.");
		assertThat(mIntent.content().toString(), is("Sharing content."));
	}

	@Test
	public void testDefaultUri() {
		assertThat(mIntent.uri(), is(nullValue()));
	}

	@Test
	public void testUri() {
		mIntent.uri(Uri.parse("content://android/data/images/leopard.png"));
		assertThat(mIntent.uri(), is(Uri.parse("content://android/data/images/leopard.png")));
	}

	@Test
	public void testDefaultUris() {
		assertThat(mIntent.uris(), is(Collections.EMPTY_LIST));
	}

	@Test
	public void testUris() {
		mIntent.uris(
				Uri.parse("content://android/data/video/nature.avi"),
				Uri.parse("content://android/data/audio/ocean.mp3")
		);

		final List<Uri> uris = mIntent.uris();
		assertThat(uris, is(not(nullValue())));
		assertThat(uris.size(), is(2));
		assertThat(uris.get(0), is(Uri.parse("content://android/data/video/nature.avi")));
		assertThat(uris.get(1), is(Uri.parse("content://android/data/audio/ocean.mp3")));
	}

	@Test
	public void testDefaultTitle() {
		assertThat(mIntent.title(), is(not(nullValue())));
		assertThat(mIntent.title().length(), is(0));
	}

	@Test
	public void testTitle() {
		mIntent.title("Title");
		assertThat(mIntent.title().toString(), is("Title"));
	}

	@Test
	public void testDefaultMimeType() {
		assertThat(mIntent.mimeType(), is(MimeType.TEXT));
	}

	@Test
	public void testMimeType() {
		mIntent.mimeType(MimeType.AUDIO);
		assertThat(mIntent.mimeType(), is(MimeType.AUDIO));
	}

	@Test
	public void testBuildWithTextContent() {
		mIntent.content("Text to share.");
		final Intent intent = mIntent.build(mContext);
		assertThat(intent.getAction(), is(Intent.ACTION_SEND));
		assertThat(intent.getData(), is(nullValue()));
		assertThat(intent.getStringExtra(Intent.EXTRA_TEXT), is("Text to share."));
	}

	@Test
	public void testBuildWithUriContent() {
		mIntent.uri(Uri.parse("content://android/data/images/lion.png"));
		mIntent.title("Beautiful Lion");
		final Intent intent = mIntent.build(mContext);
		assertThat(intent.getAction(), is(Intent.ACTION_SEND));
		assertThat(intent.getCharSequenceExtra(Intent.EXTRA_TITLE).toString(), is("Beautiful Lion"));
		assertThat(intent.getCharSequenceExtra(Intent.EXTRA_TEXT), is(nullValue()));
		assertThat(intent.<Uri>getParcelableExtra(Intent.EXTRA_STREAM), is(Uri.parse("content://android/data/images/lion.png")));
	}

	@Test
	public void testBuildWithUrisContent() {
		mIntent.uris(
				Uri.parse("content://android/data/images/lion.png"),
				Uri.parse("content://android/data/images/elephant.png")
		);
		final Intent intent = mIntent.build(mContext);
		assertThat(intent.getAction(), is(Intent.ACTION_SEND_MULTIPLE));
		assertThat(intent.getCharSequenceExtra(Intent.EXTRA_TITLE), is(nullValue()));
		assertThat(intent.getCharSequenceExtra(Intent.EXTRA_TEXT), is(nullValue()));
		final List<Uri> uris = intent.getParcelableArrayListExtra(Intent.EXTRA_STREAM);
		assertThat(uris, is(not(nullValue())));
		assertThat(uris.size(), is(2));
		assertThat(uris.get(0), is(Uri.parse("content://android/data/images/lion.png")));
		assertThat(uris.get(1), is(Uri.parse("content://android/data/images/elephant.png")));
	}

	@Test
	public void testBuildWithoutContent() {
		assertThatBuildThrowsExceptionWithMessage(
				mContext,
				mIntent,
				"No content to share specified."
		);
	}

	@Test
	public void testBuildWithoutMimeType() {
		mIntent.content("Content to share");
		mIntent.mimeType("");
		assertThatBuildThrowsExceptionWithMessage(
				mContext,
				mIntent,
				"No content's MIME type specified."
		);
	}

	@Test
	public void testOnStartWith() {
		mIntent.content("Text to share.");
		final Intent intent = mIntent.build(mContext);
		final IntentStarter mockStarter = mock(IntentStarter.class);
		mIntent.onStartWith(mockStarter, intent);
		verify(mockStarter, times(1)).startIntent(any(Intent.class));
	}
}
