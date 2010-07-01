package org.foss.defaultpasswords;

import android.app.Activity;
import android.widget.SimpleCursorAdapter;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ArrayAdapter;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.util.Log;

public class DefaultPasswords extends Activity {
  private static final String TAG = "DefaultPasswords";

  private Cursor dbCursor;

  @Override
    public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.main);
    
  }
}