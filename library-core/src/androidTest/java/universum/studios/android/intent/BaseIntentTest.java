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
import android.support.test.annotation.UiThreadTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import universum.studios.android.test.BaseInstrumentedTest;
import universum.studios.android.test.TestActivity;
import universum.studios.android.test.TestResources;
import universum.studios.android.test.TestUtils;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assume.assumeTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * @author Martin Albedinsky
 */
@RunWith(AndroidJUnit4.class)
public final class BaseIntentTest extends BaseInstrumentedTest {

	@SuppressWarnings("unused")
	private static final String TAG = "BaseIntentTest";

	@Rule
	public final ActivityTestRule ACTIVITY_RULE = new ActivityTestRule<>(TestActivity.class);

	@Test
	public void testIsActivityForIntentAvailable() {
		assertThat(
				BaseIntent.isActivityForIntentAvailable(
						mContext,
						new Intent(Intent.ACTION_MAIN)
								.addCategory(Intent.CATEGORY_LAUNCHER)
								.setClassName("com.android.mms", "com.android.mms.ui.ConversationList")
				),
				is(true)
		);
	}

	@Test
	public void testIsActivityForIntentAvailableWithEmptyIntent() {
		assertThat(
				BaseIntent.isActivityForIntentAvailable(
						mContext,
						new Intent()
				),
				is(false)
		);
	}

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
		assumeTrue(TestUtils.hasLibraryRootPackageName(mContext));
		final int animationResource = TestResources.resourceIdentifier(
				mContext,
				TestResources.ANIMATION,
				"test_transition_enter"
		);
		intent.enterTransition(animationResource);
		assertThat(intent.enterTransition(), is(animationResource));
	}

	@Test
	public void testExitTransition() {
		final BaseIntent intent = new IntentImpl();
		assumeTrue(TestUtils.hasLibraryRootPackageName(mContext));
		final int animationResource = TestResources.resourceIdentifier(
				mContext,
				TestResources.ANIMATION,
				"test_transition_exit"
		);
		intent.exitTransition(animationResource);
		assertThat(intent.exitTransition(), is(animationResource));
	}

	@Test
	public void testTransitions() {
		final BaseIntent intent = new IntentImpl();
		assumeTrue(TestUtils.hasLibraryRootPackageName(mContext));
		final int enterAnimationResource = TestResources.resourceIdentifier(
				mContext,
				TestResources.ANIMATION,
				"test_transition_enter"
		);
		final int exitAnimationResource = TestResources.resourceIdentifier(
				mContext,
				TestResources.ANIMATION,
				"test_transition_exit"
		);
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
	@UiThreadTest
	public void testNotifyActivityNotFound() {
		final Activity activity = ACTIVITY_RULE.getActivity();
		new IntentImpl().notifyActivityNotFound(activity);
	}

	@Test
	public void testStartWith() {
		final IntentStarter mockStarter = mock(IntentStarter.class);
		when(mockStarter.getContext()).thenReturn(mContext);
		new IntentImpl().startWith(mockStarter);
		verify(mockStarter, times(1)).startIntent(any(Intent.class));
		verify(mockStarter, times(0)).overridePendingTransition(anyInt(), anyInt());
	}

	@Test
	@UiThreadTest
	public void testStartWithUnavailableActivity() {
		final IntentStarter mockStarter = mock(IntentStarter.class);
		when(mockStarter.getContext()).thenReturn(mContext);
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
		baseIntent.enterTransition(TestResources.resourceIdentifier(
				mContext,
				TestResources.ANIMATION,
				"test_transition_enter"
		));
		final Intent intent = baseIntent.build(mContext);
		final IntentStarter mockStarter = mock(IntentStarter.class);
		baseIntent.onStartWith(mockStarter, intent);
		verify(mockStarter, times(1)).startIntent(intent);
		verify(mockStarter, times(1)).overridePendingTransition(baseIntent.enterTransition(), baseIntent.exitTransition());
	}

	@Test
	public void testOnStartWithExitTransition() {
		final BaseIntent baseIntent = new IntentImpl();
		baseIntent.exitTransition(TestResources.resourceIdentifier(
				mContext,
				TestResources.ANIMATION,
				"test_transition_exit"
		));
		final Intent intent = baseIntent.build(mContext);
		final IntentStarter mockStarter = mock(IntentStarter.class);
		baseIntent.onStartWith(mockStarter, intent);
		verify(mockStarter, times(1)).startIntent(intent);
		verify(mockStarter, times(1)).overridePendingTransition(baseIntent.enterTransition(), baseIntent.exitTransition());
	}

	@Test
	public void testOnStartWithBothTransitions() {
		final BaseIntent baseIntent = new IntentImpl();
		baseIntent.transitions(
				TestResources.resourceIdentifier(
						mContext,
						TestResources.ANIMATION,
						"test_transition_enter"
				),
				TestResources.resourceIdentifier(
						mContext,
						TestResources.ANIMATION,
						"test_transition_exit"
				)
		);
		final Intent intent = baseIntent.build(mContext);
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
