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

import android.app.Activity;
import android.app.Fragment;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import java.io.File;
import java.io.IOException;

/**
 * A {@link ContentIntent} implementation providing API for building and starting of intents to obtain
 * or preview an image content.
 *
 * @author Martin Albedinsky
 */
public class ImageIntent extends ContentIntent<ImageIntent> {

	/**
	 * Constants ===================================================================================
	 */

	/**
	 * Log TAG.
	 */
	private static final String TAG = "ImageIntent";

	/**
	 * Flag to identify request code used to obtain image from gallery.
	 */
	public static final int REQUEST_CODE_GALLERY = 0x5001;

	/**
	 * Flag to identify request code used to obtain image using camera.
	 */
	public static final int REQUEST_CODE_CAMERA = 0x5002;

	/**
	 * Default format for image file name.
	 * <p>
	 * Constant value: <b>IMAGE_%s</b>
	 */
	public static final String IMAGE_FILE_NAME_FORMAT = "IMAGE_%s";

	/**
	 * Interface ===================================================================================
	 */

	/**
	 * Static members ==============================================================================
	 */

	/**
	 * Members =====================================================================================
	 */

	/**
	 * Camera intent handler.
	 */
	private ContentHandler mCameraHandler;

	/**
	 * Constructors ================================================================================
	 */

	/**
	 * Methods =====================================================================================
	 */

	/**
	 * Creates a new instance of Intent with {@link Intent#ACTION_GET_CONTENT} and {@link MimeType#IMAGE}
	 * MIME type that can be used to launch a gallery app (depends on user's choice) to pick one of
	 * available images.
	 *
	 * @return New gallery intent instance.
	 */
	@NonNull
	public static Intent createGalleryIntent() {
		return new Intent(Intent.ACTION_GET_CONTENT).setType(MimeType.IMAGE);
	}

	/**
	 * Same as {@link #createCameraIntent(File)} with {@code null} for <var>outputFile</var> parameter.
	 */
	@NonNull
	public static Intent createCameraIntent() {
		return createCameraIntent((Uri) null);
	}

	/**
	 * Same as {@link #createCameraIntent(Uri)} with <var>outputUri</var> created from the given
	 * <var>outputFile</var> if not {@code nul}.
	 *
	 * @param outputFile The desired file used to crate the output Uri.
	 * @see #createCameraIntent()
	 */
	@NonNull
	public static Intent createCameraIntent(@Nullable File outputFile) {
		return createCameraIntent(outputFile == null ? null : Uri.fromFile(outputFile));
	}

	/**
	 * Creates a new instance of Intent with {@link MediaStore#ACTION_IMAGE_CAPTURE} that can be used
	 * to launch a camera app to capture a single image.
	 *
	 * @param outputUri If not {@code null}, it will be attached to intent as {@link MediaStore#EXTRA_OUTPUT},
	 *                  so when the result is received in for example {@link Activity#onActivityResult(int, int, Intent)},
	 *                  the <var>outputUri</var> will address to a file which contains the currently
	 *                  captured image.
	 * @return New camera intent instance.
	 * @see #createCameraIntent(File)
	 */
	@NonNull
	public static Intent createCameraIntent(@Nullable Uri outputUri) {
		final Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		if (outputUri != null) {
			intent.putExtra(MediaStore.EXTRA_OUTPUT, outputUri);
		}
		return intent;
	}

	/**
	 * Same as {@link #createImageFile(String)} with <var>fileName</var> in {@link #IMAGE_FILE_NAME_FORMAT}
	 * format with a string representation of the current time stamp obtained via {@link #createContentFileTimeStamp()}.
	 */
	@Nullable
	public static File createImageFile() {
		return createImageFile(String.format(IMAGE_FILE_NAME_FORMAT, createContentFileTimeStamp()));
	}

