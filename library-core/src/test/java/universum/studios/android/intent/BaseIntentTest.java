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

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import org.junit.Test;
import org.robolectric.Robolectric;

import androidx.annotation.NonNull;
import universum.studios.android.test.local.RobolectricTestCase;
import universum.studios.android.test.local.TestActivity;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * @author Martin Albedinsky
 */
public final class BaseIntentTest extends RobolectricTestCase {

	@Test public void testInstantiation() {
		// Act:
		final BaseIntent intent = new IntentImpl();
		// Assert:
		assertThat(intent.dialogTitle(), is((CharSequence) "Choose"));
		assertThat(intent.enterTransition(), is(0));
		assertThat(intent.exitTransition(), is(0));
		assertThat(intent.activityNotFoundMessage(), is((CharSequence) "No application found to handle this action"));
	}

	@Test public void testDialogTitle() {
		// Arrange:
		final BaseIntent intent = new IntentImpl();
		// Act:
		intent.dialogTitle("Choose provider");
		// Assert:
		assertThat(intent.dialogTitle(), is((CharSequence) "Choose provider"));
	}

	@Test public void testDialogTitleWithNullValue() {
		// Arrange:
		final BaseIntent intent = new IntentImpl();
		// Act:
		intent.dialogTitle(null);
		// Assert:
		assertThat(intent.dialogTitle(), is((CharSequence) ""));
	}

	@Test public void testActivityNotFoundMessage() {
		// Arrange:
		final BaseIntent intent = new IntentImpl();
		// Act:
		intent.activityNotFoundMessage("No activity found to support launched request");
		// Assert:
		assertThat(intent.activityNotFoundMessage(), is((CharSequence) "No activity found to support launched request"));
	}

	@Test public void testActivityNotFoundMessageWithNullValue() {
		// Arrange:
		final BaseIntent intent = new IntentImpl();
		// Act:
		intent.activityNotFoundMessage(null);
		// Assert:
		assertThat(intent.activityNotFoundMessage(), is((CharSequence) ""));
	}

	@Test public void testEnterTransition() {
		// Arrange:
		final BaseIntent intent = new IntentImpl();
		final int animationResource = android.R.anim.fade_in;
		// Act:
		intent.enterTransition(animationResource);
		// Assert:
		assertThat(intent.enterTransition(), is(animationResource));
	}

	@Test public void testExitTransition() {
		// Arrange:
		final BaseIntent intent = new IntentImpl();
		final int animationResource = android.R.anim.fade_in;
		// Act:
		intent.exitTransition(animationResource);
		// Assert:
		assertThat(intent.exitTransition(), is(animationResource));
	}

	@Test public void testTransitions() {
		// Arrange:
		final BaseIntent intent = new IntentImpl();
		final int enterAnimationResource = android.R.anim.fade_in;
		final int exitAnimationResource = android.R.anim.fade_out;
		// Act:
		intent.transitions(enterAnimationResource, exitAnimationResource);
		// Assert:
		assertThat(intent.enterTransition(), is(enterAnimationResource));
		assertThat(intent.exitTransition(), is(exitAnimationResource));
	}

	@Test public void testTransitionsWithZeroAnimRes() {
		// Arrange:
		final BaseIntent intent = new IntentImpl();
		// Act:
		intent.transitions(0, 0);
		// Assert:
		assertThat(intent.enterTransition(), is(0));
		assertThat(intent.exitTransition(), is(0));
	}

	@Test public void testTransitionsWithCombinedAnimRes() {
		// Arrange:
		final BaseIntent intent = new IntentImpl();
		// Act + Assert:
		intent.transitions(0, android.R.anim.fade_out);
		assertThat(intent.enterTransition(), is(0));
		assertThat(intent.exitTransition(), is(android.R.anim.fade_out));
		intent.transitions(android.R.anim.fade_in, 0);
		assertThat(intent.enterTransition(), is(android.R.anim.fade_in));
		assertThat(intent.exitTransition(), is(0));
	}

	@Test public void testNotifyActivityNotFound() {
		// Arrange:
		final Activity activity = Robolectric.buildActivity(TestActivity.class).create().start().resume().get();
		// Act:
		new IntentImpl().notifyActivityNotFound(activity);
	}

	@Test public void testStartWithUnavailableActivity() {
		// Arrange:
		final IntentStarter mockStarter = mock(IntentStarter.class);
		when(mockStarter.getContext()).thenReturn(context);
		final BaseIntent intent = new BaseIntent() {

			@Override @NonNull protected Intent onBuild(@NonNull final Context context) {
				return new Intent("ACTION.Test");
			}
		};
		// Act:
		intent.startWith(mockStarter);
		// Assert:
		verify(mockStarter, times(0)).startIntent(any(Intent.class));
		verify(mockStarter, times(0)).overridePendingTransition(anyInt(), anyInt());
	}

	@Test public void testOnStartWithEnterTransition() {
		// Arrange:
		final BaseIntent baseIntent = new IntentImpl();
		baseIntent.enterTransition(android.R.anim.fade_in);
		final Intent intent = baseIntent.build(context);
		final IntentStarter mockStarter = mock(IntentStarter.class);
		// Act:
		baseIntent.onStartWith(mockStarter, intent);
		// Assert:
		verify(mockStarter).startIntent(intent);
		verify(mockStarter).overridePendingTransition(baseIntent.enterTransition(), baseIntent.exitTransition());
	}

	@Test public void testOnStartWithExitTransition() {
		// Arrange:
		final BaseIntent baseIntent = new IntentImpl();
		baseIntent.exitTransition(android.R.anim.fade_out);
		final Intent intent = baseIntent.build(context);
		final IntentStarter mockStarter = mock(IntentStarter.class);
		// Act:
		baseIntent.onStartWith(mockStarter, intent);
		// Assert:
		verify(mockStarter).startIntent(intent);
		verify(mockStarter).overridePendingTransition(baseIntent.enterTransition(), baseIntent.exitTransition());
	}

	@Test public void testOnStartWithBothTransitions() {
		// Arrange:
		final BaseIntent baseIntent = new IntentImpl();
		baseIntent.transitions(android.R.anim.fade_in, android.R.anim.fade_out);
		final Intent intent = baseIntent.build(context);
		final IntentStarter mockStarter = mock(IntentStarter.class);
		// Act:
		baseIntent.onStartWith(mockStarter, intent);
		// Assert:
		verify(mockStarter).startIntent(intent);
		verify(mockStarter).overridePendingTransition(baseIntent.enterTransition(), baseIntent.exitTransition());
	}

	static final class IntentImpl extends BaseIntent<IntentImpl> {

		@Override @NonNull protected Intent onBuild(@NonNull final Context context) {
			return new Intent(Intent.ACTION_VIEW).putExtra(Intent.EXTRA_TEXT, "Content");
		}
	}
}