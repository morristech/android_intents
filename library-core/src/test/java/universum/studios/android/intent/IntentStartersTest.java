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
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import org.junit.Test;
import org.robolectric.Robolectric;
import org.robolectric.annotation.Config;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import universum.studios.android.test.local.RobolectricTestCase;
import universum.studios.android.test.local.TestActivity;
import universum.studios.android.test.local.TestCompatActivity;
import universum.studios.android.test.local.TestCompatFragment;
import universum.studios.android.test.local.TestFragment;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.core.IsInstanceOf.instanceOf;
import static org.hamcrest.core.IsNot.not;
import static org.hamcrest.core.IsNull.nullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

/**
 * @author Martin Albedinsky
 */
public final class IntentStartersTest extends RobolectricTestCase {
    
	@Test(expected = IllegalAccessException.class)
	public void testInstantiation() throws Exception {
		IntentStarters.class.newInstance();
	}

	@Test(expected = InvocationTargetException.class)
	public void testInstantiationWithAccessibleConstructor() throws Exception {
		final Constructor<IntentStarters> constructor = IntentStarters.class.getDeclaredConstructor();
		constructor.setAccessible(true);
		constructor.newInstance();
	}

    @Test
	public void testActivityStarter() {
	    final Activity activity = Robolectric.buildActivity(TestActivity.class).create().get();
		final IntentStarter starter = IntentStarters.activityStarter(activity);
	    assertThat(starter, is(not(nullValue())));
	    assertThat(starter.getContext(), is((Context) activity));
	}

	@Test
	public void testActivityStarterStartIntent() {
		final Activity mockActivity = mock(Activity.class);
		final Intent intent = new Intent();
		IntentStarters.activityStarter(mockActivity).startIntent(intent);
		verify(mockActivity, times(1)).startActivity(intent);
	}

	@Test
	@Config(sdk = Build.VERSION_CODES.JELLY_BEAN)
	public void testActivityStarterStartIntentWithOptions() {
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
	@Config(sdk = Build.VERSION_CODES.JELLY_BEAN)
	public void testActivityStarterStartIntentForResultWithOptions() {
		final Activity mockActivity = mock(Activity.class);
		final Intent intent = new Intent();
		IntentStarters.activityStarter(mockActivity).startIntentForResult(intent, 1000, Bundle.EMPTY);
		verify(mockActivity, times(1)).startActivityForResult(intent, 1000, Bundle.EMPTY);
	}

	@Test
	public void testActivityStarterOverridePendingTransition() {
		final Activity mockActivity = mock(Activity.class);
		IntentStarters.activityStarter(mockActivity).overridePendingTransition(
				android.R.anim.fade_in,
				android.R.anim.fade_out
		);
	}

	@Test
	public void testFragmentStarter() throws IllegalStateException {
		final Fragment fragment = Robolectric.buildFragment(TestFragment.class, TestActivity.class).create(TestActivity.CONTENT_VIEW_ID, null).get();
		final IntentStarter starter = IntentStarters.fragmentStarter(fragment);
		assertThat(starter, is(not(nullValue())));
		assertThat(starter.getContext(), instanceOf(TestActivity.class));
	}

	@Test
	public void testFragmentStarterStartIntent() {
		final Fragment mockFragment = mock(Fragment.class);
		final Intent intent = new Intent();
		IntentStarters.fragmentStarter(mockFragment).startIntent(intent);
		verify(mockFragment, times(1)).startActivity(intent);
	}

	@Test
	@Config(sdk = Build.VERSION_CODES.JELLY_BEAN)
	public void testFragmentStarterStartIntentWithOptions() {
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
	@Config(sdk = Build.VERSION_CODES.JELLY_BEAN)
	public void testFragmentStarterStartIntentForResultWithOptions() {
		final Fragment mockFragment = mock(Fragment.class);
		final Intent intent = new Intent();
		IntentStarters.fragmentStarter(mockFragment).startIntentForResult(intent, 1000, Bundle.EMPTY);
		verify(mockFragment, times(1)).startActivityForResult(intent, 1000, Bundle.EMPTY);
	}

	@Test
	public void testFragmentStarterOverridePendingTransition() throws IllegalStateException {
		final Fragment fragment = Robolectric.buildFragment(TestFragment.class, TestActivity.class).create(TestActivity.CONTENT_VIEW_ID, null).get();
		IntentStarters.fragmentStarter(fragment).overridePendingTransition(
				android.R.anim.fade_in,
				android.R.anim.fade_out
		);
	}

	@Test
	public void testSupportFragmentStarter() throws IllegalStateException {
		final FragmentActivity activity = Robolectric.buildActivity(TestCompatActivity.class).create().start().resume().get();
		final android.support.v4.app.FragmentManager fragmentManager = activity.getSupportFragmentManager();
		final android.support.v4.app.Fragment fragment = new TestCompatFragment();
		fragmentManager.beginTransaction().add(fragment, null).commitAllowingStateLoss();
		fragmentManager.executePendingTransactions();
		final IntentStarter starter = IntentStarters.supportFragmentStarter(fragment);
		assertThat(starter, is(not(nullValue())));
		assertThat(starter.getContext(), is((Context) activity));
		fragmentManager.beginTransaction().remove(fragment).commitAllowingStateLoss();
		fragmentManager.executePendingTransactions();
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
	public void testSupportFragmentStarterOverridePendingTransition() throws IllegalStateException {
		final FragmentActivity activity = Robolectric.buildActivity(TestCompatActivity.class).create().start().resume().get();
		final android.support.v4.app.FragmentManager fragmentManager = activity.getSupportFragmentManager();
		final android.support.v4.app.Fragment fragment = new TestCompatFragment();
		fragmentManager.beginTransaction().add(fragment, null).commitAllowingStateLoss();
		fragmentManager.executePendingTransactions();
		IntentStarters.supportFragmentStarter(fragment).overridePendingTransition(
				android.R.anim.fade_in,
				android.R.anim.fade_out
		);
		fragmentManager.beginTransaction().remove(fragment).commitAllowingStateLoss();
		fragmentManager.executePendingTransactions();
	}
}