	/**
	 * Same as {@link #createContentFile(String,  String)} with <b>.jpg</b> suffix for the specified
	 * <var>fileName</var> (if it does not contain any) and {@link Environment#DIRECTORY_PICTURES} as
	 * <var>externalDirectoryType</var>.
	 *
	 * @param fileName The desired name for the image file.
	 * @see #createImageFile()
	 */
	@Nullable
	public static File createImageFile(@NonNull String fileName) {
		return createContentFile(appendDefaultFileSuffixIfNotPresented(fileName, ".jpg"), Environment.DIRECTORY_PICTURES);
	}

	/**
	 * Processes the given result <var>data</var> intent to obtain a user's picked image.
	 * <p>
	 * <b>Note</b>, that in case of {@link #REQUEST_CODE_CAMERA}, the captured photo's bitmap will be
	 * received only in quality of "place holder" image, not as full quality image. For full quality
	 * photo pass an instance of Uri to {@link #output(Uri)} and the bitmap of the captured
	 * photo will be stored on the specified uri.
	 *
	 * @param requestCode The request code from {@link Activity#onActivityResult(int, int, Intent)} or
	 *                    {@link Fragment#onActivityResult(int, int, Intent)}.
	 *                    Can be only one of {@link #REQUEST_CODE_CAMERA} or {@link #REQUEST_CODE_GALLERY}.
	 * @param resultCode  The result code from {@link Activity#onActivityResult(int, int, Intent)} or
	 *                    {@link Fragment#onActivityResult(int, int, Intent)}.
	 *                    If {@link Activity#RESULT_OK}, the passed <var>data</var> will be processed,
	 *                    otherwise {@code null} will be returned.
	 * @param data        The data from {@link Activity#onActivityResult(int, int, Intent)} or
	 *                    {@link Fragment#onActivityResult(int, int, Intent)}.
	 * @param context     Current valid context.
	 * @param options     Image options to adjust obtained bitmap.
	 * @return Instance of Bitmap obtained from the given <var>data</var> Intent.
	 */
	@Nullable
	@SuppressWarnings("deprecation")
	static Bitmap processResultIntent(int requestCode, int resultCode, @Nullable Intent data, @NonNull Context context, @Nullable ImageOptions options) {
		if (data == null || resultCode != Activity.RESULT_OK) {
			// User canceled the intent or no data are available.
			return null;
		}
		switch (requestCode) {
			case REQUEST_CODE_GALLERY:
				final Uri imageUri = data.getData();
				if (imageUri == null) {
					return null;
				}
				Bitmap galleryImage = null;
				final ContentResolver contentResolver = context.getContentResolver();
				try {
					if (options == null) {
						galleryImage = BitmapFactory.decodeStream(contentResolver.openInputStream(imageUri));
					} else {
						// Get the dimensions of the returned bitmap.
						final BitmapFactory.Options bmOptions = new BitmapFactory.Options();
						bmOptions.inJustDecodeBounds = true;
						BitmapFactory.decodeStream(contentResolver.openInputStream(imageUri), null, bmOptions);
						final int bmWidth = bmOptions.outWidth;
						final int bmHeight = bmOptions.outHeight;
						// Compute how much to scale the bitmap.
						final int scaleFactor = Math.min(bmWidth / options.width, bmHeight / options.height);
						// Decode scaled bitmap.
						bmOptions.inJustDecodeBounds = false;
						bmOptions.inSampleSize = scaleFactor;
						bmOptions.inPurgeable = true;
						galleryImage = BitmapFactory.decodeStream(contentResolver.openInputStream(imageUri), null, bmOptions);
					}
				} catch (IOException e) {
					Log.e(TAG, "Unable to open stream to image content at uri(" + imageUri + ").", e);
				}
				return galleryImage;
			case REQUEST_CODE_CAMERA:
				final Bundle extras = data.getExtras();
				final Bitmap cameraImage = extras == null ? null : (Bitmap) extras.get("data");
				if (cameraImage != null && options != null) {
					return Bitmap.createScaledBitmap(cameraImage, options.width, options.height, false);
				}
				return cameraImage;
			default:
				return null;
		}
	}

