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

	@Test public void testIsActivityForIntentAvailable() {
		// Act:
		final boolean available = BaseIntent.isActivityForIntentAvailable(
				context,
				new Intent(Intent.ACTION_MAIN)
						.addCategory(Intent.CATEGORY_LAUNCHER)
						.setClassName("com.android.mms", "com.android.mms.ui.ConversationList")
		);
		// Assert:
		assertThat(available, is(true));
	}

	@Test public void testIsActivityForIntentAvailableWithEmptyIntent() {
		// Act:
		final boolean available = BaseIntent.isActivityForIntentAvailable(context, new Intent());
		// Assert:
		assertThat(available, is(false));
	}

	@Test public void testStartWith() {
		// Arrange:
		final IntentStarter mockStarter = mock(IntentStarter.class);
		when(mockStarter.getContext()).thenReturn(context);
		// Act:
		new IntentImpl().startWith(mockStarter);
		// Assert:
		verify(mockStarter).startIntent(any(Intent.class));
		verify(mockStarter, times(0)).overridePendingTransition(anyInt(), anyInt());
	}

	static final class IntentImpl extends BaseIntent<IntentImpl> {

		@Override @NonNull protected Intent onBuild(@NonNull final Context context) {
			return new Intent(Intent.ACTION_VIEW).putExtra(Intent.EXTRA_TEXT, "Content");
		}
	}
}