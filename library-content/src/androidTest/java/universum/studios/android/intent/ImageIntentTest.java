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
import android.provider.MediaStore;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.File;
import java.util.Collections;
import java.util.List;

import universum.studios.android.test.BaseInstrumentedTest;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsNot.not;
import static org.hamcrest.core.IsNull.nullValue;
import static universum.studios.android.intent.ContentIntentTest.hasPath;
import static universum.studios.android.intent.ContentIntentTest.hasRelativePath;

/**
 * @author Martin Albedinsky
 */
@RunWith(AndroidJUnit4.class)
@SuppressWarnings("ResultOfMethodCallIgnored")
public final class ImageIntentTest extends BaseInstrumentedTest {

	@SuppressWarnings("unused")
	private static final String TAG = "ImageIntentTest";

	private ImageIntent mIntent;

	@Override
	public void beforeTest() throws Exception {
		super.beforeTest();
		this.mIntent = new ImageIntent();
	}

	@Override
	public void afterTest() throws Exception {
		super.afterTest();
		this.mIntent = null;
	}

	@Test
	public void testCreateGalleryIntent() {
		final Intent intent = ImageIntent.createGalleryIntent();
		assertThat(intent, is(not(nullValue())));
		assertThat(intent.getAction(), is(Intent.ACTION_GET_CONTENT));
		assertThat(intent.getType(), is(MimeType.IMAGE));
	}

	@Test
	public void testCreateCameraIntent() {
		final Intent intent = ImageIntent.createCameraIntent();
		assertThat(intent, is(not(nullValue())));
		assertThat(intent.getAction(), is(MediaStore.ACTION_IMAGE_CAPTURE));
		assertThat(intent.getParcelableExtra(MediaStore.EXTRA_OUTPUT), is(nullValue()));
	}

	@Test
	public void testCreateCameraIntentOutputFile() {
		final Intent intent = ImageIntent.createCameraIntent(new File("content://android/data/images", "camera-image.jpg"));
		assertThat(intent, is(not(nullValue())));
		assertThat(intent.<Uri>getParcelableExtra(MediaStore.EXTRA_OUTPUT), is(Uri.fromFile(new File("content://android/data/images", "camera-image.jpg"))));
	}

	@Test
	public void testCreateCameraIntentOutputUri() {
		final Intent intent = ImageIntent.createCameraIntent(Uri.parse("content://android/data/images/camera-image.jpg"));
		assertThat(intent, is(not(nullValue())));
		assertThat(intent.<Uri>getParcelableExtra(MediaStore.EXTRA_OUTPUT), is(Uri.parse("content://android/data/images/camera-image.jpg")));
	}

	@Test
	@SuppressWarnings("ConstantConditions")
	public void testCreateImageFile() {
		final File file = ImageIntent.createImageFile();
		if (file != null) {
			assertThat(file.exists(), is(true));
			assertThat(file, hasRelativePath("/Pictures/"));
			file.delete();
		}
	}

	@Test
	@SuppressWarnings("ConstantConditions")
	public void testCreateImageFileName() {
		final File file = ImageIntent.createImageFile("zebra-image");
		if (file != null) {
			assertThat(file.exists(), is(true));
			assertThat(file, hasPath("/Pictures/zebra-image.jpg"));
			file.delete();
		}
	}

	@Test
	public void testWithDefaultHandlers() {
		assertThat(mIntent.handlers(), is(Collections.EMPTY_LIST));
		mIntent.withDefaultHandlers(mContext);
		final List<ContentIntent.ContentHandler> handlers = mIntent.handlers();
		assertThat(handlers, is(not(nullValue())));
		assertThat(handlers.size(), is(2));
		final ContentIntent.ContentHandler galleryHandler = handlers.get(0);
		assertThat(galleryHandler.name().toString(), is("Gallery"));
		assertThat(galleryHandler.requestCode(), is(ImageIntent.REQUEST_CODE_GALLERY));
		final ContentIntent.ContentHandler cameraHandler = handlers.get(1);
		assertThat(cameraHandler.name().toString(), is("Camera"));
		assertThat(cameraHandler.requestCode(), is(ImageIntent.REQUEST_CODE_CAMERA));
	}

	@Test
	public void testInput() {
		assertThat(mIntent.dataType(), is(nullValue()));
		mIntent.input(new File("file.tmp"));
		assertThat(mIntent.dataType(), is(MimeType.IMAGE));
	}

	@Test
	public void testInputWithNullValue() {
		mIntent.input(new File("file.tmp"));
		assertThat(mIntent.dataType(), is(not(nullValue())));
		mIntent.input((Uri) null);
		assertThat(mIntent.uri(), is(nullValue()));
		assertThat(mIntent.dataType(), is(nullValue()));
	}

	@Test
	public void testOutputBeforeWithDefaultHandlers() {
		mIntent.output(new File("file.tmp"));
		mIntent.withDefaultHandlers(mContext);
		final ContentIntent.ContentHandler cameraHandler = mIntent.handlers().get(1);
		assertThat(cameraHandler.intent().<Uri>getParcelableExtra(MediaStore.EXTRA_OUTPUT), is(Uri.fromFile(new File("file.tmp"))));
	}

	@Test
	public void testOutputAfterWithDefaultHandlers() {
		mIntent.withDefaultHandlers(mContext);
		final ContentIntent.ContentHandler cameraHandler = mIntent.handlers().get(1);
		final Intent cameraIntent = cameraHandler.intent();
		assertThat(cameraIntent.getParcelableExtra(MediaStore.EXTRA_OUTPUT), is(nullValue()));
		mIntent.output(new File("file.tmp"));
		assertThat(cameraIntent.<Uri>getParcelableExtra(MediaStore.EXTRA_OUTPUT), is(Uri.fromFile(new File("file.tmp"))));
	}
}
