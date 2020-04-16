package nl.mahmood.emojifyexercise;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.File;
import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import timber.log.Timber;

//import static androidx.core.content.FileProvider.getUriForFile;

/**
 * pre requirement
 * 1 - copy all image inside drawable folder
 * 2 - create two dimens.xml one in value folder and one in values-w820dp folder
 * 3 - create file_paths.xml in xml folder inside values
 * do TODO (1) create file_paths.xml
 * do TODO (2) change style.xml code
 * do TODO (3) add values in strings.xml
 * do TODO (4,5) add value in dimens.xml inside two folders
 * do TODO (6) change colors in colors.xml
 * do TODO (7) add views in activity_main.xml
 * do TODO (8) create BitmapUtils class and write all cods
 * do TODO (9) add file provider to AndroidManifest.xml
 * do steps 1 to 10 in MainActivity class
 * do TODO (10) Add Google Mobile Vision Library dependency
 * do TODO (11) create Emojifier class and write all cods
 * do TODO (12) Add ButterKnife Library dependency
 * do TODO (13) Add compileOptions
 * do step 11 in Emojifier class
 * do step 12 in MainActivity class
 * do steps 13 to 25 in Emojifier class
 * do step 26 in MainActivity class
 * do step 27 Replace all View declarations with Butterknife annotations in MainActivity class
 * do step 28 Replace the findViewById calls with the Butterknife data binding in MainActivity class
 * do step 29 change method emojifyMe, saveMe, shareMe, clearImage  in MainActivity class
 * do TODO (14) Remove all the onClick methods in the XML
 * do TODO (15) Add Timber dependency
 * do 30 Set up Timber in MainActivity class
 * do 31 in Emojifier class
 */

public class MainActivity extends AppCompatActivity {
    /**
     * 1 define variables
     */
    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final int REQUEST_STORAGE_PERMISSION = 1;
    private static final String FILE_PROVIDER_AUTHORITY = "nl.mahmood.emojifyexercise.fileprovider";
    /**
     * 27 comment 6 lines
     */
    //private ImageView mImageView;
    //private Button mEmojifyButton;
    //private FloatingActionButton mShareFab;
    //private FloatingActionButton mSaveFab;
    //private FloatingActionButton mClearFab;
    //private TextView mTitleTextView;
    @BindView(R.id.image_view) ImageView mImageView;
    @BindView(R.id.emojify_button) Button mEmojifyButton;
    @BindView(R.id.share_button) FloatingActionButton mShareFab;
    @BindView(R.id.save_button) FloatingActionButton mSaveFab;
    @BindView(R.id.clear_button) FloatingActionButton mClearFab;
    @BindView(R.id.title_text_view) TextView mTitleTextView;
    /**
     * 1 resumption
     */
    private String mTempPhotoPath;
    private Bitmap mResultsBitmap;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /**
         * 2
         * 28 comment 6 lines
         * only bind ButterKnife
         */
        // Bind the views
//        mImageView = (ImageView) findViewById(R.id.image_view);
//        mEmojifyButton = (Button) findViewById(R.id.emojify_button);
//        mShareFab = (FloatingActionButton) findViewById(R.id.share_button);
//        mSaveFab = (FloatingActionButton) findViewById(R.id.save_button);
//        mClearFab = (FloatingActionButton) findViewById(R.id.clear_button);
//        mTitleTextView = (TextView) findViewById(R.id.title_text_view);
        ButterKnife.bind(this);
        /**
         * 30
         * Set up Timber
         */
        Timber.plant(new Timber.DebugTree());
    }

    /**
     * 3
     * OnClick method for "Emojify Me!" Button. Launches the camera app.
     *
     * 29 change method
     */
