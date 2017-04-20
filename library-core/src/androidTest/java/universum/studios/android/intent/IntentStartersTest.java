/*
 * =================================================================================================
 *                             Copyright (C) 2017 Universum Studios
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
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.v4.app.FragmentActivity;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import universum.studios.android.test.BaseInstrumentedTest;
import universum.studios.android.test.TestActivity;
import universum.studios.android.test.TestCompatActivity;
import universum.studios.android.test.TestCompatFragment;
import universum.studios.android.test.TestFragment;
import universum.studios.android.test.TestResources;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.core.IsNot.not;
import static org.hamcrest.core.IsNull.nullValue;
import static org.junit.Assert.assertThat;
import static org.junit.Assume.assumeTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

/**
 * @author Martin Albedinsky
 */
@RunWith(AndroidJUnit4.class)
public final class IntentStartersTest extends BaseInstrumentedTest {
    
	@SuppressWarnings("unused")
	private static final String TAG = "IntentStartersTest";

	@Rule public ActivityTestRule<TestActivity> ACTIVITY_RULE = new ActivityTestRule<>(TestActivity.class);
	@Rule public ActivityTestRule<TestCompatActivity> ACTIVITY_COMPAT_RULE = new ActivityTestRule<>(TestCompatActivity.class);

    @Test
	public void testActivityStarter() {
	    final Activity mockActivity = mock(Activity.class);
		final IntentStarter starter = IntentStarters.activityStarter(mockActivity);
	    assertThat(starter, is(not(nullValue())));
	    assertThat(starter.getContext(), is((Context) mockActivity));
	}

	@Test
	public void testActivityStarterStartIntent() {
		final Activity mockActivity = mock(Activity.class);
		final Intent intent = new Intent();
		IntentStarters.activityStarter(mockActivity).startIntent(intent);
		verify(mockActivity, times(1)).startActivity(intent);
	}

	@Test
	@RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
	public void testActivityStarterStartIntentWithOptions() {
		assumeTrue(Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN);
		final Activity mockActivity = mock(Activity.class);
		final Intent intent = new Intent();
		IntentStarters.activityStarter(mockActivity).startIntent(intent, Bundle.EMPTY);
		verify(mockActivity, times(1)).startActivity(intent, Bundle.EMPTY);
	}

	@Test
	public void testActivityStarterStartIntentForResult() {
		final Activity mockActivity = mock(Activity.class);
		final Intent intent = new Intent();
		IntentStarters.activityStarter(mockActivity).startIntentForResult(intent, 1000);
		verify(mockActivity, times(1)).startActivityForResult(intent, 1000);
	}

	@Test
	@RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
	public void testActivityStarterStartIntentForResultWithOptions() {
		assumeTrue(Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN);
		final Activity mockActivity = mock(Activity.class);
		final Intent intent = new Intent();
		IntentStarters.activityStarter(mockActivity).startIntentForResult(intent, 1000, Bundle.EMPTY);
		verify(mockActivity, times(1)).startActivityForResult(intent, 1000, Bundle.EMPTY);
	}

