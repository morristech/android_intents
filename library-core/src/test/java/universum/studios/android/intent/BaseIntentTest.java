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

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;

import org.junit.Test;
import org.robolectric.Robolectric;

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

	@Test
	public void testDialogTitleDefault() {
		assertThat(new IntentImpl().dialogTitle().toString(), is("Choose"));
	}

	@Test
	public void testDialogTitle() {
		final BaseIntent intent = new IntentImpl();
		intent.dialogTitle("Choose provider");
		assertThat(intent.dialogTitle().toString(), is("Choose provider"));
	}

	@Test
	public void testDialogTitleWithNullValue() {
		final BaseIntent intent = new IntentImpl();
		intent.dialogTitle(null);
		assertThat(intent.dialogTitle().toString(), is(""));
	}

	@Test
	public void testActivityNotFoundMessage() {
		final BaseIntent intent = new IntentImpl();
		intent.activityNotFoundMessage("No activity found to support launched request");
		assertThat(intent.activityNotFoundMessage().toString(), is("No activity found to support launched request"));
	}

	@Test
	public void testActivityNotFoundMessageWithNullValue() {
		final BaseIntent intent = new IntentImpl();
		intent.activityNotFoundMessage(null);
		assertThat(intent.activityNotFoundMessage().toString(), is(""));
	}

	@Test
	public void testActivityNotFoundMessageDefault() {
		final BaseIntent intent = new IntentImpl();
		assertThat(intent.activityNotFoundMessage().toString(), is("No application found to handle this action"));
	}

	@Test
	public void testTransitionsDefault() {
		final BaseIntent intent = new IntentImpl();
		assertThat(intent.enterTransition(), is(0));
		assertThat(intent.exitTransition(), is(0));
	}

	@Test
	public void testEnterTransition() {
		final BaseIntent intent = new IntentImpl();
		final int animationResource = android.R.anim.fade_in;
		intent.enterTransition(animationResource);
		assertThat(intent.enterTransition(), is(animationResource));
	}

	@Test
	public void testExitTransition() {
		final BaseIntent intent = new IntentImpl();
		final int animationResource = android.R.anim.fade_in;
		intent.exitTransition(animationResource);
		assertThat(intent.exitTransition(), is(animationResource));
	}

	@Test
	public void testTransitions() {
		final BaseIntent intent = new IntentImpl();
		final int enterAnimationResource = android.R.anim.fade_in;
		final int exitAnimationResource = android.R.anim.fade_out;
		intent.transitions(enterAnimationResource, exitAnimationResource);
		assertThat(intent.enterTransition(), is(enterAnimationResource));
		assertThat(intent.exitTransition(), is(exitAnimationResource));
	}

	@Test
	public void testTransitionsWithZeroAnimRes() {
		final BaseIntent intent = new IntentImpl();
		intent.transitions(0, 0);
		assertThat(intent.enterTransition(), is(0));
		assertThat(intent.exitTransition(), is(0));
	}

	@Test
	public void testTransitionsWithNegativeAnimRes() {
		final BaseIntent intent = new IntentImpl();
		intent.transitions(-1, -1);
		assertThat(intent.enterTransition(), is(0));
		assertThat(intent.exitTransition(), is(0));
	}

	@Test
	public void testTransitionsWithCombinedAnimRes() {
		final BaseIntent intent = new IntentImpl();
		intent.transitions(0, -1);
		assertThat(intent.enterTransition(), is(0));
		assertThat(intent.exitTransition(), is(0));
		intent.transitions(-1, 0);
		assertThat(intent.enterTransition(), is(0));
		assertThat(intent.exitTransition(), is(0));
	}

	@Test
	public void testNotifyActivityNotFound() {
		final Activity activity = Robolectric.buildActivity(TestActivity.class).create().start().resume().get();
		new IntentImpl().notifyActivityNotFound(activity);
	}

	@Test
	public void testStartWithUnavailableActivity() {
		final IntentStarter mockStarter = mock(IntentStarter.class);
		when(mockStarter.getContext()).thenReturn(mApplication);
		new BaseIntent() {

			@NonNull
			@Override
			protected Intent onBuild(@NonNull Context context) {
				return new Intent();
			}
		}.startWith(mockStarter);
		verify(mockStarter, times(0)).startIntent(any(Intent.class));
		verify(mockStarter, times(0)).overridePendingTransition(anyInt(), anyInt());
	}

	@Test
	public void testOnStartWithEnterTransition() {
		final BaseIntent baseIntent = new IntentImpl();
		baseIntent.enterTransition(android.R.anim.fade_in);
		final Intent intent = baseIntent.build(mApplication);
		final IntentStarter mockStarter = mock(IntentStarter.class);
		baseIntent.onStartWith(mockStarter, intent);
		verify(mockStarter, times(1)).startIntent(intent);
		verify(mockStarter, times(1)).overridePendingTransition(baseIntent.enterTransition(), baseIntent.exitTransition());
	}

	@Test
	public void testOnStartWithExitTransition() {
		final BaseIntent baseIntent = new IntentImpl();
		baseIntent.exitTransition(android.R.anim.fade_out);
		final Intent intent = baseIntent.build(mApplication);
		final IntentStarter mockStarter = mock(IntentStarter.class);
		baseIntent.onStartWith(mockStarter, intent);
		verify(mockStarter, times(1)).startIntent(intent);
		verify(mockStarter, times(1)).overridePendingTransition(baseIntent.enterTransition(), baseIntent.exitTransition());
	}

	@Test
	public void testOnStartWithBothTransitions() {
		final BaseIntent baseIntent = new IntentImpl();
		baseIntent.transitions(android.R.anim.fade_in, android.R.anim.fade_out);
		final Intent intent = baseIntent.build(mApplication);
		final IntentStarter mockStarter = mock(IntentStarter.class);
		baseIntent.onStartWith(mockStarter, intent);
		verify(mockStarter, times(1)).startIntent(intent);
		verify(mockStarter, times(1)).overridePendingTransition(baseIntent.enterTransition(), baseIntent.exitTransition());
	}

	static final class IntentImpl extends BaseIntent<IntentImpl> {

		@NonNull
		@Override
		protected Intent onBuild(@NonNull Context context) {
			return new Intent(Intent.ACTION_VIEW).putExtra(Intent.EXTRA_TEXT, "Content");
		}
	}
}
