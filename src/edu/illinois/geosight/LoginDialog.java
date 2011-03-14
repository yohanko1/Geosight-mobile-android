package edu.illinois.geosight;

import edu.illinois.geosight.servercom.GeosightEntity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;


public class LoginDialog {
	public static void show(Context ctx){
		final Dialog d = new Dialog(ctx);
		d.setContentView(R.layout.login);
		d.setTitle("Login");
		
		final EditText emailTxt = (EditText) d.findViewById(R.id.email_address);
		final EditText passwordTxt = (EditText) d.findViewById(R.id.password);
		final TextView error = (TextView) d.findViewById(R.id.error);
		final Button loginBtn = (Button) d.findViewById(R.id.loginBtn);
		final Button cancelBtn = (Button) d.findViewById(R.id.cancelBtn);
		
		cancelBtn.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				d.cancel();
			}
		});
		
		loginBtn.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				error.setVisibility(View.INVISIBLE);
				boolean success = GeosightEntity.login(emailTxt.getText().toString(), passwordTxt.getText().toString());
				if( success ){
					d.dismiss();
				} else {
					error.setVisibility(View.VISIBLE);
				}
			}
		});
		
		d.show();
		

	}
}
