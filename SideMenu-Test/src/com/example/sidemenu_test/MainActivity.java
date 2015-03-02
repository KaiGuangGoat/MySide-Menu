package com.example.sidemenu_test;


import com.example.sidemenu_test.widget.SideMenuView;
import com.example.sidemenu_test.widget.SideMenuView.SelectSideMenuListener;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity implements SelectSideMenuListener{

	private SideMenuView mSideMenuView;
	private TextView tv_content;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.side_menu_layout);
		init();
	}
	
	private void init(){
		int id = getResources().getIdentifier("side_menu", "id", getPackageName());
		mSideMenuView = (SideMenuView) findViewById(id);
		tv_content = (TextView) findViewById(R.id.tv_content);
		mSideMenuView.setLayoutParams(80);
		mSideMenuView.setSelectSideMenuListener(this);
	}
	
	private void select(int position){
		Toast.makeText(this, position+"", Toast.LENGTH_LONG).show();
		tv_content.setText("you select menu"+position);
		switch(position){
			
		}
	}
	
	public void changeState(View view){
		if(mSideMenuView.isShowSideMenu()){
			mSideMenuView.hideSideMenu();
		}else{
			mSideMenuView.showSideMenu();
		}
	}

	@Override
	public void onSelect(int position) {
		// TODO Auto-generated method stub
		select(position);
	}

	
}
