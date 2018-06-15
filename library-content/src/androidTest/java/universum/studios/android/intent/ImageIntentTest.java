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

import org.junit.Test;

import java.io.File;

import universum.studios.android.test.instrumented.InstrumentedTestCase;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static universum.studios.android.intent.ContentTests.hasPath;
import static universum.studios.android.intent.ContentTests.hasRelativePath;

/**
 * @author Martin Albedinsky
 */
@SuppressWarnings("ResultOfMethodCallIgnored")
public final class ImageIntentTest extends InstrumentedTestCase {

	@Test public void testCreateImageFile() {
		// Act:
		final File file = ImageIntent.createImageFile();
		// Assert:
		if (file != null) {
			assertThat(file.exists(), is(true));
			assertThat(file, hasRelativePath("/Pictures/"));
			file.delete();
		}
	}

	@Test public void testCreateImageFileName() {
		// Act:
		final File file = ImageIntent.createImageFile("zebra-image");
		// Assert:
		if (file != null) {
			assertThat(file.exists(), is(true));
			assertThat(file, hasPath("/Pictures/zebra-image.jpg"));
			file.delete();
		}
	}
}