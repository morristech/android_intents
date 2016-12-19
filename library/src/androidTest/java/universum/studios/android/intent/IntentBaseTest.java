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

import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.util.AndroidRuntimeException;

import universum.studios.android.intent.inner.ContextBaseTest;

/**
 * @author Martin Albedinsky
 */
abstract class IntentBaseTest<I extends BaseIntent> extends ContextBaseTest {

	@SuppressWarnings("unused")
	private static final String TAG = "IntentBaseTest";

	private final Class<I> mIntentClass;
	I mIntent;

	IntentBaseTest() {
		this(null);
	}

	IntentBaseTest(Class<I> intentClass) {
		this.mIntentClass = intentClass;
	}

	@Override
	public void beforeTest() throws Exception {
		super.beforeTest();
		this.createIntent();
	}

	private void createIntent() {
		if (mIntentClass != null) {
			try {
				this.mIntent = mIntentClass.newInstance();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	@NonNull
	String getString(@StringRes int resId) {
		return mContext.getString(resId);
	}

	@NonNull
	CharSequence getText(@StringRes int resId) {
		return mContext.getText(resId);
	}

	void assertThatBuildThrowsExceptionWithCause(BaseIntent intent, String cause) {
		try {
			intent.build(mContext);
		} catch (AndroidRuntimeException e) {
			final String message = "Cannot build " + intent.getClass().getSimpleName() + ". " + cause;
			final String eMessage = e.getMessage();
			if (!message.contentEquals(eMessage)) {
				throw new AssertionError(
						"Expected exception with message <" + message + "> but message was <" + eMessage + ">"
				);
			}
			return;
		}
		final String intentName = intent.getClass().getSimpleName();
		throw new AssertionError("No exception has been thrown while building intent(" + intentName + ").");
	}
}