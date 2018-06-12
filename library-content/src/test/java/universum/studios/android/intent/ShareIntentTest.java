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

import java.util.Collections;
import java.util.List;

import universum.studios.android.test.local.RobolectricTestCase;

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
@SuppressWarnings("ResourceType")
public final class ShareIntentTest extends RobolectricTestCase {

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
		final Intent intent = shareIntent.build(application);
		assertThat(intent.getAction(), is(Intent.ACTION_SEND));
		assertThat(intent.getData(), is(nullValue()));
		assertThat(intent.getStringExtra(Intent.EXTRA_TEXT), is("Text to share."));
	}

	@Test
	public void testBuildWithUriContent() {
		final ShareIntent shareIntent = new ShareIntent();
		shareIntent.uri(Uri.parse("content://android/data/images/lion.png"));
		shareIntent.title("Beautiful Lion");
		final Intent intent = shareIntent.build(application);
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
		final Intent intent = shareIntent.build(application);
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
				application,
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
				application,
				intent,
				"No content's MIME type specified."
		);
	}

	@Test
	public void testOnStartWith() {
		final ShareIntent shareIntent = new ShareIntent();
		shareIntent.content("Text to share.");
		final Intent intent = shareIntent.build(application);
		final IntentStarter mockStarter = mock(IntentStarter.class);
		shareIntent.onStartWith(mockStarter, intent);
		verify(mockStarter, times(1)).startIntent(any(Intent.class));
	}
}