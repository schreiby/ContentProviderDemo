package com.example.ettie.contentproviderdemo;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.provider.ContactsContract;
import android.content.ContentResolver;
import android.database.Cursor;


public class MainActivity extends Activity {

    private static final String TAG = "ContentProviderDemo";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ContentResolver cr = getContentResolver();
        Cursor cursor = cr.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);

        if (cursor != null && cursor.getCount() > 0 ) {
            cursor.moveToFirst();
            do {
                int contactID = cursor.getInt(cursor.getColumnIndex(ContactsContract.Contacts._ID));
                int hasPhoneNumber = cursor.getInt(cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER));
                String contactName = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));

                //if there is phone number for this contact
                if (hasPhoneNumber > 0) {
                    String[] projection = new String[] {ContactsContract.CommonDataKinds.Phone.NUMBER};
                    String selection = ContactsContract.CommonDataKinds.Phone.CONTACT_ID + "=?";
                    String[] selectionArgs = {contactID + ""};
                    Cursor phoneCursor = cr.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                            projection, selection, selectionArgs, null);

                    if (phoneCursor != null && phoneCursor.getCount() > 0) {
                        phoneCursor.moveToFirst();
                        do {
                            String phoneNumber = phoneCursor.getString(phoneCursor.getColumnIndex(
                                    ContactsContract.CommonDataKinds.Phone.NUMBER));
                            Log.d(TAG, "Name: " + contactName + ", Phone Number: " + phoneNumber);
                        } while (phoneCursor.moveToNext());
                    }
                    phoneCursor.close();
                }
            } while (cursor.moveToNext());
        }
        cursor.close();
    }


}
