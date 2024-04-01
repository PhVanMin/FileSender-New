package com.example.xender.Loader;

import android.content.ContentResolver;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.util.Log;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.xender.activity.ChooseActivity;
import com.example.xender.model.Contact;

import java.util.ArrayList;
import java.util.HashSet;

public class ContactsLoader {


    private ChooseActivity sendActivity;
    private final ArrayList<Contact> contacts = new ArrayList<>();
    public ArrayList<Contact> getContacts() {
        return contacts;
    }

    public ContactsLoader(ChooseActivity sendActivity) {
        this.sendActivity = sendActivity;
    }

    public void getContactList() {
        if(ContextCompat.checkSelfPermission(
                sendActivity, android.Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(sendActivity,
                    new String[]{android.Manifest.permission.READ_CONTACTS},
                    ChooseActivity.READ_CONTACTS_PERMISSION);
        }

        ContentResolver cr = sendActivity.getContentResolver();
        Uri uri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
        Cursor cursor = cr.query(uri,
                null, null, null, null);
        if (cursor != null) {
            HashSet<String> mobileNoSet = new HashSet<String>();
            try {
                final int nameIndex = cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME);
                final int numberIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);

                String name, number;
                while (cursor.moveToNext()) {
                    name = cursor.getString(nameIndex);
                    number = cursor.getString(numberIndex);
                    number = number.replace(" ", "");
                    if (!mobileNoSet.contains(number)) {
                        contacts.add(new Contact(name, number));
                        mobileNoSet.add(number);
                        Log.d("hvy", "onCreaterrView  Phone Number: name = " + name
                                + " No = " + number);
                    }
                }
            } finally {
                cursor.close();
            }
        }
    }
}
