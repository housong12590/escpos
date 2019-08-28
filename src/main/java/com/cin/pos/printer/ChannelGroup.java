package com.cin.pos.printer;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ChannelGroup {

    private Map<String, SocketChannel> channelMap = new ConcurrentHashMap<>();

    public void add(String channelId, SocketChannel channel) {
        channelMap.put(channelId, channel);
    }

    public void remove(String channelId) {
        channelMap.remove(channelId);
    }

    public SocketChannel find(String channelId) {
        return channelMap.get(channelId);
    }

    public void write(String channelId, ByteBuffer buffer) throws IOException {
        SocketChannel channel = channelMap.get(channelId);
        buffer.flip();
        channel.write(buffer);
    }

}
