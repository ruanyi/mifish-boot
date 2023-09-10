package com.ruanyi.mifish.x8583;

/**
 * 
 * Paddable
 * 
 * @Creator ruanyi
 * @Create-DateTime 2011-9-5
 */
public interface Paddable {

	public static final String PADD_RIGHT = "RIGHT";

	public static final String PADD_LEFT = "LEFT";

	void setPaddDirect(String direct);

	void setPaddByte(byte value);
}