//    public void emojifyMe(View view) {
    @OnClick(R.id.emojify_button)
    public void emojifyMe() {
        // Check for the external storage permission
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            // If you do not have permission, request it
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    REQUEST_STORAGE_PERMISSION);
        } else {
            // Launch the camera if the permission exists

            launchCamera();
        }
    }

    /**
     * 4
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)
    {
        // Called when you request permission to read and write to external storage
        switch (requestCode) {
            case REQUEST_STORAGE_PERMISSION: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // If you get permission, launch the camera
                    launchCamera();
                } else {
                    // If you do not get permission, show a Toast
                    Toast.makeText(this, R.string.permission_denied, Toast.LENGTH_SHORT).show();
                }
                break;
            }
        }
    }

    /**
     * 5
     * Creates a temporary image file and captures a picture to store in it.
     */
    private void launchCamera() {

        // Create the capture image intent
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the temporary File where the photo should go
            File photoFile = null;
            try {
                photoFile = BitmapUtils.createTempImageFile(this);
            } catch (IOException ex) {
                // Error occurred while creating the File
                ex.printStackTrace();
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {

                // Get the path of the temporary file
                mTempPhotoPath = photoFile.getAbsolutePath();

                // Get the content URI for the image file
                Uri photoURI = FileProvider.getUriForFile(this,
                        FILE_PROVIDER_AUTHORITY,
                        photoFile);
//
//                File imagePath = new File(Context.getFilesDir(), "images");
//                File newFile = new File(imagePath, "default_image.jpg");
//                Uri photoURI = FileProvider.getUriForFile(this, "nl.mahmood.emojifyexercise.fileprovider", photoFile);

                // Add the URI so the camera can store the image
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);

                // Launch the camera activity
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            }
        }
    }


    /**
     * 6
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data)
    {
        // If the image capture activity was called and was successful
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            // Process the image and set it to the TextView
            processAndSetImage();
        } else {

            // Otherwise, delete the temporary image file
            BitmapUtils.deleteImageFile(this, mTempPhotoPath);
        }
    }

    /**
     * 7
     * Method for processing the captured image and setting it to the TextView.
     */
    private void processAndSetImage() {

        // Toggle Visibility of the views
        mEmojifyButton.setVisibility(View.GONE);
        mTitleTextView.setVisibility(View.GONE);
        mSaveFab.setVisibility(View.VISIBLE);
        mShareFab.setVisibility(View.VISIBLE);
        mClearFab.setVisibility(View.VISIBLE);

        // Resample the saved image to fit the ImageView
        mResultsBitmap = BitmapUtils.resamplePic(this, mTempPhotoPath);

        /**
         * 12
         * 26 comment
         */
        // Detect the faces
        //Emojifier.detectFaces(this, mResultsBitmap);

        /**
         * 26
         */
        // Detect the faces and overlay the appropriate emoji
        mResultsBitmap = Emojifier.detectFacesandOverlayEmoji(this, mResultsBitmap);
        /**
         * 7 resumption
         */
        // Set the new bitmap to the ImageView
        mImageView.setImageBitmap(mResultsBitmap);
    }
    /**
     * 8
     * OnClick method for the save button.
     *
     * 29 change method
     */
//    public void saveMe(View view) {
    @OnClick(R.id.save_button)
    public void saveMe() {
        // Delete the temporary image file
        BitmapUtils.deleteImageFile(this, mTempPhotoPath);

        // Save the image
        BitmapUtils.saveImage(this, mResultsBitmap);
    }

    /**
     * 9
     * OnClick method for the share button, saves and shares the new bitmap.
     *
     * 29 change method
     */
//    public void shareMe(View view) {
    @OnClick(R.id.share_button)
    public void shareMe() {
        // Delete the temporary image file
        BitmapUtils.deleteImageFile(this, mTempPhotoPath);

        // Save the image
        BitmapUtils.saveImage(this, mResultsBitmap);

        // Share the image
        BitmapUtils.shareImage(this, mTempPhotoPath);
    }

    /**
     * 10
     * OnClick for the clear button, resets the app to original state.
     *
     * 29 change method
     */
//    public void clearImage(View view) {
    @OnClick(R.id.clear_button)
    public void clearImage() {
        // Clear the image and toggle the view visibility
        mImageView.setImageResource(0);
        mEmojifyButton.setVisibility(View.VISIBLE);
        mTitleTextView.setVisibility(View.VISIBLE);
        mShareFab.setVisibility(View.GONE);
        mSaveFab.setVisibility(View.GONE);
        mClearFab.setVisibility(View.GONE);

        // Delete the temporary image file
        BitmapUtils.deleteImageFile(this, mTempPhotoPath);
    }

}
