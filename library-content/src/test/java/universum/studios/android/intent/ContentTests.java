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
import android.support.annotation.NonNull;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

import java.io.File;

import universum.studios.android.test.local.TestUtils;

/**
 * @author Martin Albedinsky
 */
final class ContentTests {

	@NonNull
	static Matcher<File> hasPath(String path) {
		return new FilePath(TestUtils.STORAGE_BASE_PATH + path);
	}

	@NonNull
	static Matcher<File> hasRelativePath(String path) {
		return new FileRelativePath(TestUtils.STORAGE_BASE_PATH + path);
	}

	static void assertThatBuildThrowsExceptionWithMessage(@NonNull Context context, @NonNull BaseIntent intent, @NonNull String exceptionMessage) {
		try {
			intent.build(context);
		} catch (IllegalArgumentException e) {
			final String message = "Cannot build " + intent.getClass().getSimpleName() + ". " + exceptionMessage;
			final String eMessage = e.getMessage();
			if (!eMessage.contentEquals(message)) {
				throw new AssertionError(
						"Expected exception with message <" + message + "> but message was <" + eMessage + ">"
				);
			}
			return;
		}
		final String intentName = intent.getClass().getSimpleName();
		throw new AssertionError("No exception has been thrown while building intent(" + intentName + ").");
	}

	private static final class FilePath extends TypeSafeMatcher<File> {

		private final String expected;

		FilePath(String expected) {
			this.expected = expected;
		}

		@Override
		protected boolean matchesSafely(File item) {
			return expected.equals(item.getPath());
		}

		@Override
		public void describeTo(Description description) {
			// Ignored.
		}
	}

	private static final class FileRelativePath extends TypeSafeMatcher<File> {

		private final String expected;

		FileRelativePath(String expected) {
			this.expected = expected;
		}

		@Override
		protected boolean matchesSafely(File item) {
			return expected.equals(item.getPath().replace(item.getName(), ""));
		}

		@Override
		public void describeTo(Description description) {
			// Ignored
		}
	}
}