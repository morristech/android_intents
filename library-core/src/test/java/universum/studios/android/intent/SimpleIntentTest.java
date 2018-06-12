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

import android.content.ComponentName;
import android.content.Intent;

import org.hamcrest.CoreMatchers;
import org.junit.Test;

import universum.studios.android.test.local.RobolectricTestCase;
import universum.studios.android.test.local.TestActivity;

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
public final class SimpleIntentTest extends RobolectricTestCase {

	@Test
	public void testActivityClassDefault() {
		assertThat(new SimpleIntent().activityClass(), is(nullValue()));
	}

	@Test
	public void testActivityClass() {
		final SimpleIntent intent = new SimpleIntent();
		intent.activityClass(TestActivity.class);
		assertEquals(TestActivity.class, intent.activityClass());
	}

	@Test
	public void testActionDefault() {
		assertEquals("", new SimpleIntent().action());
	}

	@Test
	public void testAction() {
		final SimpleIntent intent = new SimpleIntent();
		intent.action(Intent.ACTION_DIAL);
		assertEquals(Intent.ACTION_DIAL, intent.action());
	}

	@Test
	public void testFlagsDefault() {
		assertEquals(0, new SimpleIntent().flags());
	}

	@Test
	public void testFlags() {
		final SimpleIntent intent = new SimpleIntent();
		intent.flags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
		assertEquals(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK, intent.flags());
	}

	@Test
	public void testFlag() {
		final SimpleIntent intent = new SimpleIntent();
		intent.flags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.flag(Intent.FLAG_ACTIVITY_NO_ANIMATION);
		assertEquals(Intent.FLAG_ACTIVITY_CLEAR_TASK |
						Intent.FLAG_ACTIVITY_NEW_TASK |
						Intent.FLAG_ACTIVITY_NO_ANIMATION,
				intent.flags()
		);
	}

	@Test
	public void testRequestCodeDefault() {
		assertEquals(-1, new SimpleIntent().requestCode());
	}

	@Test
	public void testRequestCode() {
		final SimpleIntent intent = new SimpleIntent();
		intent.requestCode(1234);
		assertEquals(1234, intent.requestCode());
		intent.requestCode(-1);
		assertEquals(-1, intent.requestCode());
	}

	@Test
	@SuppressWarnings("ConstantConditions")
	public void testBuildWithActivityClass() {
		final SimpleIntent simpleIntent = new SimpleIntent();
		simpleIntent.activityClass(TestActivity.class);
		final Intent intent = simpleIntent.build(application);
		assertThat(intent, is(not(nullValue())));
		final ComponentName componentName = intent.getComponent();
		assertThat(componentName, is(not(nullValue())));
		assertThat(componentName.getClassName(), is(TestActivity.class.getName()));
		assertThat(componentName.getPackageName(), is(application.getPackageName()));
	}

	@Test
	@SuppressWarnings("ConstantConditions")
	public void testBuildWithActivityClassAndFlags() {
		final SimpleIntent simpleIntent = new SimpleIntent();
		simpleIntent.activityClass(TestActivity.class);
		simpleIntent.flag(Intent.FLAG_ACTIVITY_CLEAR_TASK);
		simpleIntent.flag(Intent.FLAG_ACTIVITY_NEW_TASK);
		final Intent intent = simpleIntent.build(application);
		assertThat(intent, is(not(CoreMatchers.nullValue())));
		assertThat(intent.getFlags(), is(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK));
		final ComponentName componentName = intent.getComponent();
		assertThat(componentName, is(not(nullValue())));
		assertThat(componentName.getClassName(), is(TestActivity.class.getName()));
		assertThat(componentName.getPackageName(), is(application.getPackageName()));
	}

	@Test
	public void testBuildWithAction() {
		final SimpleIntent simpleIntent = new SimpleIntent();
		simpleIntent.action(Intent.ACTION_SEARCH);
		simpleIntent.flags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		final Intent intent = simpleIntent.build(application);
		assertThat(intent, is(not(nullValue())));
		assertThat(intent.getAction(), is(Intent.ACTION_SEARCH));
		assertThat(intent.getFlags(), is(Intent.FLAG_ACTIVITY_CLEAR_TOP));
	}

	@Test
	public void testBuildWithoutParams() {
		assertThatBuildThrowsExceptionWithMessage(
				application,
				new SimpleIntent(),
				"No activity class or action specified."
		);
	}

	@Test
	@SuppressWarnings("ConstantConditions")
	public void testBuildWithInvalidActivityClass() {
		final SimpleIntent intent = new SimpleIntent();
		intent.activityClass(null);
		assertThatBuildThrowsExceptionWithMessage(
				application,
				intent,
				"No activity class specified."
		);
	}

	@Test
	@SuppressWarnings("ConstantConditions")
	public void testBuildWithInvalidAction() {
		final SimpleIntent intent = new SimpleIntent();
		intent.action(null);
		assertThatBuildThrowsExceptionWithMessage(
				application,
				intent,
				"No action specified."
		);
	}

	@Test
	public void testOnStartWith() {
		final SimpleIntent simpleIntent = new SimpleIntent();
		simpleIntent.activityClass(TestActivity.class);
		final Intent intent = simpleIntent.build(application);
		final IntentStarter mockStarter = mock(IntentStarter.class);
		assertThat(simpleIntent.onStartWith(mockStarter, intent), is(true));
		verify(mockStarter, times(1)).startIntent(intent);
	}

	@Test
	public void testOnStartWithForResult() {
		final SimpleIntent simpleIntent = new SimpleIntent();
		simpleIntent.activityClass(TestActivity.class);
		simpleIntent.requestCode(1000);
		final Intent intent = simpleIntent.build(application);
		final IntentStarter mockStarter = mock(IntentStarter.class);
		assertThat(simpleIntent.onStartWith(mockStarter, intent), is(true));
		verify(mockStarter, times(1)).startIntentForResult(intent, 1000);
	}
}