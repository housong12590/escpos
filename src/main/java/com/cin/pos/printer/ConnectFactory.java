package com.cin.pos.printer;

import com.cin.pos.common.Dict;
import com.cin.pos.orderset.StandardOrderSet;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Arrays;
import java.util.Iterator;

public class ConnectFactory {

    private static ChannelGroup channelGroup = new ChannelGroup();

    public static void main(String[] args) throws IOException {
        Selector selector = Selector.open();
        StandardOrderSet orderSet = new StandardOrderSet();
        Dict printer1 = Dict.create();
        printer1.set("host", "192.168.10.60").set("port", 9100);
        Dict printer2 = Dict.create();
        printer2.set("host", "192.168.10.60").set("port", 4000);
        Dict[] _list = new Dict[2];
        _list[0] = printer1;
        _list[1] = printer2;
        for (Dict printer : _list) {
            SocketChannel channel = SocketChannel.open();
            channel.configureBlocking(false);
            String host = printer.getString("host");
            String port = printer.getString("port");
            channel.connect(new InetSocketAddress(host, Integer.parseInt(port)));
            channel.register(selector, SelectionKey.OP_CONNECT);
        }
        while (true) {
            selector.select();
            Iterator<SelectionKey> iter = selector.selectedKeys().iterator();
            while (iter.hasNext()) {
                SelectionKey selectionKey = iter.next();
                SocketChannel channel = (SocketChannel) selectionKey.channel();

                if (selectionKey.isConnectable()) {
                    if (channel.isConnectionPending()) {
                        channel.finishConnect();
                        channel.register(selector, SelectionKey.OP_READ);
                        channelGroup.add(getChannelId(channel), channel);
                    }
                } else if (selectionKey.isReadable()) {
                    ByteBuffer buffer = ByteBuffer.allocate(1024);
                    channel.read(buffer);
                    buffer.flip();
                    byte[] bytes = new byte[buffer.remaining()];
                    buffer.get(bytes);
                    System.out.println(Arrays.toString(bytes));
                    String s = new String(bytes);
                    System.out.println(s);

                }
                iter.remove();
            }
        }
    }

    private static String getChannelId(String host, String port) {
        return host + ":" + port;
    }

    private static String getChannelId(SocketChannel channel) {
        try {
            return channel.getRemoteAddress().toString().replace("/", "");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
