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

	@SuppressWarnings("unused")
	private static final String TAG = "PlayIntentTest";

	@Test
	public void testDeprecatedPackageName() {
		final PlayIntent intent = new PlayIntent();
		intent.packageName("com.google.android.inbox");
		assertThat(intent.packageName(), is("com.google.android.inbox"));
	}

	@Test
	public void tesDeprecatedPackageNameDefault() {
		assertThat(new PlayIntent().packageName(), is(""));
	}

	@Test
	public void testDeprecatedBuild() {
		final Intent intent = new PlayIntent().packageName("com.google.android.inbox").build(mApplication);
		assertThat(intent, is(not(nullValue())));
		assertThat(intent.getAction(), is(Intent.ACTION_VIEW));
		assertThat(intent.getData(), is(Uri.parse(PlayIntent.VIEW_URL_BASE + "com.google.android.inbox")));
	}

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
		final Intent intent = new PlayIntent().applicationId("com.google.android.inbox").build(mApplication);
		assertThat(intent, is(not(nullValue())));
		assertThat(intent.getAction(), is(Intent.ACTION_VIEW));
		assertThat(intent.getData(), is(Uri.parse(PlayIntent.VIEW_URL_BASE + "com.google.android.inbox")));
	}

	@Test
	public void testBuildWithDefaultPackageName() {
		final Intent intent = new PlayIntent().build(mApplication);
		assertThat(intent, is(not(nullValue())));
		assertThat(intent.getAction(), is(Intent.ACTION_VIEW));
		assertThat(intent.getData(), is(Uri.parse(PlayIntent.VIEW_URL_BASE + mApplication.getPackageName())));
	}
}