	@Test
	public void testActivityStarterOverridePendingTransition() {
		final Activity mockActivity = mock(Activity.class);
		IntentStarters.activityStarter(mockActivity).overridePendingTransition(
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
	}

	@Test
	public void testFragmentStarter() throws Throwable {
		ACTIVITY_RULE.runOnUiThread(new Runnable() {

			@Override
			public void run() {
				final Activity activity = ACTIVITY_RULE.getActivity();
				final FragmentManager fragmentManager = activity.getFragmentManager();
				final Fragment fragment = new TestFragment();
				fragmentManager.beginTransaction().add(fragment, null).commit();
				fragmentManager.executePendingTransactions();
				final IntentStarter starter = IntentStarters.fragmentStarter(fragment);
				assertThat(starter, is(not(nullValue())));
				assertThat(starter.getContext(), is((Context) activity));
				fragmentManager.beginTransaction().remove(fragment).commit();
				fragmentManager.executePendingTransactions();
			}
		});
	}

	@Test
	public void testFragmentStarterStartIntent() {
		final Fragment mockFragment = mock(Fragment.class);
		final Intent intent = new Intent();
		IntentStarters.fragmentStarter(mockFragment).startIntent(intent);
		verify(mockFragment, times(1)).startActivity(intent);
	}

	@Test
	@RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
	public void testFragmentStarterStartIntentWithOptions() {
		assumeTrue(Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN);
		final Fragment mockFragment = mock(Fragment.class);
		final Intent intent = new Intent();
		IntentStarters.fragmentStarter(mockFragment).startIntent(intent, Bundle.EMPTY);
		verify(mockFragment, times(1)).startActivity(intent, Bundle.EMPTY);
	}

	@Test
	public void testFragmentStarterStartIntentForResult() {
		final Fragment mockFragment = mock(Fragment.class);
		final Intent intent = new Intent();
		IntentStarters.fragmentStarter(mockFragment).startIntentForResult(intent, 1000);
		verify(mockFragment, times(1)).startActivityForResult(intent, 1000);
	}

	@Test
	@RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
	public void testFragmentStarterStartIntentForResultWithOptions() {
		assumeTrue(Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN);
		final Fragment mockFragment = mock(Fragment.class);
		final Intent intent = new Intent();
		IntentStarters.fragmentStarter(mockFragment).startIntentForResult(intent, 1000, Bundle.EMPTY);
		verify(mockFragment, times(1)).startActivityForResult(intent, 1000, Bundle.EMPTY);
	}

	@Test
	public void testFragmentStarterOverridePendingTransition() throws Throwable {
		ACTIVITY_RULE.runOnUiThread(new Runnable() {

			@Override
			public void run() {
				final Activity activity = ACTIVITY_RULE.getActivity();
				final FragmentManager fragmentManager = activity.getFragmentManager();
				final Fragment fragment = new TestFragment();
				fragmentManager.beginTransaction().add(fragment, null).commit();
				fragmentManager.executePendingTransactions();
				IntentStarters.fragmentStarter(fragment).overridePendingTransition(
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
				fragmentManager.beginTransaction().remove(fragment).commit();
				fragmentManager.executePendingTransactions();
			}
		});
	}

	@Test
	public void testSupportFragmentStarter() throws Throwable {
		ACTIVITY_COMPAT_RULE.runOnUiThread(new Runnable() {

			@Override
			public void run() {
				final FragmentActivity activity = ACTIVITY_COMPAT_RULE.getActivity();
				final android.support.v4.app.FragmentManager fragmentManager = activity.getSupportFragmentManager();
				final android.support.v4.app.Fragment fragment = new TestCompatFragment();
				fragmentManager.beginTransaction().add(fragment, null).commit();
				fragmentManager.executePendingTransactions();
				final IntentStarter starter = IntentStarters.supportFragmentStarter(fragment);
				assertThat(starter, is(not(nullValue())));
				assertThat(starter.getContext(), is((Context) activity));
				fragmentManager.beginTransaction().remove(fragment).commit();
				fragmentManager.executePendingTransactions();
			}
		});
	}

	@Test
	public void testSupportFragmentStarterStartIntent() {
		final android.support.v4.app.Fragment mockFragment = mock(android.support.v4.app.Fragment.class);
		final Intent intent = new Intent();
		IntentStarters.supportFragmentStarter(mockFragment).startIntent(intent);
		verify(mockFragment, times(1)).startActivity(intent);
	}

	@Test
	public void testSupportFragmentStarterStartIntentWithOptions() {
		final android.support.v4.app.Fragment mockFragment = mock(android.support.v4.app.Fragment.class);
		final Intent intent = new Intent();
		IntentStarters.supportFragmentStarter(mockFragment).startIntent(intent, Bundle.EMPTY);
		verify(mockFragment, times(1)).startActivity(intent, Bundle.EMPTY);
	}

	@Test
	public void testSupportFragmentStarterStartIntentForResult() {
		final android.support.v4.app.Fragment mockFragment = mock(android.support.v4.app.Fragment.class);
		final Intent intent = new Intent();
		IntentStarters.supportFragmentStarter(mockFragment).startIntentForResult(intent, 1000);
		verify(mockFragment, times(1)).startActivityForResult(intent, 1000);
	}

	@Test
	public void testSupportFragmentStarterStartIntentForResultWithOptions() {
		final android.support.v4.app.Fragment mockFragment = mock(android.support.v4.app.Fragment.class);
		final Intent intent = new Intent();
		IntentStarters.supportFragmentStarter(mockFragment).startIntentForResult(intent, 1000, Bundle.EMPTY);
		verify(mockFragment, times(1)).startActivityForResult(intent, 1000, Bundle.EMPTY);
	}

	@Test
	public void testSupportFragmentStarterOverridePendingTransition() throws Throwable {
		ACTIVITY_COMPAT_RULE.runOnUiThread(new Runnable() {

			@Override
			public void run() {
				final FragmentActivity activity = ACTIVITY_COMPAT_RULE.getActivity();
				final android.support.v4.app.FragmentManager fragmentManager = activity.getSupportFragmentManager();
				final android.support.v4.app.Fragment fragment = new TestCompatFragment();
				fragmentManager.beginTransaction().add(fragment, null).commit();
				fragmentManager.executePendingTransactions();
				IntentStarters.supportFragmentStarter(fragment).overridePendingTransition(
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
				fragmentManager.beginTransaction().remove(fragment).commit();
				fragmentManager.executePendingTransactions();
			}
		});
	}
}
