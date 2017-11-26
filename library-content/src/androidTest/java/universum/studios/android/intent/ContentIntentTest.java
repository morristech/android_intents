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

import org.junit.Rule;
import org.junit.Test;

import java.io.File;

import universum.studios.android.test.instrumented.InstrumentedTestCase;
import universum.studios.android.test.instrumented.TestActivity;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
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
	public void testStartWith() {
		final ContentIntent intent = new ContentIntentImpl();
		intent.input(Uri.EMPTY);
		intent.dataType(MimeType.TEXT_HTML);
		assumeTrue(BaseIntent.isActivityForIntentAvailable(mContext, intent.build(mContext)));
		final IntentStarter mockStarter = mock(IntentStarter.class);
		when(mockStarter.getContext()).thenReturn(mContext);
		intent.startWith(mockStarter);
		verify(mockStarter, times(1)).startIntent(any(Intent.class));
	}

	@Test
	public void testOnShowChooserDialog() throws Throwable {
		final ContentIntent intent = new ContentIntentImpl();
		intent.dialogTitle("Test Dialog");
		intent.withHandler(new ContentIntent.ContentHandler("TestHandler1", new Intent()));
		intent.withHandler(new ContentIntent.ContentHandler("TestHandler2", new Intent()));
		ACTIVITY_RULE.runOnUiThread(new Runnable() {

			@Override
			public void run() {
				intent.onShowChooserDialog(IntentStarters.activityStarter(ACTIVITY_RULE.getActivity()));
			}
		});
		waitForIdleSync();
		onView(withText("Test Dialog")).check(matches(isDisplayed()));
		onView(withText("TestHandler1")).check(matches(isDisplayed()));
		onView(withText("TestHandler2")).check(matches(isDisplayed()));
	}

	@Test
	public void testChooserDialogOnClickWithoutRequestCode() throws Throwable {
		final ContentIntent contentIntent = new ContentIntentImpl();
		contentIntent.dialogTitle("Test Dialog");
		final Intent intent = new Intent(Intent.ACTION_VIEW);
		contentIntent.withHandler(new ContentIntent.ContentHandler("TestHandler1", intent));
		final IntentStarter mockStarter = mock(IntentStarter.class);
		when(mockStarter.getContext()).thenReturn(ACTIVITY_RULE.getActivity());
		ACTIVITY_RULE.runOnUiThread(new Runnable() {

			@Override
			public void run() {
				contentIntent.onShowChooserDialog(mockStarter);
			}
		});
		waitForIdleSync();
		onView(withText("TestHandler1")).perform(click());
		verify(mockStarter, times(1)).startIntent(intent);
	}

	@Test
	public void testChooserDialogOnClickWithRequestCode() throws Throwable {
		final ContentIntent contentIntent = new ContentIntentImpl();
		contentIntent.dialogTitle("Test Dialog");
		final Intent intent = new Intent(Intent.ACTION_VIEW);
		contentIntent.withHandler(new ContentIntent.ContentHandler("TestHandler1", intent).requestCode(1000));
		final IntentStarter mockStarter = mock(IntentStarter.class);
		when(mockStarter.getContext()).thenReturn(ACTIVITY_RULE.getActivity());
		ACTIVITY_RULE.runOnUiThread(new Runnable() {

			@Override
			public void run() {
				contentIntent.onShowChooserDialog(mockStarter);
			}
		});
		waitForIdleSync();
		onView(withText("TestHandler1")).perform(click());
		verify(mockStarter, times(1)).startIntentForResult(intent, 1000);
	}

	static final class ContentIntentImpl extends ContentIntent<ContentIntentImpl> {

		@Override
		public ContentIntentImpl withDefaultHandlers(@NonNull Context context) {
			return this;
		}
	}
}
