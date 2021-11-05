package com.example.app;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import static com.example.app.MainActivity.TAG;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class AddEventFragment extends Fragment implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {

    TextView eventDateTextView;
    TextView eventTimeTextView;
    ProgressBar progressBar;

    Uri imageUri;
    EditText eventNameEditText;
    EditText eventLocationEditText;
    Calendar eventCalendar = Calendar.getInstance();
    EditText eventDescriptionEditText, eventDescriptionShortEditText;
    EditText eventJoinLinkEditText, eventRecLinkEditText;
    ImageView eventImage;

    StorageReference storageReference = FirebaseStorage.getInstance().getReference("image_uploads");
    CollectionReference collectionReference = FirebaseFirestore.getInstance().collection("EventCollection");

    public static final int PICK_IMAGE_REQUEST = 1;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_event, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        eventDateTextView = view.findViewById(R.id.text_view_event_date_display);
        eventTimeTextView = view.findViewById(R.id.text_view_event_time_display);
        progressBar = view.findViewById(R.id.progressBar);
        eventNameEditText = view.findViewById(R.id.eventName);
        eventLocationEditText = view.findViewById(R.id.eventLocation);
        eventDescriptionEditText = view.findViewById(R.id.eventDescription);
        eventDescriptionShortEditText = view.findViewById(R.id.eventDescriptionShort);
        eventJoinLinkEditText = view.findViewById(R.id.eventJoinLink);
        eventRecLinkEditText = view.findViewById(R.id.eventRecLink);
        eventImage = view.findViewById(R.id.eventImage);


        eventDateTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog();
            }
        });

        eventTimeTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTimePickerDialog();
            }
        });

        view.findViewById(R.id.uploadButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadEvent();
            }
        });

        view.findViewById(R.id.addImageButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFileChooser();
            }
        });

    }

    private void openFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == -1 && data != null && data.getData() != null) {
            imageUri = data.getData();
            Picasso.get().load(imageUri).into(eventImage);
        }
    }

    public static final String ANDROID_RESOURCE = "android.resource://";
    public static final String FORESLASH = "/";

    public static Uri resIdToUri(Context context, int resId) {
        return Uri.parse(ANDROID_RESOURCE + context.getPackageName()
                + FORESLASH + resId);
    }

    private String getFileExtension(Uri uri) {
        return MimeTypeMap.getSingleton()
                .getExtensionFromMimeType(getContext().getContentResolver().getType(uri));
    }

    void uploadEvent() {

        Event event = new Event();
        event.setEventTitle(eventNameEditText.getText().toString().trim());
        event.setEventLocation(eventLocationEditText.getText().toString().trim());
        event.setEventStartTimeStamp(String.valueOf((eventCalendar.getTime().getTime() / 1000)));
        event.setEventShortDescription(eventDescriptionShortEditText.getText().toString().trim());
        event.setEventLongDescription(eventDescriptionEditText.getText().toString().trim());
        event.setEventJoinLink(eventJoinLinkEditText.getText().toString().trim());
        event.setEventRecLink(eventRecLinkEditText.getText().toString().trim());

        Log.d(TAG, "uploadEvent: "+event.toString());

        if (imageUri != null) {
            StorageReference fileReference = storageReference.child(System.currentTimeMillis() + "." + getFileExtension(imageUri));
            UploadTask uploadTask = fileReference.putFile(imageUri);

            uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Toast.makeText(getContext(), "Upload successful", Toast.LENGTH_SHORT).show();
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            progressBar.setProgress(0);
                        }
                    }, 500);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getContext(), "Upload failed", Toast.LENGTH_SHORT).show();
                }
            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                    double progress = (100.0 * snapshot.getBytesTransferred() / snapshot.getTotalByteCount());
                    progressBar.setProgress((int) progress);
                }
            }).continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if (!task.isSuccessful()) {
                        throw task.getException();
                    }
                    // Continue with the task to get the download URL
                    return fileReference.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful()) {
                        Uri downloadUri = task.getResult();
                        event.setEventImageUrl(downloadUri.toString());
                        collectionReference.add(event);
                    } else {
                        // Handle failures
                        // ...
                    }
                }
            });
        } else {
            event.setEventImageUrl(null);
            collectionReference.add(event);
        }

    }

    private void showDatePickerDialog() {
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                getActivity(),
                this,
                Calendar.getInstance().get(Calendar.YEAR),
                Calendar.getInstance().get(Calendar.MONTH),
                Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
        );
        datePickerDialog.show();
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        eventCalendar.set(Calendar.YEAR, year);
        eventCalendar.set(Calendar.MONTH, month);
        eventCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        Date date = eventCalendar.getTime();
        SimpleDateFormat formatter = new SimpleDateFormat("E, dd MMM yyyy");
        eventDateTextView.setText(formatter.format(date));
    }

    private void showTimePickerDialog() {
        TimePickerDialog timePickerDialog = new TimePickerDialog(
                getActivity(),
                this,
                Calendar.getInstance().get(Calendar.HOUR_OF_DAY),
                Calendar.getInstance().get(Calendar.MINUTE),
                false
        );
        timePickerDialog.show();
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        eventCalendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
        eventCalendar.set(Calendar.MINUTE, minute);
        Date date = eventCalendar.getTime();
        SimpleDateFormat formatter = new SimpleDateFormat("h:mm a");
        eventTimeTextView.setText(formatter.format(date));
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.menu_add_item, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == R.id.uploadButton) {
            Toast.makeText(getContext(), "Uploaded", Toast.LENGTH_SHORT).show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}