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

import android.support.annotation.NonNull;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

import java.io.File;

import universum.studios.android.test.instrumented.TestUtils;

/**
 * @author Martin Albedinsky
 */
final class ContentTests {

	@NonNull static Matcher<File> hasPath(@NonNull final String path) {
		return new FileHasPath(TestUtils.STORAGE_BASE_PATH + path);
	}

	@NonNull static Matcher<File> hasRelativePath(@NonNull final String path) {
		return new FileHasRelativePath(TestUtils.STORAGE_BASE_PATH + path);
	}

	private static final class FileHasPath extends TypeSafeMatcher<File> {

		private final String expected;

		FileHasPath(final String expected) {
			this.expected = expected;
		}

		@Override protected boolean matchesSafely(@NonNull final File item) {
			return expected.equals(item.getPath());
		}

		@Override public void describeTo(Description description) {}
	}

	private static final class FileHasRelativePath extends TypeSafeMatcher<File> {

		private final String expected;

		FileHasRelativePath(final String expected) {
			this.expected = expected;
		}

		@Override protected boolean matchesSafely(@NonNull final File item) {
			return expected.equals(item.getPath().replace(item.getName(), ""));
		}

		@Override public void describeTo(Description description) {}
	}
}