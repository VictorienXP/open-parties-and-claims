/*
 * Open Parties and Claims - adds chunk claims and player parties to Minecraft
 * Copyright (C) 2022-2023, Xaero <xaero1996@gmail.com> and contributors
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of version 3 of the GNU Lesser General Public License
 * (LGPL-3.0-only) as published by the Free Software Foundation.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received copies of the GNU Lesser General Public License
 * and the GNU General Public License along with this program.
 * If not, see <https://www.gnu.org/licenses/>.
 */

package xaero.pac.common.event;

import com.mojang.authlib.GameProfile;
import com.mojang.brigadier.CommandDispatcher;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.entity.event.v1.ServerEntityWorldChangeEvents;
import net.fabricmc.fabric.api.entity.event.v1.ServerPlayerEvents;
import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.event.player.*;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.SectionPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.players.PlayerList;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;
import xaero.pac.OpenPartiesAndClaims;
import xaero.pac.common.claims.player.IPlayerChunkClaim;
import xaero.pac.common.claims.player.IPlayerClaimPosList;
import xaero.pac.common.claims.player.IPlayerDimensionClaims;
import xaero.pac.common.claims.tracker.api.IClaimsManagerTrackerRegisterAPI;
import xaero.pac.common.event.api.OPACServerAddonRegister;
import xaero.pac.common.mods.ModSupportFabric;
import xaero.pac.common.parties.party.IPartyPlayerInfo;
import xaero.pac.common.parties.party.ally.IPartyAlly;
import xaero.pac.common.parties.party.member.IPartyMember;
import xaero.pac.common.server.IServerData;
import xaero.pac.common.server.claims.IServerClaimsManager;
import xaero.pac.common.server.claims.IServerDimensionClaimsManager;
import xaero.pac.common.server.claims.IServerRegionClaims;
import xaero.pac.common.server.claims.player.IServerPlayerClaimInfo;
import xaero.pac.common.server.core.ServerCoreFabric;
import xaero.pac.common.server.parties.party.IServerParty;
import xaero.pac.common.server.parties.system.api.IPlayerPartySystemRegisterAPI;
import xaero.pac.common.server.player.permission.api.IPlayerPermissionSystemRegisterAPI;

import java.util.List;

public class CommonEventsFabric extends CommonEvents {

	private final ResourceLocation PROTECTION_PHASE = ResourceLocation.fromNamespaceAndPath(OpenPartiesAndClaims.MOD_ID, "protection");

	public CommonEventsFabric(OpenPartiesAndClaims modMain) {
		super(modMain);
	}

	public void registerFabricAPIEvents(){
		//trying to handle these events before other mods
		AttackBlockCallback.EVENT.addPhaseOrdering(PROTECTION_PHASE, Event.DEFAULT_PHASE);
		PlayerBlockBreakEvents.BEFORE.addPhaseOrdering(PROTECTION_PHASE, Event.DEFAULT_PHASE);
		UseBlockCallback.EVENT.addPhaseOrdering(PROTECTION_PHASE, Event.DEFAULT_PHASE);
		UseItemCallback.EVENT.addPhaseOrdering(PROTECTION_PHASE, Event.DEFAULT_PHASE);
		UseEntityCallback.EVENT.addPhaseOrdering(PROTECTION_PHASE, Event.DEFAULT_PHASE);
		AttackEntityCallback.EVENT.addPhaseOrdering(PROTECTION_PHASE, Event.DEFAULT_PHASE);

		ServerPlayerEvents.AFTER_RESPAWN.register(this::onPlayerRespawn);
		ServerPlayerEvents.COPY_FROM.register(this::onPlayerClone);
		ServerLifecycleEvents.SERVER_STARTED.register(this::onServerStarting);
		ServerLifecycleEvents.SERVER_STOPPED.register(this::onServerStopped);
		ServerEntityWorldChangeEvents.AFTER_PLAYER_CHANGE_WORLD.register(this::onPlayerChangedDimension);
		ServerTickEvents.START_SERVER_TICK.register(server -> onServerTick(server, true));
		ServerTickEvents.END_SERVER_TICK.register(server -> onServerTick(server, false));
		CommandRegistrationCallback.EVENT.register(this::onRegisterCommands);
		AttackBlockCallback.EVENT.register(PROTECTION_PHASE, this::onLeftClickBlock);
		PlayerBlockBreakEvents.BEFORE.register(PROTECTION_PHASE, this::onDestroyBlock);
		UseBlockCallback.EVENT.register(PROTECTION_PHASE, this::onRightClickBlock);
		UseItemCallback.EVENT.register(PROTECTION_PHASE, this::onItemRightClick);
		UseEntityCallback.EVENT.register(PROTECTION_PHASE, this::onEntityInteract);
		AttackEntityCallback.EVENT.register(PROTECTION_PHASE, this::onEntityAttack);
		OPACServerAddonRegister.EVENT.register(this::onAddonRegister);
	}

