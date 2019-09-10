package com.ciin.pos.convert;


import com.ciin.pos.element.Element;
import com.ciin.pos.device.Device;

public interface Converter<T extends Element> {

    byte[] toBytes(Device device, T element);
}
