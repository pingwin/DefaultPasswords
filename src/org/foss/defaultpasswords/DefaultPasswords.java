package org.foss.defaultpasswords;

import org.foss.defaultpasswords.R;
import android.app.ListActivity;
import android.os.Bundle;
import android.database.Cursor;
import android.util.Log;
import android.widget.SimpleCursorAdapter;
import java.io.IOException;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;
import android.content.Intent;

public class DefaultPasswords extends ListActivity {
  private static final String TAG = "DefaultPasswords";
  private PassDbAdapter mDbHelper;
  private Cursor vendorCursor;
    /** Called when the activity is first created. */
  @Override
  public void onCreate(Bundle savedInstanceState) {
    Log.d(TAG, "Creating DefaultPasswords");
    super.onCreate(savedInstanceState);
    setContentView(R.layout.vendor_list);
    mDbHelper = new PassDbAdapter(this);
    try {
      mDbHelper.createDataBase();
    } catch (IOException ioe) {
      throw new Error("Unable to open / create Database");
    }
    mDbHelper.openDataBase();
    fillData();
  }

  private void fillData() {
    vendorCursor = mDbHelper.fetchAllVendors();
    Toast.makeText(this,"Found "+vendorCursor.getCount()+" Vendors", Toast.LENGTH_LONG).show();

    startManagingCursor(vendorCursor);
    String[] from = new String[] {PassDbAdapter.VENDOR};
    int[] to = new int[] {R.id.text1};
    // Now create an array adapter and set it to display using our row
    SimpleCursorAdapter vendors =
      new SimpleCursorAdapter(this, R.layout.vendor_row, vendorCursor, from, to);
    setListAdapter(vendors);
  }

  @Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
		Cursor c = vendorCursor;
		c.moveToPosition(position);
		Intent i = new Intent(this, ModelListActivity.class);
		i.putExtra(PassDbAdapter.ID, c.getLong(c.getColumnIndexOrThrow(PassDbAdapter.ID)));
		i.putExtra(PassDbAdapter.VENDOR, c.getString(c.getColumnIndexOrThrow(PassDbAdapter.VENDOR)));
    startActivityForResult(i, 0);
    Log.i(TAG, "Hello, we got click!");
	}
}