	@Override
	public void onServerAboutToStart(MinecraftServer server) throws Throwable {
		super.onServerAboutToStart(server);
	}

	@Override
	public void onServerStarting(MinecraftServer server) {
		super.onServerStarting(server);
	}

	private void onPlayerRespawn(ServerPlayer oldPlayer, ServerPlayer newPlayer, boolean alive) {
		super.onPlayerRespawn(newPlayer);
	}

	private void onPlayerChangedDimension(ServerPlayer serverPlayer, ServerLevel serverLevel, ServerLevel serverLevel1) {
		super.onPlayerChangedDimension(serverPlayer);
	}

	public void onPlayerLogIn(ServerPlayer player) {
		super.onPlayerLogIn(player);
	}

	private void onPlayerClone(ServerPlayer oldPlayer, ServerPlayer newPlayer, boolean alive) {
		super.onPlayerClone(oldPlayer, newPlayer);
	}

	public void onPlayerLogOut(ServerPlayer player) {
		super.onPlayerLogOut(player);
	}

	@Override
	public void onServerTick(MinecraftServer server, boolean isTickStart) {
		try {
			super.onServerTick(server, isTickStart);
		} catch (Throwable e) {
			throw new RuntimeException(e);
		}
	}

	public void onPlayerTick(boolean isTickStart, Player player) {
		try {
			super.onPlayerTick(isTickStart, player instanceof ServerPlayer, player);
		} catch (Throwable e) {
			throw new RuntimeException(e);
		}
	}

	public void onServerStopped(MinecraftServer server) {
		super.onServerStopped(server);
		ServerCoreFabric.reset();
	}


	private void onRegisterCommands(CommandDispatcher<CommandSourceStack> dispatcher, CommandBuildContext commandBuildContext, Commands.CommandSelection commandSelection) {
		super.onRegisterCommands(dispatcher, commandSelection);
	}

	private InteractionResult onLeftClickBlock(Player player, Level level, InteractionHand interactionHand, BlockPos blockPos, Direction direction) {
		if(super.onLeftClickBlock(level, blockPos, player))
			return InteractionResult.FAIL;
		return InteractionResult.PASS;
	}

	private boolean onDestroyBlock(Level level, Player player, BlockPos blockPos, BlockState blockState, BlockEntity blockEntity) {
		return !super.onDestroyBlock(level, blockPos, player);
	}

	private InteractionResult onRightClickBlock(Player player, Level level, InteractionHand interactionHand, BlockHitResult blockHitResult) {
		if(player.isSpectator())
			return InteractionResult.PASS;
		if(super.onRightClickBlock(level, blockHitResult.getBlockPos(), player, interactionHand, blockHitResult))
			return InteractionResult.FAIL;
		return InteractionResult.PASS;
	}

	private InteractionResultHolder<ItemStack> onItemRightClick(Player player, Level level, InteractionHand interactionHand) {
		ItemStack stack = player.getItemInHand(interactionHand);
		if(super.onItemRightClick(level, player.blockPosition(), player, interactionHand, stack))
			return InteractionResultHolder.fail(stack);
		return InteractionResultHolder.pass(stack);
	}

	public boolean onMobGrief(Entity entity) {
		return super.onMobGrief(entity);
	}

