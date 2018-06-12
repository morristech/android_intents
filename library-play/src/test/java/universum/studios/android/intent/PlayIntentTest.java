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

import android.content.Intent;
import android.net.Uri;

import org.junit.Test;

import universum.studios.android.test.local.RobolectricTestCase;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsNot.not;

/**
 * @author Martin Albedinsky
 */
@SuppressWarnings("deprecation")
public final class PlayIntentTest extends RobolectricTestCase {

	@Test
	public void testApplicationId() {
		final PlayIntent intent = new PlayIntent();
		intent.applicationId("com.google.android.inbox");
		assertThat(intent.applicationId(), is("com.google.android.inbox"));
	}

	@Test
	public void tesApplicationIdDefault() {
		assertThat(new PlayIntent().applicationId(), is(""));
	}

	@Test
	public void testBuild() {
		final Intent intent = new PlayIntent().applicationId("com.google.android.inbox").build(application);
		assertThat(intent, is(not(nullValue())));
		assertThat(intent.getAction(), is(Intent.ACTION_VIEW));
		assertThat(intent.getData(), is(Uri.parse(PlayIntent.VIEW_URL_BASE + "com.google.android.inbox")));
	}

	@Test
	public void testBuildWithDefaultPackageName() {
		final Intent intent = new PlayIntent().build(application);
		assertThat(intent, is(not(nullValue())));
		assertThat(intent.getAction(), is(Intent.ACTION_VIEW));
		assertThat(intent.getData(), is(Uri.parse(PlayIntent.VIEW_URL_BASE + application.getPackageName())));
	}
}