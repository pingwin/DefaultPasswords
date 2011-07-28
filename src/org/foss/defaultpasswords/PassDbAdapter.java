package org.foss.defaultpasswords;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.FileOutputStream;

/**
 * Simple notes database access helper class. Defines the basic CRUD operations
 * for the notepad example, and gives the ability to list all notes as well as
 * retrieve or modify a specific note.
 * 
 * This has been improved from the first version of this tutorial through the
 * addition of better error handling and also using returning a Cursor instead
 * of using a collection of inner classes (which is less scalable and not
 * recommended).
 */
public class PassDbAdapter extends SQLiteOpenHelper{
  public static final String ID         = "_id";
  public static final String VENDOR     = "Vendor";
  public static final String MODEL      = "Model";
  public static final String VERSION    = "Version";
  public static final String ACCESSTYPE = "Access_Type";
  public static final String USERNAME   = "Username";
  public static final String PASSWORD   = "Password";
  public static final String PRIVILEGES = "Privileges";
  public static final String NOTES      = "Notes";

  private static final String TAG = "PassDbAdapter";
  //private DatabaseHelper mDbHelper;
  private SQLiteDatabase mDb;
  private static final String DB_PATH = "/data/data/org.foss.defaultpasswords/databases/";

  private static final String DATABASE_TABLE = "default_passwords";
  private static final String DATABASE_NAME = DATABASE_TABLE+".db";
  private static final int DATABASE_VERSION = 4;
  
  private final Context mCtx;
  public void createDataBase() throws IOException{
     	boolean dbExist = checkDataBase();
     	if(dbExist){
    		//do nothing - database already exist
    	}else{
     		//By calling this method and empty database will be created into the default system path
        //of your application so we are gonna be able to overwrite that database with our database.
        this.getReadableDatabase();
        try {
     			copyDataBase();
        } catch (IOException e) {
        		throw new Error("Error copying database");
        }
    	}
  }
 
