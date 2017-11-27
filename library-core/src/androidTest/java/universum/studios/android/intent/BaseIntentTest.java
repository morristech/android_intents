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

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;

import org.junit.Test;

import universum.studios.android.test.instrumented.InstrumentedTestCase;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * @author Martin Albedinsky
 */
public final class BaseIntentTest extends InstrumentedTestCase {

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
	public void testStartWith() {
		final IntentStarter mockStarter = mock(IntentStarter.class);
		when(mockStarter.getContext()).thenReturn(mContext);
		new IntentImpl().startWith(mockStarter);
		verify(mockStarter, times(1)).startIntent(any(Intent.class));
		verify(mockStarter, times(0)).overridePendingTransition(anyInt(), anyInt());
	}

	static final class IntentImpl extends BaseIntent<IntentImpl> {

		@NonNull
		@Override
		protected Intent onBuild(@NonNull Context context) {
			return new Intent(Intent.ACTION_VIEW).putExtra(Intent.EXTRA_TEXT, "Content");
		}
	}
}
