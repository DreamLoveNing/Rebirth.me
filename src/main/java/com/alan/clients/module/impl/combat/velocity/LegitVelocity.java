package com.alan.clients.module.impl.combat.velocity;

import com.alan.clients.module.impl.combat.Velocity;
import com.alan.clients.newevent.Listener;
import com.alan.clients.newevent.annotations.EventLink;
import com.alan.clients.newevent.impl.input.MoveInputEvent;
import com.alan.clients.newevent.impl.motion.PreMotionEvent;
import com.alan.clients.newevent.impl.packet.PacketReceiveEvent;
import com.alan.clients.util.interfaces.InstanceAccess;
import com.alan.clients.util.player.MoveUtil;
import com.alan.clients.value.Mode;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S12PacketEntityVelocity;
import net.minecraft.network.play.server.S27PacketExplosion;

public final class LegitVelocity extends Mode<Velocity> {

    private boolean jump;

    public LegitVelocity(String name, Velocity parent) {
        super(name, parent);
    }

    @EventLink()
    public final Listener<PreMotionEvent> onPreMotionEvent = event -> {
        jump = false;
    };

    @EventLink()
    public final Listener<MoveInputEvent> onMove = event -> {
        if (getParent().onSwing.getValue() || getParent().onSprint.getValue() && !InstanceAccess.mc.thePlayer.isSwingInProgress) return;

        if (jump && MoveUtil.isMoving()) {
            event.setJump(true);
        }
    };

    @EventLink()
    public final Listener<PacketReceiveEvent> onPacketReceiveEvent = event -> {
        if (getParent().onSwing.getValue() || getParent().onSprint.getValue() && !InstanceAccess.mc.thePlayer.isSwingInProgress) return;

        if (!InstanceAccess.mc.thePlayer.onGround) {
            return;
        }

        final Packet<?> p = event.getPacket();

        if (p instanceof S12PacketEntityVelocity) {
            final S12PacketEntityVelocity wrapper = (S12PacketEntityVelocity) p;
            if(InstanceAccess.mc.thePlayer!=null) {
                if (wrapper.getEntityID() == InstanceAccess.mc.thePlayer.getEntityId()) {
                    jump = true;
                }
            }
        }

        if (p instanceof S27PacketExplosion) {
            jump = true;
        }
    };
}
