package org.yaawp.bl;

public interface PlayerSessionListener
{
    public static final int CARTRIDGE_LIST_UPDATING = 0;
    public static final int CARTRIDGE_LIST_UPDATED = 1;
    public static final int CARTRIDGE_NOT_AVAILABLE = 2;
    
    /** Logs debugging info from the engine.
     * <p>
     * This is only useful for debugging OpenWIG. UI might choose to
     * not show this message to the user and only log it.
     * @param msg text of the message
     */    
    public void UpdatedPlayerSession( int type );
}
