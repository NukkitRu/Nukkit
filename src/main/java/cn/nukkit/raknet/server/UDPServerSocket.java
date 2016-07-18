package cn.nukkit.raknet.server;

import cn.nukkit.utils.ThreadedLogger;

import java.io.IOException;
import java.net.*;
import java.util.Arrays;

/**
 * author: MagicDroidX
 * Nukkit Project
 */
public class UDPServerSocket {

    protected final ThreadedLogger logger;
    protected DatagramSocket socket;

    public UDPServerSocket(ThreadedLogger logger) {
        this(logger, 19132, "0.0.0.0");
    }

    public UDPServerSocket(ThreadedLogger logger, int port) {
        this(logger, port, "0.0.0.0");
    }

    public UDPServerSocket(ThreadedLogger logger, int port, String interfaz) {
        this.logger = logger;
        try {
            socket = new DatagramSocket(new InetSocketAddress(interfaz, port));
            socket.setBroadcast(true);
            socket.setSendBufferSize(1024 * 1024 * 8);
            socket.setReceiveBufferSize(1024 * 1024 * 8);
            socket.setSoTimeout(1);
        } catch (SocketException e) {
            logger.critical("**** FAILED TO BIND TO " + interfaz + ":" + port + "!");
            logger.critical("Perhaps a server is already running on that port?");
            System.exit(1);
        }
    }

    public DatagramSocket getSocket() {
        return socket;
    }

    public void close() {
        this.socket.close();
    }

    public DatagramPacket readPacket() throws IOException {
        DatagramPacket dp = new DatagramPacket(new byte[65535], 65535);
        try {
            socket.receive(dp);
            dp.setData(Arrays.copyOf(dp.getData(), dp.getLength()));
            return dp;
        } catch (SocketTimeoutException e) {
            return null;
        }
    }

    public void writePacket(byte[] buffer, InetSocketAddress dest) throws IOException {
        DatagramPacket dp = new DatagramPacket(buffer, buffer.length, dest);
        socket.send(dp);
    }

    public UDPServerSocket setSendBuffer(int size) throws SocketException {
        socket.setSendBufferSize(size);
        return this;
    }

    public UDPServerSocket setRecvBuffer(int size) throws SocketException {
        socket.setReceiveBufferSize(size);
        return this;
    }

}
