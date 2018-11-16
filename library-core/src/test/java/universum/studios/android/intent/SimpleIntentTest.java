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

import org.junit.Test;

import universum.studios.android.test.local.RobolectricTestCase;
import universum.studios.android.test.local.TestActivity;

import static junit.framework.Assert.assertEquals;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static universum.studios.android.intent.CoreTests.assertThatBuildThrowsExceptionWithMessage;

/**
 * @author Martin Albedinsky
 */
public final class SimpleIntentTest extends RobolectricTestCase {

	@Test public void testInstantiation() {
		// Act:
		final SimpleIntent intent = new SimpleIntent();
		// Assert:
		assertThat(intent.activityClass(), is(nullValue()));
		assertThat(intent.action(), is(""));
		assertThat(intent.flags(), is(0));
		assertThat(intent.requestCode(), is(-1));
	}

	@Test public void testActivityClass() {
		// Arrange:
		final SimpleIntent intent = new SimpleIntent();
		// Act:
		intent.activityClass(TestActivity.class);
		// Assert:
		assertEquals(TestActivity.class, intent.activityClass());
	}

	@Test public void testAction() {
		// Arrange:
		final SimpleIntent intent = new SimpleIntent();
		// Act:
		intent.action(Intent.ACTION_DIAL);
		// Assert:
		assertThat(intent.action(), is(Intent.ACTION_DIAL));
	}

	@Test public void testFlags() {
		// Arrange:
		final SimpleIntent intent = new SimpleIntent();
		// Act:
		intent.flags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
		// Assert:
		assertThat(intent.flags(), is(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK));
	}

	@Test public void testFlag() {
		// Arrange:
		final SimpleIntent intent = new SimpleIntent();
		// Act:
		intent.flags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.flag(Intent.FLAG_ACTIVITY_NO_ANIMATION);
		// Assert:
		assertThat(intent.flags(), is(Intent.FLAG_ACTIVITY_CLEAR_TASK |
				Intent.FLAG_ACTIVITY_NEW_TASK |
				Intent.FLAG_ACTIVITY_NO_ANIMATION));
	}

	@Test public void testRequestCode() {
		// Arrange:
		final SimpleIntent intent = new SimpleIntent();
		// Act + Assert:
		intent.requestCode(1234);
		assertThat(intent.requestCode(), is(1234));
		intent.requestCode(-1);
		assertThat(intent.requestCode(), is(-1));
	}

	@SuppressWarnings("ConstantConditions")
	@Test public void testBuildWithActivityClass() {
		// Arrange:
		final SimpleIntent simpleIntent = new SimpleIntent();
		simpleIntent.activityClass(TestActivity.class);
		// Act:
		final Intent intent = simpleIntent.build(context);
		// Assert:
		assertThat(intent, is(notNullValue()));
		final ComponentName componentName = intent.getComponent();
		assertThat(componentName, is(notNullValue()));
		assertThat(componentName.getClassName(), is(TestActivity.class.getName()));
		assertThat(componentName.getPackageName(), is(context.getPackageName()));
	}

	@SuppressWarnings("ConstantConditions")
	@Test public void testBuildWithActivityClassAndFlags() {
		// Arrange:
		final SimpleIntent simpleIntent = new SimpleIntent();
		simpleIntent.activityClass(TestActivity.class);
		simpleIntent.flag(Intent.FLAG_ACTIVITY_CLEAR_TASK);
		simpleIntent.flag(Intent.FLAG_ACTIVITY_NEW_TASK);
		// Act:
		final Intent intent = simpleIntent.build(context);
		// Assert:
		assertThat(intent, is(notNullValue()));
		assertThat(intent.getFlags(), is(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK));
		final ComponentName componentName = intent.getComponent();
		assertThat(componentName, is(notNullValue()));
		assertThat(componentName.getClassName(), is(TestActivity.class.getName()));
		assertThat(componentName.getPackageName(), is(context.getPackageName()));
	}

	@Test public void testBuildWithAction() {
		// Arrange:
		final SimpleIntent simpleIntent = new SimpleIntent();
		simpleIntent.action(Intent.ACTION_SEARCH);
		simpleIntent.flags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		// Act:
		final Intent intent = simpleIntent.build(context);
		// Assert:
		assertThat(intent, is(notNullValue()));
		assertThat(intent.getAction(), is(Intent.ACTION_SEARCH));
		assertThat(intent.getFlags(), is(Intent.FLAG_ACTIVITY_CLEAR_TOP));
	}

	@Test public void testBuildWithoutParams() {
		assertThatBuildThrowsExceptionWithMessage(
				context,
				new SimpleIntent(),
				"No activity class or action specified."
		);
	}

	@SuppressWarnings("ConstantConditions")
	@Test public void testBuildWithInvalidActivityClass() {
		// Arrange:
		final SimpleIntent intent = new SimpleIntent();
		intent.activityClass(null);
		// Act + Assert:
		assertThatBuildThrowsExceptionWithMessage(
				context,
				intent,
				"No activity class specified."
		);
	}

	@SuppressWarnings("ConstantConditions")
	@Test public void testBuildWithInvalidAction() {
		// Arrange:
		final SimpleIntent intent = new SimpleIntent();
		intent.action(null);
		// Act + Assert:
		assertThatBuildThrowsExceptionWithMessage(
				context,
				intent,
				"No action specified."
		);
	}

	@Test public void testOnStartWith() {
		// Arrange:
		final SimpleIntent simpleIntent = new SimpleIntent();
		simpleIntent.activityClass(TestActivity.class);
		final Intent intent = simpleIntent.build(context);
		final IntentStarter mockStarter = mock(IntentStarter.class);
		// Act + Assert:
		assertThat(simpleIntent.onStartWith(mockStarter, intent), is(true));
		verify(mockStarter).startIntent(intent);
	}

	@Test public void testOnStartWithForResult() {
		// Arrange:
		final SimpleIntent simpleIntent = new SimpleIntent();
		simpleIntent.activityClass(TestActivity.class);
		simpleIntent.requestCode(1000);
		final Intent intent = simpleIntent.build(context);
		final IntentStarter mockStarter = mock(IntentStarter.class);
		// Act + Assert:
		assertThat(simpleIntent.onStartWith(mockStarter, intent), is(true));
		verify(mockStarter).startIntentForResult(intent, 1000);
	}
}