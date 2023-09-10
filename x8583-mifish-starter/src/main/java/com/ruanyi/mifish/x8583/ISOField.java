package com.ruanyi.mifish.x8583;

/**
 * 
 * ISOField
 * 
 * @Creator ruanyi
 * @Create-DateTime 2011-9-2
 */
public interface ISOField extends ISOComponent{

	int getIndex();
	
	void setIndex(int index);
	
	byte[] getValue();
	
	void setValue(byte[] value);
}
