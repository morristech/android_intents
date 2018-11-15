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
import android.os.Environment;

import org.junit.Rule;
import org.junit.Test;

import java.io.File;

import androidx.annotation.NonNull;
import androidx.test.rule.ActivityTestRule;
import universum.studios.android.test.instrumented.InstrumentedTestCase;
import universum.studios.android.test.instrumented.TestActivity;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assume.assumeTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static universum.studios.android.intent.ContentTests.hasRelativePath;

/**
 * @author Martin Albedinsky
 */
public final class ContentIntentTest extends InstrumentedTestCase {

	@Rule public ActivityTestRule<TestActivity> ACTIVITY_RULE = new ActivityTestRule<>(TestActivity.class);

	@Test public void testCreateContentFileNameInDirectory() {
		// Arrange:
		final File externalFilesDir = context.getExternalFilesDir(null);
		if (externalFilesDir != null) {
			// Act:
			final File file = ContentIntent.createContentFile("test-file.tmp", externalFilesDir);
			// Assert:
			if (file != null) {
				assertThat(file.exists(), is(true));
				assertThat(file, hasRelativePath("/Android/data/universum.studios.android.intent.test/files/"));
				assertThat(file.delete(), is(true));
			}
		}
	}

	@Test public void testCreateContentFileNameInExternalDirectory() {
		// Act:
		final File file = ContentIntent.createContentFile("test-file.tmp", Environment.DIRECTORY_DOWNLOADS);
		if (file != null) {
			// Assert:
			assertThat(file.exists(), is(true));
			assertThat(file, hasRelativePath("/Download/"));
			assertThat(file.delete(), is(true));
		}
	}

	@Test public void testStartWith() {
		// Arrange:
		final ContentIntent intent = new ContentIntentImpl();
		intent.input(Uri.EMPTY);
		intent.dataType(MimeType.TEXT_HTML);
		assumeTrue(BaseIntent.isActivityForIntentAvailable(context, intent.build(context)));
		final IntentStarter mockStarter = mock(IntentStarter.class);
		when(mockStarter.getContext()).thenReturn(context);
		// Act:
		intent.startWith(mockStarter);
		// Assert:
		verify(mockStarter).startIntent(any(Intent.class));
	}

	@Test public void testOnShowChooserDialog() throws Throwable {
		// Arrange:
		final ContentIntent intent = new ContentIntentImpl();
		intent.dialogTitle("Test Dialog");
		intent.withHandler(new ContentIntent.ContentHandler("TestHandler1", new Intent()));
		intent.withHandler(new ContentIntent.ContentHandler("TestHandler2", new Intent()));
		// Act:
		ACTIVITY_RULE.runOnUiThread(new Runnable() {

			@Override public void run() {
				intent.onShowChooserDialog(IntentStarters.activityStarter(ACTIVITY_RULE.getActivity()));
			}
		});
		// Assert:
		waitForIdleSync();
		onView(withText("Test Dialog")).check(matches(isDisplayed()));
		onView(withText("TestHandler1")).check(matches(isDisplayed()));
		onView(withText("TestHandler2")).check(matches(isDisplayed()));
	}

	@Test public void testChooserDialogOnClickWithoutRequestCode() throws Throwable {
		// Arrange:
		final ContentIntent contentIntent = new ContentIntentImpl();
		contentIntent.dialogTitle("Test Dialog");
		final Intent intent = new Intent(Intent.ACTION_VIEW);
		contentIntent.withHandler(new ContentIntent.ContentHandler("TestHandler1", intent));
		final IntentStarter mockStarter = mock(IntentStarter.class);
		when(mockStarter.getContext()).thenReturn(ACTIVITY_RULE.getActivity());
		// Act:
		ACTIVITY_RULE.runOnUiThread(new Runnable() {

			@Override public void run() {
				contentIntent.onShowChooserDialog(mockStarter);
			}
		});
		// Assert:
		waitForIdleSync();
		onView(withText("TestHandler1")).perform(click());
		verify(mockStarter).startIntent(intent);
	}

	@Test public void testChooserDialogOnClickWithRequestCode() throws Throwable {
		// Arrange:
		final ContentIntent contentIntent = new ContentIntentImpl();
		contentIntent.dialogTitle("Test Dialog");
		final Intent intent = new Intent(Intent.ACTION_VIEW);
		contentIntent.withHandler(new ContentIntent.ContentHandler("TestHandler1", intent).requestCode(1000));
		final IntentStarter mockStarter = mock(IntentStarter.class);
		when(mockStarter.getContext()).thenReturn(ACTIVITY_RULE.getActivity());
		// Act:
		ACTIVITY_RULE.runOnUiThread(new Runnable() {

			@Override public void run() {
				contentIntent.onShowChooserDialog(mockStarter);
			}
		});
		// Assert:
		waitForIdleSync();
		onView(withText("TestHandler1")).perform(click());
		verify(mockStarter, times(1)).startIntentForResult(intent, 1000);
	}

	static final class ContentIntentImpl extends ContentIntent<ContentIntentImpl> {

		@Override public ContentIntentImpl withDefaultHandlers(@NonNull Context context) {
			return this;
		}
	}
}