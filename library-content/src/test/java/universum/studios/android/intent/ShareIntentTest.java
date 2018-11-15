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
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static universum.studios.android.intent.ContentTests.assertThatBuildThrowsExceptionWithMessage;

/**
 * @author Martin Albedinsky
 */
@SuppressWarnings("ResourceType")
public final class ShareIntentTest extends RobolectricTestCase {

	@Test public void testInstantiation() {
		// Act:
		final ShareIntent intent = new ShareIntent();
		// Assert:
		assertThat(intent.content(), is((CharSequence) ""));
		assertThat(intent.uri(), is(nullValue()));
		assertThat(intent.uris(), is(Collections.<Uri>emptyList()));
		assertThat(intent.mimeType(), is(MimeType.TEXT));
		assertThat(intent.title(), is((CharSequence) ""));
	}

	@Test public void testContent() {
		// Arrange:
		final ShareIntent intent = new ShareIntent();
		// Act:
		intent.content("Sharing content.");
		// Assert:
		assertThat(intent.content(), is((CharSequence) "Sharing content."));
	}

	@Test public void testUri() {
		// Arrange:
		final Uri uri = Uri.parse("content://android/data/images/leopard.png");
		final ShareIntent intent = new ShareIntent();
		// Act:
		intent.uri(uri);
		// Assert:
		assertThat(intent.uri(), is(uri));
	}

	@Test public void testUris() {
		// Arrange:
		final Uri uriFirst = Uri.parse("content://android/data/video/nature.avi");
		final Uri uriSecond = Uri.parse("content://android/data/audio/ocean.mp3");
		final ShareIntent intent = new ShareIntent();
		// Act:
		intent.uris(uriFirst, uriSecond);
		// Assert:
		final List<Uri> uris = intent.uris();
		assertThat(uris, is(notNullValue()));
		assertThat(uris.size(), is(2));
		assertThat(uris.get(0), is(uriFirst));
		assertThat(uris.get(1), is(uriSecond));
	}

	@Test public void testMimeType() {
		// Arrange:
		final ShareIntent intent = new ShareIntent();
		// Act:
		intent.mimeType(MimeType.AUDIO);
		// Assert:
		assertThat(intent.mimeType(), is(MimeType.AUDIO));
	}

	@Test public void testTitle() {
		// Arrange:
		final ShareIntent intent = new ShareIntent();
		// Act:
		intent.title("Title");
		// Assert:
	assertThat(intent.title(), is((CharSequence) "Title"));
	}

	@Test public void testBuildWithTextContent() {
		// Arrange:
		final ShareIntent shareIntent = new ShareIntent().content("Text to share.");
		// Act:
		final Intent intent = shareIntent.build(context);
		// Assert:
		assertThat(intent.getAction(), is(Intent.ACTION_SEND));
		assertThat(intent.getData(), is(nullValue()));
		assertThat(intent.getStringExtra(Intent.EXTRA_TEXT), is("Text to share."));
	}

	@Test public void testBuildWithUriContent() {
		// Arrange:
		final ShareIntent shareIntent = new ShareIntent();
		shareIntent.uri(Uri.parse("content://android/data/images/lion.png"));
		shareIntent.title("Beautiful Lion");
		// Act:
		final Intent intent = shareIntent.build(context);
		// Assert:
		assertThat(intent.getAction(), is(Intent.ACTION_SEND));
		assertThat(intent.getCharSequenceExtra(Intent.EXTRA_TITLE).toString(), is("Beautiful Lion"));
		assertThat(intent.getCharSequenceExtra(Intent.EXTRA_TEXT), is(nullValue()));
		assertThat(intent.<Uri>getParcelableExtra(Intent.EXTRA_STREAM), is(Uri.parse("content://android/data/images/lion.png")));
	}

	@Test public void testBuildWithUrisContent() {
		// Arrange:
		final Uri uriFirst = Uri.parse("content://android/data/images/lion.png");
		final Uri uriSecond = Uri.parse("content://android/data/images/elephant.png");
		final ShareIntent shareIntent = new ShareIntent();
		shareIntent.uris(uriFirst, uriSecond);
		// Act:
		final Intent intent = shareIntent.build(context);
		// Assert:
		assertThat(intent.getAction(), is(Intent.ACTION_SEND_MULTIPLE));
		assertThat(intent.getCharSequenceExtra(Intent.EXTRA_TITLE), is(nullValue()));
		assertThat(intent.getCharSequenceExtra(Intent.EXTRA_TEXT), is(nullValue()));
		final List<Uri> uris = intent.getParcelableArrayListExtra(Intent.EXTRA_STREAM);
		assertThat(uris, is(notNullValue()));
		assertThat(uris.size(), is(2));
		assertThat(uris.get(0), is(uriFirst));
		assertThat(uris.get(1), is(uriSecond));
	}

	@Test public void testBuildWithoutContent() {
		assertThatBuildThrowsExceptionWithMessage(
				context,
				new ShareIntent(),
				"No content to share specified."
		);
	}

	@Test public void testBuildWithoutMimeType() {
		// Arrange:
		final ShareIntent intent = new ShareIntent();
		intent.content("Content to share");
		intent.mimeType("");
		// Act + Assert:
		assertThatBuildThrowsExceptionWithMessage(
				context,
				intent,
				"No content's MIME type specified."
		);
	}

	@Test public void testOnStartWith() {
		// Arrange:
		final ShareIntent shareIntent = new ShareIntent();
		shareIntent.content("Text to share.");
		final Intent intent = shareIntent.build(context);
		final IntentStarter mockStarter = mock(IntentStarter.class);
		// Act:
		shareIntent.onStartWith(mockStarter, intent);
		// Assert:
		verify(mockStarter).startIntent(any(Intent.class));
	}
}