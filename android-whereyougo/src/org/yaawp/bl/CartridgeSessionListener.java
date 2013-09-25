package org.yaawp.bl;

import cz.matejcik.openwig.formats.ICartridge;

public interface CartridgeSessionListener
{
    public static final int CARTRIDGE_SESSION_LOADINING = 0;
    
    public void UpdatedCartridgeSession( int msgid, ICartridge cartridge );
}
