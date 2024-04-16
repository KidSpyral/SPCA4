package com.example.spca4.Actvity;

import static android.app.Activity.RESULT_OK;

import android.Manifest;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.spca4.Model.Items;
import com.example.spca4.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

public class AddNewItem extends BottomSheetDialogFragment {

    public static final String TAG = "AddNewItem";
    private static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 123;
    FirebaseAuth mAuth;
    private EditText title, manufacturer, price, quantity, category;
    private Button save;
    private Context context;
    private ImageView imageView;
    private Uri imageUri;
    private DatabaseReference itemReference;
    private StorageReference storageReference;

    public static AddNewItem newInstance(){
        return new AddNewItem();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.new_item , container , false);


    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        FirebaseApp.initializeApp(context);

        mAuth = FirebaseAuth.getInstance();

        // Initialize Firebase components
        itemReference = FirebaseDatabase.getInstance().getReference("Stock");
        storageReference = FirebaseStorage.getInstance().getReference("Images");

        save = view.findViewById(R.id.saveItem);
        title = view.findViewById(R.id.title);
        manufacturer = view.findViewById(R.id.Manufacturer);
        price = view.findViewById(R.id.Price);
        quantity = view.findViewById(R.id.Quantity);
        category = view.findViewById(R.id.Category);

        Activity activity = getActivity();

        if (ContextCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            // Request the permission
            ActivityCompat.requestPermissions(activity,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
        } else {
            // Permission already granted, proceed with the operation
            openGallery();
        }

        // Initialize ImageView for image selection
        imageView = view.findViewById(R.id.Image);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery();
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addItem(true,null); // This is a new item, no need to provide itemId
            }
        });

        // Check if arguments contain item details
        Bundle args = getArguments();
        if (args != null && args.containsKey("stockItem")) {
            Items stockItem = args.getParcelable("stockItem");
            if (stockItem != null) {
                title.setText(stockItem.getTitle());
                manufacturer.setText(stockItem.getManufacturer());
                price.setText(String.valueOf(stockItem.getPrice()));
                quantity.setText(String.valueOf(stockItem.getQuantity()));
                category.setText(stockItem.getCategory());
                // Set the imageUri field with the existing image URI
                String imageUrl = args.getString("imageUri");
                if (imageUrl != null) {
                    imageUri = Uri.parse(imageUrl);
                    // Load the image into ImageView
                    Picasso.get().load(imageUri).into(imageView);
                }
            }
        }

        boolean isNewItem = args != null && args.getBoolean("isNewItem", true);
        // Determine if it's a new item or an existing one
        if (isNewItem) {
            // Handle adding a new item
        } else {
            // Handle updating an existing item
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE) {
            // Check if the permission is granted
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted, proceed with the operation
                openGallery();
            } else {
                // Permission denied, handle accordingly (e.g., show a message or disable functionality)
                Toast.makeText(context, "Permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }



    private void addItem(boolean isNewItem, String itemId) {
        final String ttl = title.getText().toString();
        final String manu = manufacturer.getText().toString();
        final String prStr = price.getText().toString();
        final String qnStr = quantity.getText().toString();
        final String cat = category.getText().toString();

        if (TextUtils.isEmpty(ttl) || TextUtils.isEmpty(manu) || TextUtils.isEmpty(prStr) || TextUtils.isEmpty(qnStr) || TextUtils.isEmpty(cat)) {
            Toast.makeText(context, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        final double pr = Double.parseDouble(prStr);
        final int qn = Integer.parseInt(qnStr);

        // Check if imageUri is null
        if (imageUri == null) {
            Toast.makeText(context, "Please select an image", Toast.LENGTH_SHORT).show();
            return;
        }

        // Upload image to Firebase Storage
        final StorageReference imageReference = storageReference.child(System.currentTimeMillis() + "." + getFileExtension(imageUri, context));
        imageReference.putFile(imageUri)
                .addOnSuccessListener(taskSnapshot -> {
                    // Get the download URL for the image
                    imageReference.getDownloadUrl().addOnSuccessListener(uri -> {
                        String imageUrl = uri.toString();
                        // Create stock item object
                        Items stockItem = new Items(ttl, manu, pr, qn, cat, imageUrl);

                        // Reference to the "Stock" node directly
                        DatabaseReference stockReference = FirebaseDatabase.getInstance().getReference("Stock");

                        if (isNewItem) {
                            // Save new stock item to Firebase Database under the "Stock" node
                            stockReference.push().setValue(stockItem);
                            Toast.makeText(context, "New stock item added successfully", Toast.LENGTH_SHORT).show();
                        } else {
                            if (itemId != null) {
                                // Update existing stock item in Firebase Database under the "Stock" node
                                stockReference.child(itemId).setValue(stockItem);
                                Toast.makeText(context, "Stock item updated successfully", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(context, "Failed to update stock item: Item ID is null", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }).addOnFailureListener(e -> {
                        // Log the error message
                        Log.e("UploadImage", "Failed to upload image", e);
                        // Show a Toast or alert the user about the failure
                        Toast.makeText(context, "Failed to upload image: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    });
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(context, "Failed to upload image: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
        dismiss();
    }





    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, 1000);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1000 && resultCode == RESULT_OK && data != null) {
            imageUri = data.getData();
            imageView.setImageURI(imageUri);
        }
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Override
    public void onDismiss(@NonNull DialogInterface dialog) {
        super.onDismiss(dialog);
        Activity activity = getActivity();
        if (activity instanceof  OnDialogCloseListener){
            ((OnDialogCloseListener)activity).onDialogClose(dialog);
        }
    }

    private String getFileExtension(Uri uri, Context context) {
        ContentResolver contentResolver = context.getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        String extension = mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
        Log.d(TAG, "File extension: " + extension);
        return extension;
    }




    public interface OnItemSavedListener {
        void onItemSaved(String description);
    }
    private OnItemSavedListener savedListener;

    // Other existing code

    public void setOnItemSavedListener(OnItemSavedListener listener) {
        this.savedListener = listener;
    }
}
