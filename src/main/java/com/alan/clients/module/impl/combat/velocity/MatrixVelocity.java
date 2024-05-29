package com.alan.clients.module.impl.combat.velocity;

import com.alan.clients.module.impl.combat.Velocity;
import com.alan.clients.newevent.Listener;
import com.alan.clients.newevent.annotations.EventLink;
import com.alan.clients.newevent.impl.motion.PreMotionEvent;
import com.alan.clients.util.interfaces.InstanceAccess;
import com.alan.clients.value.Mode;

public final class MatrixVelocity extends Mode<Velocity> {

    public MatrixVelocity(String name, Velocity parent) {
        super(name, parent);
    }

    @EventLink()
    public final Listener<PreMotionEvent> onPreMotionEvent = event -> {
        if (getParent().onSwing.getValue() || getParent().onSprint.getValue() && !InstanceAccess.mc.thePlayer.isSwingInProgress) return;

        if (InstanceAccess.mc.thePlayer.hurtTime > 0) {
            InstanceAccess.mc.thePlayer.motionX *= 0.6D;
            InstanceAccess.mc.thePlayer.motionZ *= 0.6D;
        }
    };
}
