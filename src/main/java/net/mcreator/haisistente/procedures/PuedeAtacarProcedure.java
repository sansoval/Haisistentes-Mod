package net.mcreator.haisistente.procedures;

import net.minecraft.world.entity.Entity;

public class PuedeAtacarProcedure {
	public static boolean execute(Entity entity) {
		if (entity == null)
			return false;
		return !entity.getPersistentData().getBoolean("comiendo");
	}
}
