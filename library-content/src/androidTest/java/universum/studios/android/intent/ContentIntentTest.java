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
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import universum.studios.android.test.BaseInstrumentedTest;
import universum.studios.android.test.TestActivity;
import universum.studios.android.test.TestUtils;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static junit.framework.Assert.assertEquals;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsNot.not;
import static org.hamcrest.core.IsNull.nullValue;
import static org.junit.Assume.assumeTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static universum.studios.android.intent.ContentTests.assertThatBuildThrowsExceptionWithMessage;

/**
 * @author Martin Albedinsky
 */
@RunWith(AndroidJUnit4.class)
public final class ContentIntentTest extends BaseInstrumentedTest {

	@SuppressWarnings("unused")
	private static final String TAG = "ContentIntentTest";

	@Rule public ActivityTestRule<TestActivity> ACTIVITY_RULE = new ActivityTestRule<>(TestActivity.class);

	private ContentIntentImpl mIntent;

	@Override
	public void beforeTest() throws Exception {
		super.beforeTest();
		this.mIntent = new ContentIntentImpl();
	}

	@Override
	public void afterTest() throws Exception {
		super.afterTest();
		this.mIntent = null;
	}

	@NonNull
	static Matcher<File> hasPath(String path) {
		return new FilePath(TestUtils.STORAGE_BASE_PATH + path);
	}

	@NonNull
	static Matcher<File> hasRelativePath(String path) {
		return new FileRelativePath(TestUtils.STORAGE_BASE_PATH + path);
	}

	public void testCreateContentFileTimeStamp() {
		assertThat(
				ContentIntent.createContentFileTimeStamp(),
				is(new SimpleDateFormat(ContentIntent.CONTENT_FILE_TIME_STAMP_FORMAT, Locale.getDefault()).format(new Date()))
		);
	}

	@Test
	@SuppressWarnings("ConstantConditions")
	public void testCreateContentFileNameInExternalDirectory() {
		final File file = ContentIntent.createContentFile("test-file.tmp", Environment.DIRECTORY_DOWNLOADS);
		if (file != null) {
			assertThat(file.exists(), is(true));
			assertThat(file, hasRelativePath("/Download/"));
			assertThat(file.delete(), is(true));
		}
	}

	@Test
	@SuppressWarnings("ConstantConditions")
	public void testCreateContentFileNameInDirectory() {
		final File externalFilesDir = mContext.getExternalFilesDir(null);
		if (externalFilesDir != null) {
			final File file = ContentIntent.createContentFile("test-file.tmp", externalFilesDir);
			if (file != null) {
				assertThat(file.exists(), is(true));
				assertThat(file, hasRelativePath("/Android/data/universum.studios.android.intent.test/files/"));
				assertThat(file.delete(), is(true));
			}
		}
	}

	@Test
	public void testAppendDefaultFileSuffixIfNotPresented() {
		assertEquals("lion.png", ContentIntent.appendDefaultFileSuffixIfNotPresented("lion.png", ".jpg"));
		assertEquals("elephant.jpg", ContentIntent.appendDefaultFileSuffixIfNotPresented("elephant", ".jpg"));
		assertEquals("cat.1", ContentIntent.appendDefaultFileSuffixIfNotPresented("cat.1", ".jpg"));
	}

	@Test
	public void testDefaultHandlers() {
		assertThat(mIntent.handlers(), is(Collections.EMPTY_LIST));
	}

	@Test
	public void testWithHandlers() {
		mIntent.withHandlers(
				new ContentIntent.ContentHandler("TestHandler1", new Intent()),
				new ContentIntent.ContentHandler("TestHandler2", new Intent())
		);
		final List<ContentIntent.ContentHandler> handlers = mIntent.handlers();
		assertThat(handlers, is(not(nullValue())));
		assertThat(handlers.size(), is(2));
		assertThat(handlers.get(0).name().toString(), is("TestHandler1"));
		assertThat(handlers.get(1).name().toString(), is("TestHandler2"));
	}
	@Test
	public void testWithHandlersMultiple() {
		mIntent.withHandlers(
				new ContentIntent.ContentHandler("TestHandler1", new Intent()),
				new ContentIntent.ContentHandler("TestHandler2", new Intent())
		);
		mIntent.withHandlers(
				new ContentIntent.ContentHandler("TestHandler3", new Intent()),
				new ContentIntent.ContentHandler("TestHandler4", new Intent())
		);
		final List<ContentIntent.ContentHandler> handlers = mIntent.handlers();
		assertThat(handlers, is(not(nullValue())));
		assertThat(handlers.size(), is(4));
		assertThat(handlers.get(0).name().toString(), is("TestHandler1"));
		assertThat(handlers.get(1).name().toString(), is("TestHandler2"));
		assertThat(handlers.get(2).name().toString(), is("TestHandler3"));
		assertThat(handlers.get(3).name().toString(), is("TestHandler4"));
	}

