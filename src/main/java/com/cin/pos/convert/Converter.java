package com.cin.pos.convert;


import com.cin.pos.device.Device;
import com.cin.pos.element.Element;

public interface Converter<T extends Element> {

    byte[] toBytes(Device device, T element);
}
