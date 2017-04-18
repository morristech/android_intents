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

	private IntentImpl mIntent;

	@Override
	public void beforeTest() throws Exception {
		super.beforeTest();
		this.mIntent = new IntentImpl();
	}

	@Override
	public void afterTest() throws Exception {
		super.afterTest();
		this.mIntent = null;
	}

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
	public void testDefaultDialogTitle() {
		assertThat(mIntent.dialogTitle().toString(), is("Choose"));
	}

	@Test
	public void testDialogTitle() {
		mIntent.dialogTitle("Choose provider");
		assertThat(mIntent.dialogTitle().toString(), is("Choose provider"));
	}

	@Test
	public void testDialogTitleWithNullValue() {
		mIntent.dialogTitle(null);
		assertThat(mIntent.dialogTitle().toString(), is(""));
	}

	@Test
	public void testActivityNotFoundMessage() {
		mIntent.activityNotFoundMessage("No activity found to support launched request");
		assertThat(mIntent.activityNotFoundMessage().toString(), is("No activity found to support launched request"));
	}

	@Test
	public void testActivityNotFoundMessageWithNullValue() {
		mIntent.activityNotFoundMessage(null);
		assertThat(mIntent.activityNotFoundMessage().toString(), is(""));
	}

	@Test
	public void testDefaultActivityNotFoundMessage() {
		assertThat(mIntent.activityNotFoundMessage().toString(), is("No application found to handle this action"));
	}

	@Test
	public void testDefaultTransitions() {
		assertThat(mIntent.enterTransition(), is(0));
		assertThat(mIntent.exitTransition(), is(0));
	}

	@Test
	public void testEnterTransition() {
		assumeTrue(TestUtils.hasLibraryRootPackageName(mContext));
		final int animationResource = TestResources.resourceIdentifier(
				mContext,
				TestResources.ANIMATION,
				"test_transition_enter"
		);
		mIntent.enterTransition(animationResource);
		assertThat(mIntent.enterTransition(), is(animationResource));
	}

	@Test
	public void testExitTransition() {
		assumeTrue(TestUtils.hasLibraryRootPackageName(mContext));
		final int animationResource = TestResources.resourceIdentifier(
				mContext,
				TestResources.ANIMATION,
				"test_transition_exit"
		);
		mIntent.exitTransition(animationResource);
		assertThat(mIntent.exitTransition(), is(animationResource));
	}

	@Test
	public void testTransitions() {
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
		mIntent.transitions(enterAnimationResource, exitAnimationResource);
		assertThat(mIntent.enterTransition(), is(enterAnimationResource));
		assertThat(mIntent.exitTransition(), is(exitAnimationResource));
	}

	@Test
	public void testTransitionsWithZeroAnimRes() {
		mIntent.transitions(0, 0);
		assertThat(mIntent.enterTransition(), is(0));
		assertThat(mIntent.exitTransition(), is(0));
	}

	@Test
	public void testTransitionsWithNegativeAnimRes() {
		mIntent.transitions(-1, -1);
		assertThat(mIntent.enterTransition(), is(0));
		assertThat(mIntent.exitTransition(), is(0));
	}

	@Test
	public void testTransitionsWithCombinedAnimRes() {
		mIntent.transitions(0, -1);
		assertThat(mIntent.enterTransition(), is(0));
		assertThat(mIntent.exitTransition(), is(0));
		mIntent.transitions(-1, 0);
		assertThat(mIntent.enterTransition(), is(0));
		assertThat(mIntent.exitTransition(), is(0));
	}

	@Test
	@UiThreadTest
	public void testNotifyActivityNotFound() {
		final Activity activity = ACTIVITY_RULE.getActivity();
		mIntent.notifyActivityNotFound(activity);
	}

	@Test
	public void testStartWith() {
		final IntentStarter mockStarter = mock(IntentStarter.class);
		when(mockStarter.getContext()).thenReturn(mContext);
		mIntent.startWith(mockStarter);
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
		mIntent.enterTransition(TestResources.resourceIdentifier(
				mContext,
				TestResources.ANIMATION,
				"test_transition_enter"
		));
		final Intent intent = mIntent.build(mContext);
		final IntentStarter mockStarter = mock(IntentStarter.class);
		mIntent.onStartWith(mockStarter, intent);
		verify(mockStarter, times(1)).startIntent(intent);
		verify(mockStarter, times(1)).overridePendingTransition(mIntent.enterTransition(), mIntent.exitTransition());
	}

	@Test
	public void testOnStartWithExitTransition() {
		mIntent.exitTransition(TestResources.resourceIdentifier(
				mContext,
				TestResources.ANIMATION,
				"test_transition_exit"
		));
		final Intent intent = mIntent.build(mContext);
		final IntentStarter mockStarter = mock(IntentStarter.class);
		mIntent.onStartWith(mockStarter, intent);
		verify(mockStarter, times(1)).startIntent(intent);
		verify(mockStarter, times(1)).overridePendingTransition(mIntent.enterTransition(), mIntent.exitTransition());
	}

	@Test
	public void testOnStartWithBothTransitions() {
		mIntent.transitions(
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
		final Intent intent = mIntent.build(mContext);
		final IntentStarter mockStarter = mock(IntentStarter.class);
		mIntent.onStartWith(mockStarter, intent);
		verify(mockStarter, times(1)).startIntent(intent);
		verify(mockStarter, times(1)).overridePendingTransition(mIntent.enterTransition(), mIntent.exitTransition());
	}

	static final class IntentImpl extends BaseIntent<IntentImpl> {

		@NonNull
		@Override
		protected Intent onBuild(@NonNull Context context) {
			return new Intent(Intent.ACTION_VIEW).putExtra(Intent.EXTRA_TEXT, "Content");
		}
	}
}
