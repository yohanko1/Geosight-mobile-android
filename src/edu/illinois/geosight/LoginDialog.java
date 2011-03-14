package edu.illinois.geosight;

import edu.illinois.geosight.servercom.GeosightEntity;
import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;


/**
 * LoginDialog represents a class that can be called from any Activity to 
 * present a login dialog box.
 * @author Steven Kabbes
 *
 */
public class LoginDialog {
	
	/**
	 * Shows the login dialog box
	 * @param ctx The context in which to display the box.
	 */
	public static void show(Context ctx){
		final Dialog d = new Dialog(ctx);
		d.setContentView(R.layout.login);
		d.setTitle("Login");
		
		//Collect the field elements for the login box
		final EditText emailTxt = (EditText) d.findViewById(R.id.email_address);
		final EditText passwordTxt = (EditText) d.findViewById(R.id.password);
		final TextView error = (TextView) d.findViewById(R.id.error);
		final Button loginBtn = (Button) d.findViewById(R.id.loginBtn);
		final Button cancelBtn = (Button) d.findViewById(R.id.cancelBtn);
		
		// What to do on cancel
		cancelBtn.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				d.cancel();
			}
		});
		
		// what to do on "login"
		loginBtn.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				error.setVisibility(View.INVISIBLE);
				
				//Was the login successful?
				boolean success = GeosightEntity.login(emailTxt.getText().toString(), passwordTxt.getText().toString());
				if( success ){
					d.dismiss();
				} else {
					error.setVisibility(View.VISIBLE);
				}
			}
		});
		
		// finally, show the dialog box
		d.show();
	}
}
