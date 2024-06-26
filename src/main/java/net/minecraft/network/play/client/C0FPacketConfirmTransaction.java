package net.minecraft.network.play.client;

import com.alan.clients.Client;
import com.alan.clients.module.impl.exploit.Disabler;
import com.alan.clients.module.impl.exploit.disabler.GrimACDisabler;
import lombok.Setter;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayServer;

import java.io.IOException;

@Setter
public class C0FPacketConfirmTransaction implements Packet<INetHandlerPlayServer> {
    private int windowId;
    private short uid;
    private boolean accepted;

    public C0FPacketConfirmTransaction() {
    }

    public C0FPacketConfirmTransaction(final int windowId, final short uid, final boolean accepted) {
        this.windowId = windowId;
        this.uid = uid;
        this.accepted = accepted;
    }

    /**
     * Passes this Packet on to the NetHandler for processing.
     */
    public void processPacket(final INetHandlerPlayServer handler) {
        handler.processConfirmTransaction(this);
    }

    /**
     * Reads the raw packet data from the data stream.
     */
    public void readPacketData(final PacketBuffer buf) throws IOException {
        this.windowId = buf.readByte();
        this.uid = buf.readShort();
        this.accepted = buf.readByte() != 0;

        GrimACDisabler dis = (GrimACDisabler) Client.INSTANCE.getModuleManager().get(Disabler.class).grimac.getMode();
        if(dis.getGrimPost() && this.uid < 0){
            dis.getPingPackets().add(Integer.valueOf(this.uid));
        }
    }

    /**
     * Writes the raw packet data to the data stream.
     */
    public void writePacketData(final PacketBuffer buf) throws IOException {
        buf.writeByte(this.windowId);
        buf.writeShort(this.uid);
        buf.writeByte(this.accepted ? 1 : 0);
    }

    public int getWindowId() {
        return this.windowId;
    }

    public short getUid() {
        return this.uid;
    }
}