	/**
	 * Adds two default {@link ContentHandler}s. One for {@link #REQUEST_CODE_GALLERY} and second one
	 * for {@link #REQUEST_CODE_CAMERA}.
	 */
	@Override
	@SuppressWarnings("ConstantConditions")
	public ImageIntent withDefaultHandlers(@NonNull Context context) {
		withHandlers(
				onCreateGalleryHandler(context.getResources()),
				mCameraHandler = onCreateCameraHandler(context.getResources())
		);
		if (mUri == null) {
			mCameraHandler.intent.removeExtra(MediaStore.EXTRA_OUTPUT);
		} else {
			mCameraHandler.intent.putExtra(MediaStore.EXTRA_OUTPUT, mUri);
		}
		return this;
	}

	/**
	 * Invoked from {@link #withDefaultHandlers(Context)} to create gallery intent handler.
	 * <p>
	 * This method may be override to create a handler with a localized name.
	 *
	 * @param resources Application resources.
	 * @return Content handler with gallery intent.
	 * @see #onCreateCameraHandler(Resources)
	 */
	@NonNull
	protected ContentHandler onCreateGalleryHandler(@NonNull Resources resources) {
		return new ContentHandler(
				"Gallery",
				createGalleryIntent()
		).requestCode(REQUEST_CODE_GALLERY);
	}

	/**
	 * Invoked from {@link #withDefaultHandlers(Context)} to create camera intent handler.
	 * <p>
	 * This method may be override to create a handler with a localized name.
	 *
	 * @param resources Application resources.
	 * @return Content handler with camera intent.
	 * @see #onCreateGalleryHandler(Resources)
	 */
	@NonNull
	protected ContentHandler onCreateCameraHandler(@NonNull Resources resources) {
		return new ContentHandler(
				"Camera",
				createCameraIntent()
		).requestCode(REQUEST_CODE_CAMERA);
	}

	/**
	 * If the passed <var>uri</var> is not {@code null}, the current data (MIME) type will be set
	 * by default to {@link MimeType#IMAGE}.
	 */
	@Override
	public ImageIntent input(@Nullable Uri uri) {
		super.input(uri);
		if (uri != null) {
			this.mDataType = MimeType.IMAGE;
		}
		return this;
	}

	/**
	 * Sets an output uri for an image to be captured by the camera.
	 *
	 * @param uri If not {@code null}, it will be attached to {@link ContentHandler}
	 *            holding intent with the {@link #REQUEST_CODE_CAMERA} request code, so when the camera
	 *            handler's item is selected within a <b>chooser dialog</b> and user confirms his
	 *            captured photo, result for this action will be received in for example
	 *            {@link Activity#onActivityResult(int, int, Intent)}. The passed
	 *            <var>uri</var> will then address to a file which contains the currently captured
	 *            image.
	 * @return This intent builder to allow methods chaining.
	 */
	@Override
	public ImageIntent output(@Nullable Uri uri) {
		super.output(uri);
		if (mCameraHandler != null) {
			if (mUri == null) {
				mCameraHandler.intent.removeExtra(MediaStore.EXTRA_OUTPUT);
			} else {
				mCameraHandler.intent.putExtra(MediaStore.EXTRA_OUTPUT, mUri);
			}
		}
		return this;
	}

	/**
	 * Inner classes ===============================================================================
	 */

	/**
	 * Simple options for {@link #processResultIntent(int, int, Intent, Context, ImageOptions)}.
	 *
	 * @author Martin Albedinsky
	 */
	public static class ImageOptions {

		/**
		 * Dimensions to which should be obtained image bitmap re-sized.
		 */
		int width, height;

		/**
		 * Sets the dimensions to which should be the obtained image bitmap re-sized.
		 *
		 * @param width  The desired image width to re-size to.
		 * @param height The desired image height to re-size to.
		 * @return This options instance.
		 */
		public ImageOptions inSize(@IntRange(from = 0, to = Integer.MAX_VALUE) int width, @IntRange(from = 0, to = Integer.MAX_VALUE) int height) {
			this.width = width;
			this.height = height;
			return this;
		}
	}
}
