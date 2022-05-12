package com.example.myapplication;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.app.PictureInPictureParams;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.myapplication.adapter.ListItem;
import com.example.myapplication.db.AppExecutor;
import com.example.myapplication.db.MyConstants;
import com.example.myapplication.db.MyDbManager;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import kotlin.OverloadResolutionByLambdaReturnType;

public class EditActivity extends AppCompatActivity {
    private final int CHOSEN_IMAGE_CODE = 123;
    private EditText edTitle, edDesc;
    private MyDbManager myDbManager;
    private ImageView imUsersImage;
    private ImageButton edEditImage, edRemoveImage;
    private ConstraintLayout constImageLayout;
    private FloatingActionButton fbAddImage;
    private String tempUri = "empty";
    private boolean isEditState = true;
    private ListItem item;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_edit);

        init();
        getAnIntent();
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == CHOSEN_IMAGE_CODE && data != null) {
            imUsersImage.setImageURI(data.getData());
            tempUri = data.getData().toString();
            getContentResolver().takePersistableUriPermission(data.getData(), Intent.FLAG_GRANT_READ_URI_PERMISSION);
        }
    }


    protected void onResume() {
        super.onResume();
        myDbManager.openDb();

    }

    private void init() {
        edDesc = findViewById(R.id.edDesc);
        edTitle = findViewById(R.id.edTitle);
        myDbManager = new MyDbManager(this);
        constImageLayout = findViewById(R.id.constImageLayout);
        edEditImage = findViewById(R.id.imEditImage);
        imUsersImage = findViewById(R.id.imUsersImage);
        edRemoveImage = findViewById(R.id.imTrashImage);
        fbAddImage = findViewById(R.id.fbAddImage);
    }

    private void getAnIntent() {
        Intent i = getIntent();
        if (i != null) {

            item = (ListItem) i.getSerializableExtra(MyConstants.LIST_ITEM_INTENT);
            isEditState = i.getBooleanExtra(MyConstants.EDIT_STATE, true);

            if (!isEditState) {
                edTitle.setText(item.getTitle());
                edDesc.setText(item.getDesc());

                if (!item.getUri().equals("empty")) {
                    tempUri = item.getUri();
                    constImageLayout.setVisibility(View.VISIBLE);
                    imUsersImage.setImageURI(Uri.parse(item.getUri()));
                    fbAddImage.setVisibility(View.GONE);
                    // git initedRemoveImage.setVisibility(View.GONE);
                    // edEditImage.setVisibility(View.GONE);

                }
            }
        }
    }

    public void onClickSave(View view) {

        final String title = edDesc.getText().toString();
        final String description = edTitle.getText().toString();

        if ((title.equals("") || description.equals("")) && tempUri.equals("empty")) {
            Toast.makeText(this, R.string.fields_are_empty, Toast.LENGTH_SHORT).show();

        }
         else {
            if(isEditState) {
                AppExecutor.getInstance().getSubThread().execute(new Runnable() {
                    @Override
                    public void run() {
                        myDbManager.insertToBd(description, title, tempUri);
                    }
                });
                Toast.makeText(this, R.string.Saved, Toast.LENGTH_SHORT).show();
            }else{
                myDbManager.update(description, title, tempUri, item.getId());

                Toast.makeText(this, R.string.Saved, Toast.LENGTH_SHORT).show();
            }
            myDbManager.closeDb();
            finish();

        }
    }


    public void onClickRemoveImage(View view) {
        imUsersImage.setImageResource(R.drawable.ic_image_def);
        tempUri = "empty";
        constImageLayout.setVisibility(View.GONE);
        fbAddImage.setVisibility(View.VISIBLE);
    }

    public void onClickAddImage(View view) {
        constImageLayout.setVisibility(View.VISIBLE);
        view.setVisibility(View.GONE);
    }

    public void OnClickEdit(View view) {
        Intent edit = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        edit.setType("image/*");
        startActivityForResult(edit, CHOSEN_IMAGE_CODE);
    }

    public void OnClickBack(View View){
        Intent i = new Intent(EditActivity.this, MainActivity.class);
        startActivity(i);
    }

}