	@Test
	public void testWithEmptyHandlers() {
		mIntent.withHandlers(new ArrayList<ContentIntent.ContentHandler>(0));
		assertThat(mIntent.handlers(), is(Collections.EMPTY_LIST));
	}

	@Test
	public void testWithNullHandlers() {
		mIntent.withHandlers(new ArrayList<ContentIntent.ContentHandler>(0));
		mIntent.withHandlers((List<ContentIntent.ContentHandler>) null);
		assertThat(mIntent.handlers(), is(Collections.EMPTY_LIST));
	}

	@Test
	public void testWithHandler() {
		mIntent.withHandler(new ContentIntent.ContentHandler("TestHandler1", new Intent()));
		mIntent.withHandler(new ContentIntent.ContentHandler("TestHandler2", new Intent()));
		final List<ContentIntent.ContentHandler> handlers = mIntent.handlers();
		assertThat(handlers, is(not(nullValue())));
		assertThat(handlers.size(), is(2));
		assertThat(handlers.get(0).name().toString(), is("TestHandler1"));
		assertThat(handlers.get(1).name().toString(), is("TestHandler2"));
	}

	@Test
	public void testDefaultUri() {
		assertThat(mIntent.uri(), is(nullValue()));
	}

	@Test
	public void testInputFile() {
		mIntent.input(new File("TestFile"));
		final Uri input = mIntent.uri();
		assertThat(input, is(not(nullValue())));
		assertThat(input, is(Uri.fromFile(new File("TestFile"))));
	}

	@Test
	public void testInputNullFile() {
		mIntent.input((File) null);
		assertThat(mIntent.uri(), is(nullValue()));
	}

	@Test
	public void testInputUri() {
		mIntent.dataType(MimeType.IMAGE_JPEG);
		mIntent.input(Uri.parse("content://android/data/images/lion.jpg"));
		final Uri input = mIntent.uri();
		assertThat(input, is(not(nullValue())));
		assertThat(input, is(Uri.parse("content://android/data/images/lion.jpg")));
		// Data type is null as we did not specify it after the input.
		assertThat(mIntent.dataType(), is(nullValue()));
	}

	@Test
	public void testInputNullUri() {
		mIntent.input((Uri) null);
		assertThat(mIntent.uri(), is(nullValue()));
	}

	@Test
	public void testOutputFile() {
		mIntent.output(new File("TestFile"));
		final Uri output = mIntent.uri();
		assertThat(output, is(not(nullValue())));
		assertThat(output, is(Uri.fromFile(new File("TestFile"))));
	}

	@Test
	public void testOutputNullFile() {
		mIntent.output((File) null);
		assertThat(mIntent.uri(), is(nullValue()));
	}

	@Test
	public void testOutputUri() {
		mIntent.output(Uri.parse("content://android/data/images/lion.jpg"));
		final Uri output = mIntent.uri();
		assertThat(output, is(not(nullValue())));
		assertThat(output, is(Uri.parse("content://android/data/images/lion.jpg")));
		assertThat(mIntent.dataType(), is(nullValue()));
	}

	@Test
	public void testOutputNullUri() {
		mIntent.output((Uri) null);
		assertThat(mIntent.uri(), is(nullValue()));
	}

	@Test
	public void testDefaultDataType() {
		assertThat(mIntent.dataType(), is(nullValue()));
	}

	@Test
	public void testDataType() {
		mIntent.dataType(MimeType.AUDIO_MP3);
		assertThat(mIntent.dataType(), is(MimeType.AUDIO_MP3));
	}

	@Test
	public void testBuildWithInputUri() {
		mIntent.input(Uri.parse("content://android/data/images/lion.jpg"));
		mIntent.dataType(MimeType.IMAGE_JPEG);
		final Intent intent = mIntent.build(mContext);
		assertThat(intent, is(not(nullValue())));
		assertThat(intent.getAction(), is(Intent.ACTION_VIEW));
		assertThat(intent.getData(), is(Uri.parse("content://android/data/images/lion.jpg")));
		assertThat(intent.getType(), is(MimeType.IMAGE_JPEG));
	}

	@Test
	public void testBuildWithOutputUri() {
		mIntent.output(Uri.parse("content://android/data/images/lion.jpg"));
		assertThatBuildThrowsExceptionWithMessage(
				mContext,
				mIntent,
				"No input Uri specified."
		);
	}

	@Test
	public void testBuildWithoutUri() {
		assertThatBuildThrowsExceptionWithMessage(
				mContext,
				mIntent,
				"No input Uri specified."
		);
	}

	@Test
	public void testBuildWithoutDataType() {
		mIntent.input(Uri.parse("content://android/data/images/lion.jpg"));
		assertThatBuildThrowsExceptionWithMessage(
				mContext,
				mIntent,
				"No MIME type specified for input Uri."
		);
	}

	@Test(expected = IllegalStateException.class)
	public void testBuildWithHandlers() {
		mIntent.withHandler(new ContentIntent.ContentHandler("TestHandler1", new Intent()));
		mIntent.build(mContext);
	}

