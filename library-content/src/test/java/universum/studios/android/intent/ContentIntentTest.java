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

	@Test public void testCreateContentFileTimeStamp() {
		// Act:

		// Assert:
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

	@Test public void testInstantiation() {
		// Act:
		final ContentIntent intent = new ContentIntentImpl();
		// Assert:
		assertThat(intent.handlers(), is(Collections.EMPTY_LIST));
		assertThat(intent.uri(), is(nullValue()));
		assertThat(intent.dataType(), is(nullValue()));
	}

	@SuppressWarnings("unchecked")
	@Test public void testWithHandlers() {
		// Arrange:
		final ContentIntent.ContentHandler handlerFirst = new ContentIntent.ContentHandler("TestHandler#1", new Intent());
		final ContentIntent.ContentHandler handlerSecond = new ContentIntent.ContentHandler("TestHandler#2", new Intent());
		final ContentIntent intent = new ContentIntentImpl();
		// Act:
		intent.withHandlers(handlerFirst, handlerSecond);
		// Assert:
		final List<ContentIntent.ContentHandler> handlers = intent.handlers();
		assertThat(handlers, is(not(nullValue())));
		assertThat(handlers.size(), is(2));
		assertThat(handlers.get(0), is(handlerFirst));
		assertThat(handlers.get(1), is(handlerSecond));
	}

	@SuppressWarnings("unchecked")
	@Test public void testWithHandlersMultiple() {
		// Arrange:
		final ContentIntent.ContentHandler handlerFirst = new ContentIntent.ContentHandler("TestHandler#1", new Intent());
		final ContentIntent.ContentHandler handlerSecond = new ContentIntent.ContentHandler("TestHandler#2", new Intent());
		final ContentIntent.ContentHandler handlerThird = new ContentIntent.ContentHandler("TestHandler#3", new Intent());
		final ContentIntent.ContentHandler handlerFourth = new ContentIntent.ContentHandler("TestHandler#4", new Intent());
		final ContentIntent intent = new ContentIntentImpl();
		// Act:
		intent.withHandlers(handlerFirst, handlerSecond);
		intent.withHandlers(handlerThird, handlerFourth);
		// Assert:
		final List<ContentIntent.ContentHandler> handlers = intent.handlers();
		assertThat(handlers, is(not(nullValue())));
		assertThat(handlers.size(), is(4));
		assertThat(handlers.get(0), is(handlerFirst));
		assertThat(handlers.get(1), is(handlerSecond));
		assertThat(handlers.get(2), is(handlerThird));
		assertThat(handlers.get(3), is(handlerFourth));
	}

	@Test public void testWithEmptyHandlers() {
		// Arrange:
		final ContentIntent intent = new ContentIntentImpl();
		// Act:
		intent.withHandlers(new ArrayList<ContentIntent.ContentHandler>(0));
		// Assert:
		assertThat(intent.handlers(), is(Collections.EMPTY_LIST));
	}

	@Test public void testWithNullHandlers() {
		// Arrange:
		final ContentIntent intent = new ContentIntentImpl();
		intent.withHandlers(new ArrayList<ContentIntent.ContentHandler>(0));
		// Act:
		intent.withHandlers((List<ContentIntent.ContentHandler>) null);
		// Assert:
		assertThat(intent.handlers(), is(Collections.EMPTY_LIST));
	}

	@SuppressWarnings("unchecked")
	@Test public void testWithHandler() {
		// Arrange:
		final ContentIntent.ContentHandler handlerFirst = new ContentIntent.ContentHandler("TestHandler#1", new Intent());
		final ContentIntent.ContentHandler handlerSecond = new ContentIntent.ContentHandler("TestHandler#2", new Intent());
		final ContentIntent intent = new ContentIntentImpl();
		// Act:
		intent.withHandler(handlerFirst);
		intent.withHandler(handlerSecond);
		// Assert:
		final List<ContentIntent.ContentHandler> handlers = intent.handlers();
		assertThat(handlers, is(not(nullValue())));
		assertThat(handlers.size(), is(2));
		assertThat(handlers.get(0), is(handlerFirst));
		assertThat(handlers.get(1), is(handlerSecond));
	}

	@Test public void testInputFile() {
		// Arrange:
		final File inputFile = new File("input.tmp");
		final ContentIntent intent = new ContentIntentImpl();
		// Act:
		intent.input(inputFile);
		// Assert:
		final Uri input = intent.uri();
		assertThat(input, is(not(nullValue())));
		assertThat(input, is(Uri.fromFile(inputFile)));
	}

	@Test public void testInputNullFile() {
		// Arrange:
		final ContentIntent intent = new ContentIntentImpl();
		// Act:
		intent.input((File) null);
		// Assert:
		assertThat(intent.uri(), is(nullValue()));
	}

	@Test public void testInputUri() {
		// Arrange:
		final Uri inputUri = Uri.parse("content://android/data/images/lion.jpg");
		final ContentIntent intent = new ContentIntentImpl();
		intent.dataType(MimeType.IMAGE_JPEG);
		// Act:
		intent.input(inputUri);
		// Assert:
		final Uri input = intent.uri();
		assertThat(input, is(not(nullValue())));
		assertThat(input, is(inputUri));
		// Data type is null as we did not specify it after the input.
		assertThat(intent.dataType(), is(nullValue()));
	}

	@Test public void testInputNullUri() {
		// Arrange:
		final ContentIntent intent = new ContentIntentImpl();
		// Act:
		intent.input((Uri) null);
		// Assert:
		assertThat(intent.uri(), is(nullValue()));
	}

	@Test public void testOutputFile() {
		// Arrange:
		final File outputFile = new File("output.tmp");
		final ContentIntent intent = new ContentIntentImpl();
		// Act:
		intent.output(outputFile);
		// Assert:
		final Uri output = intent.uri();
		assertThat(output, is(not(nullValue())));
		assertThat(output, is(Uri.fromFile(outputFile)));
	}

	@Test public void testOutputNullFile() {
		// Arrange:
		final ContentIntent intent = new ContentIntentImpl();
		// Act:
		intent.output((File) null);
		// Assert:
		assertThat(intent.uri(), is(nullValue()));
	}

	@Test public void testOutputUri() {
		// Arrange:
		final Uri outputUri = Uri.parse("content://android/data/images/lion.jpg");
		final ContentIntent intent = new ContentIntentImpl();
		// Act:
		intent.output(outputUri);
		// Assert:
		final Uri output = intent.uri();
		assertThat(output, is(not(nullValue())));
		assertThat(output, is(output));
		assertThat(intent.dataType(), is(nullValue()));
	}

	@Test public void testOutputNullUri() {
		// Arrange:
		final ContentIntent intent = new ContentIntentImpl();
		// Act:
		intent.output((Uri) null);
		// Assert:
		assertThat(intent.uri(), is(nullValue()));
	}

	@Test public void testDataType() {
		// Arrange:
		final ContentIntent intent = new ContentIntentImpl();
		// Act:
		intent.dataType(MimeType.AUDIO_MP3);
		// Assert:
		assertThat(intent.dataType(), is(MimeType.AUDIO_MP3));
	}

	@Test public void testBuildWithInputUri() {
		// Arrange:
		final ContentIntent contentIntent = new ContentIntentImpl();
		contentIntent.input(Uri.parse("content://android/data/images/lion.jpg"));
		contentIntent.dataType(MimeType.IMAGE_JPEG);
		// Act:
		final Intent intent = contentIntent.build(application);
		// Assert:
		assertThat(intent, is(not(nullValue())));
		assertThat(intent.getAction(), is(Intent.ACTION_VIEW));
		assertThat(intent.getData(), is(Uri.parse("content://android/data/images/lion.jpg")));
		assertThat(intent.getType(), is(MimeType.IMAGE_JPEG));
	}

	@Test public void testBuildWithOutputUri() {
		// Arrange:
		final ContentIntent intent = new ContentIntentImpl();
		intent.output(Uri.parse("content://android/data/images/lion.jpg"));
		// Act + Assert:
		assertThatBuildThrowsExceptionWithMessage(
				application,
				intent,
				"No input Uri specified."
		);
	}

	@Test public void testBuildWithoutUri() {
		assertThatBuildThrowsExceptionWithMessage(
				application,
				new ContentIntentImpl(),
				"No input Uri specified."
		);
	}

	@Test public void testBuildWithoutDataType() {
		// Arrange:
		final ContentIntent intent = new ContentIntentImpl();
		intent.input(Uri.parse("content://android/data/images/lion.jpg"));
		// Act + Assert:
		assertThatBuildThrowsExceptionWithMessage(
				application,
				intent,
				"No MIME type specified for input Uri."
		);
	}

	@Test(expected = IllegalStateException.class)
	public void testBuildWithHandlers() {
		// Arrange:
		final ContentIntent intent = new ContentIntentImpl();
		intent.withHandler(new ContentIntent.ContentHandler("TestHandler1", new Intent()));
		// Act:
		intent.build(application);
	}

	@Test public void testStartWithHandlers() {
		// Arrange:
		final ContentIntent intent = new ContentIntentImpl();
		intent.withHandler(new ContentIntent.ContentHandler("TestHandler1", new Intent()));
		final IntentStarter mockStarter = mock(IntentStarter.class);
		when(mockStarter.getContext()).thenReturn(application);
		// Act:
		intent.startWith(mockStarter);
		// Assert:
		verify(mockStarter, times(0)).startIntent(any(Intent.class));
	}

	@Test public void testOnStartWith() {
		// Arrange:
		final ContentIntent contentIntent = new ContentIntentImpl();
		contentIntent.input(Uri.EMPTY);
		contentIntent.dataType(MimeType.TEXT_HTML);
		final Intent intent = contentIntent.build(application);
		final IntentStarter mockStarter = mock(IntentStarter.class);
		// Act:
		contentIntent.onStartWith(mockStarter, intent);
		// Assert:
		verify(mockStarter).startIntent(any(Intent.class));
	}

	static final class ContentIntentImpl extends ContentIntent<ContentIntentImpl> {

		@Override public ContentIntentImpl withDefaultHandlers(@NonNull final Context context) {
			return this;
		}
	}
}