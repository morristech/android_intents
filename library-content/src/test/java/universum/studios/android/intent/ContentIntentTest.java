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

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;

import org.junit.Test;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import universum.studios.android.test.local.RobolectricTestCase;

import static junit.framework.Assert.assertEquals;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsNot.not;
import static org.hamcrest.core.IsNull.nullValue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static universum.studios.android.intent.ContentTests.assertThatBuildThrowsExceptionWithMessage;

/**
 * @author Martin Albedinsky
 */
public final class ContentIntentTest extends RobolectricTestCase {

	@Test
	public void testCreateContentFileTimeStamp() {
		assertThat(
				ContentIntent.createContentFileTimeStamp(),
				is(new SimpleDateFormat(ContentIntent.CONTENT_FILE_TIME_STAMP_FORMAT, Locale.getDefault()).format(new Date()))
		);
	}

	@Test
	public void testAppendDefaultFileSuffixIfNotPresented() {
		assertEquals("lion.png", ContentIntent.appendDefaultFileSuffixIfNotPresented("lion.png", ".jpg"));
		assertEquals("elephant.jpg", ContentIntent.appendDefaultFileSuffixIfNotPresented("elephant", ".jpg"));
		assertEquals("cat.1", ContentIntent.appendDefaultFileSuffixIfNotPresented("cat.1", ".jpg"));
	}

	@Test
	public void testHandlersDefault() {
		assertThat(new ContentIntentImpl().handlers(), is(Collections.EMPTY_LIST));
	}

	@Test
	@SuppressWarnings("unchecked")
	public void testWithHandlers() {
		final ContentIntent intent = new ContentIntentImpl();
		intent.withHandlers(
				new ContentIntent.ContentHandler("TestHandler1", new Intent()),
				new ContentIntent.ContentHandler("TestHandler2", new Intent())
		);
		final List<ContentIntent.ContentHandler> handlers = intent.handlers();
		assertThat(handlers, is(not(nullValue())));
		assertThat(handlers.size(), is(2));
		assertThat(handlers.get(0).name().toString(), is("TestHandler1"));
		assertThat(handlers.get(1).name().toString(), is("TestHandler2"));
	}

	@Test
	@SuppressWarnings("unchecked")
	public void testWithHandlersMultiple() {
		final ContentIntent intent = new ContentIntentImpl();
		intent.withHandlers(
				new ContentIntent.ContentHandler("TestHandler1", new Intent()),
				new ContentIntent.ContentHandler("TestHandler2", new Intent())
		);
		intent.withHandlers(
				new ContentIntent.ContentHandler("TestHandler3", new Intent()),
				new ContentIntent.ContentHandler("TestHandler4", new Intent())
		);
		final List<ContentIntent.ContentHandler> handlers = intent.handlers();
		assertThat(handlers, is(not(nullValue())));
		assertThat(handlers.size(), is(4));
		assertThat(handlers.get(0).name().toString(), is("TestHandler1"));
		assertThat(handlers.get(1).name().toString(), is("TestHandler2"));
		assertThat(handlers.get(2).name().toString(), is("TestHandler3"));
		assertThat(handlers.get(3).name().toString(), is("TestHandler4"));
	}

	@Test
	public void testWithEmptyHandlers() {
		final ContentIntent intent = new ContentIntentImpl();
		intent.withHandlers(new ArrayList<ContentIntent.ContentHandler>(0));
		assertThat(intent.handlers(), is(Collections.EMPTY_LIST));
	}

	@Test
	public void testWithNullHandlers() {
		final ContentIntent intent = new ContentIntentImpl();
		intent.withHandlers(new ArrayList<ContentIntent.ContentHandler>(0));
		intent.withHandlers((List<ContentIntent.ContentHandler>) null);
		assertThat(intent.handlers(), is(Collections.EMPTY_LIST));
	}

	@Test
	@SuppressWarnings("unchecked")
	public void testWithHandler() {
		final ContentIntent intent = new ContentIntentImpl();
		intent.withHandler(new ContentIntent.ContentHandler("TestHandler1", new Intent()));
		intent.withHandler(new ContentIntent.ContentHandler("TestHandler2", new Intent()));
		final List<ContentIntent.ContentHandler> handlers = intent.handlers();
		assertThat(handlers, is(not(nullValue())));
		assertThat(handlers.size(), is(2));
		assertThat(handlers.get(0).name().toString(), is("TestHandler1"));
		assertThat(handlers.get(1).name().toString(), is("TestHandler2"));
	}

	@Test
	public void testUriDefault() {
		assertThat(new ContentIntentImpl().uri(), is(nullValue()));
	}

	@Test
	public void testInputFile() {
		final ContentIntent intent = new ContentIntentImpl();
		intent.input(new File("TestFile"));
		final Uri input = intent.uri();
		assertThat(input, is(not(nullValue())));
		assertThat(input, is(Uri.fromFile(new File("TestFile"))));
	}

	@Test
	public void testInputNullFile() {
		final ContentIntent intent = new ContentIntentImpl();
		intent.input((File) null);
		assertThat(intent.uri(), is(nullValue()));
	}

