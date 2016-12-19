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
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import universum.studios.android.intent.inner.TestActivity;
import universum.studios.android.intent.test.R;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * @author Martin Albedinsky
 */
@RunWith(AndroidJUnit4.class)
public final class BaseIntentTest extends IntentBaseTest<BaseIntentTest.IntentImpl> {

	@SuppressWarnings("unused")
	private static final String TAG = "BaseIntentTest";

	@Rule
	public final ActivityTestRule ACTIVITY_RULE = new ActivityTestRule<>(TestActivity.class);

	public BaseIntentTest() {
		super(IntentImpl.class);
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
	public void testActivityNotFoundMessageText() {
		mIntent.activityNotFoundMessage("No activity found to support launched request");
		assertThat(mIntent.activityNotFoundMessage().toString(), is("No activity found to support launched request"));
	}

	@Test
	public void testDefaultTransitions() {
		assertThat(mIntent.enterTransition(), is(0));
		assertThat(mIntent.exitTransition(), is(0));
	}

	@Test
	public void testEnterTransition() {
		mIntent.enterTransition(R.anim.test_transition_enter);
		assertThat(mIntent.enterTransition(), is(R.anim.test_transition_enter));
	}

	@Test
	public void testExitTransition() {
		mIntent.exitTransition(R.anim.test_transition_exit);
		assertThat(mIntent.exitTransition(), is(R.anim.test_transition_exit));
	}

	@Test
	public void testTransitions() {
		mIntent.transitions(R.anim.test_transition_enter, R.anim.test_transition_exit);
		assertThat(mIntent.enterTransition(), is(R.anim.test_transition_enter));
		assertThat(mIntent.exitTransition(), is(R.anim.test_transition_exit));
	}

	@Test
	public void testTransitionsWithZeroAnimRes() {
		mIntent.transitions(0, 0);
		assertThat(mIntent.enterTransition(), is(0));
		assertThat(mIntent.exitTransition(), is(0));
	}

	@Test
	public void testNotifyActivityNotFound() {
		final Activity activity = ACTIVITY_RULE.getActivity();
		activity.runOnUiThread(new Runnable() {

			@Override
			public void run() {
				mIntent.notifyActivityNotFound(activity);
			}
		});
	}

	static final class IntentImpl extends BaseIntent<IntentImpl> {

		@NonNull
		@Override
		protected Intent onBuild(@NonNull Context context) {
			return new Intent();
		}
	}
}