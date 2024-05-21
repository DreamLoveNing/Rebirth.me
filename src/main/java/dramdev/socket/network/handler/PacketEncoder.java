package dramdev.socket.network.handler;

import dramdev.socket.network.buffer.PacketBuffer;
import dramdev.socket.network.packet.Packet;
import dramdev.socket.network.packet.PacketFactory;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * @author DiaoLing
 * @since 4/7/2024
 */
public class PacketEncoder extends MessageToByteEncoder<Packet> {

    @Override
    protected void encode(ChannelHandlerContext ctx, Packet packet, ByteBuf out) throws Exception {
        out.writeInt(PacketFactory.getPacketId(packet.getClass()));

        PacketBuffer buffer = new PacketBuffer(out);
        packet.encode(buffer);
    }
}