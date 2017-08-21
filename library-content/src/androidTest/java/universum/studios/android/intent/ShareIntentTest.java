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

import universum.studios.android.test.instrumented.InstrumentedTestCase;

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
public final class ShareIntentTest extends InstrumentedTestCase {

	@SuppressWarnings("unused")
	private static final String TAG = "ShareIntentTest";

	@Test
	public void testContentDefault() {
		final ShareIntent intent = new ShareIntent();
		assertThat(intent.content(), is(not(nullValue())));
		assertThat(intent.content().length(), is(0));
	}

	@Test
	public void testContent() {
		final ShareIntent intent = new ShareIntent();
		intent.content("Sharing content.");
		assertThat(intent.content().toString(), is("Sharing content."));
	}

	@Test
	public void testUriDefault() {
		assertThat(new ShareIntent().uri(), is(nullValue()));
	}

	@Test
	public void testUri() {
		final ShareIntent intent = new ShareIntent();
		intent.uri(Uri.parse("content://android/data/images/leopard.png"));
		assertThat(intent.uri(), is(Uri.parse("content://android/data/images/leopard.png")));
	}

	@Test
	public void testUrisDefault() {
		assertThat(new ShareIntent().uris(), is(Collections.EMPTY_LIST));
	}

	@Test
	public void testUris() {
		final ShareIntent intent = new ShareIntent();
		intent.uris(
				Uri.parse("content://android/data/video/nature.avi"),
				Uri.parse("content://android/data/audio/ocean.mp3")
		);
		final List<Uri> uris = intent.uris();
		assertThat(uris, is(not(nullValue())));
		assertThat(uris.size(), is(2));
		assertThat(uris.get(0), is(Uri.parse("content://android/data/video/nature.avi")));
		assertThat(uris.get(1), is(Uri.parse("content://android/data/audio/ocean.mp3")));
	}

	@Test
	public void testTitleDefault() {
		final ShareIntent intent = new ShareIntent();
		assertThat(intent.title(), is(not(nullValue())));
		assertThat(intent.title().length(), is(0));
	}

	@Test
	public void testTitle() {
		final ShareIntent intent = new ShareIntent();
		intent.title("Title");
		assertThat(intent.title().toString(), is("Title"));
	}

	@Test
	public void testMimeTypeDefault() {
		assertThat(new ShareIntent().mimeType(), is(MimeType.TEXT));
	}

	@Test
	public void testMimeType() {
		final ShareIntent intent = new ShareIntent();
		intent.mimeType(MimeType.AUDIO);
		assertThat(intent.mimeType(), is(MimeType.AUDIO));
	}

	@Test
	public void testBuildWithTextContent() {
		final ShareIntent shareIntent = new ShareIntent();
		shareIntent.content("Text to share.");
		final Intent intent = shareIntent.build(mContext);
		assertThat(intent.getAction(), is(Intent.ACTION_SEND));
		assertThat(intent.getData(), is(nullValue()));
		assertThat(intent.getStringExtra(Intent.EXTRA_TEXT), is("Text to share."));
	}

	@Test
	public void testBuildWithUriContent() {
		final ShareIntent shareIntent = new ShareIntent();
		shareIntent.uri(Uri.parse("content://android/data/images/lion.png"));
		shareIntent.title("Beautiful Lion");
		final Intent intent = shareIntent.build(mContext);
		assertThat(intent.getAction(), is(Intent.ACTION_SEND));
		assertThat(intent.getCharSequenceExtra(Intent.EXTRA_TITLE).toString(), is("Beautiful Lion"));
		assertThat(intent.getCharSequenceExtra(Intent.EXTRA_TEXT), is(nullValue()));
		assertThat(intent.<Uri>getParcelableExtra(Intent.EXTRA_STREAM), is(Uri.parse("content://android/data/images/lion.png")));
	}

	@Test
	public void testBuildWithUrisContent() {
		final ShareIntent shareIntent = new ShareIntent();
		shareIntent.uris(
				Uri.parse("content://android/data/images/lion.png"),
				Uri.parse("content://android/data/images/elephant.png")
		);
		final Intent intent = shareIntent.build(mContext);
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
				new ShareIntent(),
				"No content to share specified."
		);
	}

	@Test
	public void testBuildWithoutMimeType() {
		final ShareIntent intent = new ShareIntent();
		intent.content("Content to share");
		intent.mimeType("");
		assertThatBuildThrowsExceptionWithMessage(
				mContext,
				intent,
				"No content's MIME type specified."
		);
	}

	@Test
	public void testOnStartWith() {
		final ShareIntent shareIntent = new ShareIntent();
		shareIntent.content("Text to share.");
		final Intent intent = shareIntent.build(mContext);
		final IntentStarter mockStarter = mock(IntentStarter.class);
		shareIntent.onStartWith(mockStarter, intent);
		verify(mockStarter, times(1)).startIntent(any(Intent.class));
	}
}
