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

import android.content.ComponentName;
import android.content.Intent;
import android.support.test.runner.AndroidJUnit4;

import org.hamcrest.CoreMatchers;
import org.junit.Test;
import org.junit.runner.RunWith;

import universum.studios.android.test.BaseInstrumentedTest;
import universum.studios.android.test.TestActivity;

import static junit.framework.Assert.assertEquals;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsNot.not;
import static org.hamcrest.core.IsNull.nullValue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static universum.studios.android.intent.CoreTests.assertThatBuildThrowsExceptionWithMessage;

/**
 * @author Martin Albedinsky
 */
@RunWith(AndroidJUnit4.class)
public final class SimpleIntentTest extends BaseInstrumentedTest {

	@SuppressWarnings("unused")
	private static final String TAG = "SimpleIntentTest";

	private SimpleIntent mIntent;

	@Override
	public void beforeTest() throws Exception {
		super.beforeTest();
		this.mIntent = new SimpleIntent();
	}

	@Override
	public void afterTest() throws Exception {
		super.afterTest();
		this.mIntent = null;
	}

	@Test
	public void testDefaultActivityClass() {
		assertThat(mIntent.activityClass(), is(nullValue()));
	}

	@Test
	public void testActivityClass() {
		mIntent.activityClass(TestActivity.class);
		assertEquals(TestActivity.class, mIntent.activityClass());
	}

	@Test
	public void testDefaultAction() {
		assertEquals("", mIntent.action());
	}

	@Test
	public void testAction() {
		mIntent.action(Intent.ACTION_DIAL);
		assertEquals(Intent.ACTION_DIAL, mIntent.action());
	}

	@Test
	public void testDefaultFlags() {
		assertEquals(0, mIntent.flags());
	}

	@Test
	public void testFlags() {
		mIntent.flags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
		assertEquals(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK, mIntent.flags());
	}

	@Test
	public void testFlag() {
		mIntent.flags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
		mIntent.flag(Intent.FLAG_ACTIVITY_NO_ANIMATION);
		assertEquals(Intent.FLAG_ACTIVITY_CLEAR_TASK |
						Intent.FLAG_ACTIVITY_NEW_TASK |
						Intent.FLAG_ACTIVITY_NO_ANIMATION,
				mIntent.flags()
		);
	}

	@Test
	public void testDefaultRequestCode() {
		assertEquals(-1, mIntent.requestCode());
	}

	@Test
	public void testRequestCode() {
		mIntent.requestCode(1234);
		assertEquals(1234, mIntent.requestCode());
		mIntent.requestCode(-1);
		assertEquals(-1, mIntent.requestCode());
	}

	@Test
	public void testBuildWithActivityClass() {
		mIntent.activityClass(TestActivity.class);
		final Intent intent = mIntent.build(mContext);
		assertThat(intent, is(not(nullValue())));
		final ComponentName componentName = intent.getComponent();
		assertThat(componentName, is(not(nullValue())));
		assertThat(componentName.getClassName(), is(TestActivity.class.getName()));
		assertThat(componentName.getPackageName(), is(mContext.getPackageName()));
	}

	@Test
	public void testBuildWithActivityClassAndFlags() {
		mIntent.activityClass(TestActivity.class);
		mIntent.flag(Intent.FLAG_ACTIVITY_CLEAR_TASK);
		mIntent.flag(Intent.FLAG_ACTIVITY_NEW_TASK);
		final Intent intent = mIntent.build(mContext);
		assertThat(intent, is(not(CoreMatchers.nullValue())));
		assertThat(intent.getFlags(), is(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK));
		final ComponentName componentName = intent.getComponent();
		assertThat(componentName, is(not(nullValue())));
		assertThat(componentName.getClassName(), is(TestActivity.class.getName()));
		assertThat(componentName.getPackageName(), is(mContext.getPackageName()));
	}

	@Test
	public void testBuildWithAction() {
		mIntent.action(Intent.ACTION_SEARCH);
		mIntent.flags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		final Intent intent = mIntent.build(mContext);
		assertThat(intent, is(not(nullValue())));
		assertThat(intent.getAction(), is(Intent.ACTION_SEARCH));
		assertThat(intent.getFlags(), is(Intent.FLAG_ACTIVITY_CLEAR_TOP));
	}

	@Test
	public void testBuildWithoutParams() {
		assertThatBuildThrowsExceptionWithMessage(
				mContext,
				mIntent,
				"No activity class or action specified."
		);
	}

	@Test
	@SuppressWarnings("ConstantConditions")
	public void testBuildWithInvalidActivityClass() {
		mIntent.activityClass(null);
		assertThatBuildThrowsExceptionWithMessage(
				mContext,
				mIntent,
				"No activity class specified."
		);
	}

	@Test
	@SuppressWarnings("ConstantConditions")
	public void testBuildWithInvalidAction() {
		mIntent.action(null);
		assertThatBuildThrowsExceptionWithMessage(
				mContext,
				mIntent,
				"No action specified."
		);
	}

	@Test
	public void testOnStartWith() {
		mIntent.activityClass(TestActivity.class);
		final Intent intent = mIntent.build(mContext);
		final IntentStarter mockStarter = mock(IntentStarter.class);
		assertThat(mIntent.onStartWith(mockStarter, intent), is(true));
		verify(mockStarter, times(1)).startIntent(intent);
	}

	@Test
	public void testOnStartWithForResult() {
		mIntent.activityClass(TestActivity.class);
		mIntent.requestCode(1000);
		final Intent intent = mIntent.build(mContext);
		final IntentStarter mockStarter = mock(IntentStarter.class);
		assertThat(mIntent.onStartWith(mockStarter, intent), is(true));
		verify(mockStarter, times(1)).startIntentForResult(intent, 1000);
	}
}