	@Test
	public void testStartWith() {
		mIntent.input(Uri.EMPTY);
		mIntent.dataType(MimeType.TEXT_HTML);
		assumeTrue(BaseIntent.isActivityForIntentAvailable(mContext, mIntent.build(mContext)));
		final IntentStarter mockStarter = mock(IntentStarter.class);
		when(mockStarter.getContext()).thenReturn(mContext);
		mIntent.startWith(mockStarter);
		verify(mockStarter, times(1)).startIntent(any(Intent.class));
	}

	@Test
	public void testStartWithWithHandlers() throws Throwable {
		mIntent.withHandler(new ContentIntent.ContentHandler("TestHandler1", new Intent()));
		final IntentStarter mockStarter = mock(IntentStarter.class);
		when(mockStarter.getContext()).thenReturn(ACTIVITY_RULE.getActivity());
		ACTIVITY_RULE.runOnUiThread(new Runnable() {

			@Override
			public void run() {
				mIntent.startWith(mockStarter);
			}
		});
		waitForIdleSync();
		verify(mockStarter, times(0)).startIntent(any(Intent.class));
	}

	@Test
	public void testOnShowChooserDialog() throws Throwable {
		mIntent.dialogTitle("Test Dialog");
		mIntent.withHandler(new ContentIntent.ContentHandler("TestHandler1", new Intent()));
		mIntent.withHandler(new ContentIntent.ContentHandler("TestHandler2", new Intent()));
		ACTIVITY_RULE.runOnUiThread(new Runnable() {

			@Override
			public void run() {
				mIntent.onShowChooserDialog(IntentStarters.activityStarter(ACTIVITY_RULE.getActivity()));
			}
		});
		waitForIdleSync();
		onView(withText("Test Dialog")).check(matches(isDisplayed()));
		onView(withText("TestHandler1")).check(matches(isDisplayed()));
		onView(withText("TestHandler2")).check(matches(isDisplayed()));
	}

	@Test
	public void testChooserDialogOnClickWithoutRequestCode() throws Throwable {
		mIntent.dialogTitle("Test Dialog");
		final Intent intent = new Intent(Intent.ACTION_VIEW);
		mIntent.withHandler(new ContentIntent.ContentHandler("TestHandler1", intent));
		final IntentStarter mockStarter = mock(IntentStarter.class);
		when(mockStarter.getContext()).thenReturn(ACTIVITY_RULE.getActivity());
		ACTIVITY_RULE.runOnUiThread(new Runnable() {

			@Override
			public void run() {
				mIntent.onShowChooserDialog(mockStarter);
			}
		});
		waitForIdleSync();
		onView(withText("TestHandler1")).perform(click());
		verify(mockStarter, times(1)).startIntent(intent);
	}

	@Test
	public void testChooserDialogOnClickWithRequestCode() throws Throwable {
		mIntent.dialogTitle("Test Dialog");
		final Intent intent = new Intent(Intent.ACTION_VIEW);
		mIntent.withHandler(new ContentIntent.ContentHandler("TestHandler1", intent).requestCode(1000));
		final IntentStarter mockStarter = mock(IntentStarter.class);
		when(mockStarter.getContext()).thenReturn(ACTIVITY_RULE.getActivity());
		ACTIVITY_RULE.runOnUiThread(new Runnable() {

			@Override
			public void run() {
				mIntent.onShowChooserDialog(mockStarter);
			}
		});
		waitForIdleSync();
		onView(withText("TestHandler1")).perform(click());
		verify(mockStarter, times(1)).startIntentForResult(intent, 1000);
	}

	@Test
	public void testOnStartWith() {
		mIntent.input(Uri.EMPTY);
		mIntent.dataType(MimeType.TEXT_HTML);
		final Intent intent = mIntent.build(mContext);
		final IntentStarter mockStarter = mock(IntentStarter.class);
		mIntent.onStartWith(mockStarter, intent);
		verify(mockStarter, times(1)).startIntent(any(Intent.class));
	}

	static final class ContentIntentImpl extends ContentIntent<ContentIntentImpl> {

		@Override
		public ContentIntentImpl withDefaultHandlers(@NonNull Context context) {
			return this;
		}
	}

	private static final class FilePath extends TypeSafeMatcher<File> {

		private final String expected;

		FilePath(String expected) {
			this.expected = expected;
		}

		@Override
		protected boolean matchesSafely(File item) {
			return expected.equals(item.getPath());
		}

		@Override
		public void describeTo(Description description) {
			// Ignored.
		}
	}

	private static final class FileRelativePath extends TypeSafeMatcher<File> {

		private final String expected;

		FileRelativePath(String expected) {
			this.expected = expected;
		}

		@Override
		protected boolean matchesSafely(File item) {
			return expected.equals(item.getPath().replace(item.getName(), ""));
		}

		@Override
		public void describeTo(Description description) {
			// Ignored
		}
	}
}
