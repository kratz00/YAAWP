package org.yaawp.hmi.panelbar.buttons;

public class PanelBarButton {

    public interface OnClickListener {
    	public boolean onClick();
    }
	
    protected boolean mVisible = true;
	protected boolean mEnabled = true;
	protected String mText = "";
	protected OnClickListener mClickListener = null;
	
	public PanelBarButton( String text, OnClickListener clickListener ) {
		mText = text;
		mClickListener = clickListener;
	}
	
	public boolean isVisible() {
		return mVisible;
	}
	
	public boolean isEnabled() {
		return mEnabled;
	}
	
	public void setVisible( boolean visible ) {
		mVisible = visible;
		// TODO UpdateUI();
	}
	
	public void setEnabled( boolean enabled ) {
		mEnabled = enabled;
		// TODO UpdateUI();
	}
	
	public String getText() {
		return mText;
	}
	
	public void onClick() {
		if ( mClickListener != null ) {
			mClickListener.onClick();
		}
	}
		
}