  /**
   * Check if the database already exist to avoid re-copying the file each time you open the application.
   * @return true if it exists, false if it doesn't
   */
  private boolean checkDataBase(){
    SQLiteDatabase checkDB = null;
    try{
      String myPath = DB_PATH + DATABASE_NAME;
      checkDB = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);
    }catch(SQLiteException e){
     		//database does't exist yet.
    }
    if(checkDB != null){
      checkDB.close();
    }
    return checkDB != null ? true : false;
  }
 
  /**
   * Copies your database from your local assets-folder to the just created empty database in the
   * system folder, from where it can be accessed and handled.
   * This is done by transfering bytestream.
   * */
  private void copyDataBase() throws IOException{
    Log.i(TAG, "Copying new Password database");
    //Open your local db as the input stream
    InputStream myInput = mCtx.getAssets().open(DATABASE_NAME);
    // Path to the just created empty db
    String outFileName = DB_PATH + DATABASE_NAME;
    //Open the empty db as the output stream
    OutputStream myOutput = new FileOutputStream(outFileName);
    //transfer bytes from the inputfile to the outputfile
    byte[] buffer = new byte[1024];
    int length;
    while ((length = myInput.read(buffer))>0){
      myOutput.write(buffer, 0, length);
    }
    //Close the streams
    myOutput.flush();
    myOutput.close();
    myInput.close();
  }
 
  public void openDataBase() throws SQLException{
    //Open the database
    String myPath = DB_PATH + DATABASE_NAME;
    mDb = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);
  }

    /**
     * Constructor - takes the context to allow the database to be
     * opened/created
     * 
     * @param ctx the Context within which to work
     */
    public PassDbAdapter(Context ctx) {
      super(ctx, DATABASE_TABLE, null, 1);
      this.mCtx = ctx;
    }

    /**
     * Create a new note using the title and body provided. If the note is
     * successfully created return the new rowId for that note, otherwise return
     * a -1 to indicate failure.
     * 
     * @param title the title of the note
     * @param body the body of the note
     * @return rowId or -1 if failed
     * /
    public long createNote(String title, String body) {
        ContentValues initialValues = new ContentValues();
        initialValues.put(KEY_TITLE, title);
        initialValues.put(KEY_BODY, body);

        return mDb.insert(DATABASE_TABLE, null, initialValues);
    }

    /**
     * Return a Cursor over the list of all notes in the database
     * 
     * @return Cursor over all notes
     */
  public Cursor fetchAllVendors() {
    Log.w(TAG, "Fetching list of Vendors");
    Cursor C = mDb.query(DATABASE_TABLE, new String[] {ID, VENDOR}, null,
                         null, VENDOR, null, VENDOR+" ASC", null);
    Log.w(TAG, "Got vendors " + C.getCount());
    return C;
  }

  public String vendorNameAt(long rowID) {
    Log.w(TAG, "Grab vendor name at rowId: "+String.valueOf(rowID));
    Cursor c = mDb.query(DATABASE_TABLE, new String[] {VENDOR},
                         ID+" = ?", new String[] {String.valueOf(rowID)}, null, null, null, null);
    if (c.isFirst() == false)
      c.moveToFirst();

    if (c.isNull(0)) {
      return "UNKNOWN";
    }
    return c.getString(0);
  }

  public Cursor fetchAllModelsForVendorAt(long rowID) {
    Log.w(TAG, "Fetching all Models for Vendor Matching at row "+rowID);
    
    Cursor c = mDb.query(DATABASE_TABLE, new String[] {ID, MODEL},
                         VENDOR+" = ?", new String[] {vendorNameAt(rowID)},
                         MODEL, null, MODEL+" ASC", null);
    Log.w(TAG, "Got models "+c.getCount());
    return c;
  }

  public Cursor at(long rowID) {
    Log.w(TAG, "Grab everything at rowID: "+String.valueOf(rowID));
    Cursor c = mDb.query(DATABASE_TABLE, new String[] {
        VENDOR, MODEL, VERSION, ACCESSTYPE, USERNAME, PASSWORD, PRIVILEGES, NOTES
      }, ID+" = ?", new String[] {String.valueOf(rowID)}, null, null, null, null);
    return c;
  }

  public String modelNameAt(long rowID) {
    Log.w(TAG, "Grab model name at rowId: "+String.valueOf(rowID));
    Cursor c = mDb.query(DATABASE_TABLE, new String[] {MODEL},
                         ID+" = ?", new String[] {String.valueOf(rowID)}, null, null, null, null);
    if (c.isFirst() == false)
      c.moveToFirst();

    if (c.isNull(0)) {
      return "UNKNOWN";
    }
    return c.getString(0);
  }

  public Cursor fetchAllVersionsForModelAt(long rowID) {
    Log.w(TAG, "Fetching all Versions for Model Matching at row "+rowID);
    Cursor c = mDb.query(DATABASE_TABLE, new String[] {ID, VERSION},
                         MODEL+" = ?", new String[] {modelNameAt(rowID)},
                         VERSION, null, VERSION+" ASC", null);
    Log.w(TAG, "Got versions "+c.getCount());
    return c;    
  }

    /**
     * Return a Cursor positioned at the note that matches the given rowId
     * 
     * @param rowId id of note to retrieve
     * @return Cursor positioned to matching note, if found
     * @throws SQLException if note could not be found/retrieved
     * /
    public Cursor fetchAllRecord(long rowId) throws SQLException {
        Cursor mCursor =
                mDb.query(true, DATABASE_TABLE, new String[] {KEY_ROWID,
                        KEY_TITLE, KEY_BODY}, KEY_ROWID + "=" + rowId, null,
                        null, null, null, null);
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;

    }
    */
 @Override
	public synchronized void close() {
 
    	    if(mDb != null)
    		    mDb.close();
 
    	    super.close();
 
	}
 
	@Override
	public void onCreate(SQLiteDatabase db) {
 
	}
 
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
 
	}

}
    