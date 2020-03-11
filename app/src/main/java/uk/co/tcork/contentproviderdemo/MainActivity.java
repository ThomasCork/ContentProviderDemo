package uk.co.tcork.contentproviderdemo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private EditText e1, e2;

    ContentValues contentValues = new ContentValues();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        e1 = findViewById(R.id.edit1);
        e2 = findViewById(R.id.edit2);
    }

    public void doLoadContent(View view) {
        contentValues.put("emp_name", e1.getText().toString());
        contentValues.put("profile", e2.getText().toString());

        Uri uri = getContentResolver().insert(CompanyProvider.CONTENT_URI, contentValues);
        Toast.makeText(this, uri.toString(), Toast.LENGTH_SHORT).show();
    }

    public void doSaveContent(View view) {
        Cursor cursor = getContentResolver().query(CompanyProvider.CONTENT_URI, null, null, null, "_id");
        StringBuilder stringBuilder = new StringBuilder();

        while (cursor.moveToNext()) {
            int id = cursor.getInt(0);
            String employeeName = cursor.getString(1);
            String employeeProfile = cursor.getString(2);

            stringBuilder.append(id + "     " + employeeName + "      " + employeeProfile + "\n");

            Toast.makeText(this, stringBuilder.toString(), Toast.LENGTH_SHORT).show();
        }
    }
}