	@Test
	public void testInputUri() {
		final ContentIntent intent = new ContentIntentImpl();
		intent.dataType(MimeType.IMAGE_JPEG);
		intent.input(Uri.parse("content://android/data/images/lion.jpg"));
		final Uri input = intent.uri();
		assertThat(input, is(not(nullValue())));
		assertThat(input, is(Uri.parse("content://android/data/images/lion.jpg")));
		// Data type is null as we did not specify it after the input.
		assertThat(intent.dataType(), is(nullValue()));
	}

	@Test
	public void testInputNullUri() {
		final ContentIntent intent = new ContentIntentImpl();
		intent.input((Uri) null);
		assertThat(intent.uri(), is(nullValue()));
	}

	@Test
	public void testOutputFile() {
		final ContentIntent intent = new ContentIntentImpl();
		intent.output(new File("TestFile"));
		final Uri output = intent.uri();
		assertThat(output, is(not(nullValue())));
		assertThat(output, is(Uri.fromFile(new File("TestFile"))));
	}

	@Test
	public void testOutputNullFile() {
		final ContentIntent intent = new ContentIntentImpl();
		intent.output((File) null);
		assertThat(intent.uri(), is(nullValue()));
	}

	@Test
	public void testOutputUri() {
		final ContentIntent intent = new ContentIntentImpl();
		intent.output(Uri.parse("content://android/data/images/lion.jpg"));
		final Uri output = intent.uri();
		assertThat(output, is(not(nullValue())));
		assertThat(output, is(Uri.parse("content://android/data/images/lion.jpg")));
		assertThat(intent.dataType(), is(nullValue()));
	}

	@Test
	public void testOutputNullUri() {
		final ContentIntent intent = new ContentIntentImpl();
		intent.output((Uri) null);
		assertThat(intent.uri(), is(nullValue()));
	}

	@Test
	public void testDataTypeDefault() {
		assertThat(new ContentIntentImpl().dataType(), is(nullValue()));
	}

	@Test
	public void testDataType() {
		final ContentIntent intent = new ContentIntentImpl();
		intent.dataType(MimeType.AUDIO_MP3);
		assertThat(intent.dataType(), is(MimeType.AUDIO_MP3));
	}

	@Test
	public void testBuildWithInputUri() {
		final ContentIntent contentIntent = new ContentIntentImpl();
		contentIntent.input(Uri.parse("content://android/data/images/lion.jpg"));
		contentIntent.dataType(MimeType.IMAGE_JPEG);
		final Intent intent = contentIntent.build(mApplication);
		assertThat(intent, is(not(nullValue())));
		assertThat(intent.getAction(), is(Intent.ACTION_VIEW));
		assertThat(intent.getData(), is(Uri.parse("content://android/data/images/lion.jpg")));
		assertThat(intent.getType(), is(MimeType.IMAGE_JPEG));
	}

	@Test
	public void testBuildWithOutputUri() {
		final ContentIntent intent = new ContentIntentImpl();
		intent.output(Uri.parse("content://android/data/images/lion.jpg"));
		assertThatBuildThrowsExceptionWithMessage(
				mApplication,
				intent,
				"No input Uri specified."
		);
	}

	@Test
	public void testBuildWithoutUri() {
		assertThatBuildThrowsExceptionWithMessage(
				mApplication,
				new ContentIntentImpl(),
				"No input Uri specified."
		);
	}

	@Test
	public void testBuildWithoutDataType() {
		final ContentIntent intent = new ContentIntentImpl();
		intent.input(Uri.parse("content://android/data/images/lion.jpg"));
		assertThatBuildThrowsExceptionWithMessage(
				mApplication,
				intent,
				"No MIME type specified for input Uri."
		);
	}

	@Test(expected = IllegalStateException.class)
	public void testBuildWithHandlers() {
		final ContentIntent intent = new ContentIntentImpl();
		intent.withHandler(new ContentIntent.ContentHandler("TestHandler1", new Intent()));
		intent.build(mApplication);
	}

	@Test
	public void testStartWithHandlers() throws Throwable {
		final ContentIntent intent = new ContentIntentImpl();
		intent.withHandler(new ContentIntent.ContentHandler("TestHandler1", new Intent()));
		final IntentStarter mockStarter = mock(IntentStarter.class);
		when(mockStarter.getContext()).thenReturn(mApplication);
		intent.startWith(mockStarter);
		verify(mockStarter, times(0)).startIntent(any(Intent.class));
	}

	@Test
	public void testOnStartWith() {
		final ContentIntent contentIntent = new ContentIntentImpl();
		contentIntent.input(Uri.EMPTY);
		contentIntent.dataType(MimeType.TEXT_HTML);
		final Intent intent = contentIntent.build(mApplication);
		final IntentStarter mockStarter = mock(IntentStarter.class);
		contentIntent.onStartWith(mockStarter, intent);
		verify(mockStarter, times(1)).startIntent(any(Intent.class));
	}

	static final class ContentIntentImpl extends ContentIntent<ContentIntentImpl> {

		@Override
		public ContentIntentImpl withDefaultHandlers(@NonNull Context context) {
			return this;
		}
	}
}