	public boolean onLivingHurt(DamageSource source, Entity target) {
		return super.onLivingHurt(source, target);
	}

	private InteractionResult onEntityAttack(Player player, Level level, InteractionHand interactionHand, Entity entity, @Nullable EntityHitResult entityHitResult) {
		if(super.onEntityAttack(player, entity))
			return InteractionResult.FAIL;
		return InteractionResult.PASS;
	}

	public InteractionResult onEntityInteract(Player player, Level world, InteractionHand interactionHand, Entity entity, @Nullable EntityHitResult hitResult) {
		if(hitResult == null) {
			if(super.onEntityInteract(player, entity, interactionHand))
				return InteractionResult.FAIL;
			return InteractionResult.PASS;
		}
		if(super.onInteractEntitySpecific(player, entity, interactionHand))
			return InteractionResult.FAIL;
		return InteractionResult.PASS;
	}

	public void onExplosionDetonate(Explosion explosion, List<Entity> entities, Level level) {
		super.onExplosionDetonate(level, explosion, entities, explosion.getToBlow());
	}

	public boolean onChorusFruit(Entity entity, Vec3 target){
		return super.onChorusFruit(entity, target);
	}

	public boolean onEntityJoinWorld(Entity entity, Level level, boolean fromDisk){
		if(!fromDisk && entity.tickCount == 0) {//is being spawned
			MobSpawnType mobSpawnType = ServerCoreFabric.getMobSpawnTypeForNewEntities(entity.getServer());
			if(mobSpawnType != null)
				return super.onMobSpawn(entity, entity.getX(), entity.getY(), entity.getZ(), mobSpawnType);
		}
		return super.onEntityJoinWorld(entity, level, fromDisk);
	}

	public void onEntityEnteringSection(Entity entity, long oldSectionKey, long newSectionKey){
		SectionPos oldSection = SectionPos.of(oldSectionKey);
		SectionPos newSection = SectionPos.of(newSectionKey);
		boolean chunkChanged = oldSection.x() != newSection.x() || oldSection.z() != newSection.z();
		super.onEntityEnteringSection(entity, oldSection, newSection, chunkChanged);
	}

	public void onPermissionsChanged(PlayerList playerList, GameProfile profile) {
		ServerPlayer player = playerList.getPlayer(profile.getId());
		if(player == null)
			return;
		super.onPermissionsChanged(player);
	}

	public boolean onCropTrample(Entity entity, BlockPos pos) {
		return super.onCropTrample(entity, pos);
	}

	public boolean onBucketUse(Entity entity, Level level, HitResult hitResult, ItemStack itemStack){
		return super.onBucketUse(entity, level, hitResult, itemStack);
	}

	public void onTagsUpdate() {
		super.onTagsUpdate();
	}

	@Override
	public void onAddonRegister(MinecraftServer server, IPlayerPermissionSystemRegisterAPI permissionSystemManagerAPI, IPlayerPartySystemRegisterAPI partySystemManagerAPI, IClaimsManagerTrackerRegisterAPI claimsManagerTrackerAPI) {
		super.onAddonRegister(server, permissionSystemManagerAPI, partySystemManagerAPI, claimsManagerTrackerAPI);
		ModSupportFabric modSupportFabric = (ModSupportFabric)modMain.getModSupport();
		if(modSupportFabric.FABRIC_PERMISSIONS)
			permissionSystemManagerAPI.register("permission_api", modSupportFabric.getFabricPermissionsSupport().getPermissionSystem());

	}

	@Override
	protected void fireAddonRegisterEvent(IServerData<IServerClaimsManager<IPlayerChunkClaim, IServerPlayerClaimInfo<IPlayerDimensionClaims<IPlayerClaimPosList>>, IServerDimensionClaimsManager<IServerRegionClaims>>, IServerParty<IPartyMember, IPartyPlayerInfo, IPartyAlly>> serverData) {
		OPACServerAddonRegister.EVENT.invoker().registerAddons(serverData.getServer(), serverData.getPlayerPermissionSystemManager(), serverData.getPlayerPartySystemManager(), serverData.getServerClaimsManager().getTracker());
	}

}
