package com.example.spca4.Actvity;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.spca4.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AddNewItem extends BottomSheetDialogFragment {

    public static final String TAG = "AddNewItem";
    FirebaseAuth mAuth;
    private EditText title, manufacturer, price, quantity, category;
    private String ttl, manu, pr, qn, cat;
    private Button save;
    private Context context;
    private String id = "";
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

        save = view.findViewById(R.id.saveItem);
        title = view.findViewById(R.id.title);
        manufacturer = view.findViewById(R.id.Manufacturer);
        price = view.findViewById(R.id.Price);
        quantity = view.findViewById(R.id.Quantity);
        category = view.findViewById(R.id.Category);

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ttl = title.getText().toString();
                manu = manufacturer.getText().toString();
                pr = price.getText().toString();
                qn = quantity.getText().toString();
                cat = category.getText().toString();


                if (TextUtils.isEmpty(ttl)) {
                    Toast.makeText(context, "Enter a title!", Toast.LENGTH_SHORT).show();
                    return;
                } if (TextUtils.isEmpty(manu)) {
                    Toast.makeText(context, "Enter a manufacturer!", Toast.LENGTH_SHORT).show();
                    return;
                } if (TextUtils.isEmpty(pr)) {
                    Toast.makeText(context, "Enter a price!", Toast.LENGTH_SHORT).show();
                    return;
                } if (TextUtils.isEmpty(qn)) {
                    Toast.makeText(context, "Enter a quantity!", Toast.LENGTH_SHORT).show();
                    return;
                } else if (TextUtils.isEmpty(cat)) {
                    Toast.makeText(context, "Enter a category!", Toast.LENGTH_SHORT).show();
                    return;
                }

            }
        });
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

    public interface OnItemSavedListener {
        void onItemSaved(String description);
    }
    private OnItemSavedListener savedListener;

    // Other existing code

    public void setOnItemSavedListener(OnItemSavedListener listener) {
        this.savedListener = listener;
    }
}
