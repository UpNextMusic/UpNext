package com.schneenet.android.upnext;

import android.app.Activity;
import android.os.Bundle;

public class UpNextAppActivity extends Activity
{
	protected UpNextApplication mApplicationObj;
	
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		mApplicationObj = (UpNextApplication) getApplication();
	}
}
